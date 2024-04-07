package by.dima00138.coursework.views

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import by.dima00138.coursework.ui.theme.CourseWorkTheme

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Surface (
        modifier = modifier,
        content = { MainScreenContent() }
    )
}

@Composable
fun MainScreenContent() {
    CourseWorkTheme {
        Text(text = "Main Screen")
    }
}