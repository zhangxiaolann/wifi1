package com.wificonnect.qiezhi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class set_up_activity extends Activity {
    private TextView te;
    private LinearLayout lin, lin1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        View view = findViewById(R.id.title_setup);
        TextView textView = (TextView) view.findViewById(R.id.title_tv);
        textView.setText("设置");
        Button button = (Button) view.findViewById(R.id.back_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        te = (TextView) findViewById(R.id.te);
        lin = (LinearLayout) findViewById(R.id.lin);
        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(set_up_activity.this);
                builder.setTitle("设置存储位置");
                builder.setMessage("内部存储设备\n/内部存储设备/QieZi");
                builder.setPositiveButton("确定", null);
                builder.create();
                builder.show();
            }
        });
        lin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(set_up_activity.this);
                builder.setTitle("这是一个信息");
                builder.setMessage("确定清空所有缓存吗?");
                builder.setPositiveButton("确定", null);
                builder.setNegativeButton("取消", null);
                builder.create();
                builder.show();

            }
        });

    }

}
