package by.dima00138.coursework.views

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import by.dima00138.coursework.ui.theme.CourseWorkTheme
import by.dima00138.coursework.viewModels.MainVM

@Composable
fun MainScreen(navController: NavController, viewModel: MainVM = viewModel()) {
    Surface (
        content = { MainScreenContent() }
    )
}

@Composable
fun MainScreenContent() {
    CourseWorkTheme {
        Text(text = "Main Screen")
    }
}