package by.dima00138.coursework.sign_in

import by.dima00138.coursework.Firebase

data class SignInResult(
    val data: Firebase.User?,
    val errorMessage: String?
)