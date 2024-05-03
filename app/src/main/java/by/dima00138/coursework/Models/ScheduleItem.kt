package by.dima00138.coursework.Models

import java.time.LocalDateTime

data class ScheduleItem(
    val id: String = "",
    val from: String = "",
    val to: String = "",
    val date: LocalDateTime = LocalDateTime.now(),
    val stationName: String = "",
) : IModel