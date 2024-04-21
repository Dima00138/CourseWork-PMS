package by.dima00138.coursework.ui.theme

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import by.dima00138.coursework.R
import by.dima00138.coursework.views.FullScreenList

@Composable
fun FancyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable() (() -> Unit)? = null) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier.fillMaxWidth().shadow(
            elevation = 5.dp,
            shape = CutCornerShape(6.dp)
        ),
        enabled = false,
        singleLine = true,
        shape = CutCornerShape(6.dp),
        colors = TextFieldDefaults.colors(
            disabledContainerColor = TF_Unfocused,
            focusedContainerColor = TF_Unfocused,
            disabledTextColor = Color.Black,
            focusedTextColor = Color.Black,
            disabledLabelColor = Color.Gray,
            focusedLabelColor = Color.Black,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
    )

}

@Composable
fun FancyTextField(value: TextFieldValue,
                   onValueChange: (TextFieldValue) -> Unit,
                   modifier: Modifier = Modifier,
                   label: @Composable() (() -> Unit)? = null) {

    var showList by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("") }
    var searchText by remember { mutableStateOf("") }

    val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5")

    if (showList) {
        FullScreenList(
            items = items.filter { it.contains(searchText, ignoreCase = true) },
            onItemClick = { item ->
                onValueChange(TextFieldValue(item))
                showList = false
            },
            onBackClick = { showList = false },
            searchText = searchText,
            onSearchTextChange = { newText -> searchText = newText }
        )
    } else {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            modifier = modifier.fillMaxWidth().shadow(
                elevation = 5.dp,
                shape = CutCornerShape(6.dp),
                ).clickable{ showList = true },
            enabled = false,
            singleLine = true,
            shape = CutCornerShape(6.dp),
            colors = TextFieldDefaults.colors(
                disabledContainerColor = TF_Unfocused,
                focusedContainerColor = TF_Unfocused,
                disabledTextColor = Color.Black,
                focusedTextColor = Color.Black,
                disabledLabelColor = Color.Gray,
                focusedLabelColor = Color.Black,
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
        )
    }
}