package udp;

import java.io.IOException;

import tcpip.listener.OnUdpMulticastReceiveListener;


/**
 * Caiyuan Huang
 * <p>
 * 2016-4-13
 * </p>
 * <p>
 * UDP接收者
 * </p>
 */
public class UdpReceiver {
    private static UdpReceiver instance = null;
    private MulticastReceiver multicastReceiver;

    public static UdpReceiver getInstance() {
        if (instance == null) {
            synchronized (UdpReceiver.class) {
                if (instance == null) {
                    instance = new UdpReceiver();
                    return instance;
                }
            }
        }
        return instance;
    }

    public UdpReceiver() {

    }

    /**
     * 设置UDP广播接收监听器
     *
     * @param l
     * @param host        主机地址
     * @param port        监听端口
     * @param recvBufSize 接收缓冲区大小
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public synchronized void setOnUdpMulticastReceiveListener(
            OnUdpMulticastReceiveListener l) {
        multicastReceiver.setOnUdpMulticastReceiveListener(l);
    }

    /**
     * 开始监听UDP广播
     *
     * @param host
     * @param port
     * @param recvBufSize
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public synchronized void startListenUdpMulticast(String host, int port,
                                                     int recvBufSize) throws IllegalArgumentException, IOException {
        if (multicastReceiver == null) {
            multicastReceiver = new MulticastReceiver(host, port, recvBufSize);
            multicastReceiver.start();
        }
    }

    /**
     * 停止监听UDP广播
     */
    public synchronized void stopListenUdpMulticast() {
        if (multicastReceiver != null) {
            multicastReceiver.exit();
            multicastReceiver = null;
        }
    }

}
