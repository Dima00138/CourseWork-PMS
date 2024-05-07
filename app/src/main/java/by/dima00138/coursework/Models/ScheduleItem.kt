package by.dima00138.coursework.Models

import java.time.LocalDateTime
import java.time.ZoneOffset

data class ScheduleItem(
    val id: String = "",
    val from: String = "",
    val to: String = "",
    val date: LocalDateTime = LocalDateTime.now(),
) : IModel {
    override fun toFirebase(): Any {
        return ScheduleItemFirebase(
            id = id,
            from = from,
            to = to,
            date = date.toEpochSecond(ZoneOffset.UTC),
        )
    }
}

data class ScheduleItemFirebase(
    val id: String = "",
    val from: String = "",
    val to: String = "",
    val date: Long = 0,
)