package by.dima00138.coursework.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.dima00138.coursework.R
import by.dima00138.coursework.ui.theme.QRCodeComposable
import by.dima00138.coursework.viewModels.OrderVM

@Composable
fun OrderDetailsPage(
    viewModel: OrderVM
) {
    val selectedTicket = viewModel.selectedTicket.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 18.dp),
            text = stringResource(id = R.string.ticket_info),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )
        Row(modifier = Modifier.padding(bottom = 18.dp)) {
            Text(
                text = "${stringResource(id = R.string.train)}: ",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = selectedTicket.value!!.trainId,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 18.dp),
            Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedTicket.value!!.from,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = " — ",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = selectedTicket.value!!.to,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Row(modifier = Modifier.padding(bottom = 18.dp)) {
            Text(
                text = "${stringResource(id = R.string.date)}: ",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = selectedTicket.value!!.date,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(modifier = Modifier.padding(bottom = 18.dp)) {
            Text(
                text = "${stringResource(id = R.string.seat_number)}: ",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = selectedTicket.value!!.numberOfSeat,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(modifier = Modifier.padding(bottom = 18.dp)) {
            Text(
                text = "${stringResource(id = R.string.passenger)}: ",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = selectedTicket.value!!.user.fullName,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(modifier = Modifier.padding(bottom = 18.dp)) {
            Text(
                text = "${stringResource(id = R.string.passport)}: ",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = selectedTicket.value!!.user.passport,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        QRCodeComposable(
            data = "ticketId: ${selectedTicket.value!!.id};" +
                    "user: ${selectedTicket.value!!.user.id}",
            modifier = Modifier.fillMaxWidth().size(150.dp),
            alignment = Alignment.Center)
    }
}