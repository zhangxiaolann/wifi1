package wifi;

import android.content.Context;

/**
 * Caiyuan Huang
 * <p>
 * 2016-3-23
 * </p>
 * <p>
 * WIFI库初始化
 * </p>
 */
public class WifiLibInitializer {
	private static Context sContext;

	public static void init(Context context) {
		sContext = context;
	}

	synchronized public static Context getAppContext() {
		if (sContext == null)
			throw new RuntimeException("请调用init方法在Application里面进行初始化");
		return sContext;
	}
}
