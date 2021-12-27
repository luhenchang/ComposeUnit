package com.example.composeunit.project

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.blankj.utilcode.util.ToastUtils
import com.example.base.ui.PermissionActivity
import com.example.composeunit.R
import com.example.composeunit.confing.MainActions
import com.example.composeunit.confing.NavGraph
import com.example.composeunit.project.SplashActivity.Companion.TAG
import com.example.composeunit.project.service.RecorderService
import com.example.composeunit.project.view_model.splash.SplashViewModel
import com.example.composeunit.ui.theme.PlayTheme
import com.google.accompanist.insets.ProvideWindowInsets

class SplashActivity : PermissionActivity() {
    companion object {
        var TAG = this.javaClass.name.toString()
    }

    val viewModel: SplashViewModel = SplashViewModel()
    val serveConnect = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "serveConnect :: onServiceConnected")
            viewModel.mService = (service as RecorderService.RecorderBind).service

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "serveConnect :: onServiceDisconnected")
        }
    }

    override fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        bindService(
            Intent(applicationContext, RecorderService::class.java),
            serveConnect,
            BIND_AUTO_CREATE
        )
        setContent {
            ProvideWindowInsets {
                PlayTheme {
                    NavGraph()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume :: showRecordingNotification show::false")
        showRecordingNotification(this, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serveConnect)
    }
}

@Composable
fun SplashCompass(actions: MainActions, viewModel: SplashViewModel = SplashViewModel()) {
    val animalValue: Float? = viewModel.animalValue.observeAsState().value
    initAnimal(viewModel, actions)
    val context = LocalContext.current
    Box(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.splash_icon))
                    .offset(y = (-animalValue!! * 100).dp)
                    .rotate(360 * animalValue)
                    .clickable {
                        Toast
                            .makeText(context, "通知服务显示通知栏", Toast.LENGTH_LONG)
                            .show()
                        showRecordingNotification(context, true)
                    },
                painter = painterResource(R.mipmap.jetpack_icon),
                contentDescription = ""
            )
            Image(
                painter = painterResource(R.drawable.head_god),
                contentDescription = "w",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.splash_head_icon))
                    .offset(y = (-(1 - animalValue) * 100).dp)
                    .background(color = Color(0XFF0DBEBF), shape = CircleShape)
                    .padding(3.dp)
                    .clip(CircleShape)
                    .shadow(elevation = 150.dp, clip = true)
            )
        }
    }

}

fun showRecordingNotification(context: Context, show: Boolean) {
    Log.i(TAG, "showRecordingNotification show::$show")
    val intent = Intent(context, RecorderService::class.java)
    intent.putExtra(RecorderService.ACTION_NAME, RecorderService.ACTION_RECORD_NOTIFICATION)
    intent.putExtra(RecorderService.PARAM_SHOW_NOTIFICATION, show)
    context.startService(intent)
}

@Composable
fun initAnimal(viewModel: SplashViewModel, actions: MainActions) {
    viewModel.setAnimal()
    val transition = updateTransition(viewModel.currentState, label = "splashCompass")
    val animalValues = animateFloatAsState(
        if (!transition.currentState) {
            1f
        } else {
            0f
        }, animationSpec = TweenSpec(durationMillis = 2000),
        finishedListener = {
            actions.loginPage()
        }
    )
    viewModel.updateAnimalValue(animalValues.value)
}