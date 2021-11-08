package com.example.base.ui

import android.Manifest
import android.app.AlertDialog
import android.util.Log
import com.blankj.utilcode.util.ToastUtils
import pub.devrel.easypermissions.EasyPermissions
import java.lang.StringBuilder
import kotlin.system.exitProcess

/**
 * Created by wangfei44 on 2021/11/5.
 */
abstract class PermissionActivity : BaseActivity(), EasyPermissions.PermissionCallbacks {

    /**
     * 权限获取失败
     *
     * @param requestCode /
     * @param perms       /
     */
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

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    abstract override fun initView()

    override fun requireSomePermission() {
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
}