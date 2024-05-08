package by.dima00138.coursework.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import by.dima00138.coursework.Models.ScheduleItem
import by.dima00138.coursework.R
import by.dima00138.coursework.viewModels.BoardVM
import kotlin.math.ceil
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardScreen(navController: NavController, viewModel: BoardVM) {
    val selectedTabIndex = viewModel.selectedTabIndex.collectAsStateWithLifecycle(0)
    val departureList = viewModel.departureSchedule.collectAsStateWithLifecycle()
    val arrivalList = viewModel.arrivalSchedule.collectAsStateWithLifecycle()
    val currentPage = viewModel.currentPage.collectAsStateWithLifecycle()

    Column {
        PrimaryTabRow(selectedTabIndex = selectedTabIndex.value) {
            viewModel.tabs.forEachIndexed {index, item ->
                Tab(
                    selected = selectedTabIndex.value == index,
                    onClick = { viewModel.onSelectedTabChange(index) },
                    text = {Text(text = stringResource(id = item.resourceId), maxLines = 1, overflow = TextOverflow.Ellipsis)}
                )
            }
        }
        when(selectedTabIndex.value) {
            0 -> ScheduleList(
                departureList.value,
                currentPage = currentPage.value,
                onPageChange = {
                    viewModel.onCurrentPageChange(it)
                })
            1 -> ScheduleList(
                arrivalList.value,
                currentPage = currentPage.value,
                onPageChange = {
                    viewModel.onCurrentPageChange(it)
                })
        }

    }
}

@Composable
fun ScheduleList(
    items: List<ScheduleItem>?,
    currentPage: Int = 0,
    onPageChange: (Int) -> Unit
) {
    if (items?.isEmpty() == true || items == null)
        return Text(
            text = stringResource(id = R.string.board_empty),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )

    val itemsPerPage = 1
    val totalPages = ceil(items.size.toFloat() / itemsPerPage).toInt()

    Column {
        LazyColumn {
            items(
                count = min(items.size, (currentPage + 1) * itemsPerPage) - currentPage * itemsPerPage,
                key = { index -> items[currentPage * itemsPerPage + index].id }
            ) { index ->
                val item = items[currentPage * itemsPerPage + index]
                ScheduleItemCard(item)
            }
        }

        HorizontalDivider(Modifier.fillMaxWidth().padding(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            for (page in 0 until totalPages) {
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = { onPageChange(page) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (page == currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("${page + 1}")
                }
            }
        }
    }
}

@Composable
fun ScheduleItemCard(item: ScheduleItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(item.from, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(item.to, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(item.date, style = MaterialTheme.typography.bodyMedium)
        }
    }
}