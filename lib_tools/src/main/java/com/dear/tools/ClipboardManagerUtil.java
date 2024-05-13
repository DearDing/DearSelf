package com.dear.tools;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import com.dear.tools.toast.ToastUtil;
import com.dear.tools.toast.ToastUtil;

public class ClipboardManagerUtil {

    public static void copyText(Context context,String text){
        copyText(context,text,null);
    }

    public static void copyText(Context context,String text,String toast){
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("text", text);
        cm.setPrimaryClip(mClipData);
        if(!TextUtils.isEmpty(toast)){
            ToastUtil.INSTANCE.show(toast);
        }
    }
}
