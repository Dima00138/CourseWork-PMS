package by.dima00138.coursework.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import by.dima00138.coursework.R
import by.dima00138.coursework.viewModels.NavigationBarVM
import by.dima00138.coursework.viewModels.Screen
import by.dima00138.coursework.viewModels.TopBarVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, viewModel: TopBarVM = viewModel()) {
    val title : String by viewModel.title.observeAsState("")
    val showNavIcon: Boolean by viewModel.showNavIcon.observeAsState(true)
    TopAppBar(
        navigationIcon = {
            if (showNavIcon) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go Back"
                    )
                }
            }
        },
        title = { TitleTopAppBar(title = title) },
        actions = {IconButton(onClick = { navController.navigate(Screen.Orders.route) }) {
           Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = "Cart")
        }}
    )
}

@Composable
fun TitleTopAppBar(title: String) {

    Row{
        Icon(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "Logo",
            Modifier.height(50.dp))
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}