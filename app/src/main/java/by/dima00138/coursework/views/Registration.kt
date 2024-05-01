package by.dima00138.coursework.views

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import by.dima00138.coursework.R
import by.dima00138.coursework.ui.theme.FancyTextField
import by.dima00138.coursework.ui.theme.PrimaryFancyButton
import by.dima00138.coursework.viewModels.ProfileVM
import by.dima00138.coursework.viewModels.Screen
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(navController: NavHostController, viewModel: ProfileVM) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()
    val fullName = viewModel.fullName.collectAsStateWithLifecycle()
    val passport = viewModel.passport.collectAsStateWithLifecycle()
    val birthdate = viewModel.birthdate.collectAsStateWithLifecycle()
    val email = viewModel.email.collectAsStateWithLifecycle()
    val password = viewModel.password.collectAsStateWithLifecycle()
    val confirmPassword = viewModel.confirmPassword.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state.value.user) {
        if (state.value.user != null) {
            navController.navigate(Screen.Profile.route)

        }
    }

    LaunchedEffect(key1 = state.value.signInError) {
        if (!state.value.isSignInSuccessful && state.value.signInError != null) {
            Toast.makeText(
                context,
                state.value.signInError, Toast.LENGTH_LONG
            ).show()
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 0.dp)
            .verticalScroll(rememberScrollState())) {
//        Text(
//            stringResource(id = R.string.register),
//            style = MaterialTheme.typography.headlineLarge,
//            textAlign = TextAlign.Center,
//            modifier = Modifier
//                .padding(16.dp, 24.dp, 16.dp, 8.dp)
//                .fillMaxSize()
//        )
        Text(text = "Full Name",
            Modifier.padding(0.dp, 24.dp, 0.dp, 0.dp))
        FancyTextField(
            value = fullName.value,
            enabled = true,
            onValueChange = { viewModel.onFullNameChange(it) },
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = "Passport Number")
        FancyTextField(
            value = passport.value,
            enabled = true,
            onValueChange = { viewModel.onPassportChange(it) },
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = "Birthdate")
        FancyTextField(
            value = birthdate.value,
            enabled = true,
            onValueChange = { viewModel.onBirthdateChange(it) },
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = "Email")
        FancyTextField(
            value = email.value,
            enabled = true,
            onValueChange = { viewModel.onEmailChange(it) },
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = "Password")
        FancyTextField(
            value = password.value,
            enabled = true,
            onValueChange = { viewModel.onPasswordChange(it) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = "Confirm Password")
        FancyTextField(
            value = confirmPassword.value,
            enabled = true,
            onValueChange = { viewModel.onConfirmPasswordChange(it) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        PrimaryFancyButton(
            modifier = Modifier.padding(20.dp, 16.dp),
            text = stringResource(id = R.string.register),
            onClick = {
                if (password.value == confirmPassword.value) {
                    viewModel.viewModelScope.launch {
                        viewModel.onRegistration(
                            navController
                        )
                    }
                } else {

                }
            })
    }
}
