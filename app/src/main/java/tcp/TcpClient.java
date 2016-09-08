package tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import tcpip.listener.OnTcpClientConnectListener;


/**
 * Caiyuan Huang
 * <p>
 * 2016-4-14
 * </p>
 * <p>
 * TCP客户端
 * </p>
 */
public class TcpClient {
	private static TcpClient instance = null;
	private Socket clientSocket;

	public static TcpClient getInstance() throws IOException {
		if (instance == null) {
			synchronized (TcpClient.class) {
				if (instance == null) {
					instance = new TcpClient();
					return instance;
				}
			}
		}
		return instance;
	}

	private TcpClient() throws IOException {
		clientSocket = new Socket();
	}

	/**
	 * 连接到服务端
	 * 
	 * @param serverHost
	 *            服务端IP
	 * @param port
	 *            服务端监听连接的端口
	 * @param timeout
	 *            超时时间，单位毫秒
	 * @param l
	 *            连接监听器
	 */
	public void connnetToServer(final String serverHost, final int port,
			final int timeout, final OnTcpClientConnectListener l) {
		new Thread() {
			@Override
			public void run() {
				try {
					InetAddress address = InetAddress.getByName(serverHost);
					SocketAddress remoteAddr = new InetSocketAddress(address,
							port);
					clientSocket.connect(remoteAddr, timeout);
					if (l != null) {
						l.onConnectSuccess();
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (l != null) {
						l.onConnectFailure();
					}
				}
			}
		}.start();
	}
}
