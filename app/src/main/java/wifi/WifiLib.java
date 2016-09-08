package wifi;

import android.content.Context;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.util.List;



/**
 * Caiyuan Huang
 * <p>
 * 2016-3-22
 * </p>
 * <p>
 * wifi库,对外接口封装
 * </p>
 */
public class WifiLib {
	private WifiBroadcastReceiver wifiBroadcastReceiver;
	private WifiApScanner wifiApScanner;
	private WifiApManager wifiApManager;
	private WifiManager wifiManager;
	private WifiBroadcastReceiver.OnWifiBroadcastReceiveCallback wifiCbk;
	private static WifiLib mInstance = null;

	public static WifiLib getInstance() {
		if (mInstance == null) {
			synchronized (WifiLib.class) {
				if (mInstance == null)
					mInstance = new WifiLib(WifiLibInitializer.getAppContext());
			}
		}
		return mInstance;
	}

	private WifiLib(Context context) {
		if (context == null) {
			throw new RuntimeException("context should not be null");
		}
		wifiBroadcastReceiver = new WifiBroadcastReceiver(context,
				innerWifiBroadcastReceiveCallback);
		wifiApScanner = new WifiApScanner(context);
		wifiApManager = new WifiApManager(context);
		wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		wifiBroadcastReceiver.register();
	}

	private WifiBroadcastReceiver.OnWifiBroadcastReceiveCallback innerWifiBroadcastReceiveCallback = new WifiBroadcastReceiver.OnWifiBroadcastReceiveCallback() {

		@Override
		public void onWifiStateChanged(int wifiState) {
			if (wifiCbk != null) {
				wifiCbk.onWifiStateChanged(wifiState);
			}
		}

		@Override
		public void onNetWorkIdsChanged() {
			if (wifiCbk != null) {
				wifiCbk.onNetWorkIdsChanged();
			}
		}

		@Override
		public void onSupplicantStateChanged(DetailedState state) {
			if (wifiCbk != null) {
				wifiCbk.onSupplicantStateChanged(state);
			}
		}

		@Override
		public void onNetworkStateChanged(DetailedState state) {
			if (wifiCbk != null) {
				wifiCbk.onNetworkStateChanged(state);
			}
		}

		@Override
		public void onRssiChanged() {
			if (wifiCbk != null) {
				wifiCbk.onRssiChanged();
			}
		}

		@Override
		public void onScanFailed() {
			if (wifiCbk != null) {
				wifiCbk.onScanFailed();
			}
		}

		@Override
		public void onScanResultsAvailable(List<AccessPoint> accessPoints) {
			if (wifiCbk != null) {
				wifiCbk.onScanResultsAvailable(accessPoints);
			}
		}

	};

	/**
	 * 设置wifi广播回调
	 * 
	 * @param cbk
	 */
	public void setOnWifiBroadcastReceiveCallback(
			WifiBroadcastReceiver.OnWifiBroadcastReceiveCallback cbk) {
		this.wifiCbk = cbk;
	}

	/**
	 * 创建wifi热点
	 * 
	 * @param apType
	 *            热点类型
	 *            <ul>
	 *            <li>{@link WifiApManager.WifiApType#TYPE_NONE},无密码
	 *            <li>{@link WifiApManager.WifiApType#TYPE_WPA_PSK},WPA_PSK
	 *            <li>{@link WifiApManager.WifiApType#TYPE_WPA2_PSK}
	 *            ,TYPE_WPA2_PSK
	 *            </ul>
	 * @param ssid
	 *            网络标识符
	 * @param password
	 *            热点密码 ，密码可以为8-63个ASCII码或8-64个十六进制的字符
	 * @return true表示创建成功，false表示创建失败
	 */
	public boolean createAccessPoint(int apType, String ssid, String password) {
		WifiConfiguration config = wifiApManager.createWifiApConfiguration(
				apType, ssid, password);
		return wifiApManager.openWifiAp(config);
	}

	/**
	 * 连接到指定热点
	 * 
	 * @param accessPoint
	 *            热点信息
	 * @param password
	 *            密码,热点安全类型为WPA_PSK或者WPA2_PSK,密码必填
	 * @return
	 */
	public boolean connectToAccessPoint(AccessPoint accessPoint, String password) {
		if (accessPoint == null) {
			return false;
		}
		int apType = WifiApManager.WifiApType.TYPE_NONE;
		switch (accessPoint.getSecurity()) {
		case AccessPoint.SECURITY_NONE:
			break;
		case AccessPoint.SECURITY_WPA_PSK:
			apType = WifiApManager.WifiApType.TYPE_WPA_PSK;
			if (TextUtils.isEmpty(password)) {
				throw new IllegalArgumentException(
						"when accessPoint security is WPA_PSK,the password should not be null");
			}
			break;
		case AccessPoint.SECURITY_WPA2_PSK:
			apType = WifiApManager.WifiApType.TYPE_WPA2_PSK;
			if (TextUtils.isEmpty(password)) {
				throw new IllegalArgumentException(
						"when accessPoint security is WPA2_PSK,the password should not be null");
			}
			break;
		}
		// 如果已经保存的wifi，里面有networkId，可以直接连接
		int networkId = accessPoint.getNetworkId();
		if (networkId == -1) {
			WifiConfiguration config = wifiApManager
					.createWifiConnectConfiguration(apType,
							accessPoint.getSsid(), password);
			networkId = wifiApManager.wifiManager.addNetwork(config);
		}
		return wifiManager.enableNetwork(networkId, true);

	}

	/**
	 * 关闭wifi热点功能
	 * 
	 * @return
	 */
	public boolean closeWifiAp() {
		return wifiApManager.closeWifiAp();
	}

	/**
	 * 开启wifi
	 * 
	 * @return
	 */
	public boolean openWifi() {
		if (!wifiManager.isWifiEnabled()) {

			return wifiManager.setWifiEnabled(true);
		}
		return true;
	}

	/**
	 * 关闭wifi
	 * 
	 * @return
	 */
	public boolean closeWifi() {
		if (wifiManager.isWifiEnabled()) {
			return wifiManager.setWifiEnabled(false);
		}
		return true;
	}

	/**
	 * 开始扫描热点 </br> 若有结果则会在
	 *
	 * 进行回调。</br> 若失败则会在{@link WifiBroadcastReceiver.OnWifiBroadcastReceiveCallback#onScanFailed()}
	 * 方法中进行回调
	 * 
	 * @param periodMillis
	 *            扫描的周期时间，单位为毫秒
	 * 
	 */
	public void startScan(long periodMillis) {
		wifiApScanner.pause();
		wifiApScanner.setScanPeriod(periodMillis);
		wifiApScanner.resume();
	}

	/**
	 * 停止扫描热点
	 */
	public void stopScan() {
		wifiApScanner.pause();
	}

	/**
	 * 获取sdk中的wifi管理器
	 * 
	 * @return
	 */
	public WifiManager getNativeWifiManager() {
		return wifiManager;
	}

	public void realse() {
		wifiApScanner.removeCallbacksAndMessages(null);
		wifiBroadcastReceiver.unRegister();
		mInstance = null;
	}
}
