package com.dear.self.fragment

import android.content.Intent
import android.view.View
import com.dear.self.applist.AppListActivity
import com.dear.self.databinding.FragmentIndexBinding
import com.dear.base.fragment.BaseViewBindingFragment

class IndexFragment : BaseViewBindingFragment<FragmentIndexBinding>() {

    override fun initView(view: View) {
        mBind?.tvList?.setOnClickListener {
            startActivity(Intent(context, AppListActivity::class.java))
        }
    }

}