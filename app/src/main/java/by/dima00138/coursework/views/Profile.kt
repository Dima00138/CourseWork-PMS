package by.dima00138.coursework.views

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import by.dima00138.coursework.Firebase
import by.dima00138.coursework.R
import by.dima00138.coursework.ui.theme.FancyTextField
import by.dima00138.coursework.ui.theme.PrimaryFancyButton
import by.dima00138.coursework.ui.theme.SecondaryFancyButton
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
            onOrdersClicked = {
                navController.navigate(Screen.Orders.route)
            },
            onLogoutClicked = {
                viewModel.viewModelScope.launch {
                    viewModel.onLogOut(navController)
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
    onLogoutClicked: () -> Unit = {}
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
            text = "Passengers",
            icon = Icons.Filled.PeopleAlt,
            onClick = onPassengersClicked
        )
        FancyButton(
            text = "Favorites",
            icon = Icons.Filled.Favorite,
            onClick = onFavoritesClicked
        )
        FancyButton(
            text = "Messages",
            icon = Icons.Filled.Email,
            onClick = onMessagesClicked
        )
        FancyButton(
            text = stringResource(id = R.string.logout),
            icon = Icons.AutoMirrored.Filled.Logout,
            onClick = onLogoutClicked
        )
    }
}

@Composable
fun FancyButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color.Gray.copy(alpha = 0.1f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Blue
            )
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.width(32.dp))
        }
    }
}