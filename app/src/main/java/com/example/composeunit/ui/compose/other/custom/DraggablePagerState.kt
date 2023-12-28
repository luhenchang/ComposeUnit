package com.example.composeunit.ui.compose.other.custom

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberDraggablePagerState(
    levelState: PagerState,
    level2State: PagerState
): DraggablePagerState {
    return remember(levelState, level2State) {
        DraggablePagerState(levelState, level2State)
    }
}

@OptIn(ExperimentalFoundationApi::class)
class DraggablePagerState(
    levelState: PagerState,
    level2State: PagerState,
    private var draggableOffset: Int = 36
) {
    private var mLevelState by mutableStateOf(levelState)
    private var mLevel2State by mutableStateOf(level2State)

    //默认是可以滑动的
    private var userScrollEnabled by mutableStateOf(true)
    private var userScrollEnabledType by
    mutableStateOf<PageScrollConfigType>(PageScrollConfigType.DefaultPageType)

    fun mLevelState(): PagerState = mLevelState
    fun userScrollEnabled(): Boolean = userScrollEnabled
    fun draggableEnabled(): Boolean = !userScrollEnabled
    sealed class PageScrollConfigType {
        //一级的第一个页面，二级的最后一个页面、手势向左边拖动，需要切换到一级的下一个页面
        object LeftPageType : PageScrollConfigType()

        //非一级的第一个和非一级最后一个页面，也就是一级的中间页面，切换时候需要考虑：手势左拖动切换到一级的上一个页面，手势右拖动切换到一级的下一个页面，那么二级需要禁止第一个和最后一个userScrollEnabled，进行拖拽操作
        object CenterPageType : PageScrollConfigType()

        //表示一级最后一个页面，需要处理第一个即可
        object RightPageType : PageScrollConfigType()
        //默认不需处理

        object DefaultPageType : PageScrollConfigType()
    }

    fun initUserScrollEnableType() {
        userScrollEnabledType =
            if (mLevelState.currentPage == 0 && (mLevel2State.currentPage == mLevel2State.pageCount - 1)) {
                PageScrollConfigType.LeftPageType
            } else (if (mLevelState.currentPage != 0 && mLevelState.currentPage != mLevelState.pageCount - 1 &&
                ((mLevel2State.currentPage == mLevel2State.pageCount - 1) || (mLevel2State.currentPage == 0))
            ) {
                PageScrollConfigType.CenterPageType
            } else if (mLevelState.currentPage == mLevelState.pageCount - 1 && (mLevel2State.currentPage == 0)) {
                PageScrollConfigType.RightPageType
            } else PageScrollConfigType.DefaultPageType)

        userScrollEnabled = userScrollEnabledType == PageScrollConfigType.DefaultPageType
    }

    suspend fun setDraggableOnDetailToScrollToPage(onDetail: Float) {

        when (userScrollEnabledType) {
            PageScrollConfigType.LeftPageType -> {
                //切换第一项的切换
                if (onDetail < -draggableOffset) {//右滑动
                    mLevelState.scrollToPage(mLevelState.currentPage + 1)
                }
                //切换到上一页
                else if (onDetail > draggableOffset && mLevel2State.canScrollBackward && !mLevel2State.isScrollInProgress) {
                    mLevel2State.animateScrollToPage(mLevel2State.currentPage - 1)
                }
            }

            PageScrollConfigType.CenterPageType -> {
                if (mLevel2State.currentPage == 0) {
                    if (onDetail > draggableOffset) {//一级页面向左边Tab滑动，手势向右边走
                        mLevelState.animateScrollToPage(
                            mLevelState.currentPage - 1,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium)
                        )
                    } else if (onDetail < -draggableOffset && mLevel2State.canScrollForward()) {//向右边滑动
                        mLevel2State.animateScrollToPage(
                            mLevel2State.currentPage + 1,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium)
                        )
                    }
                } else if (mLevel2State.currentPage == mLevel2State.pageCount - 1) {
                    if (onDetail > draggableOffset) {//左边
                        mLevel2State.animateScrollToPage(
                            mLevel2State.currentPage - 1,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium)
                        )
                    } else if (onDetail < -6 && mLevel2State.canScrollBackward()) {//左边滑动
                        mLevelState.animateScrollToPage(
                            mLevelState.currentPage + 1,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium)
                        )
                    }
                }
            }

            PageScrollConfigType.RightPageType -> {
                if (onDetail < -draggableOffset && mLevel2State.canScrollForward()) {//右滑动
                    mLevel2State.animateScrollToPage(
                        mLevel2State.currentPage + 1,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                    )

                } else if (onDetail > draggableOffset) {
                    mLevelState.animateScrollToPage(
                        mLevelState.currentPage - 1,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                    )
                }
            }

            else -> {}
        }
    }


    private fun PagerState.canScrollForward(): Boolean {
        return canScrollForward && !isScrollInProgress
    }

    private fun PagerState.canScrollBackward(): Boolean {
        return canScrollBackward && !isScrollInProgress
    }


}