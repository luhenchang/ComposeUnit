package com.example.composeunit.project.widget.openai

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.composeunit.R
import com.example.composeunit.project.fragment.SpannableText
import com.example.composeunit.project.view_model.ai.OpenAiViewModel
import com.example.composeunit.utils.ai.getDelayTime
import com.example.composeunit.utils.ai.submitColor
import kotlinx.coroutines.delay

/**
 * Created by wangfei44 on 2023/4/22.
 */
@Composable
fun OpenAIMessageUI(
    content: String,
    errorNet: Boolean,
    viewModel: OpenAiViewModel,
    index: Int
) {
    Log.e("OpenAIMessageUI", content)
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(68, 70, 84, 255))
            .padding(top = 20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(Modifier.width(20.dp))
        ConstraintLayout {
            val (imageView, errorView) = createRefs()
            Image(
                painter = painterResource(id = R.mipmap.open_ai_head),
                contentDescription = "head",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .constrainAs(imageView) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(30.dp)
            )
            if (errorNet)
                Image(
                    painter = painterResource(id = R.mipmap.open_ai_error),
                    colorFilter = ColorFilter.tint(Color.Red),
                    contentDescription = "error",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .constrainAs(errorView) {
                            bottom.linkTo(imageView.bottom)
                            end.linkTo(imageView.end)
                        }
                        .offset(x = 6.dp, y = 8.dp)
                        .size(16.dp)
                )
        }
        SelectionContainer {
            OpenAIInputUI(content, viewModel, index)
        }
    }
}


@Composable
fun UserMessagesUI(content: String?) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(52, 54, 65, 255))
            .padding(top = 20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(Modifier.width(20.dp))
        Box(
            Modifier
                .size(30.dp)
                .background(
                    color = Color(
                        3,
                        149,
                        135,
                        255
                    ),
                    shape = RoundedCornerShape(5.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "11",
                color = Color.White,
            )
        }

        SelectionContainer {
            Text(
                text = content.toString(),
                color = Color.White,
                modifier = Modifier
                    .padding(start = 20.dp, bottom = 20.dp, end = 20.dp)
            )
        }
    }
}

@Composable
fun OpenAIImageUI(content: String, errorNet: Boolean) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(68, 70, 84, 255))
            .padding(top = 20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(Modifier.width(20.dp))
        ConstraintLayout {
            val (imageView, errorView) = createRefs()
            Image(
                painter = painterResource(id = R.mipmap.open_ai_head),
                contentDescription = "head",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .constrainAs(imageView) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(30.dp)
            )
            if (errorNet)
                Image(
                    painter = painterResource(id = R.mipmap.open_ai_error),
                    colorFilter = ColorFilter.tint(Color(95, 1, 1, 255)),
                    contentDescription = "error",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .constrainAs(errorView) {
                            bottom.linkTo(imageView.bottom)
                            end.linkTo(imageView.end)
                        }
                        .size(10.dp)
                )
        }
        AsyncImage(
            modifier = Modifier
                .size(125.dp)
                .padding(start = 20.dp, bottom = 20.dp, end = 20.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(content)
                .crossfade(true)
                .build(),
            onLoading = {

            },
            onSuccess = {

            },
            onError = {

            },
            placeholder = painterResource(R.drawable.jetpack),
            contentDescription = ""
        )
    }
}


@Composable
fun TrailingAnimalIcon() {
    val infiniteTransition = rememberInfiniteTransition()
    val animation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .width(15.dp)
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .size(3.dp)
                .background(Color(141, 141, 159, 255), shape = CircleShape)
        )
        Box(Modifier.width(3.dp))
        AnimatedVisibility(visible = (animation >= 1f)) {
            Box(
                modifier = Modifier
                    .size(3.dp)
                    .background(Color(141, 141, 159, 255), shape = CircleShape)
            )
        }
        Box(Modifier.width(3.dp))
        AnimatedVisibility(visible = (animation >= 2f)) {
            Box(
                modifier = Modifier
                    .size(3.dp)
                    .background(Color(141, 141, 159, 255), shape = CircleShape)
            )
        }

    }
}

@Composable
fun OpenAIInputUI(
    content: String,
    viewModel: OpenAiViewModel,
    currentIndex: Int
) {
    var text by remember { mutableStateOf("") }
    SpannableText(text)
    LaunchedEffect(Unit) {
        val data = content.toCharArray()
        if (!viewModel.startAnimal) {
            text = content
            return@LaunchedEffect
        }
        Log.e("size ==", "${(viewModel.getListSize())}::${currentIndex}")
        if (viewModel.getListSize() - 1 != currentIndex) {
            text = content
            return@LaunchedEffect
        }
        for (index in data.indices) {
            delay(getDelayTime(data.size))
            val endContext = text.replace("_", "") + data[index]
            text = if (index < data.size - 1) {
                endContext + "_"
            } else {
                viewModel.updateStarAnimalValue(false)
                endContext
            }
        }
    }
}

@Composable
fun TrailingSubmitIcon(
    textFieldValue: String,
    viewModel: OpenAiViewModel,
    focusManager: FocusManager,
    isFocused: Boolean,
    textFieldFun: (content: String) -> Unit
) {
    val context = LocalContext.current
    Icon(
        modifier = Modifier.clickable {
            if (textFieldValue.isNotEmpty()) {
                viewModel.setLoadValue(true)
                viewModel.getChatGTPMessage(context, textFieldValue)
                viewModel.setRegenerateInfo(textFieldValue)
                textFieldFun("")
                focusManager.clearFocus()
            }
        },
        painter = painterResource(R.mipmap.send_icon),
        contentDescription = "sendIcon",
        tint = submitColor(isFocused, textFieldValue)
    )
}