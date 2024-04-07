package by.dima00138.coursework.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import by.dima00138.coursework.R

class NavigationBarVM(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _selectedItem : MutableLiveData<Int> = MutableLiveData(0)
    var selectedItem : LiveData<Int> = _selectedItem
    val items = listOf( "Search", "Board", "Orders", "Profile", "More")
    val icons = listOf<Int>(R.drawable.search_icon, R.drawable.board_icon, R.drawable.orders_icon, R.drawable.profile_icon, R.drawable.more_icon)

    fun selectedItemChange(index : Int) {
        _selectedItem.value = index
    }
}