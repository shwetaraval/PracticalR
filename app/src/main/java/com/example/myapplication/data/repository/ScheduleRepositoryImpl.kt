package com.example.myapplication.data.repository

import android.content.Context
import com.example.myapplication.data.model.ScheduleGame
import com.example.myapplication.data.model.ScheduleWrapper
import com.example.myapplication.data.model.Team
import com.example.myapplication.data.model.TeamsWrapper
import com.example.myapplication.domain.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal class ScheduleRepositoryImpl (
    private val context: Context,
    private val json: Json
) : ScheduleRepository {
    override suspend fun getScheduleGames(): List<ScheduleGame> = withContext(Dispatchers.IO) {
        val scheduleJson =
            context.assets.open("schedule.json").bufferedReader().use { it.readText() }
        val scheduleWrapper = json.decodeFromString(ScheduleWrapper.serializer(), scheduleJson)
        scheduleWrapper.data.schedules
    }

    override suspend fun getTeams(): List<Team> = withContext(Dispatchers.IO) {
        val teamJson = context.assets.open("teams.json").bufferedReader().use { it.readText() }
        val teamWrapper = json.decodeFromString(TeamsWrapper.serializer(), teamJson)
        teamWrapper.data.teams
    }
}