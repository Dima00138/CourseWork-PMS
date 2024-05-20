@file:OptIn(ExperimentalMaterial3Api::class)

package by.dima00138.coursework.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.dima00138.coursework.Models.IModel
import kotlinx.coroutines.flow.MutableStateFlow

val preferSize = 120.dp

@Composable
fun <T : IModel> GenericTable(
    data: List<T>,
    exampleItem: T,
    onDelete: (T) -> Unit,
    onCreate: (T) -> Unit,
    onUpdate: (T, T) -> Unit,
    columnNames: List<String>,
    getColumnValues: (T) -> List<Any?>
) {
    val _editItem = MutableStateFlow<T?>(null)
    val editItem = _editItem.collectAsStateWithLifecycle()
    val _newItem = MutableStateFlow<T?>(exampleItem)
    val newItem = _newItem.collectAsStateWithLifecycle()
//    val scrollState = rememberLazyListState()
//    val coroutineScope = rememberCoroutineScope()
//
//    LaunchedEffect(scrollState.firstVisibleItemScrollOffset) {
//        coroutineScope.launch {
//            scrollState.scrollToItem(
//                scrollState.firstVisibleItemIndex,
//                scrollState.firstVisibleItemScrollOffset
//            )
//        }
//    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                GenericTableHeader(
                    columnNames = columnNames,
//                    scrollState = scrollState
                )
            }
            items(data) { item ->
                if (item == editItem.value) {
                    EditGenericRow(
//                        scrollState = scrollState,
                        item = item,
                        editItem = _editItem,
                        onUpdateClicked = { it, oldItem ->
                            onUpdate(it, oldItem)
                            _editItem.value = null
                        },
                        onCancelClicked = {
                            _editItem.value = null
                        },
                    )
                }else {
                    GenericTableRow(
//                        scrollState = scrollState,
                        item = item,
                        onDeleteClicked = onDelete,
                        columnValues = getColumnValues(item),
                        onUpdateClicked = { _editItem.value = it }
                    )
                }
            }
            item {
                EditGenericRow(
//                    scrollState = scrollState,
                    item = exampleItem,
                    editItem = _newItem,
                    onUpdateClicked = { it, _ ->
                        onCreate(it)
                        _newItem.value = null
                    },
                    onCancelClicked = {
                        _newItem.value = exampleItem
                    },
                )
            }
        }
    }
}

@Composable
fun GenericTableHeader(
    modifier: Modifier = Modifier,
    columnNames: List<String>,
//    scrollState: LazyListState
) {
    LazyRow(
//        state = scrollState,
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        columnNames.forEach { name ->
            item {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall.copy(color = Color.White),
                    modifier = Modifier
                        .width(preferSize)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Composable
fun <T : IModel> GenericTableRow(
    modifier: Modifier = Modifier,
    item: T,
    onDeleteClicked: (T) -> Unit,
    onUpdateClicked: (T) -> Unit,
    columnValues: List<Any?>,
//    scrollState: LazyListState
) {
    LazyRow(
//        state = scrollState,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        columnValues.forEach { value ->
            item {
                Text(
                    value?.toString() ?: "",
                    modifier = Modifier
                        .width(preferSize)
                        .fillMaxHeight()
                )

            }
        }
        item {
            IconButton(
                onClick = { onUpdateClicked(item) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Edit"
                )
            }
        }
        item {
            IconButton(
                onClick = { onDeleteClicked(item) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun <T : IModel> EditGenericRow(
    modifier: Modifier = Modifier,
    item: T,
    editItem: MutableStateFlow<T?>,
    onUpdateClicked: (T, T) -> Unit,
    onCancelClicked: () -> Unit,
//    scrollState: LazyListState
) {
    LazyRow(
//        state = scrollState,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item.getAllFieldValues().forEach { (key, _) ->
            item {
                var editItemValue by remember(editItem.value, key) {
                    mutableStateOf(editItem.value?.getField(key) ?: "")
                }
                val editItemOnValueChange: (Any) -> Unit = { newValue ->
                    editItemValue = newValue
                    editItem.value?.setField(key, newValue)
                }
                if (key == "date" || key == "birthdate") {
                    FancyTextField(
                        enabled = true,
                        value = editItemValue.toString().trim()
                        ,
                        onValueChange = { newValue ->
                            editItemOnValueChange(newValue)
                        },
                        modifier = Modifier
                            .width(preferSize)
                    )
                } else {
                    FancyTextField(
                        enabled = key != "id",
                        value = editItemValue.toString(),
                        onValueChange = { newValue ->
                            editItemOnValueChange(newValue)
                        },
                        modifier = Modifier.width(preferSize)
                    )

                }
            }
        }
        item {
            IconButton(
                onClick = {
                    onUpdateClicked(editItem.value!!, item)
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "Update"
                )
            }
        }
        item {
            IconButton(
                onClick = { onCancelClicked() },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Cancel"
                )
            }
        }
    }
}