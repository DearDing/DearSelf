package com.dear.self

import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.chaychan.library.BottomBarItem
import com.chaychan.library.BottomBarLayout

class MainActivity : com.dear.base.activity.BaseActivity(), BottomBarLayout.OnItemSelectedListener {

    private lateinit var mBottomNavLayout: BottomBarLayout
    private val mController: NavController by lazy {
        findController()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        mBottomNavLayout = findViewById(R.id.bottom_bar_layout)
    }

    override fun initListener() {
        mBottomNavLayout.setOnItemSelectedListener(this)
    }

    private fun findController(): NavController {
        return findNavController(R.id.nav_host_fragment)
    }

    override fun onItemSelected(
        bottomBarItem: BottomBarItem?,
        previousPosition: Int,
        currentPosition: Int
    ) {
        when (currentPosition) {
            0 -> {
                mController.navigate(R.id.nav_home)
                throw NullPointerException("空了")
            }
            1 -> {
                mController.navigate(R.id.nav_gallery)
            }
        }
    }
}
