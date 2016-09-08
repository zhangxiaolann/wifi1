package com.liuguilin.only.utils;

/*
 *  项目名：  Only 
 *  包名：    com.liuguilin.only.utils
 *  文件名:   NetWorkUtils
 *  创建者:   LGL
 *  创建时间:  2016/7/6 18:20
 *  描述：    网络工具类
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtils {

    /**
     * 判断网络是否连接
     *
     * @param context 上下文
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 打开系统界面
     */
    public static void openSystemSetting(Context mContext) {
        Intent i = new Intent(android.provider.Settings.ACTION_SETTINGS);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }

}
