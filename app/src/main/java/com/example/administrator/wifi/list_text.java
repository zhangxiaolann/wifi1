package com.example.administrator.wifi;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class list_text extends AppCompatActivity {

    private ListView listView;
    List<String> list;
    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(list_text.this,
                        android.R.layout.simple_list_item_1, list);
                listView.setAdapter(arrayAdapter);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_text);
        listView = (ListView) findViewById(R.id.listview);
        new Thread(runnable).start();
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            list = new ArrayList<>();
            getFromAssets();
            handler.sendEmptyMessage(1);
        }
    };
    public void getFromAssets() {
        try {
            String encoding = "utf-8";
            InputStream in = getResources().getAssets().open("all1.txt");
            InputStreamReader read = new InputStreamReader(in, encoding);// 考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;

            while ((lineTxt = bufferedReader.readLine()) != null) {
                System.out.println(lineTxt);
                list.add(lineTxt);
            }
            read.close();
        } catch (Exception e) {
            System.out.println("读取文件内容出错" + e.toString());
            e.printStackTrace();
        }

    }
}
