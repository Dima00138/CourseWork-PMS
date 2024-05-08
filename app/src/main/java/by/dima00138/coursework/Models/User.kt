package by.dima00138.coursework.Models

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class User(
    var id: String = "",
    val fullName: String = "",
    val passport: String = "",
    val birthdate: String = "",
    val email: String = "",
    val password: String = "",
    val role: String = "user"
) : IModel {
    override fun toFirebase() : UserFirebase  {
        return UserFirebase(
            id = id,
            fullName = fullName,
            passport = passport,
            birthdate = LocalDateTime.parse(birthdate, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")).toEpochSecond(ZoneOffset.UTC),
            email = email,
            password = password,
            role = role
            )
    }
}

data class UserFirebase(
    val id: String = "",
    val fullName: String = "",
    val passport: String = "",
    val birthdate: Long = 0,
    val email: String = "",
    val password: String = "",
    val role: String = "user"
)
