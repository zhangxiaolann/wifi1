package com.example.administrator.wifi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import wifi.AccessPoint;
import wifi.WifiApManager;
import wifi.WifiBroadcastReceiver;
import wifi.WifiLib;
import wifi.WifiLibInitializer;
import wifi.WifiUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btOpenWifi;
    private Button btCloseWifi;
    private Button btStartScan;
    private Button btStopScan;
    private Button btCreateAp;
    private Button btCloseAp;
    private Button btConnect;
    Button list_text;
    Button next_act;
    private WifiLib wifiLib;
    private String connectSsid = "hcy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_text = (Button) findViewById(R.id.list_text);
        btOpenWifi = (Button) findViewById(R.id.btOpenWifi);
        btCloseWifi = (Button) findViewById(R.id.btCloseWifi);
        btStartScan = (Button) findViewById(R.id.btStartScan);
        btStopScan = (Button) findViewById(R.id.btStopScan);
        btCreateAp = (Button) findViewById(R.id.btCreateAp);
        btCloseAp = (Button) findViewById(R.id.btCloseAp);
        btConnect = (Button) findViewById(R.id.btConnect);
        next_act = (Button) findViewById(R.id.next_act);
        btOpenWifi.setOnClickListener(this);
        btCloseWifi.setOnClickListener(this);
        btStartScan.setOnClickListener(this);
        btStopScan.setOnClickListener(this);
        btCreateAp.setOnClickListener(this);
        btCloseAp.setOnClickListener(this);
        btConnect.setOnClickListener(this);
        next_act.setOnClickListener(this);
        list_text.setOnClickListener(this);

        WifiLibInitializer.init(this);
        wifiLib = WifiLib.getInstance();
        wifiLib.setOnWifiBroadcastReceiveCallback(callback);
    }

    private WifiBroadcastReceiver.OnWifiBroadcastReceiveCallback callback = new WifiBroadcastReceiver.OnWifiBroadcastReceiveCallback() {
        public void onScanResultsAvailable(
                java.util.List<AccessPoint> accessPoints) {
            WifiUtils.printAccessPoints(accessPoints);
            for (int i = 0; i < accessPoints.size(); i++) {
                AccessPoint ap = accessPoints.get(i);
                if (!TextUtils.isEmpty(connectSsid)
                        && ap.getSsid().equals(connectSsid)) {//如果遍历到了下面点击了连接给的ssid就会执行开始连接
                    boolean isSuccess = wifiLib.connectToAccessPoint(ap,
                            "12345678");
                    Log.i("HCY", isSuccess ? "连接热点成功" : "连接热点失败");//三木运算符
                    wifiLib.stopScan();//连接成功停止扫描
                }
            }
        }

        ;
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btOpenWifi://开启wifi
                wifiLib.openWifi();
                break;
            case R.id.btCloseWifi://关闭wifi
                wifiLib.closeWifi();
                break;
            case R.id.btStartScan://扫描wifi
                wifiLib.startScan(1000);//时间间隔
                break;
            case R.id.btStopScan://停止扫描
                wifiLib.stopScan();
                break;
            case R.id.btCloseAp://关闭热点
                wifiLib.closeWifiAp();
                break;
            case R.id.btCreateAp://开启热点
                wifiLib.createAccessPoint(WifiApManager.WifiApType.TYPE_WPA_PSK,
                        "money666", "12345678");//三个参数，第一个为开启的状态：无密码，wpa_psk,WPA2_PSK,第二个参数为wifi名称，第三个为密码
                break;
            case R.id.btConnect://连接wifi
                connectSsid = "money666";//给个wifi的ssid然后就会回调广播接受者中的方法
                Log.i("HCY", "wifi 连接中...");
                break;
            case R.id.next_act://跳转
                startActivity(new Intent(this, udpActivity.class));
                break;
            case R.id.list_text:
                startActivity(new Intent(this, com.example.administrator.wifi.list_text.class));
                break;
        }
    }
}
