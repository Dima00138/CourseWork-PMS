package by.dima00138.coursework.views

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import by.dima00138.coursework.Models.Ticket
import by.dima00138.coursework.Models.Train
import by.dima00138.coursework.R
import by.dima00138.coursework.ui.theme.LoadingIndicator
import by.dima00138.coursework.ui.theme.PopupWindow
import by.dima00138.coursework.ui.theme.ScheduleItemCard
import by.dima00138.coursework.viewModels.TicketVM
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketPage(navController: NavController, viewModel: TicketVM, scheduleItemId: String) {
    val context = LocalContext.current
    val scheduleItem = viewModel.scheduleItem.collectAsStateWithLifecycle()
    val tickets = viewModel.tickets.collectAsStateWithLifecycle()
    val error = viewModel.error.collectAsStateWithLifecycle()
    val popUp = viewModel.showPopUp.collectAsStateWithLifecycle()
    val passengers = viewModel.passengers.collectAsStateWithLifecycle()
    val isRefreshing = viewModel.isRefreshing.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    val currentConfiguration by rememberUpdatedState(LocalConfiguration.current)

    LaunchedEffect(key1 = error.value) {
        if (error.value == "") return@LaunchedEffect
        Toast.makeText(context,
            error.value, Toast.LENGTH_LONG
        ).show()
        viewModel.error.value = ""
    }

    if (isRefreshing.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator()
        }
        return
    }

    when {
        popUp.value -> {
            PopupWindow(
                title = stringResource(id = R.string.booking_request),
                options = passengers.value,
                onOptionSelected = {
                    viewModel.getTicket(it)
                }) {
                viewModel.popUpChange(false)
            }
        }
    }

    PullToRefreshBox(
        modifier = Modifier
            .fillMaxSize()
            .pullToRefresh(
                isRefreshing = isRefreshing.value,
                state = pullToRefreshState,
                onRefresh = { viewModel.refresh() }),
        isRefreshing = isRefreshing.value,
        onRefresh = {viewModel.refresh()}) {
        Column {
            if (currentConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                ScheduleItemCard(item = scheduleItem.value, onClick = {})
            }
            if (tickets.value.isEmpty()) {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.no_tickets),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            tickets.value.forEach { entry ->
                TicketGrid(
                    modifier = Modifier.padding(16.dp),
                    tickets = entry,
                    onTicketClick = {
                        viewModel.onTicketClick(it, navController)
                    })

            }
        }
    }
}

@Composable
fun TicketGrid(
    tickets: Map.Entry<Train, List<Ticket>>,
    onTicketClick: (Ticket) -> Unit,
    modifier: Modifier = Modifier
) {
    val sortedArr = tickets.value.sortedBy { it.numberOfSeat.toInt() }
    val countTicketPerRow = 5
    val countOfVans = tickets.key.countOfVans.toInt()
    val countOfSeats = tickets.key.countOfSeats.toInt()
    val rows = ceil(countOfSeats.toDouble() / countTicketPerRow).toInt()

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,

    ) {
        items(countOfVans) { vanIndex ->
            Text(
                text = stringResource(id = R.string.number_van) + " ${vanIndex + 1}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
            )
            for (rowIndex in 0 until rows) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(4.dp)) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        items(sortedArr.chunked(countTicketPerRow)
                            .getOrNull(rowIndex + (vanIndex * rows))
                            ?: emptyList()) { ticket ->
                            TicketCard(
                                ticket = ticket,
                                onClick = { onTicketClick(ticket) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TicketCard(
    ticket: Ticket,
    onClick: () -> Unit
) {
    if (ticket.free != "true") {
        Card(
            Modifier
                .width(40.dp)
                .height(40.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.7f))
        ) {

        }
    } else {
        Card(
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .clickable { onClick() },
            elevation = CardDefaults.cardElevation(4.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxSize(),
                text = ticket.numberOfSeat,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
