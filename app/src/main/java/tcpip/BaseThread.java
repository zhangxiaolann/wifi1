package tcpip;

/**
 * Caiyuan Huang
 * <p>
 * 2016-4-14
 * </p>
 * <p>
 * 线程基类
 * </p>
 */
public class BaseThread extends Thread {
	protected boolean isExit = false;

	/**
	 * 退出线程
	 */
	public void exit() {
		isExit = true;
		try {
			this.interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void start() {
		isExit = false;
		super.start();
	}
}
