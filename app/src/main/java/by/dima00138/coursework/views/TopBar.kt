package by.dima00138.coursework.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import by.dima00138.coursework.R
import by.dima00138.coursework.ui.theme.BlueAnalog2
import by.dima00138.coursework.viewModels.TopBarVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, viewModel: TopBarVM = viewModel()) {
    val title : String by viewModel.title.observeAsState("")
    val showNavIcon: Boolean by viewModel.showNavIcon.observeAsState(true)
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
        title = { TitleTopAppBar(title = title) },
        actions = {IconButton(onClick = { viewModel.cartClicked(navController) }) {
           Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = "Cart", tint = Color.White)
        }}
    )
}

@Composable
fun TitleTopAppBar(title: String) {

    Row{
        Icon(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "Logo",
            tint = Color.White,
            modifier = Modifier.height(50.dp)
        )
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}