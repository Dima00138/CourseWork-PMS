package by.dima00138.coursework.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

class TopBarVM : ViewModel() {

    val title = MutableLiveData("")
    val showNavIcon = MutableLiveData(false)

    fun cartClicked(navController: NavController) {
        navController.navigate(Screen.Orders.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

}