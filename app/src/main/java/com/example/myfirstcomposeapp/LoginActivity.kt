package com.example.myfirstcomposeapp
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.blankj.utilcode.util.BarUtils
import com.bumptech.glide.Glide
import com.example.myfirstcomposeapp.confing.NavGraph
import com.example.myfirstcomposeapp.ui.theme.PlayTheme
import dev.chrisbanes.accompanist.glide.GlideImage
import dev.chrisbanes.accompanist.glide.LocalRequestManager
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    @InternalComposeApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setNavBarVisibility(window, true)
        BarUtils.transparentStatusBar(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            //设置路由导航
            ProvideWindowInsets {
                PlayTheme {
                    NavGraph()
                }
            }
        }
    }

}
