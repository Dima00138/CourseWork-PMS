package by.dima00138.coursework.views

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import by.dima00138.coursework.R
import by.dima00138.coursework.ui.theme.FancyButton
import by.dima00138.coursework.viewModels.ProfileVM
import by.dima00138.coursework.viewModels.Screen
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavHostController, viewModel: ProfileVM) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state.value.user) {
        if (state.value.user == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }

        }
    }

    LaunchedEffect(key1 = state.value.signInError) {
        if (!state.value.isSignInSuccessful && state.value.signInError != null) {
            Toast.makeText(context,
                state.value.signInError, Toast.LENGTH_LONG
            ).show()
            viewModel.resetState()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp, 0.dp)
            .verticalScroll(rememberScrollState())) {
        FancyButtonList(
            isAdmin = (state.value.user?.role  == "admin") || (state.value.user?.role == "manager"),
            onOrdersClicked = {
                navController.navigate(Screen.Orders.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            },
            onLogoutClicked = {
                viewModel.viewModelScope.launch {
                    viewModel.onLogOut(navController)
                }
            },
            onAdminClicked = {
                navController.navigate(Screen.AdminPanel.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
        )
    }
}

@Composable
fun FancyButtonList(
    modifier: Modifier = Modifier,
    onOrdersClicked: () -> Unit = {},
    onPassengersClicked: () -> Unit = {},
    onFavoritesClicked: () -> Unit = {},
    onMessagesClicked: () -> Unit = {},
    onLogoutClicked: () -> Unit = {},
    isAdmin: Boolean = false,
    onAdminClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp, 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FancyButton(
            text = stringResource(id = R.string.orders),
            icon = Icons.Filled.ShoppingCart,
            onClick = onOrdersClicked
        )
        FancyButton(
            text = stringResource(id = R.string.passengers),
            icon = Icons.Filled.PeopleAlt,
            onClick = onPassengersClicked
        )
        FancyButton(
            text = stringResource(id = R.string.favorite),
            icon = Icons.Filled.Favorite,
            onClick = onFavoritesClicked
        )
        FancyButton(
            text = stringResource(id = R.string.messenges),
            icon = Icons.Filled.Email,
            onClick = onMessagesClicked
        )
        if (isAdmin) {
            FancyButton(
                text = stringResource(id = R.string.admin_panel),
                icon = Icons.Filled.AdminPanelSettings,
                onClick = onAdminClicked
            )
        }
        FancyButton(
            text = stringResource(id = R.string.logout),
            icon = Icons.AutoMirrored.Filled.Logout,
            onClick = onLogoutClicked
        )
    }
}