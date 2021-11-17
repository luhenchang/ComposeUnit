package com.example.composeunit.project.view_model.splash

import android.app.Service
import android.util.Log
import androidx.compose.animation.core.MutableTransitionState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.base.model.BaseViewModel

/**
 * Created by wangfei44 on 2021/11/8.
 */
class SplashViewModel : BaseViewModel() {
    private val _currentState = MutableTransitionState(true)
    private val _animalValue = MutableLiveData(0f)
    val currentState = _currentState
    val animalValue:LiveData<Float> = _animalValue

    var mService: Service? = null

    fun setAnimal(){
        _currentState.targetState = false
    }
    fun updateAnimalValue(animalValue: Float){
        _animalValue.value =  animalValue
    }
}