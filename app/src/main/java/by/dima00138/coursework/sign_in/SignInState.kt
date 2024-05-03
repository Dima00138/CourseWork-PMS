package by.dima00138.coursework.sign_in

import by.dima00138.coursework.Models.User

data class SignInState(
    val user : User? = null,
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
