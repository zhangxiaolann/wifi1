package tcpip.listener;

/**
 * Caiyuan Huang
 * <p>
 * 2016-4-15
 * </p>
 * <p>
 * 客户端连接监听器
 * </p>
 */
public interface OnTcpClientConnectListener {
	public void onConnectSuccess();

	public void onConnectFailure();
}
