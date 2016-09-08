package tcpip.listener;

/**
 * Caiyuan Huang
 * <p>
 * 2016-4-13
 * </p>
 * <p>
 * 数据接收监听器
 * </p>
 */
public interface OnReceiveListener {
	public void onReceive(byte[] message);
}
