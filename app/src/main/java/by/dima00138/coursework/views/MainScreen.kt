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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import by.dima00138.coursework.Models.Station
import by.dima00138.coursework.R
import by.dima00138.coursework.ui.theme.FancyTextField
import by.dima00138.coursework.ui.theme.LoadingIndicator
import by.dima00138.coursework.ui.theme.PrimaryFancyButton
import by.dima00138.coursework.viewModels.Inputs
import by.dima00138.coursework.viewModels.MainVM
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MainScreen(navController: NavController, viewModel: MainVM) {
    Surface (
        content = { MainScreenContent(navController, viewModel) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(navController: NavController, viewModel: MainVM) {
    val showList by viewModel.showList.collectAsStateWithLifecycle(false)
    val showDatePicker by viewModel.showDatePicker.collectAsStateWithLifecycle(false)
    val datePick by viewModel.datePick.collectAsStateWithLifecycle(Inputs.None)
    val input by viewModel.input.collectAsStateWithLifecycle(Inputs.None)
    val searchText by viewModel.searchText.collectAsStateWithLifecycle("")
    val whenDateState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val returnDateState = rememberDatePickerState(null)
    val stations = viewModel.stations.collectAsStateWithLifecycle()
    val isRefresh = viewModel.isRefresh.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberPullToRefreshState()

    if (isRefresh.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator()
        }
        return
    }


    if (showList) {
        FullScreenList(
            items = stations.value.filter{ it.name.contains(searchText, ignoreCase = true) },
            onItemClick = { item ->
                viewModel.onValueChange(input, item)
                viewModel.onShowListChange(false, Inputs.None)
            },
            onBackClick = { viewModel.onShowListChange(false, Inputs.None) },
            searchText = searchText,
            onSearchTextChange = { newText -> viewModel.onSearchTextChange(newText) }
        )
    } else {
        if (showDatePicker) {
            val selectedDateState = when (datePick == Inputs.WhenDateInput) {
                true -> whenDateState
                false -> returnDateState
            }
            val confirmEnabled = remember {
                derivedStateOf { selectedDateState.selectedDateMillis != null }
            }
            DatePickerDialog(onDismissRequest = {
                viewModel.onShowDatePickerChange(false, Inputs.None)
            }, confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onValueChange(
                            datePick,
                            selectedDateState.selectedDateMillis.toString()
                        )
                        viewModel.onShowDatePickerChange(false, Inputs.None)
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }

            }, dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.onShowDatePickerChange(false, Inputs.None)
                    }
                ) {
                    Text("Cancel")
                }
            }) {
                DatePicker(
                    state = selectedDateState,
                    showModeToggle = false,
                )
            }
        }
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .pullToRefresh(
                    isRefreshing = isRefresh.value,
                    state = pullToRefreshState,
                    onRefresh = { viewModel.refresh() }),
            isRefreshing = isRefresh.value,
            onRefresh = { viewModel.refresh() }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    stringResource(id = R.string.header_search),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(16.dp, 24.dp, 16.dp, 16.dp)
                )
                SearchForm(navController, viewModel)
            }
        }
    }
}

@Composable
fun SearchForm(navController: NavController, viewModel: MainVM) {
    val from by viewModel.from.collectAsState("")
    val to by viewModel.to.collectAsState("")
    val whenDate by viewModel.whenDate.collectAsState(LocalDate.now())
    val returnDate by viewModel.returnDate.collectAsState(null)

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
                modifier = Modifier
                    .weight(1f)
                    .clickable { viewModel.onShowListChange(true, Inputs.ToInput) },
                label = { Text(stringResource(id = R.string.where)) },
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = {
                val temp = from
                viewModel.onValueChange(Inputs.FromInput, to)
                viewModel.onValueChange(Inputs.ToInput, temp)
            }) {
                Icon(painterResource(id = R.drawable.arrows_sort_icon), contentDescription = "Менять местами")
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
                onValueChange = { viewModel.onValueChange(Inputs.WhenDateInput, it) },
                label = { Text(stringResource(id = R.string.`when`)) },
                modifier = Modifier
                    .weight(1f)
                    .clickable { viewModel.onShowDatePickerChange(true, Inputs.WhenDateInput) },
            )

            Spacer(modifier = Modifier.width(8.dp))

            FancyTextField(
                value = TextFieldValue(returnDate?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) ?: ""),
                onValueChange = { viewModel.onValueChange(Inputs.ReturnDateInput, it.toString()) },
                label = { Text(stringResource(id = R.string.back)) },
                modifier = Modifier
                    .weight(1f)
                    .clickable { viewModel.onShowDatePickerChange(true, Inputs.ReturnDateInput) },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryFancyButton(
            onClick = { viewModel.onSearchButtonClick(navController) },
            text = stringResource(id = R.string.search)
        )
    }
}

@Composable
fun FullScreenList(
    items: List<Station>,
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
                    singleLine = true,
                    label = { Text(stringResource(id = R.string.search)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
            items(items) { item ->
                Text(
                    text = item.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(item.name) }
                        .padding(16.dp)
                )
            }
        }
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back))
        }
    }
}
