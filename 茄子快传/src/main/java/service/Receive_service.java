package service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import tools.connect_tools;

public class Receive_service extends Service {

    private String fileName;
    private int leng;


    public Receive_service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        received();
    }

    private void received() {
        Thread listener = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //绑定端口
                    int port = connect_tools.PORT_NUMBER;
                    try {
                        ServerSocket server = new ServerSocket(port);
                        String response = ReceiveFile(server);
                        System.out.println("" + response);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        });
        listener.start();
    }//tcp接收

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

            Intent intent=new Intent();
            intent.putExtra("leng",leng);
            intent.putExtra("fileName",fileName);
            intent.setAction("com.rec.leng");
            sendBroadcast(intent);//总进度广播

            br.close();
            streamReader.close();
            nameStream.close();
            name.close();
            //接收文件数据
            Socket data = server.accept();
            InputStream dataStream = data.getInputStream();
            String savePath = myPath() + "/" + fileName;
            FileOutputStream file = new FileOutputStream(savePath, false);

            byte[] buffer = new byte[1024 * 1024];//文件缓存区大小
            int size = -1;
            while ((size = dataStream.read(buffer)) != -1) {
                file.write(buffer, 0, size);
                total = total + size;
                Log.e("get", total + "");
                Intent intent1=new Intent();
                intent1.putExtra("total",total);
                intent1.setAction("com.rec.total");
                sendBroadcast(intent1);//实时进度发送广播

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
