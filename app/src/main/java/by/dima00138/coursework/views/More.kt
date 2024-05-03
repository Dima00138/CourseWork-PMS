package by.dima00138.coursework.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import by.dima00138.coursework.R
import by.dima00138.coursework.ui.theme.FancyButton
import by.dima00138.coursework.viewModels.Screen

@Composable
fun MoreScreen(navController: NavController) {
    Column(Modifier
        .fillMaxWidth()
        .padding(16.dp, 0.dp)) {
        FancyButton(
            modifier = Modifier.padding(0.dp, 24.dp),
            text = stringResource(id = R.string.about),
            icon = Icons.Outlined.ErrorOutline,
            onClick = {
                navController.navigate(Screen.About.route)
        })
    }
}

@Composable
fun AboutScreen(navController: NavController) {
    val appVersion = "0.2"
    val appName = "Railway App"
    val appDescription = stringResource(id = R.string.appDescription)
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon),
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = appName,
                modifier = Modifier.size(128.dp)
            )
            Text(
                text = appName,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
            Text(
                text = appDescription,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            )
            Text(
                text = "Version: $appVersion",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = {},
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Gray
                    )
                ) {
                    Text("Privacy Policy")
                }
                TextButton(
                    onClick = { },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Gray
                    )
                ) {
                    Text("Terms of Use")
                }
            }
        }
    }
}