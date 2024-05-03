package by.dima00138.coursework.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

enum class Inputs{
        None, FromInput, ToInput, WhenDateInput, ReturnDateInput
    }

@HiltViewModel
class MainVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
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
    val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5",
                        "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5")

    fun onShowListChange(state: Boolean, inputs : Inputs) {
        showList.value = state
        input.value = inputs
    }

    fun onShowDatePickerChange(state: Boolean, inputs : Inputs) {
        showDatePicker.value = state
        datePick.value = inputs
    }

    fun onSearchTextChange(state: String) {
        searchText.value = state
    }

    fun onValueChange(index:Inputs, state: String) {
        when (index) {
            Inputs.None -> return
            Inputs.FromInput -> from.value = state
            Inputs.ToInput -> to.value = state
            Inputs.WhenDateInput -> {
                whenDate.value = Instant.ofEpochMilli(state.toLong()).atZone(ZoneId.systemDefault()).toLocalDate()
            }
            Inputs.ReturnDateInput -> {
                returnDate.value = Instant.ofEpochMilli(state.toLong()).atZone(ZoneId.systemDefault()).toLocalDate()
            }
        }

    }
}