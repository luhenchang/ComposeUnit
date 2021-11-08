package com.example.composeunit.project
import android.util.Log
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.base.ui.PermissionActivity
import com.example.composeunit.confing.NavGraph
import com.example.composeunit.ui.theme.PlayTheme
import com.google.accompanist.insets.ProvideWindowInsets

class LoginActivity : PermissionActivity() {
    override fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        Log.e("initView", "LoginActivity" )
        setContent {
            ProvideWindowInsets {
                PlayTheme {
                    NavGraph()
                }
            }
        }
    }

}
