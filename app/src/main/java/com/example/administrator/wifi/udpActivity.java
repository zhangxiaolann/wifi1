package com.example.administrator.wifi;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class udpActivity extends AppCompatActivity implements View.OnClickListener {


    Button button1, button2;
    TextView received_text;
    private String received_date;
    private Message message;

    private String input;
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);

        received_text = (TextView) findViewById(R.id.received_text);
        new Thread(new Runnable() {
            @Override
            public void run() {
                receive();
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        send(getip(),"发送给你");
                    }
                }).start();

                break;
            case R.id.button2:
                break;

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Object obj = msg.obj;
            received_text.setText(obj.toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    send(ip,"我收到了");
                }
            }).start();



        }
    };

    public void receive() {
        // 创建接收端的Socket对象
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(10086);
            while (true) {
                // 创建一个包裹
                byte[] bys = new byte[1024];
                DatagramPacket dp = new DatagramPacket(bys, bys.length);
                // 接收数据
                ds.receive(dp);
                // 解析数据
                ip = dp.getAddress().getHostAddress();
                received_date = new String(dp.getData(), 0, dp.getLength());
                message = new Message();
                message.obj = received_date;
                handler.sendMessage(message);
                System.out.println("from " + ip + " data is : " + received_date);
            }
            // 释放资源
            // 接收端应该一直开着等待接收数据，是不需要关闭
            // ds.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void send(String ip,String text) {
        // 创建发送端Socket对象
        // DatagramSocket()
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            // 创建数据，并把数据打包
            // DatagramPacket(byte[] buf, int length, InetAddress address, int port)
            // 创建数据
            byte[] bys = text.getBytes();
            // 长度
            int length = bys.length;
            // IP地址对象
            System.out.println(ip);
            InetAddress address = InetAddress.getByName(ip);
            // 端口
            int port = 10086;
            DatagramPacket dp = new DatagramPacket(bys, length, address, port);

            // 调用Socket对象的发送方法发送数据包
            // public void send(DatagramPacket p)
            ds.send(dp);

            // 释放资源
            ds.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //
    // 获得主机地址
    private String getip() {
        WifiManager wifiManager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        DhcpInfo info = wifiManager.getDhcpInfo();
        return intToIp(info.serverAddress);
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + ((i >> 24) & 0xFF);
    }
}
