package by.dima00138.coursework.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Validator(
    value: String,
    validator: (String) -> Boolean,
    errorMessage: String,
    onValidationResult: (Boolean) -> Unit
) {
    val isValid by remember(value) { mutableStateOf(validator(value)) }
    LaunchedEffect(value) {
        onValidationResult(isValid)
    }
    if (!isValid) {
        Text(
            text = errorMessage,
            color = Color.Red,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}