package by.dima00138.coursework.viewModels


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import by.dima00138.coursework.Models.User
import by.dima00138.coursework.R
import by.dima00138.coursework.services.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderVM @Inject constructor(
    private val firebase: Firebase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val error = MutableStateFlow("")
    val selectedTicket = MutableStateFlow<FancyTicket?>(null)
    val tickets = MutableStateFlow<List<FancyTicket>>(emptyList())
    val isRefreshing = MutableStateFlow(false)

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing.update { true }
            val user = firebase.getUser()
            if (user == null) {
                error.update {
                    firebase.context.getString(R.string.view_orders)
                }
                tickets.update {
                    emptyList()
                }
            }
            else {
                tickets.update {
                    firebase.getFancyTickets(user)
                }
                if (tickets.value.isEmpty()) {
                    error.update {
                        firebase.context.getString(R.string.no_tickets)
                    }
                } else error.update { "" }
            }
            isRefreshing.update { false }
        }
    }

    fun onTicketClicked(ticket: FancyTicket, navController: NavController) {
        selectedTicket.update {
            ticket
        }
        navController.navigate(Screen.OrdersDetail.route)
    }

    data class FancyTicket(
        val id: String,
        val trainId: String,
        val from: String,
        val to: String,
        val date: String,
        val user: User,
        val numberOfSeat: String
        )
}