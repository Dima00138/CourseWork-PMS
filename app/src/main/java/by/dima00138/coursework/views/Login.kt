package by.dima00138.coursework.views

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import by.dima00138.coursework.R
import by.dima00138.coursework.ui.theme.FancyTextField
import by.dima00138.coursework.ui.theme.LoadingIndicator
import by.dima00138.coursework.ui.theme.PrimaryFancyButton
import by.dima00138.coursework.ui.theme.SecondaryFancyButton
import by.dima00138.coursework.viewModels.ProfileVM
import by.dima00138.coursework.viewModels.Screen
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController, viewModel: ProfileVM) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()
    val email = viewModel.email.collectAsStateWithLifecycle(TextFieldValue())
    val password = viewModel.password.collectAsStateWithLifecycle(TextFieldValue())
    val error = viewModel.error.collectAsStateWithLifecycle()
    val isRefresh = viewModel.isRefresh.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state.value.user) {
        if (state.value.user != null) {
            navController.navigate(Screen.Profile.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }

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

    if (isRefresh.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 0.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = stringResource(id = R.string.email), Modifier.padding(0.dp, 24.dp, 0.dp, 0.dp))
        FancyTextField(
            value = email.value,
            enabled = true,
            onValueChange = { viewModel.onEmailChange(it) },
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = stringResource(id = R.string.password))
        FancyTextField(
            value = password.value,
            enabled = true,
            onValueChange = { viewModel.onPasswordChange(it) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (error.value) {
            Text(
                text = stringResource(id = R.string.login_error),
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        PrimaryFancyButton(
            modifier = Modifier.padding(20.dp, 16.dp),
            onClick = {
                viewModel.viewModelScope.launch {
                    viewModel.onLogin(navController)
                }
            },
            text = stringResource(id = R.string.login)
        )
        SecondaryFancyButton(
            modifier = Modifier.padding(20.dp, 4.dp),
            onClick = {
                viewModel.resetState()
                viewModel.resetFields()
                navController.navigate(Screen.Register.route)
            },
            text = stringResource(id = R.string.register)
        )

        Row(
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 0.dp)
        ) {
            IconButton(
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .padding(10.dp, 10.dp, 10.dp, 10.dp),
                onClick = { viewModel.signInGoogle(context) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = "Google"
                )
            }

//            IconButton(
//                modifier = Modifier
//                    .width(101.dp)
//                    .height(100.dp)
//                    .padding(30.dp, 10.dp, 10.dp, 10.dp),
//                onClick = {viewModel.signInGithub(context)}
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.github_logo),
//                    contentDescription = "GitHub"
//                )
//            }
        }
    }
}
