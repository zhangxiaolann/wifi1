package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2016/6/26.
 */
public class Receiver extends Thread {
    public String receiverContent;
    public boolean flag = true;
    public ServerSocket serverSocket = null;

    public void run() {
        try {
            // 创建ServerSocket
            serverSocket = new ServerSocket(3358);
            while (flag) {
                // 接受客户端请求
                Socket client = serverSocket.accept();
                System.out.println("accept");
                try {
                    // 接收客户端消息
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(client.getInputStream()));
                    receiverContent = in.readLine();
                    System.out.println("read:" + receiverContent);
//        handler.sendEmptyMessage(0);
                    // 向服务器发送消息
                    PrintWriter out = new PrintWriter(
                            new BufferedWriter(new OutputStreamWriter(
                                    client.getOutputStream())), true);
                    out.println("server message");
                    // 关闭流
                    out.close();
                    in.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                } finally {
                    // 关闭
                    client.close();
                    System.out.println("close");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        flag = false;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

