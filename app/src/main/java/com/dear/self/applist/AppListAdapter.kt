package com.dear.self.applist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dear.self.R
import com.chad.library.adapter4.BaseQuickAdapter

class AppListAdapter : BaseQuickAdapter<AppBean, RecyclerView.ViewHolder>() {

    companion object {
        const val USER_APP = 0x1000
        const val SYS_APP = 0x1001
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tv_app_name)
        var tvPackage: TextView = itemView.findViewById(R.id.tv_app_package)
        var ivIcon: ImageView = itemView.findViewById(R.id.iv_app_icon)
    }

    inner class SysViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tv_app_name)
        var tvPackage: TextView = itemView.findViewById(R.id.tv_app_package)
        var tvType: TextView = itemView.findViewById(R.id.tv_app_type)
        var ivIcon: ImageView = itemView.findViewById(R.id.iv_app_icon)
    }

    override fun getItemViewType(position: Int, list: List<AppBean>): Int {
        return list[position].type ?: USER_APP
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        item: AppBean?
    ) {
        item?.let {
            if (holder is UserViewHolder) {
                holder.tvName.text = it.appName
                holder.tvPackage.text = it.packageName
                holder.ivIcon.setImageDrawable(it.appIcon)
            }else if (holder is SysViewHolder) {
                holder.tvName.text = it.appName
                holder.tvPackage.text = it.packageName
                holder.ivIcon.setImageDrawable(it.appIcon)
            }
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == SYS_APP) {
            SysViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_app_sys_layout, parent, false)
            )
        } else {
            UserViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_app_user_layout, parent, false)
            )
        }
    }

}