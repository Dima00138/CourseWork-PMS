package by.dima00138.coursework.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import by.dima00138.coursework.Models.IModel
import by.dima00138.coursework.Models.ScheduleItem
import by.dima00138.coursework.Models.User
import by.dima00138.coursework.ui.theme.FancyTextField
import by.dima00138.coursework.ui.theme.GenericTable
import by.dima00138.coursework.ui.theme.Triadic400
import by.dima00138.coursework.viewModels.AdminPanelVM
import by.dima00138.coursework.viewModels.Tables

@Composable
fun AdminPanelScreen(navController: NavController, viewModel: AdminPanelVM) {
    val selectedTable = viewModel.selectedTable.collectAsStateWithLifecycle()
    val tableData = viewModel.tableData.collectAsStateWithLifecycle()
    val columnNames: List<String> = viewModel.getExampleElement().getFields().keys.toList()
    val countElement: Int = tableData.value[selectedTable.value]?.count() ?: 0
    val countOfField: Int =
        tableData.value[selectedTable.value]?.firstOrNull()?.getFields()?.count() ?: 0

//    LaunchedEffect(key1 = selectedTable) {
//
//    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)

    ) {

        FancyTableDropdown(
            tableNames = tableData.value.keys.toList(),
            selectedTable = selectedTable.value,
            onTableSelected = { viewModel.onSelectedTable(it) })

        Spacer(modifier = Modifier.height(16.dp))

        GenericTable(
            columnNames = columnNames,
            onDelete = { item -> viewModel.onDeleteItem(selectedTable.value, item) },
            data = tableData.value[selectedTable.value] ?: emptyList(),
            getColumnValues = { item -> item.getAllFieldValues().values.toList() }
        )

//        CrudTable(
//            tableName = selectedTable.value.toString(),
//            data = tableData.value[selectedTable.value] ?: emptyList(),
//            onCreateItem = { item -> viewModel.onCreateItem(selectedTable.value, item) },
//            onUpdateItem = { item, newItem ->
//                viewModel.onUpdateItem(selectedTable.value, item, newItem)
//            },
//            onDeleteItem = { item -> viewModel.onDeleteItem(selectedTable.value, item) }
//        )
    }
}

@Composable
fun FancyTableDropdown(
    tableNames: List<Tables>,
    selectedTable: Tables,
    onTableSelected: (Tables) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                selectedTable.toString(),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Icon(
                imageVector = if (isExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                contentDescription = null
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Triadic400,
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    tableNames.forEach { tableName ->
                        FancyTableDropdownItem(
                            tableName = tableName,
                            isSelected = tableName == selectedTable,
                            onClick = {
                                onTableSelected(tableName)
                                isExpanded = false }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun FancyTableDropdownItem(
    tableName: Tables,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tableName.toString(),
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else Color.Black.copy(alpha = 0.8f)
            )
        )
    }
}

@Composable
private fun CrudTable(
    tableName: String,
    data: List<IModel>,
    onCreateItem: (IModel) -> Unit,
    onUpdateItem: (IModel, IModel) -> Unit,
    onDeleteItem: (IModel) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Table header
            data.forEachIndexed { index, item ->
                FancyCrudTableRow(
                    item = item,
                    onUpdate = { newItem -> onUpdateItem(item, newItem) },
                    onDelete = { onDeleteItem(item) }
                )
                if (index < data.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                var newItem by remember { mutableStateOf<IModel>(ScheduleItem()) }
                if (data.firstOrNull() is ScheduleItem) {
                    newItem = ScheduleItem()
                    (data.firstOrNull() as ScheduleItem).getFields().forEach { (key, value) ->
                        FancyTextField(
                            value = newItem.getField(key).toString(),
                            onValueChange = { newValue ->
                                newItem = newItem.setField(key, newValue)
                            },
                            label = { Text("New $key") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                } else if (data.firstOrNull() is User) {
                    newItem = User()
                    (data.firstOrNull() as User).getFields().forEach { (key, value) ->
                        FancyTextField(
                            value = newItem.getField(key).toString(),
                            onValueChange = { newValue ->
                                newItem = newItem.setField(key, newValue)
                            },
                            label = { Text("New $key") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
                Button(
                    onClick = {
                        onCreateItem(newItem)
//                        newItem = Any()
                    }
                ) {
                    Text("Create")
                }
            }
        }
    }
}

@Composable
fun FancyCrudTableRow(
    item: IModel,
    onUpdate: (IModel) -> Unit,
    onDelete: () -> Unit = {},
    isCreateRow: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FancyTextField(
            enabled = true,
            value = item.toString(),
            onValueChange = { onUpdate(it as IModel) },
            label = { Text("Item") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        if (!isCreateRow) {
            IconButton(
                onClick = onDelete
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        }
    }
}