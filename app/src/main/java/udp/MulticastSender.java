package udp;

import android.text.TextUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import tcpip.BaseThread;
import tcpip.LogUtils;


/**
 * UDP广播发送者
 */
class MulticastSender extends BaseThread {
	private MulticastSocket multicastSocket;
	private String multicastHost;
	private int port;

	public MulticastSender(String host, int port) throws IOException,
			IllegalArgumentException {
		if (TextUtils.isEmpty(host)) {
			throw new IllegalArgumentException("host should not be null");
		}
		this.multicastHost = host;
		this.port = port;
		multicastSocket = new MulticastSocket();
		// 添加多播地址
		InetAddress groupAddr = InetAddress.getByName(host);
		multicastSocket.joinGroup(groupAddr);
	}

	/**
	 * 发送多播消息内部实现
	 * 
	 * @param message
	 * @throws IOException
	 */
	private void sendMessageInternal(byte[] message) throws IOException {
		if (message == null) {
			LogUtils.i("MulticastSender,message is null");
			return;
		}
		DatagramPacket datagramPacket = new DatagramPacket(message,
				message.length);
		InetAddress groupAddr = InetAddress.getByName(multicastHost);
		datagramPacket.setAddress(groupAddr);// 目标接收地址需要与多播地址相同
		datagramPacket.setPort(port); // 目标端口号
		multicastSocket.send(datagramPacket);
	}

	/**
	 * 发送多播消息
	 * 
	 * @param message
	 */
	public void sendMessage(byte[] message) {
		MulticastMessagePool.getInstance().addMessage(message);
	}

	@Override
	public void run() {
		while (!isExit) {
			byte[] message = MulticastMessagePool.getInstance().getMessage();
			if (message != null) {
				try {
					sendMessageInternal(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		multicastSocket.close();
	}

}
