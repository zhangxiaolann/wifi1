package tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * �����ƣ�AppInfoProvider
 * ����������ȡӦ�ó���������Ϣ
 * �����ˣ�LXH
 */
public class AppInfoProvider {
    private static PackageManager packageManager;

    //private static PackageStats packageStats;
    //��ȡһ����������
    public AppInfoProvider(Context context) {
        packageManager = context.getPackageManager();
    }

    /**
     * ��ȡϵͳ������Ӧ����Ϣ��
     * ����Ӧ�������Ϣ���浽list�б��С�
     **/
    public static List<AppInfo> getAllApps() {
        List<AppInfo> list = new ArrayList<AppInfo>();
        AppInfo myAppInfo;
        //��ȡ�����а�װ�˵�Ӧ�ó������Ϣ��������Щж���˵ģ���û��������ݵ�Ӧ�ó���
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo info : packageInfos) {
            myAppInfo = new AppInfo();
            //�õ�����
            String packageName = info.packageName;
            String packagePath = info.applicationInfo.sourceDir;
            //�õ�Ӧ�ó������Ϣ
            ApplicationInfo appInfo = info.applicationInfo;
            if (filterApp(appInfo) == false) {
                continue;
            }
            //�õ�Ӧ�ó����ͼ��
            Drawable icon = appInfo.loadIcon(packageManager);
            //�õ�Ӧ�ó���Ĵ�С
            // long codesize =;
            //long codesize = packageStats.codeSize;
            //�õ�Ӧ�ó���ĳ�����
            String appName = appInfo.loadLabel(packageManager).toString();
            myAppInfo.setPackageName(packageName);
            myAppInfo.setPackagePath(packagePath);
            myAppInfo.setAppName(appName);
            myAppInfo.setIcon(icon);
            //myAppInfo.setCodesize(codesize);
            if (filterApp(appInfo)) {
                myAppInfo.setSystemApp(false);
            } else {
                myAppInfo.setSystemApp(true);
            }
            list.add(myAppInfo);
        }
        return list;
    }

    /**
     * �ж�ĳһ��Ӧ�ó����ǲ���ϵͳ��Ӧ�ó���
     * ����Ƿ���true�����򷵻�false��
     */
    public static boolean filterApp(ApplicationInfo info) {
        //��ЩϵͳӦ���ǿ��Ը��µģ�����û��Լ�������һ��ϵͳ��Ӧ����������ԭ���ģ�������ϵͳӦ�ã���������ж����������
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {//�ж��ǲ���ϵͳӦ��
            return true;
        }
        return false;
    }


}
