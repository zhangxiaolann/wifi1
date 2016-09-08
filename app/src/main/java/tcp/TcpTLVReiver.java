package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Caiyuan Huang
 * <p>
 * 2016-4-14
 * </p>
 * <p>
 * TLV协议的TCP数据接收者
 * </p>
 */
public class TcpTLVReiver extends TcpReceiver {

	public TcpTLVReiver(Socket socket, int recvBufSize) throws IOException {
		super(socket, recvBufSize);
	}

	@Override
	protected int readMessage(InputStream inputStream, byte[] recvBuf) {
		// TLV编码，第一、第二个字节为TAG,第三、第四个字节为Length,后面Length个字节为数据
		int countRead;
		int offset;
		int remaining;
		int messageLength = -1;
		offset = 0;
		remaining = 4;
		try {
			// 读取前四个字节，取出数据长度
			do {
				countRead = inputStream.read(recvBuf, offset, remaining);
				if (countRead < 0) {
					return -1;
				}
				offset += countRead;
				remaining -= countRead;
			} while (remaining > 0);
			messageLength = ((recvBuf[2] & 0xff) << 8) | (recvBuf[3] & 0xff);
			if (messageLength < 0) {
				return 0;
			}
			// 读取数据到缓冲区
			offset = 4;
			remaining = messageLength;
			do {
				countRead = inputStream.read(recvBuf, offset, remaining);
				if (countRead < 0) {
					return -1;
				}
				offset += countRead;
				remaining -= countRead;
			} while (remaining > 0);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return messageLength + 4;
	}

}
