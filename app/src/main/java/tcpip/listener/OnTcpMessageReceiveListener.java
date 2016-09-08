package tcpip.listener;

/**
 * Caiyuan Huang
 * <p>
 * 2016-4-14
 * </p>
 * <p>
 * TCP消息接收监听器
 * </p>
 */
public interface OnTcpMessageReceiveListener {
	/**
	 * 消息接收回调
	 * 
	 * @param message
	 */
	public void onTcpMessageReceive(byte[] message);

	/**
	 * socket断开
	 */
	public void onSocketBreak();

}
