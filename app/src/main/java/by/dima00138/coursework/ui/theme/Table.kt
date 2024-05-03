package by.dima00138.coursework.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val preferSize = 120.dp

@Composable
fun <T> GenericTable(
    data: List<T>,
    onDelete: (T) -> Unit,
    columnNames: List<String>,
    getColumnValues: (T) -> List<Any?>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                GenericTableHeader(columnNames)
            }
            items(data) { item ->
                GenericTableRow(item, onDelete, getColumnValues(item))
            }
        }
    }
}

@Composable
fun GenericTableHeader(
    columnNames: List<String>,
//    scrollState: NestedScrollConnection
) {
    LazyRow(
        modifier = Modifier
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
fun <T> GenericTableRow(
    item: T,
    onDelete: (T) -> Unit,
    columnValues: List<Any?>,
//    scrollState: NestedScrollConnection
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDelete(item) }
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
                onClick = { onDelete(item) },
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