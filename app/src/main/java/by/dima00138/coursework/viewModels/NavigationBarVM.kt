package by.dima00138.coursework.viewModels

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import by.dima00138.coursework.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NavigationBarVM@Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _selectedItem : MutableStateFlow<Int> = MutableStateFlow(0)
    val items = listOf( Screen.SearchNested, Screen.Board, Screen.Orders, Screen.ProfileNested, Screen.MoreNested)

    fun selectedItemChange(index : Int, navController: NavController) {
        if (_selectedItem.value == index) return
        _selectedItem.value = index
        navController.navigate(items[index].route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int, @DrawableRes val icon: Int) {
    data object SearchNested : Screen("SearchNested", R.string.search, R.drawable.search_icon)
    data object Search : Screen("Search", R.string.search, R.drawable.search_icon)
    data object SearchResult : Screen("search/result", R.string.search, R.drawable.search_icon)
    data object Ticket : Screen("search/result/", R.string.book_ticket, R.drawable.search_icon)
    data object Board : Screen("Board", R.string.board, R.drawable.board_icon)
    data object OrdersNested : Screen("OrdersNested", R.string.orders, R.drawable.orders_icon)
    data object Orders : Screen("Orders", R.string.orders, R.drawable.orders_icon)
    data object OrdersDetail : Screen("OrderDetail", R.string.orders, R.drawable.orders_icon)
    data object ProfileNested : Screen("profileNested", R.string.profile,R.drawable.profile_icon )
    data object Profile : Screen("Profile", R.string.profile,R.drawable.profile_icon )
//    data object Github : Screen("Github", R.string.profile, R.drawable.profile_icon )
data object Register : Screen("Register", R.string.register, R.drawable.profile_icon)
    data object Login : Screen("Login", R.string.login, R.drawable.profile_icon)
    data object Passenger : Screen("Passengers", R.string.passengers, R.drawable.profile_icon)
    data object PassengerNested : Screen("PassengersNested", R.string.passengers, R.drawable.profile_icon)
    data object PassengerEdit : Screen("PassengersEdit", R.string.passenger_edit, R.drawable.profile_icon)
    data object AdminPanel : Screen("AdminPanel", R.string.admin_panel, R.drawable.profile_icon)
    data object MoreNested : Screen("MoreNested", R.string.more, R.drawable.more_icon)
    data object More : Screen("More", R.string.more, R.drawable.more_icon)
    data object About : Screen("About", R.string.about, R.drawable.more_icon)
}
