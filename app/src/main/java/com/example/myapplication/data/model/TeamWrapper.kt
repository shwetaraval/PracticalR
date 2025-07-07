package com.example.myapplication.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TeamsWrapper(
    val data: TeamsData
)

@Serializable
data class TeamsData(
    val teams: List<Team>
)