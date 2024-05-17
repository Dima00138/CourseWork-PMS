package by.dima00138.coursework.services

import android.util.Patterns
import by.dima00138.coursework.Models.User
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class Validator {
    fun email(state: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(state).matches()
    }

    fun fullName(state: String): Boolean {
        return state.trim().split(" ").size >= 2
    }

    fun birthdate(state: String): Boolean {
        return try {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            dateFormat.isLenient = false
            dateFormat.parse(state)
            true
        } catch (e: ParseException) {
            false
        }
    }

    fun passport(state: String): Boolean {
        return state.matches(Regex("^[A-ZА-Я]{2}\\d{7}\$"))
    }

    fun password(password: String): Boolean {
        // - Должен содержать как минимум 8 символов
        // - Должен содержать как минимум одну заглавную букву, одну строчную букву и одну цифру
        // - Может содержать некоторые специальные символы, такие как: !@#$%^&*()_+
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+])[a-zA-Z\\d!@#\$%^&*()_+]{8,}\$")
        return passwordRegex.matches(password)
    }
    fun validUser(user: User):Boolean {
        return email(user.email) && fullName(user.fullName) && birthdate(user.birthdate) && passport(user.passport)
    }

    fun validRegistration(user: User):Boolean {
        return email(user.email) && fullName(user.fullName) && birthdate(user.birthdate) && passport(user.passport)
                && password(user.password)
    }
}