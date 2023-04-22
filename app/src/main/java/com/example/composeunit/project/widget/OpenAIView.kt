package com.example.composeunit.project.widget

/**
 * Created by wang fei44 on 2023/4/7.
 */
import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.composeunit.R
import com.example.composeunit.project.fragment.SpannableText
import com.example.composeunit.project.view_model.ai.OpenAiViewModel
import com.example.composeunit.repository.dao.table.ChatContent
import com.example.composeunit.ui.theme.openAiLight
import kotlinx.coroutines.delay
import com.example.composeunit.utils.ai.*


@Preview
@Composable
fun OpenAIUI(
    modifier: Modifier,
    textFieldAlignment: Alignment = Alignment.BottomCenter,
    viewModel: OpenAiViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    Log.e("OpenAIUI=", "uiState")
    //局部刷新dataList相关UI
    val dataList by remember(uiState.dataList) {
        derivedStateOf {
            uiState.dataList
        }
    }
    //局部刷新加载相关UI
    val loading by remember(uiState.loading) {
        derivedStateOf {
            uiState.loading
        }
    }
    //局部控制刷新
    val regenerateInfo by remember(uiState.regenerateInfo) {
        derivedStateOf {
            uiState.regenerateInfo
        }
    }
    val focusManager = LocalFocusManager.current

    Box(modifier = modifier
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            focusManager.clearFocus()
        }, contentAlignment = textFieldAlignment
    ) {
        OpenAIListView(dataList, viewModel)
        Column(
            Modifier
                .fillMaxWidth()
                .offset(y = (-10).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OpenAIReRequestUI(
                loading,
                viewModel,
                regenerateInfo,
                focusManager
            )
            Box(Modifier.height(5.dp))
            OpenAIBottomInputUI(
                loading,
                viewModel,
                focusManager = focusManager
            )
        }
    }
}

@Composable
private fun OpenAIReRequestUI(
    loading: Boolean,
    viewModel: OpenAiViewModel,
    regenerateInfo: String,
    focusManager: FocusManager,
    context: Context = LocalContext.current
) {
    Log.e("OpenAIReRequestUI=", "OpenAIReRequestUI")
    if (viewModel.getListSize() > 0) {
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
                        if (regenerateInfo.isEmpty()) {
                            return@clickable
                        }
                        val data = viewModel.getLastChatContent
                        if (data?.content_is_ai == 0) {
                            viewModel.setLoadValue(true)
                            viewModel.regenerateChatGTPMMessage(context, regenerateInfo)
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
private fun OpenAIListView(
    pageList: ArrayList<ChatContent>,
    viewModel: OpenAiViewModel
) {
    Log.e("OpenAIListView=", "OpenAIListView")
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        items(pageList.size, key = { index ->
            pageList[index].content_id + index
        }) { index ->
            val data = pageList[index]
            when (data.content_type) {
                0 -> {
                    data.let { chatInfo ->
                        if (chatInfo.content_is_ai == 0)
                            UserMessagesUI(chatInfo.content)
                        else
                            OpenAIMessageUI(
                                chatInfo.content,
                                chatInfo.errorNet,
                                viewModel,
                                index
                            )
                    }
                }
                1 -> {
                    data.let { chatInfo ->
                        if (chatInfo.content_is_ai == 0)
                            UserMessagesUI(chatInfo.content)
                        else
                            OpenAIImageUI(chatInfo.content, data.errorNet)
                    }
                }
            }
        }
    }
}

@Composable
private fun OpenAIBottomInputUI(
    loading: Boolean,
    viewModel: OpenAiViewModel,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    focusManager: FocusManager
) {
    Log.e("OpenAIBottomInputUI=", "OpenAIBottomInputUI")
    val isFocused = interactionSource.collectIsFocusedAsState().value
    //局部刷新textField相关UI
    val mTextFieldValue = remember {
        mutableStateOf(TextFieldValue(""))
    }
    val focusRequester = remember { FocusRequester() }
    TextField(
        textStyle = TextStyle(
            color = (inputColor(isFocused, mTextFieldValue.value.text, loading))
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
                color = (inputColor(isFocused, mTextFieldValue.value.text))
            )
        },
        shape = RoundedCornerShape(10),
        value = mTextFieldValue.value.text,
        onValueChange = {
            val value: TextFieldValue = if (!isFocused && it.isNotEmpty()) {
                TextFieldValue(
                    text = it, selection = TextRange(0, it.lastIndex + 1)
                )
            } else {
                TextFieldValue(
                    text = it,
                    selection = TextRange(it.lastIndex + 1)
                )
            }
            mTextFieldValue.value = value
        }, trailingIcon = {
            if (loading) {
                TrailingAnimalIcon()
            } else
                TrailingSubmitIcon(mTextFieldValue.value.text, viewModel, focusManager, isFocused) {
                    mTextFieldValue.value = TextFieldValue("")
                }
        })
}

@Composable
private fun TrailingSubmitIcon(
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

@Composable
private fun TrailingAnimalIcon() {
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
private fun UserMessagesUI(content: String?) {
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
private fun OpenAIMessageUI(
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
private fun OpenAIImageUI(content: String, errorNet: Boolean) {
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
fun OpenAITopBar() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(
                openAiLight
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "OpenAI",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                shadow = Shadow(
                    Color(43, 43, 43, 255),
                    offset = Offset(2f, 6f),
                    blurRadius = 11f
                )
            )
        )
    }
}