package by.dima00138.coursework.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import by.dima00138.coursework.Models.ScheduleItem
import by.dima00138.coursework.Models.Station
import by.dima00138.coursework.services.Firebase
import com.google.firebase.firestore.Filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

enum class Inputs{
        None, FromInput, ToInput, WhenDateInput, ReturnDateInput
    }

@HiltViewModel
class MainVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val firebase: Firebase
) : ViewModel() {
    var showList = MutableStateFlow(false)
    var input = MutableStateFlow<Inputs>(Inputs.None)
    var showDatePicker = MutableStateFlow(false)
    var datePick = MutableStateFlow<Inputs>(Inputs.None)
    var searchText = MutableStateFlow("")
    var from = MutableStateFlow("")
    var to = MutableStateFlow("")
    var whenDate = MutableStateFlow(LocalDate.now())
    var returnDate = MutableStateFlow<LocalDate?>(null)
    val stations = MutableStateFlow<List<Station>>(emptyList())
    val searchResult = MutableStateFlow<List<ScheduleItem>>(emptyList())
    val currentPage = MutableStateFlow<Int>(0)
    val isRefresh = MutableStateFlow(false)

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            isRefresh.update { true }
            stations.update {
                firebase.getStations() ?: emptyList()
            }
            isRefresh.update { false }
        }
    }

    fun onShowListChange(state: Boolean, inputs : Inputs) {
        showList.value = state
        input.value = inputs
    }

    fun onCurrentPageChange(state: Int) {
        currentPage.update {
            state
        }
    }

    fun onShowDatePickerChange(state: Boolean, inputs : Inputs) {
        showDatePicker.value = state
        datePick.value = inputs
    }

    fun onSearchTextChange(state: String) {
        searchText.value = state
    }

    fun onSearchButtonClick(navController: NavController) {
        if (from.value == "" || to.value == "") return
        try {
            isRefresh.update { true }
            viewModelScope.launch {
                var filter: Filter = Filter.and(
                    Filter.equalTo("from", stations.value.filter { it.name == from.value }[0].id),
                    Filter.equalTo("to", stations.value.filter { it.name == to.value }[0].id),
                    Filter.greaterThanOrEqualTo("date", whenDate.value.atStartOfDay().toEpochSecond(ZoneOffset.UTC))
                )

                if (returnDate.value != null && returnDate.value!! >= whenDate.value) {
                    filter = Filter.and(
                        filter,
                        Filter.lessThanOrEqualTo("date", returnDate.value?.atStartOfDay()?.toEpochSecond(ZoneOffset.UTC))
                    )
                }

                searchResult.update {
                    firebase.getSearchSchedule(filter)
                }
            }
            navController.navigate(Screen.SearchResult.route)
        }catch (e: Exception) {
            Log.d("D", e.message.toString())
        }
        finally {
            isRefresh.update { false }
        }
    }

    fun onScheduleItemClick(navController: NavController, item: ScheduleItem) {
        navController.navigate(Screen.Ticket.route + item.id)
    }

    fun onValueChange(index:Inputs, state: String) {
        when (index) {
            Inputs.None -> return
            Inputs.FromInput -> from.value = state
            Inputs.ToInput -> to.value = state
            Inputs.WhenDateInput -> {
                if (whenDate.value < LocalDate.now()) return
                whenDate.value = Instant.ofEpochMilli(state.toLong()).atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }

            Inputs.ReturnDateInput -> {
                returnDate.value =
                    Instant.ofEpochMilli(state.toLong()).atZone(ZoneId.systemDefault())
                        .toLocalDate()
            }
        }
    }
}