package by.dima00138.coursework.ui.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun FancyTextField(value: String,
                   onValueChange: (String) -> Unit,
                   modifier: Modifier = Modifier,
                   label: @Composable() (() -> Unit)? = null,
                   visualTransformation: VisualTransformation = VisualTransformation.None,
                   enabled : Boolean = false) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        visualTransformation = visualTransformation,
        modifier = modifier.fillMaxWidth().shadow(
            elevation = 5.dp,
            shape = CutCornerShape(6.dp)
        ),
        enabled = enabled,
        singleLine = true,
        shape = CutCornerShape(6.dp),
        colors = TextFieldDefaults.colors(
            disabledContainerColor = TF_Unfocused,
            unfocusedContainerColor = TF_Unfocused,
            focusedContainerColor = TF_Unfocused,
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black,
            disabledTextColor = Color.Black,
            disabledLabelColor = Color.Gray,
            unfocusedLabelColor = Color.Black,
            focusedLabelColor = Color.Black,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
    )

}

@Composable
fun FancyTextField(value: TextFieldValue,
                   onValueChange: (TextFieldValue) -> Unit,
                   modifier: Modifier = Modifier,
                   label: @Composable() (() -> Unit)? = null,
                   visualTransformation: VisualTransformation = VisualTransformation.None,
                   enabled : Boolean = false) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        visualTransformation = visualTransformation,
        label = label,
        modifier = modifier.fillMaxWidth().shadow(
            elevation = 5.dp,
            shape = CutCornerShape(6.dp)
        ),
        enabled = enabled,
        singleLine = true,
        shape = CutCornerShape(6.dp),
        colors = TextFieldDefaults.colors(
            disabledContainerColor = TF_Unfocused,
            unfocusedContainerColor = TF_Unfocused,
            focusedContainerColor = TF_Unfocused,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            disabledTextColor = Color.Black,
            disabledLabelColor = Color.Gray,
            unfocusedLabelColor = Color.Black,
            focusedLabelColor = Color.Black,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
    )
}