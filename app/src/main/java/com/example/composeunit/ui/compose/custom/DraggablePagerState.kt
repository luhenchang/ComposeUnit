package com.example.composeunit.ui.compose.custom

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

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
    private var mLevelParentState: MutableState<PagerState?> = mutableStateOf(null)
    private var msLevelParentState: MutableState<PagerState?> = mutableStateOf(null)

    private var mLevelState: MutableState<PagerState> = mutableStateOf(levelState)
    private var mLevel2State: MutableState<PagerState> = mutableStateOf(level2State)

    //默认是可以滑动的
    private var userScrollEnabled: MutableState<Boolean> = mutableStateOf(true)
    private var userScrollEnabledType: MutableState<PageScrollConfigType> =
        mutableStateOf(PageScrollConfigType.DefaultPageType)

    fun mLevelState(): PagerState = mLevelState.value
    fun userScrollEnabled(): Boolean = userScrollEnabled.value
    fun draggableEnabled(): Boolean = !userScrollEnabled.value
    sealed class PageScrollConfigType {
        //一级的第一个页面，二级的最后一个页面、手势向左边拖动，需要切换到一级的下一个页面
        object LeveFirstPageType : PageScrollConfigType()

        //非一级的第一个和非一级最后一个页面，也就是一级的中间页面，切换时候需要考虑：手势左拖动切换到一级的上一个页面，手势右拖动切换到一级的下一个页面，那么二级需要禁止第一个和最后一个userScrollEnabled，进行拖拽操作
        object LeveCenterPageType : PageScrollConfigType()

        //表示一级最后一个页面，需要处理第一个即可
        object LeveLastPageType : PageScrollConfigType()
        object LeveLastCouldScrollPageType : PageScrollConfigType()

        object DefaultPageType : PageScrollConfigType()
    }

    fun initUserScrollEnableType() {
        userScrollEnabledType.value =
            if (mLevelState.value.currentPage == 0 && (mLevel2State.value.currentPage == mLevel2State.value.pageCount - 1)) {
                PageScrollConfigType.LeveFirstPageType
            } else if (mLevelState.value.currentPage != 0 && mLevelState.value.currentPage != mLevelState.value.pageCount - 1 &&
                ((mLevel2State.value.currentPage == mLevel2State.value.pageCount - 1) || (mLevel2State.value.currentPage == 0))
            ) {
                PageScrollConfigType.LeveCenterPageType
            } else if (mLevelState.value.currentPage == mLevelState.value.pageCount - 1 && (mLevel2State.value.currentPage == 0)) {
                PageScrollConfigType.LeveLastPageType
            }
            else PageScrollConfigType.DefaultPageType
        userScrollEnabled.value =
            userScrollEnabledType.value == PageScrollConfigType.DefaultPageType
    }

    suspend fun setDraggableOnDetailToScrollToPage(onDetail: Float) {

        when (userScrollEnabledType.value) {
            PageScrollConfigType.LeveFirstPageType -> {
                //切换第一项的切换
                if (onDetail < -draggableOffset) {//右滑动
                    mLevelState.value.scrollToPage(mLevelState.value.currentPage + 1)
                }
                //切换到上一页
                else if (onDetail > draggableOffset && mLevel2State.value.canScrollBackward && !mLevel2State.value.isScrollInProgress) {
                    mLevel2State.value.animateScrollToPage(mLevel2State.value.currentPage - 1)
                }
            }

            PageScrollConfigType.LeveCenterPageType -> {
                if (mLevel2State.value.currentPage == 0) {
                    if (onDetail > draggableOffset) {//一级页面向左边Tab滑动，手势向右边走
                        mLevelState.value.animateScrollToPage(
                            mLevelState.value.currentPage - 1,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium)
                        )
                    } else if (onDetail < -draggableOffset && mLevel2State.value.canScrollForward()) {//向右边滑动
                        mLevel2State.value.animateScrollToPage(
                            mLevel2State.value.currentPage + 1,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium)
                        )
                    }
                } else if (mLevel2State.value.currentPage == mLevel2State.value.pageCount - 1) {
                    if (onDetail > draggableOffset) {//左边
                        mLevel2State.value.animateScrollToPage(
                            mLevel2State.value.currentPage - 1,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium)
                        )
                    } else if (onDetail < -6 && mLevel2State.value.canScrollBackward()) {//左边滑动
                        mLevelState.value.animateScrollToPage(
                            mLevelState.value.currentPage + 1,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium)
                        )
                    }
                }
            }

            PageScrollConfigType.LeveLastPageType -> {
                if (onDetail < -draggableOffset && mLevel2State.value.canScrollForward()) {//右滑动
                    mLevel2State.value.animateScrollToPage(
                        mLevel2State.value.currentPage + 1,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                    )

                } else if (onDetail > draggableOffset) {
                    mLevelState.value.animateScrollToPage(
                        mLevelState.value.currentPage - 1,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                    )
                }
            }

            else -> {}
        }
    }

    fun setParentDraggablePagerState(draggableState: PagerState?) {
        mLevelParentState.value = draggableState
    }

    private fun PagerState.canScrollForward(): Boolean {
        return canScrollForward && !isScrollInProgress
    }

    private fun PagerState.canScrollBackward(): Boolean {
        return canScrollBackward && !isScrollInProgress
    }


}