package com.example.composeunit.ui.compose.home.widget

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource


class HomeScrollConnection(
    private val homeScrollState: HomeScrollState,
    private var targetHeightPx: Float
) : NestedScrollConnection {
    private var dyConsumed = 0f
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y
        dyConsumed += delta
        //0表示滑动顶部
        dyConsumed = dyConsumed.coerceAtMost(0f)
        if (-dyConsumed > targetHeightPx) {
            homeScrollState.setFreeSlidOffsetY(-dyConsumed)
        }
        Log.e("dyConsumed::", dyConsumed.toString())
        val percent = dyConsumed / targetHeightPx
        if (percent > -1 && percent < 0) {
            return Offset(0f, delta)
        }
        return Offset.Zero
    }
}