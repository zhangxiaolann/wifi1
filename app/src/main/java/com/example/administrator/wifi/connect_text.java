package com.example.administrator.wifi;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
tcp
 */
public class connect_text extends AppCompatActivity implements View.OnClickListener {

    Button received, send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_text);
        received = (Button) findViewById(R.id.received);
        send = (Button) findViewById(R.id.send);
        received.setOnClickListener(this);
        send.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.received:
                received();
                break;
            case R.id.send:
                send();
                break;
        }
    }



        private void received() {
        Thread listener = new Thread(new Runnable() {
            @Override
            public void run() {
                //绑定端口
                int port = 9999;
                try {
                    ServerSocket  server = new ServerSocket(port);
                    while (true) {//接收文件
                        String  response = ReceiveFile(server);
                        System.out.println("" + response);
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        listener.start();
    }

    FileInputStream fis;

    private void send() {
        final File directory = Environment.getExternalStorageDirectory();//获取sd卡的根目录
        final File file = new File(directory + "/" + "UCDownloads" + "/" + "volley.zip");

        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String response = SendFile("volley.zip",
                            directory + "/UCDownloads",
                            getip(),
                            9999, fis.available());
                    System.out.println("信息"+response);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e.toString());
                }
            }
        });
        sendThread.start();
    }

    public String GetIpAddress() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int i = wifiInfo.getIpAddress();
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + ((i >> 24) & 0xFF);
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

    private String fileName;
    private int leng;


    synchronized public String SendFile(String fileName, String path, String ipAddress, int port, int length) {
        int finished = 0;
        try {
            Socket name = new Socket(ipAddress, port);
            OutputStream outputName = name.getOutputStream();
            OutputStreamWriter outputWriter = new OutputStreamWriter(outputName);
            BufferedWriter bwName = new BufferedWriter(outputWriter);
            bwName.write(fileName + "x" + length);
            bwName.close();
            outputWriter.close();
            outputName.close();
            name.close();

            Socket data = new Socket(ipAddress, port);
            OutputStream outputData = data.getOutputStream();
            FileInputStream fileInput = new FileInputStream(path+"/"+fileName);
            int size;
            byte[] buffer = new byte[4028];
            while ((size = fileInput.read(buffer)) != -1) {
                outputData.write(buffer, 0, size);
                finished = finished + size;
                Log.e("upload", finished + "");
//                if (finished < length) {
//                    pb.setProgress(finished);
//                } else {
//                   pb.dismiss();
//                }
            }
            outputData.close();
            fileInput.close();
            data.close();
            return fileName + "发送完成";
        } catch (Exception e) {
            return "连接已断开，传输失败,请重新连接!";
        }
    }


    synchronized public String ReceiveFile(ServerSocket server) {
        int total = 0;
        try {
            //接收文件名
            Socket name = server.accept();
            InputStream nameStream = name.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(nameStream);
            BufferedReader br = new BufferedReader(streamReader);
            String fileInformation = br.readLine();
            leng = Integer.parseInt(fileInformation.substring(fileInformation.lastIndexOf("x") + 1));
            fileName = fileInformation.substring(0, fileInformation.lastIndexOf("x"));
            br.close();
            streamReader.close();
            nameStream.close();
            name.close();
            //接收文件数据
            Socket data = server.accept();
            InputStream dataStream = data.getInputStream();
            String savePath = myPath() + "/" + fileName;
            FileOutputStream file = new FileOutputStream(savePath, false);

            byte[] buffer = new byte[1024 * 1024];
            int size = -1;
            while ((size = dataStream.read(buffer)) != -1) {
                file.write(buffer, 0, size);
                total = total + size;
                Log.e("get", total + "");
//                pb.setProgress(total);
            }
            file.close();
            dataStream.close();
            data.close();
            return fileName + " 接收完成";
        } catch (Exception e) {
            return "连接已断开，传输失败,请重新连接!";
        }
    }

    private String myPath() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "shanchuan");
        if (!file.exists())
            file.mkdir();
        return Environment.getExternalStorageDirectory().getPath() + "/" + "shanchuan";
    }
}
