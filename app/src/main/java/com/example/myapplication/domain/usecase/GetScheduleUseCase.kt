package com.example.myapplication.domain.usecase

import com.example.myapplication.data.model.GameStatus
import com.example.myapplication.domain.repository.ScheduleRepository
import com.example.myapplication.ui.schedule.data.GameDisplayData
import java.time.YearMonth
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetScheduleUseCase @Inject constructor(
    private val repo: ScheduleRepository
) {
    suspend operator fun invoke(): List<Pair<YearMonth, List<GameDisplayData>>> {
        val teams = repo.getTeams().associateBy { it.tid }
        val games = repo.getScheduleGames()
        val appTeamId = "1610612748"
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        val dateFormatter = DateTimeFormatter.ofPattern("EEE MMM dd") // MON JUL 03
        val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")      // 7:30 PM

        return games.mapNotNull { game ->
            //Get Home and visitor team data
            val home = teams[game.h.tid] ?: return@mapNotNull null
            val away = teams[game.v.tid] ?: return@mapNotNull null

            // set score
            home.score = game.h.s
            away.score = game.v.s

            // Map Date and Time
            val zonedDateTime = ZonedDateTime.parse(game.gametime, inputFormatter)
            val formattedDate = zonedDateTime.format(dateFormatter).uppercase() // MON JUL 03
            val formattedTime = zonedDateTime.format(timeFormatter)             // 7:30 PM

            // Assign game status
            val gameStatus: GameStatus = when (game.st) {
                1 -> GameStatus.FUTURE_GAME
                2 -> GameStatus.LIVE_GAME
                3 -> GameStatus.PAST_GAME
                else -> GameStatus.FUTURE_GAME
            }
            GameDisplayData(
                date = zonedDateTime.toLocalDate(),
                displayDate = formattedDate,
                displayTime = formattedTime,
                homeTeam = home,
                gameStatus = gameStatus,
                gameTicketUrl = game.buy_ticket_url,
                visitorTeam = away,
                isHome = game.h.tid == appTeamId,
                startTimeOrStatus = game.stt
            )
        }.groupBy { YearMonth.from(it.date) }.toSortedMap(compareByDescending { it }).toList()
    }
}
