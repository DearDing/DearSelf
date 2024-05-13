package com.dear.self.applist

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dear.self.databinding.ActivityAppListBinding
import com.dear.res.ResMipmap
import com.dear.base.activity.BaseViewBindingActivity
import com.dear.base.exts.startActivity
import com.dear.base.titlebar.TitleCommonBar
import com.dear.tools.PinyinUtil
import com.dear.tools.toast.ToastUtil
import kotlinx.coroutines.*
import java.util.*

/**
 * app列表
 */
class AppListActivity : BaseViewBindingActivity<ActivityAppListBinding>() {

    private var mAdapter: AppListAdapter? = null
    private val mAllList: ArrayList<AppBean> = arrayListOf()
    private val mUserList: ArrayList<AppBean> = arrayListOf()
    private val mSysList: ArrayList<AppBean> = arrayListOf()

    private var mIsList: Boolean = true

    private fun findRvView(): RecyclerView {
        return mDB.rvView
    }

    private fun findTitleBar(): TitleCommonBar {
        return mDB.viewTitleBar
    }

    override fun initTitleBar() {
        findTitleBar().setTitle("应用列表")
            .setRightImage(ResMipmap.icon_title_grid)
            .setOnRightClickListener {
                changeGridLayoutManager()
            }
            .setOnBackClickListener {
                finish()
            }
    }

    override fun initView() {
        findRvView().layoutManager = LinearLayoutManager(this)
        mAdapter = AppListAdapter()
        findRvView().adapter = mAdapter
    }

    override fun initListener() {
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            val packageName = adapter.getItem(position)?.packageName
            if (packageName?.isNotEmpty() == true) {
                startActivity<AppDetailActivity>(Pair("package_name", packageName))
            }
        }
    }

    override fun initData() {
        showLoading()
        requestAppList()
    }

    /**
     * 切换为网格显示
     */
    private fun changeGridLayoutManager() {
        mIsList = !mIsList
        if (mIsList) {
            findTitleBar().setRightImage(ResMipmap.icon_title_grid)
            findRvView().layoutManager = LinearLayoutManager(this)
        } else {
            findTitleBar().setRightImage(ResMipmap.icon_title_list)
            findRvView().layoutManager = GridLayoutManager(this, 2)
        }
    }

    private fun requestAppList() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            requestFail(throwable)
        }
        val coroutineScope =
            CoroutineScope(Dispatchers.IO + exceptionHandler + CoroutineName("app_list"))
        coroutineScope.launch {
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val list = async(Dispatchers.IO) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    packageManager?.queryIntentActivities(
                        intent, PackageManager.ResolveInfoFlags.of(
                            PackageManager.MATCH_ALL.toLong()
                        )
                    )
                } else {
                    packageManager?.queryIntentActivities(intent, PackageManager.MATCH_ALL)
                }
            }
            resolveDataList(list.await())
        }
    }

    private fun clearList() {
        mUserList.clear()
        mSysList.clear()
        mAllList.clear()
    }

    private fun resolveDataList(list: List<ResolveInfo>?) {
        clearList()
        var appName = ""
        var packageName = ""
        var packageInfo: PackageInfo? = null
        var drawable: Drawable
        list?.forEach {
            appName = it.loadLabel(packageManager).toString()
            drawable = it.loadIcon(packageManager)
            packageName = it.activityInfo.packageName
            packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager?.getPackageInfo(
                    packageName,
                    PackageManager.PackageInfoFlags.of(PackageManager.MATCH_ALL.toLong())
                )
            } else {
                packageManager?.getPackageInfo(
                    packageName,
                    0
                )
            }
            val infoBean = if (((packageInfo?.applicationInfo?.flags
                    ?: 0) and ApplicationInfo.FLAG_SYSTEM) <= 0
            ) {
                //用户应用
                AppBean.create(appName, packageName, drawable, 1)
            } else {
                //系统应用
                AppBean.create(appName, packageName, drawable, 0)
            }
            mAllList.add(infoBean)
        }
        sortList(mAllList)
        mAllList.forEach {
            if (it.type == 1) {
                mUserList.add(it)
            } else {
                mSysList.add(it)
            }
        }
        MainScope().launch {
            requestSuccess()
        }
    }

    /**
     * list排序
     */
    private fun sortList(list: List<AppBean>) {
        val comparator = Comparator<AppBean> { o1, o2 ->
            val name1 = PinyinUtil.getPingYin(o1.appName) ?: ""
            val name2 = PinyinUtil.getPingYin(o2.appName) ?: ""
            name1.compareTo(name2)
        }
        Collections.sort(list, comparator)
    }

    private fun requestSuccess() {
        dismissLoading()
        mAdapter?.submitList(mAllList)
    }

    private fun requestFail(throwable: Throwable) {
        dismissLoading()
        ToastUtil.show("加载失败")
    }
}