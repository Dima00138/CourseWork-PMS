package by.dima00138.coursework.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import by.dima00138.coursework.Models.ScheduleItem
import by.dima00138.coursework.R
import kotlin.math.ceil
import kotlin.math.min

@Composable
fun ScheduleList(
    items: List<ScheduleItem>?,
    currentPage: Int = 0,
    onPageChange: (Int) -> Unit,
    onClick: (ScheduleItem) -> Unit = {}
) {
    if (items?.isEmpty() == true || items == null)
        return Text(
            text = stringResource(id = R.string.board_empty),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxSize(),
            style = MaterialTheme.typography.headlineLarge
        )

    val itemsPerPage = 10
    val totalPages = ceil(items.size.toFloat() / itemsPerPage).toInt()

    Column {
        LazyColumn {
            items(
                count = min(items.size, (currentPage + 1) * itemsPerPage) - currentPage * itemsPerPage,
                key = { index -> items[currentPage * itemsPerPage + index].id }
            ) { index ->
                val item = items[currentPage * itemsPerPage + index]
                ScheduleItemCard(item = item) {
                    onClick(it)
                }
            }

            item {
                HorizontalDivider(Modifier.fillMaxWidth().padding(16.dp))

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    items(totalPages) {page ->
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
    }
}

@Composable
fun ScheduleItemCard(
    modifier: Modifier = Modifier,
    item: ScheduleItem,
    onClick: (ScheduleItem) -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onClick(item)
            },
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