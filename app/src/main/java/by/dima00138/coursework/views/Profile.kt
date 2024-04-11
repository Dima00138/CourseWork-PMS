package by.dima00138.coursework.views

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import by.dima00138.coursework.ui.theme.CourseWorkTheme

@Composable
fun ProfileScreen(navController: NavController) {
    CourseWorkTheme {
        Text(text = "Profile Screen")
    }
}