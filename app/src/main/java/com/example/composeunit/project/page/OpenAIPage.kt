package com.example.composeunit.project.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeunit.project.view_model.ai.OpenAiViewModel
import com.example.composeunit.project.widget.OpenAITopBar
import com.example.composeunit.project.widget.OpenAIUI
import com.example.composeunit.ui.theme.openAiDark

/**
 * Created by wangfei44 on 2023/4/7.
 */
@Composable
fun OpenAIPage(viewModel: OpenAiViewModel) {
    Scaffold(
        backgroundColor = openAiDark,
        topBar = {
            OpenAITopBar()
        }) {
        OpenAIUI(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            viewModel = viewModel
        )
    }
}