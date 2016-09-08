package tcpip.listener;

import java.net.Socket;

/**
 * Caiyuan Huang
 * <p>
 * 2016-4-14
 * </p>
 * <p>
 * 接收到客户端的socket回调接口
 * </p>
 */
public interface OnAcceptClientSocketListener {
	public void onAcceptClientSocket(Socket socket);

}
