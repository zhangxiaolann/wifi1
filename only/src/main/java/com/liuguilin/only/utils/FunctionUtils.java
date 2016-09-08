package com.liuguilin.only.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/*
 *  项目名：  Only
 *  包名：    com.liuguilin.only.utils
 *  文件名:   FunctionUtils
 *  创建者:   LGL
 *  创建时间:  2016/8/5 15:06
 *  描述：    特定功能的工具类
 *            - 打电话
 *            - 打开/关闭 应用
 *            - 增大/减小 音量
 *            - 高德地图导航
 *            - 判断网络连接
 *            - 判断wifi连接
 *            - 打开设置
 *            - 判断应用是否在前台
 *            - 播放歌曲
 *            - 判断运营商
 *            - 说话
 */
public class FunctionUtils {

    /**
     * 打开应用
     *
     * @param mContext      上下文
     * @param packName      包名
     * @param firstActivity Activity入口
     */
    public static void StartAPP(Context mContext, String packName, String firstActivity) {
        ComponentName componet = new ComponentName(packName, firstActivity);
        Intent i = new Intent();
        i.setComponent(componet);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }

    /**
     * 设置系统音量
     *
     * @param mContext 上下文
     * @param flag     0 降低  1 增加
     */
    public static void SetSystemVolume(Context mContext, int flag) {
        AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //最小音量
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        // 降低音量，调出系统音量控制
        if (flag == 0) {
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER,
                    AudioManager.FX_FOCUS_NAVIGATION_UP);
            // 增加音量，调出系统音量控制
        } else if (flag == 1) {
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE,
                    AudioManager.FX_FOCUS_NAVIGATION_UP);
        }

    }

    /**
     * 判断应用是否安装
     *
     * @param packageName
     * @return
     */
    public static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

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
        mContext.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
    }


    /**
     * 添加联系人
     *
     * @param mContext 上下文
     * @param name     电话
     * @param phone    号码
     */
    public static void NewContent(Context mContext, String name, String phone) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.dir/person");
        intent.setType("vnd.android.cursor.dir/contact");
        intent.setType("vnd.android.cursor.dir/raw_contact");
        // 添加姓名
        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        // 添加手机
        intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
        intent.putExtra(ContactsContract.Intents.Insert.IM_PROTOCOL, ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ);
        intent.putExtra(ContactsContract.Intents.Insert.IM_PROTOCOL, "444255655");
        mContext.startActivity(intent);
    }

    /**
     * 判断应用是否在前台运行
     * &&在前台为true 不在为false
     *
     * @param mContext 上下文
     * @return
     */
    public static boolean isRunningForeground(Context mContext) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName)
                && currentPackageName.equals(mContext.getPackageName())) {
            return true;
        }
        return false;
    }


    /**
     * 判断运营商
     *
     * @param mContext 上下文
     */
    public static void isChinaSIM(Context mContext) {
        TelephonyManager telManager = (TelephonyManager) mContext.getSystemService(Activity.TELEPHONY_SERVICE);
        String operator = telManager.getSimOperator();
        L.d("operator：" + operator);
        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002")) {
                L.d("中国移动");
            } else if (operator.equals("46001")) {
                L.d("中国联通");
            } else if (operator.equals("46003")) {
                L.d("中国电信");
            }
        }
    }


    /**
     * 获取当前版本号
     *
     * @param mContext
     * @return
     */
    public static String getAppVersionName(Context mContext) {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            String versionName = info.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "无法获取版本号";
        }
    }

    /**
     * 获取版本码
     *
     * @param mContext
     * @return
     */
    public static int getAppVersionCode(Context mContext) {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            int versionCode = info.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            //如果获取不到我就不更新
            return Integer.MAX_VALUE;
        }
    }

    /**
     * 设置字体
     *
     * @param mContext
     * @param textView
     */
    public static void setTextViewFont(Context mContext, TextView textView) {
        final Typeface fontFace = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/YYG.TTF");
        textView.setTypeface(fontFace);
    }

    /**
     * 判断服务是否运行
     *
     * @param mContext
     * @param serviceName 包名.类名
     * @return
     */
    public static boolean isServiceRunning(Context mContext, String serviceName) {
        // 活动管理器
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取运行的服务,参数表示最多返回的数量
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            String className = runningServiceInfo.service.getClassName();
            if (className.equals(serviceName)) {
                // 判断服务是否运行
                return true;
            }
        }
        return false;
    }

    /**
     * 跳转到系统的网络设置界面
     *
     * @param mContext
     */
    public static void toSettingNetWork(Context mContext) {
        Intent intent = null;
        // 先判断当前系统版本
        if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
        }
        mContext.startActivity(intent);
    }

}
