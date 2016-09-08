package tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2016/7/6.
 */
public class connect_tools {

    public final static int PORT_NUMBER=9999;//端口号
    /**
     * 发送文件
     * @param context 上下文
     * @param fileName 文件名
     * @param path 文件地址
     * @param ipAddress ip地址
     * @param length
     * @param progress
     * @return
     */
    synchronized public static String SendFile(Context context,String fileName, String path, String ipAddress, int length, ProgressDialog progress ) {
        int finished = 0;
        try {
            Socket name = new Socket(ipAddress, PORT_NUMBER);
            OutputStream outputName = name.getOutputStream();
            OutputStreamWriter outputWriter = new OutputStreamWriter(outputName);
            BufferedWriter bwName = new BufferedWriter(outputWriter);
            bwName.write(fileName + "x" + length);
            bwName.close();
            outputWriter.close();
            outputName.close();
            name.close();

            Socket data = new Socket(ipAddress, PORT_NUMBER);
            OutputStream outputData = data.getOutputStream();
            FileInputStream fileInput = new FileInputStream(path);
            int size;
            byte[] buffer = new byte[4028];
            while ((size = fileInput.read(buffer)) != -1) {
                outputData.write(buffer, 0, size);
                finished = finished + size;
                Log.e("upload", finished + "");
                if (finished < length) {
                    progress.setProgress(finished/1024/1024);
                } else {
                    progress.dismiss();
                }
            }
            outputData.close();
            fileInput.close();
            data.close();
            SharedPreferences pref = context.getSharedPreferences("judge",
                    context.MODE_PRIVATE);
            long sendsize = pref.getLong("sendsize",length);
            long rece_size=sendsize+length;
            SharedPreferences.Editor editor =  context.getSharedPreferences("judge",
                    context.MODE_PRIVATE).edit();
            editor.putLong("sendsize",rece_size);
            editor.commit();
            return fileName + "发送完成";
        } catch (UnknownHostException e) {
            progress.dismiss();
            e.printStackTrace();
            return "连接已断开，传输失败,请重新连接!";
        } catch (FileNotFoundException e) {
            progress.dismiss();

            e.printStackTrace();
            return "文件读取失败，请重新尝试！";
        } catch (IOException e) {
            progress.dismiss();
            e.printStackTrace();
            return "传输失败，请重新尝试！";
        }


    }


    public static String getip(Activity activity) {
        WifiManager wifiManager = (WifiManager) activity
                .getSystemService(Context.WIFI_SERVICE);
        DhcpInfo info = wifiManager.getDhcpInfo();
        return intToIp(info.serverAddress);
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + ((i >> 24) & 0xFF);
    }
}
