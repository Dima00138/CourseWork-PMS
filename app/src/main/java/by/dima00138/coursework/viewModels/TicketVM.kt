package by.dima00138.coursework.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import by.dima00138.coursework.Models.ScheduleItem
import by.dima00138.coursework.Models.Ticket
import by.dima00138.coursework.Models.Train
import by.dima00138.coursework.Models.User
import by.dima00138.coursework.R
import by.dima00138.coursework.services.Firebase
import com.google.firebase.firestore.Filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TicketVM @Inject constructor(
    private val firebase: Firebase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val scheduleItemId = MutableStateFlow("")
    val error = MutableStateFlow("")
    val showPopUp = MutableStateFlow(false)
    val scheduleItem = MutableStateFlow(ScheduleItem())
    val selectedTicket = MutableStateFlow(Ticket())
    val tickets = MutableStateFlow(mapOf<Train, List<Ticket>>())
    val passengers = MutableStateFlow<List<User>>(listOf())
    val isRefreshing = MutableStateFlow(false)

    fun updateScheduleItemId(state: String) {
        scheduleItemId.update {
            state
        }
        val filter = Filter.equalTo("id", state)
        viewModelScope.launch {
            scheduleItem.update {
                firebase.getSearchSchedule(filter)[0]
            }
        }
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing.update { true }
            tickets.update {
                firebase.getTicketsForScheduleItem(scheduleItemId.value)
            }
            isRefreshing.update { false }
        }
    }

    fun onTicketClick(ticket: Ticket, navController: NavController) {
        viewModelScope.launch {
            val user = firebase.getUser()
            if (user == null) {
                error.update {
                    firebase.context.getString(R.string.booking)
                }
            }
            else {
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                if (LocalDateTime.parse(scheduleItem.value.date, formatter) <= LocalDateTime.now()) {
                    error.update {
                        firebase.context.getString(R.string.ticket_date_error)
                    }
                    return@launch
                }

                selectedTicket.update {
                    ticket
                }
                passengers.update {
                    firebase.getUsersWithRoot(user.id) ?: emptyList()
                }
                popUpChange(true)
            }
        }
    }

    fun popUpChange(state: Boolean) {
        showPopUp.update {
            state
        }
    }

    fun getTicket(user: User) {
        try {
            val ticket = selectedTicket.value.copy()
            selectedTicket.update {
                it.copy(
                    free = user.id
                )
            }
            firebase.createOrReplaceItem(Tables.Tickets, selectedTicket.value)

            tickets.update { currentMap ->
                currentMap.toMutableMap().also { map ->
                    val trainId = selectedTicket.value.train
                    val train = map.keys.filter { it.id == trainId }[0]
                    val ticketList = map[train]?.toMutableList() ?: mutableListOf()
                    ticketList.replaceAll { t -> if (t == ticket) selectedTicket.value else t }
                    map[train] = ticketList
                }
            }
        }catch (e: Exception) {
            Log.e("e", e.message.toString())
        }
    }
}