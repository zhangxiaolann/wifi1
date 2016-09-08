package tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import tcpip.BaseThread;
import tcpip.listener.OnAcceptClientSocketListener;


/**
 * Caiyuan Huang
 * <p>
 * 2016-4-14
 * </p>
 * <p>
 * TCP服务端
 * </p>
 */
public class TcpServer {
	private static TcpServer instance = null;
	private ServerSocket serverSocket;
	private OnAcceptClientSocketListener acceptClientSocketListener;
	private MonitorClientSocketThread monitorClientSocketThread;

	public static TcpServer getInstance() {
		if (instance == null) {
			synchronized (TcpServer.class) {
				if (instance == null) {
					instance = new TcpServer();
					return instance;
				}
			}
		}
		return instance;
	}

	private TcpServer() {

	}

	/**
	 * 设置客户端连接监听器
	 * 
	 * @param l
	 * @param port
	 * @throws IOException
	 */
	public synchronized void setOnAcceptClientSocketListener(
			OnAcceptClientSocketListener l, int port) throws IOException {
		if (serverSocket == null) {
			serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
			serverSocket.setSoTimeout(0);
			serverSocket.bind(new InetSocketAddress(port));
			monitorClientSocketThread = new MonitorClientSocketThread();
			monitorClientSocketThread.start();
		}
		this.acceptClientSocketListener = l;
	}

	/**
	 * 停止服务
	 * 
	 * @throws IOException
	 */
	public synchronized void stopAcceptClientSocket() throws IOException {
		if (monitorClientSocketThread != null) {
			monitorClientSocketThread.exit();
			monitorClientSocketThread = null;
			serverSocket.close();
			serverSocket = null;
		}
	}

	/**
	 * 监听客户端连接的线程
	 */
	private class MonitorClientSocketThread extends BaseThread {

		@Override
		public void run() {
			while (!isExit) {
				try {
					Socket socket = serverSocket.accept();
					onAcceptClientSocket(socket);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void onAcceptClientSocket(Socket socket) {
		if (acceptClientSocketListener != null) {
			acceptClientSocketListener.onAcceptClientSocket(socket);
		}
	}
}
