package com.example.lib_common.utils

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore.Images.Media.insertImage
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.lib_common.utils.AndroidShare.DRAWABLE
import com.example.lib_common.utils.AndroidShare.isAvilible
import org.xmlpull.v1.XmlPullParser.TEXT
@SuppressLint("StaticFieldLeak")
object AndroidShare {
   var mcontext : Context?= null
    /**
     * 分享到QQ好友
     *
     * @param msgTitle (分享标题)
     * @param msgText  (分享内容)
     * @param type     (分享类型)
     * @param drawable (分享图片，若分享类型为AndroidShare.TEXT，则可以为null)
     */
    @Composable
    fun shareQQFriend(
        msgTitle: String, msgText: String, type: Int,
        drawable: Bitmap,
        context: Context
    ) {
        shareMsg(
            "com.tencent.mobileqq",
            "com.tencent.mobileqq.activity.JumpActivity", "QQ", msgTitle,
            msgText, type, drawable,
            context
        )
    }

    /**
     * 分享到微信好友
     *
     * @param msgTitle (分享标题)
     * @param msgText  (分享内容)
     * @param type     (分享类型)
     * @param drawable (分享图片，若分享类型为AndroidShare.TEXT，则可以为null)
     */
    fun shareWeChatFriend(
        msgTitle: String, msgText: String, type: Int,
        drawable: Bitmap?
    ) {
        shareMsg("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI", "微信",
            msgTitle, msgText, type, drawable,
            mcontext
        )
    }

    /**
     * 分享到微信朋友圈(分享朋友圈一定需要图片)
     *
     * @param msgTitle (分享标题)
     * @param msgText  (分享内容)
     * @param drawable (分享图片)
     */
    fun shareWeChatFriendCircle(
        msgTitle: String, msgText: String,
        drawable: Bitmap
    ) {
        shareMsg(
            "com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI",
            "微信", msgTitle, msgText, DRAWABLE, drawable,
            mcontext
        )
    }

    /**
     * 判断相对应的APP是否存在
     *
     * @param context
     * @param packageName
     * @return
     */
    fun isAvilible(context: Context, packageName: String?): Boolean {
        val packageManager = context.packageManager
        val pinfo = packageManager.getInstalledPackages(0)
        for (i in pinfo.indices) {
            if ((pinfo[i] as PackageInfo).packageName
                    .equals(packageName, ignoreCase = true)
            ) return true
        }
        return false
    }

    /**
     * 指定分享到qq
     *
     * @param context
     * @param bitmap
     */
    fun sharedQQ(context: Activity, bitmap: Bitmap?) {
        val uri = Uri.parse(
            insertImage(
                context.contentResolver,
                BitmapFactory.decodeResource(context.resources, R.mipmap.sym_def_app_icon),
                null,
                null
            )
        )
        val imageIntent = Intent(Intent.ACTION_SEND)
        imageIntent.setPackage("com.tencent.mobileqq")
        imageIntent.type = "image/*"
        imageIntent.putExtra(Intent.EXTRA_STREAM, uri)
        imageIntent.putExtra(Intent.EXTRA_TEXT, "您的好友邀请您进入天好圈")
        imageIntent.putExtra(Intent.EXTRA_TITLE, "天好圈")
        context.startActivity(imageIntent)
    }


    /**
     * 文本类型
     */
    var TEXT = 0

    /**
     * 图片类型
     */
    var DRAWABLE = 1

}

/**
 * 点击分享的代码
 *
 * @param packageName  (包名,跳转的应用的包名)
 * @param activityName (类名,跳转的页面名称)
 * @param appname      (应用名,跳转到的应用名称)
 * @param msgTitle     (标题)
 * @param msgText      (内容)
 * @param type         (发送类型：text or pic 微信朋友圈只支持pic)
 */
@SuppressLint("NewApi")
private fun shareMsg(
    packageName: String, activityName: String,
    appname: String, msgTitle: String, msgText: String, type: Int,
    drawable: Bitmap?,
    mcontext: Context?
) {
    if (!packageName.isEmpty() && !isAvilible(mcontext!!, packageName)) { // 判断APP是否存在
        Toast.makeText(mcontext, "请先安装$appname", Toast.LENGTH_SHORT)
            .show()
        return
    }
    val intent = Intent("android.intent.action.SEND")
    if (type == AndroidShare.TEXT) {
        intent.type = "text/plain"
    } else if (type == DRAWABLE) {
        intent.type = "image/*"
        //     BitmapDrawable bd = (BitmapDrawable) drawable;
//     Bitmap bt = bd.getBitmap();
        val uri = Uri.parse(
            insertImage(
                mcontext?.contentResolver, drawable, null, null
            )
        )
        intent.putExtra(Intent.EXTRA_STREAM, uri)
    }
    intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle)
    intent.putExtra(Intent.EXTRA_TEXT, msgText)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    if (!packageName.isEmpty()) {
        intent.component = ComponentName(packageName, activityName)
        mcontext?.startActivity(intent)
    } else {
        mcontext?.startActivity(Intent.createChooser(intent, msgTitle))
    }
}