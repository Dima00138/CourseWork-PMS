package by.dima00138.coursework.viewModels

import android.util.Log
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
import java.util.UUID
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
                    Tables.Users to (firebase.getUsers() ?: emptyList()),
                    Tables.Stations to (firebase.getStations() ?: emptyList()),
                    Tables.Schedule to (firebase.getSchedule() ?: emptyList()),
                )
            }
        }
    }
    private val _tableData = MutableStateFlow<Map<Tables, List<IModel>>>(
        mutableMapOf(
            Tables.Users to listOf(),
            Tables.Stations to listOf(),
            Tables.Schedule to listOf(),
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
            item.setField("id", UUID.randomUUID().toString())
            val updatedData = currentData.toMutableMap()
            updatedData[tableName] = updatedData[tableName]?.toMutableList()?.apply { add(item) } ?: listOf(item)
            firebase.createOrReplaceItem(tableName, item)
            updatedData
        }
    }

    suspend fun onUpdateItem(tableName: Tables, newItem: IModel, oldItem: IModel) {
        try {
            _tableData.update { currentData ->
                var updatedData = currentData.toMutableMap()
                val updatedList = currentData[tableName]?.toMutableList()
                val index = updatedList?.indexOf(oldItem)
                if (index != null && index != -1) {
                    updatedList[index] = newItem
                }
                updatedData = updatedData.toMutableMap().apply {
                    set(tableName, updatedList ?: emptyList<IModel>())
                }
                firebase.createOrReplaceItem(tableName, newItem)
                updatedData
            }
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
        }
    }

    fun onDeleteItem(tableName: Tables, item: IModel) {
        try {
        _tableData.update { currentData ->
            var updatedData = currentData.toMutableMap()
            val updatedList = currentData[tableName]?.toMutableList()
            updatedList?.remove(item)
            updatedData = updatedData.toMutableMap().apply {
                set(tableName, updatedList ?: emptyList<IModel>())
            }
            updatedData
        }
        }
        catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
        }
    }
}