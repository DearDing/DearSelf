package com.dear.share;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dear.tools.AppUtil;
import com.dear.tools.toast.ToastUtil;

public class ShareTextUtil {

    /**
     * 分享纯文本到qq好友
     *
     * @param mContext
     * @param content
     */
    public static void shareTextQQ(Context mContext, String content) {
        if (TextUtils.isEmpty(content)) {
            ToastUtil.INSTANCE.show("分享信息为空");
            return;
        }
        if (AppUtil.INSTANCE.isAppAvilible(mContext, ShareConstants.PACKAGE_NAME_QQ)) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
            intent.putExtra(Intent.EXTRA_TEXT, content);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(new ComponentName(ShareConstants.PACKAGE_NAME_QQ, "com.tencent.mobileqq.activity.JumpActivity"));
            mContext.startActivity(intent);
        } else {
            ToastUtil.INSTANCE.show("未安装QQ");
        }
    }

    /**
     * 分享纯文本到微信好友
     *
     * @param mContext
     * @param content
     */
    public static void shareTextWechat(Context mContext, String content) {
        if (TextUtils.isEmpty(content)) {
            ToastUtil.INSTANCE.show("分享信息为空");
            return;
        }
        if (AppUtil.INSTANCE.isAppAvilible(mContext, ShareConstants.PACKAGE_NAME_WECHAT)) {
            Intent intent = new Intent();
            ComponentName cop = new ComponentName(ShareConstants.PACKAGE_NAME_WECHAT, "com.tencent.mm.ui.tools.ShareImgUI");
            intent.setComponent(cop);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra("android.intent.extra.TEXT", content);
//            intent.putExtra("sms_body", content);
            intent.putExtra("Kdescription", content);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } else {
            ToastUtil.INSTANCE.show("未安装微信");
        }
    }
}

