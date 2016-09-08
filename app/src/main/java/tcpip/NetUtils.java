package tcpip;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Caiyuan Huang
 * <p>
 * 2016-4-14
 * </p>
 * <p>
 * 网络工具类
 * </p>
 */
public class NetUtils {
	/**
	 * 获取使用wifi时的ip地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getIpByWifi(Context context) {
		if (context == null) {
			return "";
		}
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (wifiInfo == null) {
			return "";
		}
		int ipAddress = wifiInfo.getIpAddress();
		return intToIp(ipAddress);
	}

	public static String intToIp(int ip) {
		return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "."
				+ ((ip >> 16) & 0xFF) + "." + (ip >> 24 & 0xFF);
	}
}
