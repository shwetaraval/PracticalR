package com.example.myapplication.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleWrapper(
    val data: ScheduleData
)

@Serializable
data class ScheduleData(
    val schedules: List<ScheduleGame>
)