package by.dima00138.coursework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import by.dima00138.coursework.ui.theme.CourseWorkTheme
import by.dima00138.coursework.views.BottomNavBar
import by.dima00138.coursework.views.MainScreen
import by.dima00138.coursework.views.TopBar

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CourseWorkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    Scaffold (
                        topBar = { TopBar(navController) },
                        modifier = Modifier.padding(0.dp),
                        bottomBar = { BottomNavBar(navController) },
                    )
                    { innerPadding ->
                        NavControl(modifier = Modifier.padding(innerPadding), navController)
                    }
                }
            }
        }
    }
}