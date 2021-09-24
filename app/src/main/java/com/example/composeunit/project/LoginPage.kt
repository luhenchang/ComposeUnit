import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import com.example.composeunit.confing.MainActions
import kotlinx.coroutines.launch
import androidx.compose.animation.core.*
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.foundation.background
import kotlinx.coroutines.coroutineScope
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeunit.composeble_ui.login.*
import com.example.composeunit.project.fragment.twofragments.TextPage

@Composable
fun LoginPage(mainActions: MainActions) {
    val animatedOffset = remember { Animatable(0f) }
    val animatedScales = remember { Animatable(1f) }
    val animatedRound = remember { Animatable(30f) }
    val animatedCheckBox = remember { Animatable(0f) }
    val animatedBitmap = remember { Animatable(0f) }
    val animatedText = remember { Animatable(1f) }
    val animatedColor = remember { Animatable(Color(206, 199, 250, 121)) }
    val mutableState: MutableState<Boolean> = remember { mutableStateOf(true) }
    val inputUserName: MutableState<String> = remember { mutableStateOf("") }
    val inputPassworld: MutableState<String> = remember { mutableStateOf("") }
    Box {
        Box {
            LoginPageBackgroundBlurImage(animatedRound, animatedScales)
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.pointerInput(Unit) {
                    coroutineScope {
                        while (true) {
                            val offset = awaitPointerEventScope {
                                awaitFirstDown().position
                            }
                            // Launch a new coroutine for animation so the touch detection thread is not
                            // blocked.
                            launch {
                                if (mutableState.value) {
                                    animatedScales.animateTo(
                                        1.3f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedText.animateTo(
                                        1f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedRound.animateTo(
                                        10f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedBitmap.animateTo(
                                        10f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedCheckBox.animateTo(
                                        10f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedColor.animateTo(Color(206, 170, 209, 121))
                                } else {
                                    animatedScales.animateTo(
                                        1f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedText.animateTo(
                                        -2f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedRound.animateTo(
                                        30f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedBitmap.animateTo(
                                        0f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedCheckBox.animateTo(
                                        0f,
                                        animationSpec = spring(stiffness = StiffnessLow)
                                    )
                                    animatedColor.animateTo(Color(206, 199, 250, 121))
                                }

                                mutableState.value = !mutableState.value
                                animatedOffset.animateTo(
                                    offset.x,
                                    animationSpec = spring(stiffness = StiffnessLow)
                                )

                            }

                        }
                    }
                }) {
                Box(contentAlignment = Alignment.Center) {
                    LoginPageTopBlurImage(animatedBitmap, animatedOffset, animatedScales)
                    LoginPageTopRotaAndScaleImage(animatedColor, animatedScales, animatedOffset)
                }
                LoginPageTopTextBox(animatedOffset, animatedScales)
                LoginPageInput(
                    inputUserName,
                    animatedColor,
                    animatedRound,
                    inputPassworld
                )
                LoginPageCheckBox(animatedCheckBox, animatedOffset, animatedRound)
                LoginPageBootomButton(animatedScales, animatedColor, mainActions)

            }
        }
    }
}


@Composable
fun TextStudy1() {
    Text(
        text = "hello world",
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Blue),
        style = TextStyle(
            color = Color.Red,
            shadow = Shadow(
                color = Color.Green,
                offset = Offset(2f, 2f),
                blurRadius = 1f,
            )
        )
    )
}