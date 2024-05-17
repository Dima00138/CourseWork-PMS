package by.dima00138.coursework.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import by.dima00138.coursework.R
import by.dima00138.coursework.services.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopBarVM @Inject constructor(
    private val firebase: Firebase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val title = MutableStateFlow("")
    val showNavIcon = MutableStateFlow(false)

    fun changeTitle(navController: NavController, route: String?) {
        viewModelScope.launch {
            val user = firebase.getUser()

            Log.d("DEBUG", user.toString())

            if (route == Screen.Profile.route) {
                val name: String = user!!.fullName.split(' ')[0]
                title.update {
                    name
                }
            }else if (route == Screen.SearchResult.route) {
                title.update {
                    firebase.context.getString(R.string.search_result)
                }
            }else if (route == Screen.Board.route) {
                title.update {
                    firebase.getStations()?.get(0)?.name ?: route.toString()
                }
            }else if (route == Screen.Ticket.route + "{ticket}") {
                title.update {
                    firebase.context.getString(R.string.book_ticket)
                }
            }else {
                title.update {
                    route.toString()
                }
            }
        }
    }

//    fun cartClicked(navController: NavController) {
//        navController.navigate(Screen.Orders.route) {
//            popUpTo(navController.graph.findStartDestination().id) {
//                saveState = true
//            }
//            launchSingleTop = true
//            restoreState = true
//        }
//    }
}