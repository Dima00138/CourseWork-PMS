package by.dima00138.coursework.viewModels

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import by.dima00138.coursework.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class BoardVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
    var selectedTabIndex = MutableStateFlow(0)
    val tabs = listOf(TabsBoard.Departure, TabsBoard.Arrival)

    fun onSelectedTabChange(state : Int) {
        selectedTabIndex.value = state
    }
}

sealed class TabsBoard(val tabId : Int, @StringRes val resourceId : Int) {
    data object Departure : TabsBoard(0, R.string.departure)
    data object Arrival : TabsBoard(1, R.string.arrival)
}