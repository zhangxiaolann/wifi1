package wifi;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

/**
 * Caiyuan Huang
 * <p>
 * 2016-3-22
 * </p>
 * <p>
 * Wifi热点扫描器
 * </p>
 */
public class WifiApScanner extends Handler {
	private Context mContext;
	private WifiManager wifiManager;
	private final int MSG_SCAN_WIFI_AP = 0;
	private long scanPeriodMillis = 6000;
	private int retryTimes = 0;

	public WifiApScanner(Context context) {
		if (context == null) {
			throw new RuntimeException("context should not be null");
		}
		this.mContext = context;
		wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
	}

	/**
	 * 恢复扫描
	 */
	public void resume() {
		if (!hasMessages(MSG_SCAN_WIFI_AP)) {
			sendEmptyMessage(MSG_SCAN_WIFI_AP);
		}
	}

	/**
	 * 暂停扫描
	 */
	public void pause() {
		retryTimes = 0;
		removeMessages(MSG_SCAN_WIFI_AP);
	}

	@Override
	public void handleMessage(Message message) {
		if (wifiManager.startScan()) {
			retryTimes = 0;
		} else if (++retryTimes >= 3) {
			retryTimes = 0;
			// 重试三次失败
			mContext.sendBroadcast(new Intent(
					WifiBroadcastReceiver.WIFI_SCAN_FAILED_ACTION));
			return;
		}
		sendEmptyMessageDelayed(MSG_SCAN_WIFI_AP, scanPeriodMillis);
	}

	/**
	 * 设置扫描周期间隔时间
	 * 
	 * @param millis
	 *            单位毫秒
	 */
	public void setScanPeriod(long millis) {
		this.scanPeriodMillis = millis;
	}
}
