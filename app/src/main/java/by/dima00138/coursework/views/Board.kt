package by.dima00138.coursework.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import by.dima00138.coursework.Firebase
import by.dima00138.coursework.ui.theme.CourseWorkTheme
import by.dima00138.coursework.viewModels.BoardVM
import com.google.firebase.firestore.Query

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardScreen(navController: NavController, viewModel: BoardVM) {
    val selectedTabIndex = viewModel.selectedTabIndex.collectAsState(0)

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


    }
}

//@Composable
//fun ItemList(items: PagingData<Firebase.BoardList>) {
//    val context = LocalContext.current
//    val lazyListState = rememberLazyListState()
//    val listState = rememberLazyListState()
//
//    val config = PagingConfig(
//    pageSize = 10,
//    prefetchDistance = 5,
//    enablePlaceholders = true
//)
//
//    val pagingSourceFactory = { query: Query ->
//        FirebasePagingSource(query, context)
//    }
//
//    val pager = remember {
//        Pager(config, pagingSourceFactory) {
////            query
//        }
//    }
//
//    val lazyPagingItems = pager.flow.cachedIn(viewModelScope).collectAsLazyPagingItems()
//
//    LazyColumn(state = listState) {
//        items(lazyPagingItems) { item ->
//            item?.let {
//                Text(item.toString())
//            }
//        }
//
//        if (lazyPagingItems.loadState.append == LoadState.Loading) {
//            item {
//                CircularProgressIndicator()
//            }
//        }
//    }
//}