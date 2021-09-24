package com.example.composeunit.project.view_model.message

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MessageViewModel : ViewModel() {
    val animatedOffset = Animatable(0f)
    val animatedOffsetY = Animatable(0f)
    val animalChange = mutableStateOf(false)

}