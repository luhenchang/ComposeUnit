package com.example.composeunit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.view.WindowCompat
import com.example.base.ui.PermissionActivity
import com.example.composeunit.ui.compose.other.service.showNotification
import com.example.composeunit.ui.compose.SplashViewModel
import com.example.composeunit.ui.compose.other.confing.NavGraph
import com.example.composeunit.ui.theme.ComposeUnitTheme

class MainActivity : PermissionActivity() {
    private val viewModel: SplashViewModel by viewModels()

    open class PopBroadCastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.e("onReceiver::","pop来了")
            if (intent?.action == "com.example.composeunit.pop") {
                Toast.makeText(context, "谈起来", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        requireSomePermission()
        setContent {
            val themeType = viewModel.themTypeState.collectAsState().value
            ComposeUnitTheme(themType = themeType) {
                NavGraph(viewModel = viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showNotification(this)
    }
}
