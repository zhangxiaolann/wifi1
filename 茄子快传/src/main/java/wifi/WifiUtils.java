package wifi;

import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

/**
 * wifi相关的工具类
 */
public class WifiUtils {
	/**
	 * 移除双引号
	 * 
	 * @param string
	 * @return
	 */
	public static String removeDoubleQuotes(String string) {
		int length = string.length();
		if ((length > 1) && (string.charAt(0) == '"')
				&& (string.charAt(length - 1) == '"')) {
			return string.substring(1, length - 1);
		}
		return string;
	}

	/**
	 * 添加双引号
	 * 
	 * @param string
	 * @return
	 */
	public static String convertToQuotedString(String string) {
		return "\"" + string + "\"";
	}

	public static void printAccessPoints(List<AccessPoint> accessPoints) {
		for (int i = 0; i < accessPoints.size(); i++) {
			AccessPoint ap=accessPoints.get(i);
			Log.i("HCY", String.format("ssid=%s,sec=%d,rssi=%s", ap
					.getSsid(), ap.getSecurity(),WifiManager.calculateSignalLevel(ap.getRssi(), 4)+""));
		}
	}
}
