package wifi;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * wifi热点管理器
 */
public class WifiApManager {
	protected Context mContext;
	public WifiManager wifiManager;

	public WifiApManager(Context context) {
		if (context == null) {
			throw new RuntimeException("context should not be null");
		}
		this.mContext = context;
		wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
	}

	/**
	 * 热点类型
	 */
	public static class WifiApType {
		public static final int TYPE_NONE = 0;
		public static final int TYPE_WPA_PSK = 1;
		public static final int TYPE_WPA2_PSK = 2;
	}

	/**
	 * 创建wifi热点配置信息,详见系统Setting App的WifiApDialog.java源码,当wifiApType为
	 * {@link WifiApType#TYPE_NONE}时,ssid和psw不起作用。另外在SDK_INT<14的系统版本下，使用
	 * {@link WifiApType#TYPE_WPA2_PSK}无效
	 * 
	 * @param wifiApType
	 *            热点类型，{eg:{@link WifiApType#TYPE_NONE}
	 * @param ssid
	 * 
	 * @param password
	 *            热点密码 ，密码可以为8-63个ASCII码或8-64个十六进制的字符
	 * @return
	 */
	public WifiConfiguration createWifiApConfiguration(int wifiApType,
			String ssid, String password) {
		if (TextUtils.isEmpty(ssid)) {
			throw new RuntimeException("ssid should not be null");
		}
		WifiConfiguration config = new WifiConfiguration();
		config.SSID = ssid;
		switch (wifiApType) {
		case WifiApType.TYPE_NONE:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			break;
		case WifiApType.TYPE_WPA_PSK:
		case WifiApType.TYPE_WPA2_PSK:
			if (TextUtils.isEmpty(password) || password.length() < 8) {
				throw new RuntimeException("密码至少为8个字符");
			}
			config.preSharedKey = password;
			final int WPA2_PSK = 4;
			config.allowedKeyManagement
					.set(wifiApType == WifiApType.TYPE_WPA_PSK ? KeyMgmt.WPA_PSK
							: WPA2_PSK);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
			break;
		}
		return config;
	}

	/**
	 * 创建链接网络的wifi配置信息，详见详见系统Setting
	 * App的(低版本看WifiDialog.java，高版本看WifiConfigController.java)
	 * 
	 * @param ssid
	 *            热点名称
	 * @param password
	 *            密码可以为8-63个ASCII码或8-64个十六进制的字符
	 * @return
	 */
	public WifiConfiguration createWifiConnectConfiguration(int wifiApType,
			String ssid, String password) {
		if (TextUtils.isEmpty(ssid)) {
			throw new RuntimeException("ssid should not be null");
		}
		WifiConfiguration config = new WifiConfiguration();
		config.SSID = WifiUtils.convertToQuotedString(ssid);
		switch (wifiApType) {
		case WifiApType.TYPE_NONE:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			break;
		case WifiApType.TYPE_WPA_PSK:
		case WifiApType.TYPE_WPA2_PSK:
			if (TextUtils.isEmpty(password) || password.length() < 8) {
				throw new RuntimeException("密码至少为8个字符");
			}
			config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
			if (password.matches("[0-9A-Fa-f]{64}")) {
				config.preSharedKey = password;
			} else {
				config.preSharedKey = WifiUtils.convertToQuotedString(password);
			}
			break;
		}
		return config;
	}

	/**
	 * 设置wifi热点的激活状态,反射实现
	 * 
	 * @param wifiConfig
	 *            配置信息
	 * @param enabled
	 *            是否激活
	 */
	public boolean setWifiApEnabled(WifiConfiguration wifiConfig,
			boolean enabled) {
		try {
			Method setWifiApEnabled = WifiManager.class.getMethod(
					"setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
			setWifiApEnabled.setAccessible(true);
			return (Boolean) setWifiApEnabled.invoke(wifiManager, wifiConfig,
					enabled);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取wifi热点配置信息，反射实现
	 * 
	 * @return
	 */
	public WifiConfiguration getWifiApConfiguration() {
		try {
			Method getWifiApConfiguration = WifiManager.class
					.getMethod("getWifiApConfiguration");
			getWifiApConfiguration.setAccessible(true);
			WifiConfiguration wifiConfiguration = (WifiConfiguration) getWifiApConfiguration
					.invoke(wifiManager);
			return wifiConfiguration;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 打开wifi热点
	 * 
	 * @param wifiConfig
	 * @return
	 */
	public boolean openWifiAp(WifiConfiguration wifiConfig) {
		// 热点与wifi不能共存，开启热点需要关闭wifi
		wifiManager.setWifiEnabled(false);
		return setWifiApEnabled(wifiConfig, true);
	}

	/**
	 * 关闭wifi热点
	 * 
	 * @return
	 */
	public boolean closeWifiAp() {
		try {
			WifiConfiguration wifiConfiguration = getWifiApConfiguration();
			return setWifiApEnabled(wifiConfiguration, false);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}