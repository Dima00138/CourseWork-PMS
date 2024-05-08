package by.dima00138.coursework.Models

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class ScheduleItem(
    val id: String = "",
    val from: String = "",
    val to: String = "",
    val date: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
) : IModel {
    override fun toFirebase(): Any {
        return ScheduleItemFirebase(
            id = id,
            from = from,
            to = to,
            date = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")).toEpochSecond(ZoneOffset.UTC),
        )
    }
}

data class ScheduleItemFirebase(
    val id: String = "",
    val from: String = "",
    val to: String = "",
    val date: Long = 0,
)