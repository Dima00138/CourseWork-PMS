package by.dima00138.coursework.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import by.dima00138.coursework.R
import by.dima00138.coursework.ui.theme.FancyTextField
import by.dima00138.coursework.viewModels.Inputs
import by.dima00138.coursework.viewModels.MainVM
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MainScreen(navController: NavController, viewModel: MainVM = viewModel()) {
    Surface (
        content = { MainScreenContent(viewModel) }
    )
}

@Composable
fun MainScreenContent(viewModel: MainVM) {
    val showList by viewModel.showList.observeAsState(false)
    val input by viewModel.input.observeAsState(Inputs.None)
    val searchText by viewModel.searchText.observeAsState("")

    if (showList) {
        FullScreenList(
            items = viewModel.items.filter { it.contains(searchText, ignoreCase = true) },
            onItemClick = { item ->
                viewModel.onValueChange(input, item)
                viewModel.onShowListChange(false, Inputs.None)
            },
            onBackClick = { viewModel.onShowListChange(false, Inputs.None) },
            searchText = searchText,
            onSearchTextChange = { newText -> viewModel.onSearchTextChange(newText) }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                stringResource(id = R.string.header_search),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
            SearchForm(viewModel)
        }
    }
}

@Composable
fun SearchForm(viewModel: MainVM) {
    val from by viewModel.from.observeAsState("")
    val to by viewModel.to.observeAsState("")
    val whenDate by viewModel.whenDate.observeAsState(LocalDate.now())
    val returnDate by viewModel.returnDate.observeAsState(null)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FancyTextField(
            value = from,
            modifier = Modifier.clickable { viewModel.onShowListChange(true, Inputs.FromInput) },
            onValueChange = { viewModel.onValueChange(Inputs.FromInput, it) },
            label = { Text(stringResource(id = R.string.from)) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FancyTextField(
                value = to,
                onValueChange = { viewModel.onValueChange(Inputs.ToInput, it) },
                modifier = Modifier.weight(1f).clickable { viewModel.onShowListChange(true, Inputs.ToInput) },
                label = { Text(stringResource(id = R.string.where)) },
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = {
                val temp = from
                viewModel.onValueChange(Inputs.FromInput, to)
                viewModel.onValueChange(Inputs.ToInput, temp)
            }) {
                Icon(Icons.Filled.Warning, contentDescription = "Менять местами")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FancyTextField(
                value = whenDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                onValueChange = {  },
                label = { Text(stringResource(id = R.string.where)) },
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(8.dp))

            FancyTextField(
                value = TextFieldValue(returnDate?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) ?: ""),
                onValueChange = { /* Обработка изменения даты возвращения */ },
                label = { Text(stringResource(id = R.string.back)) },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Обработка нажатия кнопки поиск */ },
            shape = CutCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                disabledContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f)
            ),
            enabled = true
        ) {
            Text(
                stringResource(id = R.string.search).uppercase(),
                fontSize = 24.sp,
                maxLines = 1,
                fontWeight = FontWeight(1000),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
                )
        }
    }
}

@Composable
fun FullScreenList(
    items: List<String>,
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit,
    searchText: String,
    onSearchTextChange: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                TextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    label = { Text(stringResource(id = R.string.search)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            items(items) { item ->
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(item) }
                        .padding(16.dp)
                )
            }
        }
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back))
        }
    }
}
