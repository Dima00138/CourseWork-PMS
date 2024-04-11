package by.dima00138.coursework

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import by.dima00138.coursework.viewModels.Screen
import by.dima00138.coursework.views.BoardScreen
import by.dima00138.coursework.views.MainScreen
import by.dima00138.coursework.views.MainScreenContent
import by.dima00138.coursework.views.MoreScreen
import by.dima00138.coursework.views.OrdersScreen
import by.dima00138.coursework.views.ProfileScreen

@Composable
fun NavControl(modifier: Modifier, navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Search.route, modifier) {
        composable(Screen.Search.route) { MainScreen(navController) }
        composable(Screen.Board.route) { BoardScreen(navController) }
        composable(Screen.Orders.route) { OrdersScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.More.route) { MoreScreen(navController) }

    }

}