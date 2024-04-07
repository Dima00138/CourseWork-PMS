package by.dima00138.coursework.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import by.dima00138.coursework.ui.theme.CourseWorkTheme
import by.dima00138.coursework.viewModels.NavigationBarVM

@Composable
fun BottomNavBar(viewModel: NavigationBarVM = viewModel()) {
    val selectedItem by viewModel.selectedItem.observeAsState(0)
    NavigationBar(
        contentColor = Color.Gray,
        modifier = Modifier.padding(0.dp).height(60.dp).shadow(elevation = 4.dp,
            ambientColor = Color.Black, spotColor = Color.Black)
    ) {
        viewModel.items.forEachIndexed { index, item ->
            val selected = selectedItem == index
            val tintColor by animateColorAsState(
                if (selected) Color.Blue else Color.Gray
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = viewModel.icons[index]),
                        contentDescription = null,
                        tint = tintColor
                    )
                },
                label = { Text(item, color = tintColor) },
                selected = selected,
                onClick = { viewModel.selectedItemChange(index) },
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    CourseWorkTheme {
        BottomNavBar()
    }
}