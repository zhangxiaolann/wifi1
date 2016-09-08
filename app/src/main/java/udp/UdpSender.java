package udp;

import java.io.IOException;

/**
 * Caiyuan Huang
 * <p>
 * 2016-4-13
 * </p>
 * <p>
 * UDP发送者
 * </p>
 */
public class UdpSender {
    private static UdpSender instance = null;
    private MulticastSender multicastSender;

    public static UdpSender getInstance() {
        if (instance == null) {
            synchronized (UdpSender.class) {
                if (instance == null) {
                    instance = new UdpSender();
                    return instance;
                }
            }
        }
        return instance;
    }

    private UdpSender() {
    }

    /**
     * 发送多播消息
     *
     * @param host
     * @param port
     * @param message
     * @throws IOException
     */
    public synchronized void sendMulticastMessage(String host, int port,byte[] message) throws IOException {
        if (multicastSender == null) {
            multicastSender = new MulticastSender(host, port);
            multicastSender.start();
        }
        multicastSender.sendMessage(message);
    }

    /**
     * 停止发送多播消息
     */
    public synchronized void stopSendMulticastMessage() {
        if (multicastSender != null) {
            multicastSender.exit();
            multicastSender = null;
        }

    }

}
