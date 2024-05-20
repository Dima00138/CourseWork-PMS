package by.dima00138.coursework.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import by.dima00138.coursework.Models.User
import by.dima00138.coursework.R
import by.dima00138.coursework.ui.theme.FancyTextField
import by.dima00138.coursework.ui.theme.PrimaryFancyButton
import by.dima00138.coursework.ui.theme.SecondaryFancyButton
import by.dima00138.coursework.ui.theme.Validator
import by.dima00138.coursework.viewModels.PassengerVM
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassengerPage(navController: NavController, viewModel: PassengerVM) {
    val passengers = viewModel.passengers.collectAsStateWithLifecycle()
    val isRefreshing = viewModel.isRefreshing.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = Modifier
            .fillMaxSize()
            .pullToRefresh(
                isRefreshing = isRefreshing.value,
                state = pullToRefreshState,
                onRefresh = { viewModel.refresh() }),
        isRefreshing = isRefreshing.value,
        onRefresh = {viewModel.refresh()}) {

        Column {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(passengers.value) { user ->
                    UserListItem(
                        user = user,
                        onClick = { viewModel.onSelectPassenger(user, navController) }
                    )
                }
                item {
                    PrimaryFancyButton(
                        text = stringResource(id = R.string.create_passenger),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        onClick = { viewModel.onSelectPassenger(User(), navController) }
                    )
                }
            }
        }
    }

}

@Composable
fun UserListItem(
    user: User,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .height(100.dp)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val maxLength = 20

            val displayText = if (user.fullName.length > maxLength) {
                user.fullName.take(maxLength) + "..."
            } else {
                user.fullName
            }
            Text(
                displayText,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Text(user.passport)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassengerEditPage(
    navController: NavController, viewModel: PassengerVM
) {
    val user by viewModel.selectedPassenger.collectAsStateWithLifecycle()
    var fullName by remember { mutableStateOf(user.fullName) }
    var passport by remember { mutableStateOf(user.passport) }
    var birthdate by remember { mutableStateOf(user.birthdate) }
    var email by remember { mutableStateOf(user.email) }
    var showDatePicker by remember{ mutableStateOf(false) }

    var isFullNameValid by remember { mutableStateOf(true) }
    var isPassportValid by remember { mutableStateOf(true) }
    var isBirthdateValid by remember { mutableStateOf(true) }
    var isEmailValid by remember { mutableStateOf(true) }

    if (showDatePicker) {
        val selectedDateState = rememberDatePickerState()
        val confirmEnabled = remember {
            derivedStateOf { selectedDateState.selectedDateMillis != null }
        }
        DatePickerDialog(onDismissRequest = {
            showDatePicker = false
        }, confirmButton = {
            TextButton(
                onClick = {
                    birthdate = Instant.ofEpochMilli(selectedDateState.selectedDateMillis ?: 0)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    showDatePicker = false
                },
                enabled = confirmEnabled.value
            ) {
                Text("OK")
            }

        }, dismissButton = {
            TextButton(
                onClick = {
                    showDatePicker = false
                }
            ) {
                Text("Cancel")
            }
        }) {
            DatePicker(
                state = selectedDateState,
                showModeToggle = false,
            )
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        FancyTextField(
            modifier = Modifier.padding(vertical = 8.dp),
            enabled = true,
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text(stringResource(id = R.string.fullname)) }
        )
        Validator(
            value = fullName,
            validator = { viewModel.validator.fullName(it) },
            errorMessage = stringResource(id = R.string.fullname_error),
            onValidationResult = {isFullNameValid = it}
        )

        FancyTextField(
            modifier = Modifier.padding(vertical = 8.dp),
            enabled = true,
            value = passport,
            onValueChange = { passport = it },
            label = { Text(stringResource(id = R.string.passport)) }
        )
        Validator(
            value = passport,
            validator = { viewModel.validator.passport(it) },
            errorMessage = stringResource(id = R.string.passport_error),
            onValidationResult = {isPassportValid = it}
        )

        FancyTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable { showDatePicker = true },
            enabled = false,
            value = birthdate,
            onValueChange = { birthdate = it },
            label = { Text(stringResource(id = R.string.birthdate)) }
        )
        Validator(
            value = birthdate,
            validator = { viewModel.validator.birthdate(it) },
            errorMessage = stringResource(id = R.string.birthdate_error),
            onValidationResult = {isBirthdateValid = it}
        )
        FancyTextField(
            modifier = Modifier.padding(vertical = 8.dp),
            enabled = true,
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(id = R.string.email)) }
        )
        Validator(
            value = email,
            validator = { viewModel.validator.email(it) },
            errorMessage = stringResource(id = R.string.email_error),
            onValidationResult = {isEmailValid = it}
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            PrimaryFancyButton(
                modifier = Modifier.padding(16.dp, 20.dp),
                text = stringResource(id = R.string.save),
                onClick = {
                val updatedUser = user.copy(
                    fullName = fullName,
                    passport = passport,
                    birthdate = birthdate,
                    email = email
                )
                viewModel.onSaveUser(updatedUser, navController)
            })
            SecondaryFancyButton(
                modifier = Modifier.padding(16.dp, 0.dp),
                text = stringResource(id = R.string.cancel),
                onClick = { navController.popBackStack() })
        }
    }
}