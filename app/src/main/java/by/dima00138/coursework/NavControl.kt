package by.dima00138.coursework

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import by.dima00138.coursework.viewModels.AdminPanelVM
import by.dima00138.coursework.viewModels.BoardVM
import by.dima00138.coursework.viewModels.MainVM
import by.dima00138.coursework.viewModels.ProfileVM
import by.dima00138.coursework.viewModels.Screen
import by.dima00138.coursework.views.AboutScreen
import by.dima00138.coursework.views.AdminPanelScreen
import by.dima00138.coursework.views.BoardScreen
import by.dima00138.coursework.views.LoginScreen
import by.dima00138.coursework.views.MainScreen
import by.dima00138.coursework.views.MoreScreen
import by.dima00138.coursework.views.OrdersScreen
import by.dima00138.coursework.views.ProfileScreen
import by.dima00138.coursework.views.RegistrationScreen

@Composable
fun NavControl(modifier: Modifier, navController: NavHostController) {
    //VMs
    val profileVM = hiltViewModel<ProfileVM>()
    val mainVM = hiltViewModel<MainVM>()
    val boardVM = hiltViewModel<BoardVM>()
    val adminPanelVM = hiltViewModel<AdminPanelVM>()

    NavHost(navController, startDestination = Screen.SearchNested.route, modifier) {
        navigation(startDestination = Screen.Search.route, route = Screen.SearchNested.route) {
            composable(Screen.Search.route) { MainScreen(navController, mainVM) }

        }
        composable(Screen.Board.route) { BoardScreen(navController, boardVM) }
        composable(Screen.Orders.route) { OrdersScreen(navController) }
        navigation(startDestination = Screen.More.route, route = Screen.MoreNested.route) {
            composable(Screen.More.route) { MoreScreen(navController) }
            composable(Screen.About.route) { AboutScreen(navController)}
        }

        navigation(startDestination = Screen.Login.route, route = Screen.ProfileNested.route) {
            composable(Screen.Login.route) { LoginScreen(navController, profileVM) }
            composable(Screen.Register.route) { RegistrationScreen(navController, profileVM) }
            composable(Screen.Profile.route) { ProfileScreen(navController, profileVM) }
            composable(Screen.AdminPanel.route) { AdminPanelScreen(navController, adminPanelVM) }
        }
    }

}