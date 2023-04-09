package com.example.composeunit.project.widget

/**
 * Created by wangfei44 on 2023/4/7.
 */
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.composeunit.R
import com.example.composeunit.models.chatgtp.ChatGTPModel
import com.example.composeunit.models.chatgtp.Data
import com.example.composeunit.models.chatgtp.ImageData
import com.example.composeunit.models.chatgtp.ModelData
import com.example.composeunit.project.view_model.ai.OpenAiViewModel
import kotlinx.coroutines.delay


@Preview
@Composable
fun OpenAIUI(
    modifier: Modifier,
    textFieldAlignment: Alignment = Alignment.BottomCenter,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    viewModel: OpenAiViewModel,
) {
    val loading by viewModel.loading.collectAsState()
    val regenerateInfo by viewModel.regenerateResponseInfo.collectAsState()

    val pageList = viewModel.pageList.collectAsState().value.apply {
        if (isNotEmpty() && (this[size - 1] as ModelData).isAI) {
            Log.e("loading =", loading.toString())
            viewModel.setLoadValue(false)
        }
    }
    val textFieldValue = remember { mutableStateOf(TextFieldValue("")) }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    DisposableEffect(isFocused) {
        if (isFocused) {

        }
        onDispose {

        }
    }

    val focusManager = LocalFocusManager.current

    val focusRequester = remember { FocusRequester() }
    val draggableState = rememberDraggableState(onDelta = { delta ->
        Log.e("draggableState =", delta.toString())
        if (delta > 40) {
            focusRequester.requestFocus()
        }
        if (delta < -40) {
            focusManager.clearFocus()
        }
    })

    val infiniteTransition = rememberInfiniteTransition()
    val animation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(modifier = modifier
        .draggable(
            state = draggableState, orientation = Orientation.Vertical
        )
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            focusManager.clearFocus()
        }, contentAlignment = textFieldAlignment
    ) {
        OpenAIListView(pageList)
        Column(
            Modifier
                .fillMaxWidth()
                .offset(y = (-10).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OpenAIReRequestUI(
                pageList,
                loading,
                viewModel,
                regenerateInfo,
                focusManager
            )
            Box(Modifier.height(5.dp))
            OpenAIBottomInputUI(
                isFocused,
                textFieldValue,
                loading,
                interactionSource,
                focusRequester,
                animation,
                viewModel,
                focusManager
            )
        }
    }
}

@Composable
private fun OpenAIReRequestUI(
    pageList: ArrayList<ChatGTPModel>,
    loading: Boolean,
    viewModel: OpenAiViewModel,
    regenerateInfo: String,
    focusManager: FocusManager
) {
    if (pageList.isNotEmpty()) {
        Row(
            Modifier
                .background(color = Color(52, 54, 65, 255))
                .border(
                    0.6.dp,
                    Color(85, 87, 104, 255),
                    RoundedCornerShape(5.dp),
                )
                .clickable {
                    if (loading) {
                        viewModel.setLoadValue(false)
                        viewModel.cancelJob()
                    } else {
                        if (regenerateInfo.isNotEmpty() && !(pageList[pageList.size - 1] as ModelData).isAI) {
                            viewModel.setLoadValue(true)
                            viewModel.regenerateChatGTPMMessage(regenerateInfo)
                            focusManager.clearFocus()
                        }
                    }
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .height(18.dp)
                    .width(18.dp)
                    .padding(start = 5.dp),
                painter = painterResource(
                    id = if (loading)
                        R.mipmap.open_ai_stop else
                        R.mipmap.open_ai_reload
                ),
                contentDescription = "reload",
                tint = Color(216, 216, 226, 255)
            )
            Text(
                text = if (loading) "stop generating" else "Regenerate response",
                Modifier.padding(5.dp),
                color = Color(216, 216, 226, 255)
            )
        }
    }
}

@Composable
private fun OpenAIListView(pageList: ArrayList<ChatGTPModel>) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        items(pageList.size) { index ->
            when (val data = pageList[index]) {
                is ModelData -> {
                    data.choices?.get(0)?.message?.content?.let { content ->
                        if (index % 2 == 0)
                            UserMessagesUI(content)
                        else
                            OpenAIMessageUI(content)
                    }
                }
                is ImageData -> {
                    if (index % 2 == 0)
                        UserMessagesUI(data.userData)
                    else
                        OpenAIImageUI(data.data[0])
                }
            }
        }
    }
}

