package by.dima00138.coursework.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import by.dima00138.coursework.ui.theme.LoadingIndicator
import by.dima00138.coursework.ui.theme.ScheduleList
import by.dima00138.coursework.viewModels.MainVM

@Composable
fun SearchResultPage(navController: NavController, viewModel: MainVM) {
    val searchResult = viewModel.searchResult.collectAsStateWithLifecycle()
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
        ScheduleList(
            onClick = {
                      viewModel.onScheduleItemClick(navController, it)
            },
            items = searchResult.value,
            currentPage = currentPage.value,
            onPageChange = {
                viewModel.onCurrentPageChange(it)
            }
        )
    }
}
