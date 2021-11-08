package com.example.composeunit.project
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.base.ui.PermissionActivity
import com.example.composeunit.R
import com.example.composeunit.confing.MainActions
import com.example.composeunit.confing.NavGraph
import com.example.composeunit.ui.theme.PlayTheme
import com.google.accompanist.insets.ProvideWindowInsets

class SplashActivity : PermissionActivity() {
    override fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProvideWindowInsets {
                PlayTheme {
                    NavGraph()
                }
            }
        }
    }
}

@Composable
fun SplashCompass(actions: MainActions) {
    val currentState = remember { MutableTransitionState(true) }
    currentState.targetState = false
    val transition = updateTransition(currentState, label = "splashCompass")
    val animalBooleanState by animateFloatAsState(
        if (!transition.currentState) {
            1f
        } else {
            0f
        }, animationSpec = TweenSpec(durationMillis = 1000),
        finishedListener = {
            Log.e("SplashCompass", "SplashCompass: ")
            actions.loginPage()
        }
    )


//    val infiniteTransition = rememberInfiniteTransition()
//    val animal by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(1000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )
    Box(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = Color.Red, shape = RectangleShape),
        contentAlignment = Alignment.Center
    ) {
        if (animalBooleanState < 1f) Image(
            modifier = Modifier.rotate(animalBooleanState * 360),
            painter = painterResource(R.drawable.head_lhc),
            contentDescription = ""
        ) else NavGraph()
    }

}

enum class BoxState { Collapsed, Expanded }

@Composable
fun AnimatingBox(boxState: BoxState) {
    val transitionData = updateTransitionData(boxState)
    // UI tree
    Box(
        modifier = Modifier
            .background(transitionData.color)
            .size(transitionData.size)
    )
}

// Holds the animation values.
private class TransitionData(
    color: State<Color>,
    size: State<Dp>
) {
    val color by color
    val size by size
}

// Create a Transition and return its animation values.
@Composable
private fun updateTransitionData(boxState: BoxState): TransitionData {
    val transition = updateTransition(boxState)
    val color = transition.animateColor { state ->
        when (state) {
            BoxState.Collapsed -> Color.Gray
            BoxState.Expanded -> Color.Red
        }
    }
    val size = transition.animateDp { state ->
        when (state) {
            BoxState.Collapsed -> 64.dp
            BoxState.Expanded -> 128.dp
        }
    }
    return remember(transition) { TransitionData(color, size) }
}