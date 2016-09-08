package wifi;

import android.content.Context;

/**
 * WIFI库初始化
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
