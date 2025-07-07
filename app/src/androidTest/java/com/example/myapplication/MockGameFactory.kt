package com.example.myapplication

import com.example.myapplication.data.model.GameStatus
import com.example.myapplication.data.model.Team
import com.example.myapplication.ui.schedule.data.GameDisplayData
import java.time.LocalDate

object MockGameFactory {
    fun create(
        homeTeamName: String,
        visitorTeamName: String,
        isFuture: Boolean = false
    ): GameDisplayData {
        return GameDisplayData(
            isHome = true,
            gameStatus = if (isFuture) GameStatus.FUTURE_GAME else GameStatus.PAST_GAME,
            displayDate = "Jan 15",
            displayTime = "7:30 PM",
            startTimeOrStatus = "Final",
            gameTicketUrl = if (isFuture) "https://ticket.url" else null,
            date = LocalDate.now(),
            homeTeam = Team(
                ta = homeTeamName,
                tn = homeTeamName,
                score = "102",
                logo = "",
                color = "",
                tc = "",
                tid = ""
            ),
            visitorTeam = Team(
                ta = visitorTeamName,
                tn = visitorTeamName,
                score = "98",
                logo = "",
                color = "",
                tc = "",
                tid = ""
            )
        )
    }
}
