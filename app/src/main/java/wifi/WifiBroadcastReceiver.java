package wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Caiyuan Huang
 * <p>
 * 2016-3-22
 * </p>
 * <p>
 * WIFI广播接收器
 * </p>
 */
public class WifiBroadcastReceiver extends BroadcastReceiver {
	private Context mContext;
	private OnWifiBroadcastReceiveCallback onWifiBroadcastReceiverCallback;
	private WifiManager wifiManager;
	/**
	 * wifi扫描失败广播
	 */
	public static final String WIFI_SCAN_FAILED_ACTION = "android.net.wifi.WIFI_SCAN_FAILED_ACTION";

	/**
	 * 创建wifi意图过滤器
	 * 
	 * @return
	 */
	public IntentFilter createWifiIntentFilter() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		intentFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		intentFilter.addAction(WIFI_SCAN_FAILED_ACTION);
		return intentFilter;
	}

	public WifiBroadcastReceiver(Context context,
			OnWifiBroadcastReceiveCallback callback) {
		if (context == null) {
			throw new RuntimeException("context should not be null");
		}
		this.mContext = context;
		this.onWifiBroadcastReceiverCallback = callback;
		wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
	}

	/**
	 * 注册
	 */
	public void register() {
		mContext.registerReceiver(this, createWifiIntentFilter());
	}

	/**
	 * 反注册
	 */
	public void unRegister() {
		mContext.unregisterReceiver(this);
	}

	/**
	 * wifi广播接收器回调
	 */
	public static abstract class OnWifiBroadcastReceiveCallback {
		/**
		 * wifi状态改变</br>
		 * <ul>
		 * <li>{@link WifiManager#WIFI_STATE_DISABLING},WIFI网卡正在关闭</li>
		 * <li>{@link WifiManager#WIFI_STATE_DISABLED},WIFI网卡不可用</li>
		 * <li>{@link WifiManager#WIFI_STATE_ENABLING},WIFI网卡正在开启</li>
		 * <li>{@link WifiManager#WIFI_STATE_ENABLED},WIFI网卡可用</li>
		 * <li>{@link WifiManager#WIFI_STATE_UNKNOWN},WIFI网卡状态未知</li>
		 * </ul>
		 * 
		 * @param wifiState
		 */
		public void onWifiStateChanged(int wifiState) {
		};

		/**
		 * 网络id改变
		 */
		public void onNetWorkIdsChanged() {
		};

		/**
		 * 客户端状态改变
		 * 
		 * @param state
		 */
		public void onSupplicantStateChanged(DetailedState state) {
		};

		/**
		 * 网络状态改变
		 * 
		 * @param state
		 */
		public void onNetworkStateChanged(DetailedState state) {
		};

		/**
		 * wifi信号强度改变
		 */
		public void onRssiChanged() {
		};

		/**
		 * 扫描热点失败
		 */
		public void onScanFailed() {
		};

		/**
		 * 获取到扫描结果
		 * 
		 * @param accessPoints
		 *            返回扫描到的热点信息
		 */
		public void onScanResultsAvailable(List<AccessPoint> accessPoints) {
		};

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (onWifiBroadcastReceiverCallback == null) {
			return;
		}
		String action = intent.getAction();
		if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
			int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
					WifiManager.WIFI_STATE_UNKNOWN);
			onWifiBroadcastReceiverCallback.onWifiStateChanged(wifiState);
		} else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
			onWifiBroadcastReceiverCallback
					.onScanResultsAvailable(getAccessPoint());
		} else if (WifiManager.NETWORK_IDS_CHANGED_ACTION.equals(action)) {
			onWifiBroadcastReceiverCallback.onNetWorkIdsChanged();
		} else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
			onWifiBroadcastReceiverCallback.onSupplicantStateChanged(WifiInfo
					.getDetailedStateOf((SupplicantState) intent
							.getParcelableExtra(WifiManager.EXTRA_NEW_STATE)));
		} else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
			onWifiBroadcastReceiverCallback
					.onNetworkStateChanged(((NetworkInfo) intent
							.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO))
							.getDetailedState());
		} else if (WifiManager.RSSI_CHANGED_ACTION.equals(action)) {
			onWifiBroadcastReceiverCallback.onRssiChanged();
		}
	}

	/**
	 * 更新热点信息
	 */
	private List<AccessPoint> getAccessPoint() {
		// 获取已经保存过的配置信息
		List<AccessPoint> accessPoints = new ArrayList<AccessPoint>();
		List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
		if (configs != null && configs.size() != 0) {
			for (WifiConfiguration config : configs) {
				AccessPoint accessPoint = new AccessPoint(config);
				accessPoints.add(accessPoint);
			}
		}
		// 获取扫描结果
		List<ScanResult> results = wifiManager.getScanResults();
		if (results != null && results.size() != 0) {
			for (ScanResult result : results) {
				// Ignore hidden and ad-hoc networks.
				if (result.SSID == null || result.SSID.length() == 0
						|| result.capabilities.contains("[IBSS]")) {
					continue;
				}

				boolean found = false;
				for (AccessPoint accessPoint : accessPoints) {
					if (accessPoint.equals(result)) {
						// 尝试更新信号强度
						accessPoint.update(result);
						found = true;
					}
				}
				if (!found) {
					accessPoints.add(new AccessPoint(result));
				}
			}
		}
		return accessPoints;
	}

}
