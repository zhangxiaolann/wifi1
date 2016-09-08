package tcpip;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Caiyuan Huang
 * <p>
 * 2016-4-13
 * </p>
 * <p>
 * 消息池
 * </p>
 */
public class MessagePool {
	private Queue<byte[]> messageQueue = null;// 消息队列
	private Object signal = new Object();

	protected MessagePool() {
		messageQueue = new ConcurrentLinkedQueue<byte[]>();
	}

	/**
	 * 添加消息到消息池
	 * 
	 * @param message
	 */
	public void addMessage(byte[] message) {
		if (message == null) {
			return;
		}
		synchronized (signal) {
			messageQueue.add(message);
			signal.notifyAll();// 唤醒等待线程
		}
	}

	/**
	 * 获取消息
	 * 
	 * @return
	 */
	public byte[] getMessage() {
		synchronized (signal) {
			if (messageQueue.isEmpty()) {
				try {
					signal.wait();// 让线程等待
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			byte[] message = messageQueue.poll();
			return message;
		}
	}

	/**
	 * 清空消息池
	 */
	public void clearMessage() {
		synchronized (signal) {
			messageQueue.clear();
		}
	}
}
