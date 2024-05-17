package by.dima00138.coursework.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import by.dima00138.coursework.R
import by.dima00138.coursework.ui.theme.LoadingIndicator
import by.dima00138.coursework.viewModels.OrderVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavController, viewModel: OrderVM) {
    val tickets = viewModel.tickets.collectAsStateWithLifecycle()
    val error = viewModel.error.collectAsStateWithLifecycle()
    val isRefreshing = viewModel.isRefreshing.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    if (isRefreshing.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator()
        }
        return
    }

    PullToRefreshBox(
        modifier = Modifier
            .fillMaxSize()
            .pullToRefresh(isRefreshing = isRefreshing.value, state = pullToRefreshState, onRefresh = {viewModel.refresh()}),
        isRefreshing = isRefreshing.value,
        onRefresh = {viewModel.refresh()}) {

        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
        ) {
            item {
                if (error.value != "" && !isRefreshing.value) {
                    Text(
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center,
                        text = error.value,
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
            if (!isRefreshing.value) {
                items(tickets.value) { ticket ->
                    TicketItem(
                        ticket = ticket,
                        onClick = { viewModel.onTicketClicked(ticket, navController) }
                    )
                }
            }
        }
    }

}

@Composable
fun TicketItem(
    ticket: OrderVM.FancyTicket,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${stringResource(id = R.string.train)}: ",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = ticket.trainId,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 16.dp),
                    Arrangement.SpaceBetween
                ) {
                    Text(
                        text = ticket.from,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = " — ",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = ticket.to,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Text(
                    text = "${stringResource(id = R.string.date)}: ${ticket.date}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "${stringResource(id = R.string.seat_number)}: ${ticket.numberOfSeat}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

