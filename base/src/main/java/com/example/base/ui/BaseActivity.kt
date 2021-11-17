package com.example.base.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.blankj.utilcode.util.ToastUtils
import com.example.lib_common.utils.AndroidShare
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by wangfei44 on 2021/11/5.
 */
abstract class BaseActivity : AppCompatActivity() {
    private val tagName: String = BaseActivity::class.java.name
    private val netWorkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            ToastUtils.showLong("网络变化了")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireSomePermission()
        Log.e(tagName, "onCreate")
        val netWorkIntent = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(netWorkChangeReceiver, netWorkIntent)
        initView()
    }

    abstract fun initView()

    /**
     * 通过第三方插件easyPermissions来管理权限问题
     */
    abstract fun requireSomePermission();
    override fun onResume() {
        Log.e(tagName, "onResume")
        super.onResume()
    }

    override fun onRestart() {
        Log.e(tagName, "onRestart")
        super.onRestart()
    }

    override fun onStart() {
        Log.e(tagName, "onRestart")
        super.onStart()
    }

    override fun onPause() {
        Log.e(tagName, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.e(tagName, "onStop")
        super.onStop()
    }

    override fun onNewIntent(intent: Intent?) {
        Log.e(tagName, "onNewIntent")
        super.onNewIntent(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.e(tagName, "onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.e(tagName, "onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        Log.e(tagName, "onDestroy")
        super.onDestroy()
        unregisterReceiver(netWorkChangeReceiver)
    }
}