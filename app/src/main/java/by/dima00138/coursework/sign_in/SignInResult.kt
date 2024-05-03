package by.dima00138.coursework.sign_in

import by.dima00138.coursework.Models.User

data class SignInResult(
    val data: User?,
    val errorMessage: String?
)