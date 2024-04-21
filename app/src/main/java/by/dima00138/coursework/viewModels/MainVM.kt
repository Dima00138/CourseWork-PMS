package by.dima00138.coursework.viewModels

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

    enum class Inputs{
        None, FromInput, ToInput, WhenDateInput, ReturnDateInput
    }

class MainVM : ViewModel() {
    var showList = MutableLiveData(false)
    var input = MutableLiveData<Inputs>(Inputs.None)
    var searchText = MutableLiveData("")
    var from = MutableLiveData("")
    var to = MutableLiveData("")
    var whenDate = MutableLiveData(LocalDate.now())
    var returnDate = MutableLiveData<LocalDate?>(null)
    val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5")

    fun onShowListChange(state: Boolean, inputs : Inputs) {
        showList.value = state
        input.value = inputs

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
                val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy")
                whenDate.value = LocalDate.parse(state, formatter)
            }
            Inputs.ReturnDateInput -> {
                val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy")
                returnDate.value = LocalDate.parse(state, formatter)
            }
        }

    }
}