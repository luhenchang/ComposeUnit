package com.example.composeunit.project.page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import com.example.composeunit.confing.MainActions
import com.example.composeunit.project.view_model.ai.OpenAiViewModel
import com.example.composeunit.project.widget.OpenAIUI

/**
 * Created by wangfei44 on 2023/4/7.
 */
@Composable
fun OpenAIPage(actions: MainActions, viewModel: OpenAiViewModel) {
    Scaffold(
        backgroundColor = Color(52, 54, 65, 255),
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(
                        Color(68, 70, 84, 255)
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
        }) {
        OpenAIUI(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            viewModel = viewModel
        )
    }
}