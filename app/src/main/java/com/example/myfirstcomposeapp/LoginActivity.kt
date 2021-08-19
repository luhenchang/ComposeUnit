package com.example.myfirstcomposeapp
import android.Manifest
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.lib_common.utils.AndroidShare
import com.example.myfirstcomposeapp.confing.NavGraph
import com.example.myfirstcomposeapp.ui.theme.PlayTheme
import com.google.accompanist.insets.ProvideWindowInsets
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
import java.lang.StringBuilder
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity(), PermissionCallbacks {
    val TAG=LoginActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setNavBarVisibility(window, true)
        BarUtils.transparentStatusBar(this)
        requireSomePermission()
        AndroidShare.mcontext= applicationContext
        val db: AppDatabase = RoomAsset.databaseBuilder(applicationContext, AppDatabase::class.java, "chinook.db").build()
        val list1= db.chinookDao().users
        Log.e(TAG, "onCreate: "+list1[0].name)
        val list2= db.chinookDao().composeDatas
        Log.e(TAG, "onCreate: "+list2[0].item_content)
        WindowCompat.setDecorFitsSystemWindows(window, false)
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
}
