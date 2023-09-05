package com.example.composeunit.ui.compose.home

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.composeunit.project.view_model.home.HomeViewModel
import com.example.composeunit.ui.compose.home.shape.BottomBarAnimalBgView
import com.example.composeunit.ui.navigation.BottomBarScreen
import com.example.composeunit.ui.navigation.BottomBarScreens

/**
 * 自定义底部导航栏切换 - 样式一
 * @param homeViewModel viewModel管理index索引等
 *
 */
@Composable
fun BottomBarNavigation(
    homeViewModel: HomeViewModel,
    onTapBottom: (String) -> Unit,
    modifier: Modifier,
    bottomHeight: Dp = 60.dp,
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val simplifiedValue = if (homeViewModel.animalBoolean.value) 0f else 1f
    val animalBooleanState: Float by animateFloatAsState(
        simplifiedValue, animationSpec = TweenSpec(durationMillis = 600),
        label = "simplifiedValue"
    )
    val indexValue: Float by animateFloatAsState(
        homeViewModel.position.value!!.toFloat(),
        animationSpec = TweenSpec(durationMillis = 500),
        label = "indexValue"
    )
    Column(
        modifier = modifier
    ) {
        Box(
            Modifier.height(bottomHeight)
        ) {
            BottomBarAnimalBgView(indexValue)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomBarScreens.forEachIndexed { index, bottomBarScreen ->
                    BottomView(
                        homeViewModel,
                        bottomBarScreen,
                        currentDestination,
                        index,
                        bottomBarScreen.selectedIcon,
                        animalBooleanState,
                        onTapBottom,
                        bottomHeight
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .background(Color.Red)
        )
    }

}


@Preview
@Composable
fun BottomView(
    homeViewModel: HomeViewModel,
    bottomBarScreen: BottomBarScreen,
    currentDestination: NavDestination?,
    index: Int,
    icon: Int,
    animalBooleanState: Float,
    onTapBottom: (String) -> Unit,
    bottomHeight: Dp
) {
    val selected = currentDestination?.hierarchy?.any {
        it.route?.contains(bottomBarScreen.route) ?: false
    } ?: false
    val tint =
        if (selected)
            MaterialTheme.colors.primary
        else
            Color.White
    Image(painter = painterResource(id = bottomBarScreen.selectedIcon),
        colorFilter = ColorFilter.tint(tint),
        contentDescription = "1",
        modifier = Modifier
            .modifier(homeViewModel.position.value, index, animalBooleanState, bottomHeight)
            .clickable {
                homeViewModel.animalBoolean.value = !homeViewModel.animalBoolean.value
                homeViewModel.positionChanged(index)
                val route = when (bottomBarScreen.route) {
                    BottomBarScreen.Home.route -> BottomBarScreen.Home.route
                    BottomBarScreen.Setting.route -> BottomBarScreen.Setting.route
                    else -> BottomBarScreen.Widget.sendArgumentRoute(0)
                }
                onTapBottom(route)
            }
    )
}


@Composable
fun Modifier.modifiers(
    animalCenterIndex: Int?,
    i: Int,
    animalBooleanState: Float
) = this.then(
    if (animalCenterIndex == i) {
        Modifier
            .padding(bottom = 35.dp + (animalBooleanState * 100).dp)
            .width(25.dp)
            .height(25.dp)
    } else {
        Modifier
            .padding(bottom = 35.dp - (animalBooleanState * 10).dp)
            .width(25.dp)
            .height(25.dp)
    }
)

fun Modifier.modifier(
    animalCenterIndex: Int?,
    i: Int,
    animalBooleanState: Float,
    bottomHeight: Dp,
    iconSize: Dp = 25.dp
) = this.then(
    //radius
    if (animalCenterIndex == i) {
        Modifier
            .offset(y = -bottomHeight / 2)
            .width(iconSize)
            .height(iconSize)
            .rotate(animalBooleanState * 360f)
    } else {
        Modifier
            .padding(top = (bottomHeight - (iconSize * 2)) / 2)
            .width(iconSize)
            .height(iconSize)
    }
)