package by.dima00138.coursework.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import by.dima00138.coursework.ui.theme.LoadingIndicator
import by.dima00138.coursework.ui.theme.ScheduleList
import by.dima00138.coursework.viewModels.BoardVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardScreen(navController: NavController, viewModel: BoardVM) {
    val selectedTabIndex = viewModel.selectedTabIndex.collectAsStateWithLifecycle(0)
    val departureList = viewModel.departureSchedule.collectAsStateWithLifecycle()
    val arrivalList = viewModel.arrivalSchedule.collectAsStateWithLifecycle()
    val currentPage = viewModel.currentPage.collectAsStateWithLifecycle()
    val isRefresh = viewModel.isRefresh.collectAsStateWithLifecycle()

    if (isRefresh.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator()
        }
        return
    }

    Column {
        PrimaryTabRow(selectedTabIndex = selectedTabIndex.value) {
            viewModel.tabs.forEachIndexed {index, item ->
                Tab(
                    selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedContentColor = MaterialTheme.colorScheme.onPrimary,
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