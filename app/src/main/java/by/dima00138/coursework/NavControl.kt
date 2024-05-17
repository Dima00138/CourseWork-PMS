package by.dima00138.coursework

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import by.dima00138.coursework.viewModels.AdminPanelVM
import by.dima00138.coursework.viewModels.BoardVM
import by.dima00138.coursework.viewModels.MainVM
import by.dima00138.coursework.viewModels.OrderVM
import by.dima00138.coursework.viewModels.PassengerVM
import by.dima00138.coursework.viewModels.ProfileVM
import by.dima00138.coursework.viewModels.Screen
import by.dima00138.coursework.viewModels.TicketVM
import by.dima00138.coursework.views.AboutScreen
import by.dima00138.coursework.views.AdminPanelScreen
import by.dima00138.coursework.views.BoardScreen
import by.dima00138.coursework.views.LoginScreen
import by.dima00138.coursework.views.MainScreen
import by.dima00138.coursework.views.MoreScreen
import by.dima00138.coursework.views.OrderDetailsPage
import by.dima00138.coursework.views.OrdersScreen
import by.dima00138.coursework.views.PassengerEditPage
import by.dima00138.coursework.views.PassengerPage
import by.dima00138.coursework.views.ProfileScreen
import by.dima00138.coursework.views.RegistrationScreen
import by.dima00138.coursework.views.SearchResultPage
import by.dima00138.coursework.views.TicketPage

@Composable
fun NavControl(modifier: Modifier, navController: NavHostController) {
    //VMs
    val profileVM = hiltViewModel<ProfileVM>()
    val mainVM = hiltViewModel<MainVM>()
    val orderVM = hiltViewModel<OrderVM>()
    val boardVM = hiltViewModel<BoardVM>()
    val ticketVM = hiltViewModel<TicketVM>()
    val passengerVM = hiltViewModel<PassengerVM>() //HZ

    NavHost(navController, startDestination = Screen.SearchNested.route, modifier) {
        navigation(startDestination = Screen.Search.route, route = Screen.SearchNested.route) {
            composable(Screen.Search.route) { MainScreen(navController, mainVM) }
            composable(Screen.SearchResult.route) { SearchResultPage(navController, mainVM)}
            composable(
                route = Screen.Ticket.route + "{ticket}",
                arguments = listOf(
                    navArgument("ticket") { type = NavType.StringType }
                )
            ) {
                val ticket = it.arguments?.getString("ticket") ?: ""
                ticketVM.updateScheduleItemId(ticket)
                TicketPage(navController, ticketVM, ticket)
            }
        }
        composable(Screen.Board.route) { BoardScreen(navController, boardVM) }
        navigation(startDestination = Screen.Orders.route, route = Screen.OrdersNested.route) {
            composable(Screen.Orders.route) { OrdersScreen(navController, orderVM) }
            composable(route = Screen.OrdersDetail.route) {
                OrderDetailsPage(orderVM)
            }
        }
        navigation(startDestination = Screen.More.route, route = Screen.MoreNested.route) {
            composable(Screen.More.route) { MoreScreen(navController) }
            composable(Screen.About.route) { AboutScreen(navController)}
        }

        navigation(startDestination = Screen.Login.route, route = Screen.ProfileNested.route) {
            composable(Screen.Login.route) { LoginScreen(navController, profileVM) }
            composable(Screen.Register.route) { RegistrationScreen(navController, profileVM) }
            composable(Screen.Profile.route) { ProfileScreen(navController, profileVM) }
            navigation(startDestination = Screen.Passenger.route, route = Screen.PassengerNested.route) {
                composable(Screen.Passenger.route) { PassengerPage(navController, passengerVM)}
                composable(Screen.PassengerEdit.route) { PassengerEditPage(navController, passengerVM) }
            }
            composable(Screen.AdminPanel.route) { AdminPanelScreen(navController, hiltViewModel<AdminPanelVM>()) }
        }
    }

}