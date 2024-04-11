package by.dima00138.coursework.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TopBarVM() : ViewModel() {

    val title = MutableLiveData<String>("")
    val showNavIcon = MutableLiveData<Boolean>(false);

}