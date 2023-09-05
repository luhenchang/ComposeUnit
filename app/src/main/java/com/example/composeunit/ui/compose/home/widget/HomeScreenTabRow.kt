package com.example.composeunit.ui.compose.home.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.composeunit.project.view_model.home.HomeViewModel
import com.example.composeunit.ui.compose.home.ComposeTabView

@Composable
fun HomeScreenTabRow(
    modifier: Modifier = Modifier,
    scrollState:HomeScrollState,
    homeViewModel: HomeViewModel,
    tabSelectedCallBack: (Int) -> Unit,
    tabSelectedState: Int
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
    ) {
        val title =
            arrayListOf("View", "ViewGp", "Scroll", "Canvas", "OpenAi", "Animal", "End..")
        for (index in 0..6) {
            ComposeTabView(
                title[index],
                modifier = Modifier.weight(1f),
                index = index,
                tabSelectedCallBack = tabSelectedCallBack::invoke,
                scrollState,
                tabSelectedState,
                homeViewModel
            )
        }
    }
}