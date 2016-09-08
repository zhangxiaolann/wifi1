package tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import tcpip.BaseThread;
import tcpip.listener.OnTcpMessageSendListener;


/**
 * Caiyuan Huang
 * <p>
 * 2016-4-14
 * </p>
 * <p>
 * TCP消息发送者
 * </p>
 */
public class TcpSender {
	private TcpMessagePool messagePool;
	private Socket socket;
	private OutputStream outputStream;
	private OnTcpMessageSendListener onTcpMessageSendListener;
	private TcpMessageSendThread messageSenderThread;

	public TcpSender(Socket socket) throws IOException {
		messagePool = new TcpMessagePool();
		this.socket = socket;
		this.outputStream = this.socket.getOutputStream();

	}

	/**
	 * 发送消息,往消息池里面添加消息
	 * 
	 * @param message
	 */
	public synchronized void sendMessage(byte[] message) {
		if (messagePool != null) {
			messagePool.addMessage(message);
		}

	}

	/**
	 * 设置消息发送监听器
	 * 
	 * @param l
	 */
	public void setOnTcpMessageSendListener(OnTcpMessageSendListener l) {
		this.onTcpMessageSendListener = l;
	}

	/**
	 * 开始发送消息,从消息池里面取出消息进行发送
	 */
	public synchronized void startSendMessage() {
		if (messageSenderThread == null) {
			messageSenderThread = new TcpMessageSendThread();
			messageSenderThread.start();
		}
	}

	/**
	 * 停止消息发送
	 */
	public synchronized void stopSendMessage() {
		if (messageSenderThread != null) {
			messageSenderThread.exit();
			messageSenderThread = null;
		}
	}

	/**
	 * 发送消息内部实现
	 * 
	 * @param message
	 * @throws IOException
	 */
	private void sendMessageInternal(byte[] message) throws IOException {
		if (message == null) {
			return;
		}
		if (outputStream != null && socket.isConnected()) {
			outputStream.write(message, 0, message.length);
			outputStream.flush();
		} else {
			if (onTcpMessageSendListener != null) {
				onTcpMessageSendListener.onSocketBreak();
			}
			throw new IOException();
		}
	}

	/**
	 * 消息发送线程
	 */
	private class TcpMessageSendThread extends BaseThread {

		@Override
		public void run() {
			while (!isExit) {
				if (messagePool != null) {
					byte[] message = messagePool.getMessage();
					if (message != null) {
						boolean isSendSuccess = false;
						try {
							sendMessageInternal(message);
							isSendSuccess = true;
						} catch (Exception e) {
							e.printStackTrace();
							isSendSuccess = false;
						}
						if (onTcpMessageSendListener != null) {
							onTcpMessageSendListener
									.onTcpMessageSend(isSendSuccess);
						}
					}
				}
			}
		}

	}
}