@Composable
private fun OpenAIBottomInputUI(
    isFocused: Boolean,
    textFieldValue: MutableState<TextFieldValue>,
    loading: Boolean,
    interactionSource: MutableInteractionSource,
    focusRequester: FocusRequester,
    animation: Float,
    viewModel: OpenAiViewModel,
    focusManager: FocusManager
) {
    TextField(
        textStyle = TextStyle(
            color = (inputColor(isFocused, textFieldValue.value.text, loading))
        ), colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            backgroundColor = Color(64, 65, 79, 255)
        ),
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(50.dp)
            .focusRequester(focusRequester)
            .background(
                color = Color(64, 65, 79, 255), RoundedCornerShape(10)
            )
            .border(
                0.5.dp, Color(47, 49, 56, 166), RoundedCornerShape(10)
            ),
        placeholder = {
            Text(
                text = "Send a message...",
                color = (inputColor(isFocused, textFieldValue.value.text))
            )
        },
        shape = RoundedCornerShape(10),
        value = textFieldValue.value,
        onValueChange = {
            if (!isFocused && it.text.isNotEmpty()) {
                textFieldValue.value = TextFieldValue(
                    text = it.text, selection = TextRange(0, it.text.lastIndex + 1)
                )
            } else {
                textFieldValue.value = TextFieldValue(
                    text = it.text,
                    selection = TextRange(it.text.lastIndex + 1)
                )
            }
        }, trailingIcon = {
            if (loading)
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
            else
                Icon(
                    modifier = Modifier.clickable {
                        if (textFieldValue.value.text.isNotEmpty()) {
                            viewModel.setLoadValue(true)
                            viewModel.getChatGTPMessage(textFieldValue.value.text)
                            viewModel.setRegenerateInfo(textFieldValue.value.text)
                            textFieldValue.value = TextFieldValue(
                                text = ""
                            )
                            focusManager.clearFocus()
                        }
                    },
                    painter = painterResource(R.mipmap.send_icon),
                    contentDescription = "sendIcon",
                    tint = submitColor(isFocused, textFieldValue.value.text)
                )
        })
}

@Composable
fun OpenAIInputUI(content: String) {
    var text by remember { mutableStateOf("") }
    Text(
        text = text,
        color = Color.White,
        modifier = Modifier
            .padding(start = 20.dp, bottom = 20.dp, end = 20.dp)
    )

    LaunchedEffect(content) {
        val data = content.toCharArray()
        for (index in data.indices) {
            delay(getDelayTime(data.size))
            val endContext = text.replace("_", "") + data[index]
            text = if (index < data.size - 1)
                endContext + "_"
            else
                endContext
        }
    }
}

@Composable
private fun UserMessagesUI(content: String) {
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
                text = content,
                color = Color.White,
                modifier = Modifier
                    .padding(start = 20.dp, bottom = 20.dp, end = 20.dp)
            )
        }
    }
}

@Composable
private fun OpenAIMessageUI(content: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(68, 70, 84, 255))
            .padding(top = 20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(Modifier.width(20.dp))
        Image(
            painter = painterResource(id = R.mipmap.open_ai_head),
            contentDescription = "head",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(30.dp)
        )
        SelectionContainer {
            OpenAIInputUI(content)
        }
    }
}

@Composable
private fun OpenAIImageUI(content: Data) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(68, 70, 84, 255))
            .padding(top = 20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(Modifier.width(20.dp))
        Image(
            painter = painterResource(id = R.mipmap.open_ai_head),
            contentDescription = "head",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(30.dp)
        )
        AsyncImage(
            modifier = Modifier.size(60.dp),
            model = content.url,
            contentDescription = ""
        )
    }
}


fun getDelayTime(size: Int): Long {
    return if (size < 20) {
        100
    } else if (size < 100) {
        50
    } else {
        30
    }
}

@Composable
private fun submitColor(isFocused: Boolean, textValue: String, loading: Boolean = false) =
    if (isFocused && textValue.isNotEmpty() && !loading) Color.White
    else Color(
        141, 141, 159, 255
    )

@Composable
private fun inputColor(isFocused: Boolean, textValue: String, loading: Boolean = false) =
    if (isFocused && textValue.isNotEmpty() && !loading) Color.White
    else Color(
        141, 141, 159, 255
    )