package tools;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2016/6/26.
 */
public class Sender extends Thread {
    String serverIp;
    String message;

    Sender(String serverAddress, String message) {
        super();
        serverIp = serverAddress;
        this.message = message;
    }

    public void run() {
        Socket sock = null;
        PrintWriter out;
        try {
            // 声明sock，其中参数为服务端的IP地址与自定义端口
            sock = new Socket(serverIp, 3358);
            Log.w("WifiConnection", "I am try to writer" + sock);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            // 向服务器端发送消息
            out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(sock.getOutputStream())), true);
            out.println(message);

            // 接收来自服务器端的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String msg = br.readLine();

            // 关闭流
            out.close();
            out.flush();
            br.close();
            // 关闭Socket
            sock.close();
        } catch (Exception e) {
            Log.e("aaaa", "发送错误:[Sender]run()" + e.getMessage());
        }

    }
}