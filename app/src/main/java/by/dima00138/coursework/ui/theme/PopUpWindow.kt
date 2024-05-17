package by.dima00138.coursework.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import by.dima00138.coursework.Models.User

@Composable
fun PopupWindow(
    title: String,
    options: List<User>,
    onOptionSelected: (User) -> Unit,
    onDismissRequest: () -> Unit
) {
    val (showDDMenu, setShowDDMenu) = remember { mutableStateOf(false) }
    val (selectedOption, setSelectedOption) = remember { mutableStateOf<User?>(null) }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth()
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            setShowDDMenu(!showDDMenu)
                        }
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (selectedOption == null)
                            Text("Select an option")
                        else
                            Text("${selectedOption.fullName} ${selectedOption.passport}")
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown"
                        )
                    }

                    DropdownMenu(
                        expanded = showDDMenu,
                        onDismissRequest = {
                            setShowDDMenu(false)
                            setSelectedOption(null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = {Text("${option.fullName} ${option.passport}")},
                                onClick = {
                                    setShowDDMenu(false)
                                    setSelectedOption(option)
                                }
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        if (selectedOption != null) {
                            onOptionSelected(selectedOption)
                            onDismissRequest()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedOption != null
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}