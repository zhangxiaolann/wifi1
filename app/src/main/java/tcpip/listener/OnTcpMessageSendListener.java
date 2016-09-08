package tcpip.listener;

/**
 * Caiyuan Huang
 * <p>
 * 2016-4-14
 * </p>
 * <p>
 * TCP消息发送监听器
 * </p>
 */
public interface OnTcpMessageSendListener {
	/**
	 * 消息发送回调
	 * 
	 * @param isSuccess
	 */
	public void onTcpMessageSend(boolean isSuccess);

	/**
	 * socket断开
	 */
	public void onSocketBreak();

}
