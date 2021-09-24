package com.example.composeunit

import android.Manifest
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.core.view.WindowCompat
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.lib_common.utils.AndroidShare
import com.example.lib_common.utils.pxToDp
import com.example.composeunit.confing.NavGraph
import com.example.composeunit.ui.theme.PlayTheme
import com.google.accompanist.insets.ProvideWindowInsets
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
import java.lang.StringBuilder
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity(), PermissionCallbacks {
    val TAG: String = LoginActivity::class.java.name
    private val netWorkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
           ToastUtils.showLong("网络变化了")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.transparentStatusBar(this)
        requireSomePermission()
        AndroidShare.mcontext = applicationContext
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val netWorkIntent= IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(netWorkChangeReceiver,netWorkIntent)
        setContent {
            ProvideWindowInsets {
                PlayTheme {
                    NavGraph()
                }
            }
        }
    }

    /**
     * 通过第三方插件easyPermissions来管理权限问题
     */
    private fun requireSomePermission() {
        EasyPermissions.requestPermissions(
            this,
            "申请权限",
            0,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_SETTINGS,
        )
    }

    /**
     * 权限获取失败
     *
     * @param requestCode /
     * @param perms       /
     */
    @Override
    override fun onPermissionsDenied(requestCode: Int, perms: List<String?>) {
        val stringBuilder = StringBuilder()
        for (i in perms.indices) {
            stringBuilder.append(perms[i])
            stringBuilder.append("\n")
        }
        stringBuilder.replace(stringBuilder.length - 2, stringBuilder.length, "")
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            ToastUtils.showShort("已拒绝权限" + stringBuilder + "并不再询问")
            AlertDialog.Builder(this)
                .setMessage("此功能需要" + stringBuilder + "权限，否则无法正常使用，是否打开设置")
                .setPositiveButton(
                    "是"
                ) { _, _ -> }.setNegativeButton(
                    "否"
                ) { _, _ -> exitProcess(-1) }.show()
        }
    }

    @Override
    override fun onPermissionsGranted(requestCode: Int, perms: List<String?>) {

    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(netWorkChangeReceiver)
    }

    @Composable
    fun getNavigationBarHeight(): Dp {
        var result = 0f
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimension(resourceId)
        }
        return result.pxToDp()
    } //返回值就是导航栏的高度,得到的值是像素

}
