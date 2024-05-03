package by.dima00138.coursework.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.dima00138.coursework.Firebase
import by.dima00138.coursework.Models.IModel
import by.dima00138.coursework.Models.ScheduleItem
import by.dima00138.coursework.Models.Station
import by.dima00138.coursework.Models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class Tables(val str: String) {
    Users("Users"),
    Stations("Stations"),
    Schedule("Schedule")
}

@HiltViewModel
class AdminPanelVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val firebase: Firebase
) : ViewModel() {
    private var user : User? = null

    init {
        viewModelScope.launch {
            user = firebase.getUser()

            _tableData.update {
                mutableMapOf(
                    Tables.Users to mutableListOf(
                        user ?: User(), user ?: User(), user ?: User(),
                        user ?: User(), user ?: User(), user ?: User(),
                        user ?: User(), user ?: User(), user ?: User(),
                        user ?: User(), user ?: User(), user ?: User(),
                    ),
                    Tables.Stations to mutableListOf(
                        Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"),
                        Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"),
                        Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"),
                        Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"),
                        Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"),
                        Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"),
                        Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"),
                        Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"),
                        Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"),
                        Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"),
                        Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"),
                        Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"),
                        Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"),
                        Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"), Station(id = "434", name = "ssd"),
                    ),
                    Tables.Schedule to mutableListOf(),
                )
            }
        }
    }
    private val _tableData = MutableStateFlow<Map<Tables, List<IModel>>>(
        mutableMapOf(

    )
    )
    val tableData: StateFlow<Map<Tables, List<IModel>>> = _tableData
    val selectedTable = MutableStateFlow(Tables.Stations)

    fun onSelectedTable(state: Tables) {
        selectedTable.update {
            state
        }
    }

    fun getExampleElement() : IModel {
        return when (selectedTable.value) {
            Tables.Users -> User()
            Tables.Stations -> Station()
            Tables.Schedule -> ScheduleItem()
        }
    }

    fun onCreateItem(tableName: Tables, item: IModel) {
        _tableData.update { currentData ->
            val updatedData = currentData.toMutableMap()
            updatedData[tableName] = updatedData[tableName]?.toMutableList()?.apply { add(item) } ?: mutableListOf(item)
            updatedData
        }
    }

    fun onUpdateItem(tableName: Tables, oldItem: Any, newItem: IModel) {
        _tableData.update { currentData ->
            val updatedData = currentData.toMutableMap()
            val tableData = updatedData[tableName]?.toMutableList()
            val index = tableData?.indexOf(oldItem)
            if (index != null && index != -1) {
                tableData[index] = newItem
            }
            updatedData
        }
    }

    fun onDeleteItem(tableName: Tables, item: IModel) {
        _tableData.update { currentData ->
            val updatedData = currentData.toMutableMap()
            val tableData = updatedData[tableName]?.toMutableList()
            tableData?.remove(item)
            updatedData
        }
    }
}