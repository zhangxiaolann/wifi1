package com.wificonnect.qiezhi;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import tools.RadarView;
import wifi.AccessPoint;
import wifi.WifiBroadcastReceiver;
import wifi.WifiLib;
import wifi.WifiLibInitializer;
import wifi.WifiUtils;

public class is_Sending extends Activity implements View.OnClickListener {

    private WifiLib wifiLib;//wifi管理类
    private String connectSsid = "My_kuaichuan";//热点名字
    private boolean isSuccess;
    private TextView textIsConnect;
    boolean flag = true;
    private String received_date;
    private RadarView radarView;
    private Button send_button;
    private Button research_button;


    private void assignViews() {


        research_button = (Button) findViewById(R.id.research_button);
        research_button.setOnClickListener(this);
        View title = findViewById(R.id.title);
        Button button = (Button) title.findViewById(R.id.back_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView textView = (TextView) title.findViewById(R.id.title_tv);
        textView.setText("连接");
        TextView textView1 = (TextView) title.findViewById(R.id.title_lianjie);
        textView1.setText("连接不上？");
        textView1.setVisibility(View.VISIBLE);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(is_Sending.this);
                builder.setTitle("连接不上");
                builder.setIcon(R.drawable.lw);
                builder.setMessage("如果连接不上My_kuaichuan可以手动连接，密码为12345678，再打开该应用，重新连接设备即可！");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                builder.create();
                builder.show();
            }
        });
        textIsConnect = (TextView) findViewById(R.id.text_is_connect);
        radarView = (RadarView) findViewById(R.id.RadarView);
        radarView.setSearching(true);
        send_button = (Button) findViewById(R.id.send_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //连接上了设备后将connected设置为true，并更新界面提示连接成功，显示发送按钮
                SharedPreferences.Editor editor = getSharedPreferences("judge",
                        MODE_PRIVATE).edit();
                editor.putBoolean("connected", true);
                editor.commit();
                textIsConnect.setText("连接成功！");
                radarView.setSearching(false);//停止扫描动画
                send_button.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.is__sending);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//沉浸状态栏
        assignViews();
        //wifi初始化
        WifiLibInitializer.init(this);
        wifiLib = WifiLib.getInstance();
        boolean b = wifiLib.openWifi();//开启wifi
        wifiLib.setOnWifiBroadcastReceiveCallback(callback);//连接上了设置监听
        wifiLib.startScan(500);//扫描wifi，时间间隔0.5s
        new Thread(runnable1).start();//开启接收者


    }

    private WifiBroadcastReceiver.OnWifiBroadcastReceiveCallback callback = new WifiBroadcastReceiver.OnWifiBroadcastReceiveCallback() {
        public void onScanResultsAvailable(
                java.util.List<AccessPoint> accessPoints) {
            WifiUtils.printAccessPoints(accessPoints);
            for (int i = 0; i < accessPoints.size(); i++) {
                AccessPoint ap = accessPoints.get(i);
                if (!TextUtils.isEmpty(connectSsid)
                        && ap.getSsid().equals(connectSsid)) {//如果遍历到了下面点击了连接给的ssid就会执行开始连接
                    isSuccess = wifiLib.connectToAccessPoint(ap,
                            "12345678");
                    Log.i("HCY", isSuccess ? "连接热点成功" : "连接热点失败");//三木运算符
                    if (isSuccess) {
                        textIsConnect.setText("正在尝试连接");
                        new Thread(runnable).start();//开启了wifi然后开启发送udp广播，当对方接收到了广播就会回播一条数据告诉我已经成功连接
                    } else {
                        textIsConnect.setText("连接热点失败");
                    }
                    wifiLib.stopScan();//连接成功停止扫描
                }
            }
        }

        ;
    };


    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            while (flag) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                send(getip(), "1");//发送udp广播给对方
            }

        }
    };

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            receive();
        }
    };

    //接收对方回发的udp广播
    public void receive() {
        // 创建接收端的Socket对象
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(54321);
            while (true) {
                // 创建一个包裹
                byte[] bys = new byte[1024];
                DatagramPacket dp = new DatagramPacket(bys, bys.length);
                // 接收数据
                ds.receive(dp);
                // 解析数据
                String ip = dp.getAddress().getHostAddress();
                received_date = new String(dp.getData(), 0, dp.getLength());
                System.out.println("from " + ip + " data is : " + received_date);
                if (received_date.equals("11111")) {
                    flag = false;
                    handler.sendEmptyMessage(1);
                }
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

    //连接上发送udp广播
    public void send(String ip, String text) {
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

    // 获得主机地址
    private String getip() {
        WifiManager wifiManager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        DhcpInfo info = wifiManager.getDhcpInfo();
        return intToIp(info.serverAddress);
    }

    //ip格式化
    private String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + ((i >> 24) & 0xFF);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.research_button:
                startActivityForResult(new Intent(this, CaptureActivity.class), 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");//当拿到结果
            String[] split = result.split("-");
            String name = split[0];
            String psd = split[1];
            AccessPoint accessPoint = new AccessPoint(name, AccessPoint.SECURITY_WPA2_PSK);
            isSuccess = wifiLib.connectToAccessPoint(accessPoint,
                    psd);
            Log.i("HCY", isSuccess ? "连接热点成功" : "连接热点失败");//三木运算符
            if (isSuccess) {
                textIsConnect.setText("正在尝试连接");
                new Thread(runnable).start();//开启了wifi然后开启发送udp广播，当对方接收到了广播就会回播一条数据告诉我已经成功连接
            } else {
                textIsConnect.setText("连接热点失败");
            }
            wifiLib.stopScan();//连接成功停止扫描

        }
    }
}
