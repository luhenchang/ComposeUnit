package com.example.composeunit.ui.compose.home.widget

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

class HomeScrollState(scrollOffsetOfY: Float, density: Density) {
    //1、记录列表滑动的距离
    private var scrollYOffset: MutableState<Float> = mutableStateOf(scrollOffsetOfY)

    //2、记录列表状态
    private var listState: MutableState<LazyListState> = mutableStateOf(LazyListState(0, 0))
    private var isFirstOverOneItem: MutableState<Boolean> = mutableStateOf(true)
    private var saveFirstItemEndScrollOffset: MutableState<Float> = mutableStateOf(0f)
    private var freeSlidOffsetY: MutableState<Float> = mutableStateOf(0f)

    //3、滑动落差为20dp
    private val targetHeight = 20.dp
    private val targetHeightPx = with(density) { targetHeight.roundToPx().toFloat() }

    private fun updateScrollYOffset(offsetY: Float) {
        scrollYOffset.value = offsetY
    }

    private val firstVisibleItemScrollOffset: Int get() = listState.value.firstVisibleItemScrollOffset
    private val firstVisibleItemIndex: Int get() = listState.value.firstVisibleItemIndex

    fun setListState(lazyListState: LazyListState) {
        Log.e("setListState=", lazyListState.firstVisibleItemIndex.toString())
        listState.value = lazyListState
        Log.e("setListState1=", listState.value.firstVisibleItemIndex.toString())
    }

    fun getScrollYOffset(): Float {
        //手指拖动Item慢速滑动->>表示第一个Item滑动时候，且向上滑动的距离小于目标高度
        Log.e("firstVisibleItemIndex::", firstVisibleItemIndex.toString())
        Log.e("freeSlidOffsetY::", freeSlidOffsetY.value.toString())
        if (firstVisibleItemIndex == 0) {
            isFirstOverOneItem.value = true
        }
        if (firstVisibleItemScrollOffset < targetHeightPx && firstVisibleItemIndex == 0) {
            saveFirstItemEndScrollOffset.value = firstVisibleItemScrollOffset.toFloat()
            updateScrollYOffset(firstVisibleItemScrollOffset.toFloat())
        }
        //手指快速滑动离开item->>滑动累加距离大雨目标高度时候，且 可显示的itemIndex大雨0且有且只有一次：目的防止滑动到其他item时候多次触发
        else if (freeSlidOffsetY.value > targetHeightPx && firstVisibleItemIndex > 0 && isFirstOverOneItem.value) {
            isFirstOverOneItem.value = false
            val endScrollOff =
                if (saveFirstItemEndScrollOffset.value <= 0) targetHeightPx else saveFirstItemEndScrollOffset.value
            updateScrollYOffset(endScrollOff)
        }
        return scrollYOffset.value
    }

    fun setFreeSlidOffsetY(offsetY: Float) {
        freeSlidOffsetY.value = offsetY
    }
}