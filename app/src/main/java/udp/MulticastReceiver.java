package udp;

import android.text.TextUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import tcpip.BaseThread;
import tcpip.listener.OnUdpMulticastReceiveListener;


/**
 * Caiyuan Huang
 * <p>
 * 2016-4-13
 * </p>
 * <p>
 * UDP广播接收者
 * </p>
 */
class MulticastReceiver extends BaseThread {
	private MulticastSocket multicastSocket;
	private String host;
	private int port;
	private int recvBufSize = 1024;// 接收数据缓冲区大小
	private OnUdpMulticastReceiveListener udpMulticastReceiveListener;

	public MulticastReceiver(String host, int port, int recvBufSize)
			throws IOException, IllegalArgumentException {
		if (TextUtils.isEmpty(host)) {
			throw new IllegalArgumentException("host should not be null");
		}
		this.host = host;
		this.port = port;
		this.recvBufSize = recvBufSize;
		multicastSocket = new MulticastSocket(port);
		InetAddress groupAddr = InetAddress.getByName(host);
		multicastSocket.joinGroup(groupAddr);

	}

	@Override
	public void run() {
		try {
			byte buf[] = new byte[recvBufSize];
			DatagramPacket datagramPacket = null;
			datagramPacket = new DatagramPacket(buf, buf.length,
					InetAddress.getByName(host), port);
			while (!isExit) {
				multicastSocket.receive(datagramPacket);
				int len = datagramPacket.getLength();
				byte[] receivedData = new byte[len];
				System.arraycopy(buf, 0, receivedData, 0, len);
				onRecieveMessage(receivedData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			multicastSocket.close();
		}
	}

	/**
	 * 设置广播接收监听器
	 * 
	 * @param l
	 */
	public void setOnUdpMulticastReceiveListener(OnUdpMulticastReceiveListener l) {
		this.udpMulticastReceiveListener = l;
	}

	private void onRecieveMessage(byte[] message) {
		if (udpMulticastReceiveListener != null) {
			udpMulticastReceiveListener.onReceive(message);
		}
	}

}
