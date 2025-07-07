package com.example.myapplication

import com.example.myapplication.data.model.ScheduleGame
import com.example.myapplication.data.model.ScheduleGame.TeamInfo
import com.example.myapplication.data.model.Team
import com.example.myapplication.domain.repository.ScheduleRepository

class FakeScheduleRepository : ScheduleRepository {
    override suspend fun getScheduleGames(): List<ScheduleGame> {
        return listOf(
            ScheduleGame(
                gametime = "2025-02-15T19:00:00.000Z",
                h = TeamInfo("1610612748", "100"),
                v = TeamInfo("1610612750", "90"),
                st = 3,
                stt = "Final",
                buy_ticket_url = "https://ticketmaster.com",
                uid = ""
            )
        )
    }

    override suspend fun getTeams(): List<Team> {
        return listOf(
            Team(tid = "1610612748", ta = "MIA", tn = "Miami Heat", tc = "Miami", logo = "logo_url",  color = "", score = "0"),
            Team(tid = "1610612750", ta = "LAL", tn = "Lakers", tc = "Los Angeles", logo = "logo_url", color = "", score = "0")
        )
    }
}
