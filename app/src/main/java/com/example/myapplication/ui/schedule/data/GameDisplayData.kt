package com.example.myapplication.ui.schedule.data

import com.example.myapplication.data.model.GameStatus
import com.example.myapplication.data.model.Team
import java.time.LocalDate

data class GameDisplayData(
    val date: LocalDate,
    val displayDate: String,
    val displayTime: String,
    val homeTeam: Team,
    val visitorTeam: Team,
    val gameStatus: GameStatus,
    val gameTicketUrl: String?,
    val startTimeOrStatus: String,
    val isHome: Boolean
)