package com.example.composeunit.ui.compose

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeunit.R
import com.example.composeunit.ui.compose.canvas_ui.QureytoImageShapes
import com.example.composeunit.utils.getBitmap
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.verticalDrag
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.IntOffset
import com.example.composeunit.ui.compose.confing.CicleImageShape
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

//coroutineScope {
//    while (true) {
//        val offset = awaitPointerEventScope {
//            awaitFirstDown().position
//        }
//        // Launch a new coroutine for animation so the touch detection thread is not
//        // blocked.
//        launch {
//            // Animates to the pressed position, with the given animation spec.
//            animatedOffset.animateTo(
//                offset.x,
//                animationSpec = spring(stiffness = Spring.StiffnessLow)
//            )
//        }
//
//    }
//}
@Preview
@Composable
fun clickStudyView() {
    val animatedOffset = remember { Animatable(0f) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .pointerInput(Unit) {
                coroutineScope {
                    while (true) {
                        val offset = awaitPointerEventScope {
                            awaitFirstDown().position
                        }
                        // Launch a new coroutine for animation so the touch detection thread is not
                        // blocked.
                        launch {
                            // Animates to the pressed position, with the given animation spec.
                            animatedOffset.animateTo(
                                offset.x,
                                animationSpec = spring(stiffness = Spring.DampingRatioLowBouncy)
                            )
                        }

                    }
                }
            }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                bitmap = getBitmap(R.drawable.head_god),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(QureytoImageShapes(160f),)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(0.dp)
                    .clip(CicleImageShape())
                    .background(Color(206, 236, 250, 121))
                    .width(130.dp)
                    .height(130.dp)
            ) {
                Image(
                    bitmap = getBitmap(R.drawable.head_god),
                    contentDescription = "w",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp)
                        .background(color = Color(0XFF0DBEBF), shape = CircleShape)
                        .padding(3.dp)
                        .clip(
                            CircleShape
                        )
                        .shadow(elevation = 150.dp, clip = true)
                        .rotate(
                        animatedOffset.value
                    )
                )
            }
        }


//        Box(
//            Modifier
//                .fillMaxSize()
//                .background(Color(0xffb99aff))
//                .pointerInput(Unit) {
//                    coroutineScope {
//                        while (true) {
//                            val offset = awaitPointerEventScope {
//                                awaitFirstDown().position
//                            }
//                            // Launch a new coroutine for animation so the touch detection thread is not
//                            // blocked.
//                            launch {
//                                // Animates to the pressed position, with the given animation spec.
//                                animatedOffset.animateTo(
//                                    offset.x,
//                                    animationSpec = spring(stiffness = Spring.StiffnessLow)
//                                )
//                            }
//
//                        }
//                    }
//                }
//        ) {
//            Text("Tap anywhere", Modifier.align(Alignment.Center))
//            Box(
//                Modifier
//                    .offset {
//                        // Use the animated offset as the offset of the Box.
//                        IntOffset(
//                            animatedOffset.value.roundToInt() - Dp(20f)
//                                .toPx()
//                                .toInt(),
//                            animatedOffset.value.roundToInt() - Dp(20f)
//                                .toPx()
//                                .toInt()
//                        )
//                    }
//                    .size(40.dp)
//                    .background(Color(0xff3c1361), CircleShape)
//            )
//        }

    }
}


/**
 * In this example, we create a swipe-to-dismiss modifier that dismisses the child via a
 * vertical swipe-up.
 */
fun Modifier.swipeToDismiss(): Modifier = composed {
    // Creates a Float type `Animatable` and `remember`s it
    val animatedOffsetY = remember { Animatable(0f) }
    this
        .pointerInput(Unit) {
            coroutineScope {
                while (true) {
                    val pointerId = awaitPointerEventScope {
                        awaitFirstDown().id
                    }
                    val velocityTracker = VelocityTracker()
                    awaitPointerEventScope {
                        verticalDrag(pointerId) {
                            // Snaps the value by the amount of finger movement
                            launch {
                                animatedOffsetY.snapTo(
                                    animatedOffsetY.value + it.positionChange().y
                                )
                            }
                            velocityTracker.addPosition(
                                it.uptimeMillis,
                                it.position
                            )
                        }
                    }
                    // At this point, drag has finished. Now we obtain the velocity at the end of
                    // the drag, and animate the offset with it as the starting velocity.
                    val velocity = velocityTracker.calculateVelocity().y

                    // The goal for the animation below is to animate the dismissal if the fling
                    // velocity is high enough. Otherwise, spring back.
                    launch {
                        // Checks where the animation will end using decay
                        val decay = splineBasedDecay<Float>(this@pointerInput)

                        // If the animation can naturally end outside of visual bounds, we will
                        // animate with decay.
                        if (decay.calculateTargetValue(
                                animatedOffsetY.value,
                                velocity
                            ) < -size.height
                        ) {
                            // (Optionally) updates lower bounds. This stops the animation as soon
                            // as bounds are reached.
                            animatedOffsetY.updateBounds(
                                lowerBound = -size.height.toFloat()
                            )
                            // Animate with the decay animation spec using the fling velocity
                            animatedOffsetY.animateDecay(velocity, decay)
                        } else {
                            // Not enough velocity to be dismissed, spring back to 0f
                            animatedOffsetY.animateTo(0f, initialVelocity = velocity)
                        }
                    }
                }
            }
        }
        .offset { IntOffset(0, animatedOffsetY.value.roundToInt()) }
}