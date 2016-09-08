package tcp;


import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import tcpip.BaseThread;
import tcpip.listener.OnTcpMessageReceiveListener;


/**
 * Caiyuan Huang
 * <p>
 * 2016-4-14
 * </p>
 * <p>
 * TCP消息接收者
 * </p>
 */
public abstract class TcpReceiver {
	private Socket socket;
	private InputStream inputStream;
	private byte[] recvBuf;
	private OnTcpMessageReceiveListener onTcpMessageReceiveListener;
	private TcpMessageReceiveThread tcpMessageReceiveThread;

	public TcpReceiver(Socket socket, int recvBufSize) throws IOException {
		this.socket = socket;
		this.inputStream = this.socket.getInputStream();
		recvBuf = new byte[recvBufSize];
	}

	/**
	 * 设置数据接收监听器
	 * 
	 * @param l
	 */
	public void setOnTcpMessageReceiveListener(OnTcpMessageReceiveListener l) {
		this.onTcpMessageReceiveListener = l;
	}

	/**
	 * 开始接收数据
	 */
	public synchronized void startReceiveMessage() {
		if (tcpMessageReceiveThread == null) {
			tcpMessageReceiveThread = new TcpMessageReceiveThread();
			tcpMessageReceiveThread.start();
		}
	}

	/**
	 * 停止消息接收
	 */
	public synchronized void stopReceiveMessage() {
		if (tcpMessageReceiveThread != null) {
			tcpMessageReceiveThread.exit();
			tcpMessageReceiveThread = null;
		}
	}

	/**
	 * TCP消息接收线程
	 */
	private class TcpMessageReceiveThread extends BaseThread {
		@Override
		public void run() {
			while (!isExit) {
				try {
					if (inputStream != null && inputStream.available() > 0) {
						int len = readMessage(inputStream, recvBuf);
						if (len > 0) {
							byte[] message = new byte[len];
							System.arraycopy(recvBuf, 0, message, 0, len);
							if (onTcpMessageReceiveListener != null) {
								onTcpMessageReceiveListener
										.onTcpMessageReceive(message);
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 读取消息,读取一个数据包
	 * 
	 * @param inputStream
	 *            输入流
	 * @param recvBuf
	 *            接收缓冲区
	 * @return 读取的字节长度
	 */
	protected abstract int readMessage(InputStream inputStream, byte[] recvBuf);
}
