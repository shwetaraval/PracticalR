package com.example.myapplication.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleGame(
    val uid: String,
    val h: TeamInfo,
    val v: TeamInfo,
    val gametime: String,
    val st: Int,
    val stt: String,
    val buy_ticket_url: String?
) {
    @Serializable
    data class TeamInfo(
        val tid: String, val s: String
    )
}

enum class GameStatus {
    FUTURE_GAME,
    LIVE_GAME,
    PAST_GAME,
}