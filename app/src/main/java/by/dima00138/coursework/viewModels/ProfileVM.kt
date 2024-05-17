package by.dima00138.coursework.viewModels

import android.content.Context
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import by.dima00138.coursework.Models.User
import by.dima00138.coursework.R
import by.dima00138.coursework.services.Firebase
import by.dima00138.coursework.services.Validator
import by.dima00138.coursework.sign_in.SignInState
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileVM @Inject constructor(
    private val firebase: Firebase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()
    val error = MutableStateFlow(false)
    val validator = Validator()
    val isRefresh = MutableStateFlow(false)

    init {
        isRefresh.update { true }
        viewModelScope.launch {
            val user = firebase.getUser()
            _state.update {
                it.copy(
                    user = firebase.getUser(),
                    isSignInSuccessful = user != null,
                    signInError = null
                )
            }
        }
        isRefresh.update { false }
    }

    val fullName = MutableStateFlow(TextFieldValue())
    fun onFullNameChange(state: TextFieldValue) {
        fullName.value = state
    }

    val passport = MutableStateFlow(TextFieldValue())
    fun onPassportChange(state: TextFieldValue) {
        passport.value = state
    }

    val birthdate = MutableStateFlow(TextFieldValue())
    fun onBirthdateChange(state: TextFieldValue) {
        birthdate.value = state
    }

    val email = MutableStateFlow(TextFieldValue())
    fun onEmailChange(state: TextFieldValue) {
        email.value = state
    }

    val password = MutableStateFlow(TextFieldValue())
    fun onPasswordChange(state: TextFieldValue) {
        password.value = state
    }

    val confirmPassword = MutableStateFlow(TextFieldValue())
    fun onConfirmPasswordChange(state: TextFieldValue) {
        confirmPassword.value = state
    }

//    fun signInGithub(context: Context) {
//        viewModelScope.launch {
//        firebase.signInWithGithub((context as Activity), {
//
//        }) {
//
//        }
//        }
//    }

    fun signInGoogle(context : Context) {
        isRefresh.update { true }

        val getCredentials = firebase.getCredentials(context)

        viewModelScope.launch {
            firebase.signUpWithCredentials(context, getCredentials.credentialManager,
                getCredentials.request, {doc ->
                    _state.update {it.copy(
                    user = doc.toObject<User>(),
                    isSignInSuccessful = true,
                    signInError = null
                )
                }
            }) {e ->
                _state.update {it.copy(
                    user = null,
                    isSignInSuccessful = false,
                    signInError = e.message
                )
                }
            }
        }
        isRefresh.update { false }
    }

    fun resetState() {
        _state.update { SignInState() }
        fullName.value = TextFieldValue()
        email.value = TextFieldValue()
        passport.value = TextFieldValue()
        password.value = TextFieldValue()
        confirmPassword.value = TextFieldValue()
        birthdate.value = TextFieldValue()
    }

    suspend fun onLogin(navController: NavHostController) {
        isRefresh.update { true }
        val user = User(email = email.value.text, password = password.value.text)

        firebase.signUpWithEmail(user, {res, us ->
            error.update { false }
            _state.update { it.copy(
                user = us,
                isSignInSuccessful = res.user != null,
                signInError = null
            )
            }
            navController.navigate(Screen.Profile.route)
        }) {e ->
            error.update { true }
            _state.update { it.copy(
                    user = null,
                    isSignInSuccessful = false,
                    signInError = e.message
                )
            }
        }
        isRefresh.update { false }
    }

    suspend fun onRegistration(
        navController: NavHostController
    ) {
        isRefresh.update { true }
        val user = User(
            fullName = fullName.value.text,
            passport = passport.value.text,
            birthdate = birthdate.value.text,
            email = email.value.text,
            password = password.value.text,
            role = "user")
        if (!validator.validRegistration(user)) {
            _state.update {
                it.copy(
                    user = null,
                    isSignInSuccessful = false,
                    signInError = firebase.context.getString(R.string.user_invalid)
                )
            }
            return
        }
        firebase.createUserWithEmail(user = user, { res ->
            resetState()
            navController.navigate(Screen.Login.route)
        }) { e ->
            _state.update {
                it.copy(
                    user = null,
                    isSignInSuccessful = false,
                    signInError = e.message
                )
            }
        }
        isRefresh.update { false }
    }

    suspend fun onLogOut(navController: NavHostController) {
        firebase.logout({
            resetState()
        }) { e ->
            _state.update {
                it.copy(
                    signInError = e.message
                )
            }
        }
    }
}