package by.dima00138.coursework.viewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import by.dima00138.coursework.Models.User
import by.dima00138.coursework.services.Firebase
import by.dima00138.coursework.services.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PassengerVM @Inject constructor(
    private val firebase: Firebase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val validator = Validator()
    val error = MutableStateFlow("")
    val user = MutableStateFlow(User())
    val isRefreshing = MutableStateFlow(false)
    val passengers = MutableStateFlow<List<User>>(emptyList())
    val selectedPassenger = MutableStateFlow<User>(User())

    init {
        viewModelScope.launch {
            refresh()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing.update { true }
            user.update {
                firebase.getUser() ?: User()
            }
            passengers.update {
                firebase.getPassengers(user.value.id)
            }
            isRefreshing.update { false }
        }
    }

    fun onSelectPassenger(user: User, navController: NavController) {
        selectedPassenger.update {
            user
        }
        navController.navigate(Screen.PassengerEdit.route)
    }

    @SuppressLint("SimpleDateFormat")
    fun onSaveUser(user1: User, navController: NavController) {
        try {
            if (!validator.validUser(user1)) {
                return
            }
            isRefreshing.update { true }
            passengers.update { currentData ->
                val index = currentData.indexOf(selectedPassenger.value)
                val updatedData = currentData.toMutableList()
                if (index != -1) {
                    updatedData[index] = user1
                } else {
                    val id = UUID.randomUUID().toString()
                    user1.setField("id", id)
                    user1.setField("root", user.value.id)
                    user1.setField("role", "user")
                    updatedData.add(user1)
                }
                firebase.createOrReplaceItem(Tables.Users, user1)
                updatedData
            }
            selectedPassenger.update {
                user1
            }
            navController.navigate(Screen.Passenger.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
        }
        finally {
            isRefreshing.update { false }
        }
    }
}