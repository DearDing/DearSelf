package com.dear.self.applist

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.dear.base.activity.BaseDataBindingActivity
import com.dear.self.R
import com.dear.self.databinding.ActivityAppDetailBinding
import com.dear.http.viewmodel.BaseViewModel
import com.dear.share.ShareBottomDialog
import com.dear.share.ShareTextUtil
import com.dear.tools.ClipboardManagerUtil
import java.security.MessageDigest

class AppDetailActivity : BaseDataBindingActivity<BaseViewModel, ActivityAppDetailBinding>() {

    private var mPackageName = ""
    private var mAppData: AppInfoBean? = null
    private var mShareDialog: ShareBottomDialog? = null

    override fun initParams() {
        intent?.getStringExtra("package_name")?.let {
            mPackageName = it
        }
    }

    override fun initView() {
        mDB.tbToolbar.title = ""
        setSupportActionBar(mDB.tbToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ViewCompat.setNestedScrollingEnabled(mDB.scrollView, true)
    }

    override fun initListener() {
        mDB.fabShareBtn.setOnClickListener {
            showShareDialog()
        }
    }

    override fun initData() {
        if (mPackageName.isNotEmpty()) {
            mAppData = loadAppData()
            mDB.data = mAppData
            mDB.llContent.removeAllViews()
            mAppData?.let {
                if (it.version?.isNotEmpty() == true) {
                    addItem("VersionName", it.version)
                }
                if (it.code?.isNotEmpty() == true) {
                    addItem("VersionCode", it.code)
                }
                if (it.targetSDKVersion?.isNotEmpty() == true) {
                    addItem("TargetSdkVersion", it.targetSDKVersion)
                }
                addItem("包名", mPackageName)
                if (it.signMD5?.isNotEmpty() == true) {
                    addItem("MD5", it.signMD5)
                }
                if (it.signSHA1?.isNotEmpty() == true) {
                    addItem("SHA1", it.signSHA1)
                }
            }
        }
    }

    private fun showShareDialog() {
        if (null == mShareDialog) {
            mShareDialog = ShareBottomDialog(this)
            val text = getShareText()
            mShareDialog?.setCancelClickEvent {
                    true
                }?.setCopyClickEvent {
                    ClipboardManagerUtil.copyText(this, text, "复制成功")
                    true
                }?.setShareQQClickEvent {
                    ShareTextUtil.shareTextQQ(
                        this,
                        text
                    )
                    true
                }?.setShareWxClickEvent {
                    ShareTextUtil.shareTextWechat(
                        this,
                        text
                    )
                    true
                }?.setTitle("分享应用信息到")
        }
        mShareDialog?.show()
    }

    private fun getShareText(): String {
        val builder = StringBuilder()
        builder.append("应用名称：${mAppData?.name}\n")
        builder.append("版本号：${mAppData?.version}\n")
        builder.append("code：${mAppData?.code}\n")
        builder.append("包名：${mAppData?.packageName}\n")
        builder.append("签名：${mAppData?.signMD5}\n")
        builder.append("SHA1：${mAppData?.signSHA1}\n")
        return builder.toString()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadAppData(): AppInfoBean {
        val applicationInfo = packageManager.getApplicationInfo(mPackageName, 0)
        val appName = applicationInfo.loadLabel(packageManager).toString()
        val appIcon: Drawable = applicationInfo.loadIcon(packageManager)
        val targetSdkVersion = applicationInfo.targetSdkVersion.toString()
        val packageInfo = packageManager.getPackageInfo(mPackageName, 0)
        val versionName = packageInfo.versionName
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode
        }
        val signMD5Str = getMD5Sign(packageName) //签名
        val signSHA1Str = getSHA1Sign(packageName) //SHA1
        return AppInfoBean(
            appName, appIcon, versionName,
            versionCode.toString(), packageName, targetSdkVersion, signMD5Str, signSHA1Str
        )
    }

    private fun addItem(key: String, value: String) {
        val view = LayoutInflater.from(this).inflate(R.layout.item_app_detail_layout, null)
        view.findViewById<TextView>(R.id.tv_key).text = key
        view.findViewById<TextView>(R.id.tv_value).text = value
        mDB.llContent.addView(view)
    }

    /**
     * 获取签名
     *
     * @param packageName
     * @return
     */
    private fun getMD5Sign(packageName: String): String? {
        var md5SignStr: String? = ""
        try {
            val packageInfo =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            val arrayOfSignature = packageInfo.signatures
            md5SignStr = getMessageDigest(arrayOfSignature[0].toByteArray(), "MD5")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return md5SignStr
    }

    /**
     * 获取sha1
     *
     * @param packageName
     * @return
     */
    private fun getSHA1Sign(packageName: String): String? {
        var sha1SignStr: String? = ""
        try {
            val packageInfo =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            val arrayOfSignature = packageInfo.signatures
            sha1SignStr = getMessageDigest(arrayOfSignature[0].toByteArray(), "SHA1")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sha1SignStr
    }

    private fun getMessageDigest(paramArrayOfByte: ByteArray?, messageDigest: String?): String? {
        val arrayOfChar1 = charArrayOf(
            48.toChar(),
            49.toChar(),
            50.toChar(),
            51.toChar(),
            52.toChar(),
            53.toChar(),
            54.toChar(),
            55.toChar(),
            56.toChar(),
            57.toChar(),
            97.toChar(),
            98.toChar(),
            99.toChar(),
            100.toChar(),
            101.toChar(),
            102.toChar()
        )
        try {
            val localMessageDigest = MessageDigest.getInstance(messageDigest)
            localMessageDigest.update(paramArrayOfByte)
            val arrayOfByte = localMessageDigest.digest()
            val i = arrayOfByte.size
            val arrayOfChar2 = CharArray(i * 2)
            var j = 0
            var k = 0
            while (true) {
                if (j >= i) return String(arrayOfChar2)
                val m = arrayOfByte[j].toInt()
                val n = k + 1
                arrayOfChar2[k] = arrayOfChar1[0xF and (m ushr 4)]
                k = n + 1
                arrayOfChar2[n] = arrayOfChar1[m and 0xF]
                j++
            }
        } catch (localException: Exception) {
        }
        return ""
    }

}