package com.example.myapplication.domain.repository

import com.example.myapplication.data.model.ScheduleGame
import com.example.myapplication.data.model.Team

interface ScheduleRepository {
    suspend fun getScheduleGames(): List<ScheduleGame>
    suspend fun getTeams(): List<Team>
}