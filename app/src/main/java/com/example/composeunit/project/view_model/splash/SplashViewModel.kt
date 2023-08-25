package com.example.composeunit.project.view_model.splash

import android.app.Service
import android.util.Log
import androidx.compose.animation.core.MutableTransitionState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.base.model.BaseViewModel
import com.example.composeunit.ui.theme.ThemeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Created by wangfei44 on 2021/11/8.
 */
class SplashViewModel : BaseViewModel() {
    private val _currentState = MutableTransitionState(true)
    private val _animalValue = MutableLiveData(0f)
    val currentState = _currentState
    val animalValue: LiveData<Float> = _animalValue
    private val _themTypeState = MutableStateFlow<ThemeType>(ThemeType.BLUE_THEM)
    val themTypeState: StateFlow<ThemeType> = _themTypeState

    var mService: Service? = null

    fun setAnimal() {
        _currentState.targetState = false
    }

    fun updateAnimalValue(animalValue: Float) {
        Log.e(this.javaClass.name, "updateAnimalValue::animalValue = $animalValue")
        _animalValue.value = animalValue
    }

    fun updateTheme(themeType: Int) = viewModelScope.launch {
        when (themeType) {
            0 -> {
                _themTypeState.emit(ThemeType.BLUE_THEM)
            }
            1 -> {
                _themTypeState.emit(ThemeType.RED_THEM)
            }
            2 -> {
                _themTypeState.emit(ThemeType.PURPLE_THEM)
            }
            3 -> {
                _themTypeState.emit(ThemeType.GREEN_THEM)
            }
            4 -> {
                _themTypeState.emit(ThemeType.ORANGE_THEM)
            }
            5 -> {
                _themTypeState.emit(ThemeType.LIGHT_BLUE_THEM)
            }
            6 -> {
                _themTypeState.emit(ThemeType.YELLOW_THEM)
            }
            else -> {
                _themTypeState.emit(ThemeType.BLUE_THEM)
            }
        }
    }
}