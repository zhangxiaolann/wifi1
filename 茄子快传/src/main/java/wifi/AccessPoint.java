package wifi;

import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiManager;

/**
 * 热点实体类
 */
public class AccessPoint {
	private String ssid;// 标识id
	private int security;// 安全类型
	private int networkId;// 网络id
	private int rssi;// 信号强度
	private WifiConfiguration config;
	public static final int SECURITY_NONE = 0;// 无密码
	public static final int SECURITY_WPA_PSK = 1;// WPA_PSK
	public static final int SECURITY_WPA2_PSK = 2;// WPA2_PSK
	public static final int STATUS_NOT_IN_RANGE = 0;// 不在范围内
	public static final int STATUS_SAVED = 1;// 已保存
	public static final int STATUS_CONNECTED = 2;// 已连接
	public static final int STATUS_DISABLED = 3;// 已停用
	public static final int STATUS_OTHER = 4;// 其它状态
	private DetailedState detailedState;

	public AccessPoint(String ssid, int security) {
		super();
		this.ssid = ssid;
		this.security = security;
	}

	public AccessPoint(WifiConfiguration config) {
		if (config == null) {
			throw new RuntimeException("config should not be null");
		}
		// 配置的网络信息里面,ssid加了双引号需要去除
		ssid = (config.SSID == null ? "" : WifiUtils
				.removeDoubleQuotes(config.SSID));
		security = getSecurity(config);
		networkId = config.networkId;
		rssi = Integer.MAX_VALUE;
		this.config = config;
	}

	public AccessPoint(ScanResult result) {
		if (result == null) {
			throw new RuntimeException("result should not be null");
		}
		ssid = result.SSID == null ? "" : result.SSID;
		security = getSecurity(result);
		networkId = -1;
		rssi = result.level;

	}

	/**
	 * 该热点是否和扫描结果相等,即ssid和安全类型是否一致
	 * 
	 * @param result
	 * @return true相等，false不相等
	 */
	public boolean equals(ScanResult result) {
		if (result == null) {
			return false;
		}
		return ssid.equals(result.SSID) && security == getSecurity(result);
	}

	/**
	 * 更新热点信息
	 * 
	 * @param result
	 */
	public void update(ScanResult result) {
		// 如果相同的热点，当信号强度变强时更新信息强度
		if (equals(result)) {
			if (WifiManager.compareSignalLevel(result.level, rssi) > 0) {
				rssi = result.level;
			}
		}
	}

	/**
	 * 从配置的热点信息中获取热点网络类型
	 * 
	 * @param config
	 * @return
	 */
	private int getSecurity(WifiConfiguration config) {
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
			return SECURITY_WPA_PSK;
		}
		final int WPA2_PSK = 4;
		if (config.allowedKeyManagement.get(WPA2_PSK)) {
			return SECURITY_WPA2_PSK;
		}
		return SECURITY_NONE;
	}

	/**
	 * 从扫描的热点信息中获取热点网络类型
	 * 
	 * @param result
	 * @return
	 */
	private int getSecurity(ScanResult result) {
		if (result.capabilities.contains("PSK")) {
			return SECURITY_WPA2_PSK;
		}
		return SECURITY_NONE;
	}

	/**
	 * 更新状态
	 * 
	 * @param detailedState
	 */
	public void updateState(DetailedState detailedState) {
		this.detailedState = detailedState;
	}

	/**
	 * 获取状态
	 * <ul>
	 * <li>{@link #STATUS_CONNECTED},已连接，需要调用{@link #updateState()}
	 * 方法进行更新才能获取此状态</li>
	 * <li>{@link #STATUS_DISABLED},已停用</li>
	 * <li>{@link #STATUS_NOT_IN_RANGE},不在范围内</li>
	 * <li>{@link #STATUS_SAVED},已保存</li>
	 * <li>{@link #STATUS_OTHER},其它状态</li>
	 * </ul>
	 * 
	 * @return
	 */
	public int getStatus() {
		if (detailedState != null && DetailedState.CONNECTED == detailedState) {
			return STATUS_CONNECTED;
		}
		if (rssi == Integer.MAX_VALUE) {
			return STATUS_NOT_IN_RANGE;
		} else if (config != null) {
			return config.status == WifiConfiguration.Status.DISABLED ? STATUS_DISABLED
					: STATUS_SAVED;
		}
		return STATUS_OTHER;

	}

	/**
	 * 获取ssid
	 * 
	 * @return
	 */
	public String getSsid() {
		return ssid;
	}

	/**
	 * 获取安全类型
	 * <ul>
	 * <li>{@link #SECURITY_NONE},开放，无密码</li>
	 * <li>{@link #SECURITY_WPA_PSK}, WPA_PSK
	 * <li>{@link #SECURITY_WPA2_PSK},WPA2_PSK
	 * </ul>
	 * 
	 * @return
	 */
	public int getSecurity() {
		return security;
	}

	/**
	 * 获取网络id
	 * 
	 * @return
	 */
	public int getNetworkId() {
		return networkId;
	}

	/**
	 * 获取信号强度
	 * 
	 * @return
	 */
	public int getRssi() {
		return rssi;
	}

	/**
	 * 获取热点详细的状态
	 * 
	 * @return
	 */
	public DetailedState getDetailedState() {
		return detailedState;
	}

}
