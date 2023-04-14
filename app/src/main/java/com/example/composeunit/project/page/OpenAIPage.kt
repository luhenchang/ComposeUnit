package com.example.composeunit.project.page

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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