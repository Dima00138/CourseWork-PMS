package by.dima00138.coursework.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import by.dima00138.coursework.ui.theme.CourseWorkTheme
import by.dima00138.coursework.viewModels.BoardVM
import by.dima00138.coursework.viewModels.NavigationBarVM

@Composable
fun BottomNavBar( navController: NavController, viewModel: NavigationBarVM = hiltViewModel<NavigationBarVM>()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar(
        contentColor = Color.Gray,
        modifier = Modifier.padding(0.dp).shadow(elevation = 4.dp,
            ambientColor = Color.Black, spotColor = Color.Black),
    ) {
        viewModel.items.forEachIndexed { index, item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            val tintColor by animateColorAsState(
                if (selected) Color.Blue else Color.Gray
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        tint = tintColor
                    )
                },
                label = { Text(stringResource(item.resourceId), color = tintColor) },
                selected = selected,
                onClick = { viewModel.selectedItemChange(index, navController) },
                modifier = Modifier.padding(4.dp, 0.dp, 4.dp, 0.dp)
            )
        }
    }
}