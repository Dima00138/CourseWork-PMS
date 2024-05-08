package by.dima00138.coursework.viewModels

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.dima00138.coursework.Firebase
import by.dima00138.coursework.Models.ScheduleItem
import by.dima00138.coursework.Models.Station
import by.dima00138.coursework.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val firebase: Firebase
    ) : ViewModel() {
    var selectedTabIndex = MutableStateFlow(0)
    val station = MutableStateFlow<Station?>(null)
    val tabs = listOf(TabsBoard.Departure, TabsBoard.Arrival)
    val departureSchedule = MutableStateFlow<List<ScheduleItem>?>(null)
    val arrivalSchedule = MutableStateFlow<List<ScheduleItem>?>(null)
    val currentPage = MutableStateFlow<Int>(0)

    init {
            viewModelScope.launch {
            station.update {
                firebase.getStations()?.get(0)
            }
            departureSchedule.update {
                station.value?.let { it1 -> firebase.getSchedule("departure", it1) }
            }
            arrivalSchedule.update {
                station.value?.let { it1 -> firebase.getSchedule("arrival", it1) }
            }
        }
    }

    fun onSelectedTabChange(state : Int) {
        selectedTabIndex.update { state }
        currentPage.update { 0 }
    }

    fun onCurrentPageChange(state: Int) {
        currentPage.update { state }
    }
}

sealed class TabsBoard(val tabId : Int, @StringRes val resourceId : Int) {
    data object Departure : TabsBoard(0, R.string.departure)
    data object Arrival : TabsBoard(1, R.string.arrival)
}