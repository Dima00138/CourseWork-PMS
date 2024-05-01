package by.dima00138.coursework.sign_in

import by.dima00138.coursework.Firebase

data class SignInState(
    val user : Firebase.User? = null,
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
