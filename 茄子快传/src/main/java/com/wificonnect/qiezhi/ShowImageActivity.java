package com.wificonnect.qiezhi;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import adapter.ChildAdapter;
import tools.connect_tools;

/*
图片显示的布局
 */
public class ShowImageActivity extends Activity {
    private GridView mGridView;
    private List<String> list;
    private ChildAdapter adapter;
    private String path;
    private FileInputStream fileInputStream;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View viewById = findViewById(R.id.title_include);
        Button button = (Button) viewById.findViewById(R.id.back_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView textView = (TextView) viewById.findViewById(R.id.title_tv);
        textView.setText("发送图片");

        mGridView = (GridView) findViewById(R.id.child_grid);
        list = getIntent().getStringArrayListExtra("data");

        adapter = new ChildAdapter(this, list, mGridView);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//先的到connected，判断是否连接上设备
                SharedPreferences pref = getSharedPreferences("judge", MODE_PRIVATE);
                boolean connected = pref.getBoolean("connected", false);
                if (!connected) {
                    Toast.makeText(ShowImageActivity.this, "请先连接设备", Toast.LENGTH_SHORT).show();
                } else {
                    path = list.get(position);//得到图片路径
                    /////////传输//////传输/////传输////传输/传输/////传输////传输//
                    progress = new ProgressDialog(ShowImageActivity.this);
                    progress.setTitle("任务完成度");

                    try {
                        fileInputStream = new FileInputStream(path);
                        progress.setMessage(getFileName(path));
                        progress.setMax(fileInputStream.available()/1024/1024);
                        progress.setCancelable(true);//设置不能使用取消按钮
                        progress.setIndeterminate(false); // 设置对话框的进度条是否显示进度
                        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progress.show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Log.e("tag", "找不到文件");
                        Toast.makeText(ShowImageActivity.this, "找不到文件", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("tag", "io异常");
                        progress.dismiss();
                        Toast.makeText(ShowImageActivity.this, "读取文件异常", Toast.LENGTH_SHORT).show();
                    }
                    new Thread(runnable).start();//开始发送

                }
                return true;
            }
        });

        //点击打开图片
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                path = list.get(position);//得到图片路径
                Intent intent = new Intent(Intent.ACTION_VIEW);
                File file = new File(path);
                intent.setDataAndType(Uri.fromFile(file), "image/*");
                startActivity(intent);
            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                String response = connect_tools.SendFile(ShowImageActivity.this, getFileName(path), path,
                        connect_tools.getip(ShowImageActivity.this),
                         fileInputStream.available(), progress);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.toString());
            }
        }
    };


    private String getFileName(String url) {
        int index = url.lastIndexOf("/") + 1;
        return url.substring(index);
    }

}
