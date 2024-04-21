package by.dima00138.coursework.viewModels

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import by.dima00138.coursework.R

class NavigationBarVM(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _selectedItem : MutableLiveData<Int> = MutableLiveData(0)
    var selectedItem : LiveData<Int> = _selectedItem
    val items = listOf( Screen.Search, Screen.Board, Screen.Orders, Screen.Profile, Screen.More)
//    val icons = listOf(R.drawable.search_icon, R.drawable.board_icon, R.drawable.orders_icon, R.drawable.profile_icon, R.drawable.more_icon)

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

sealed class Screen(val route: String, @StringRes val resourceId: Int,@DrawableRes val icon: Int) {
    data object Search : Screen("Search", R.string.search, R.drawable.search_icon)
    data object Board : Screen("board", R.string.board, R.drawable.board_icon)
    data object Orders : Screen("orders", R.string.orders, R.drawable.orders_icon)
    data object Profile : Screen("profile", R.string.profile,R.drawable.profile_icon )
    data object More : Screen("more", R.string.more, R.drawable.more_icon)
}
