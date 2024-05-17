package by.dima00138.coursework.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import by.dima00138.coursework.R
import by.dima00138.coursework.ui.theme.BlueAnalog2
import by.dima00138.coursework.viewModels.TopBarVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, viewModel: TopBarVM = hiltViewModel<TopBarVM>()) {
    val title : String by viewModel.title.collectAsStateWithLifecycle("")
    val showNavIcon: Boolean by viewModel.showNavIcon.collectAsStateWithLifecycle(true)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    LaunchedEffect(key1 = currentDestination) {
        viewModel.changeTitle(navController, currentDestination?.route)
    }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BlueAnalog2,
        ),
        navigationIcon = {
            if (showNavIcon) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go Back",
                        tint = Color.White
                    )
                }
            }
        },
        title = { TitleTopAppBar(title = if (title == "null") LocalContext.current.getString(R.string.search) else title) },
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "Logo",
                tint = Color.White,
                modifier = Modifier.height(50.dp).padding(10.dp, 0.dp)
            )
//            IconButton(onClick = { viewModel.cartClicked(navController) }) {
//           Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = "Cart", tint = Color.White) }
        }
    )
}

@Composable
fun TitleTopAppBar(title: String) {

    Row{
        Text(
            modifier = Modifier.weight(700F),
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}