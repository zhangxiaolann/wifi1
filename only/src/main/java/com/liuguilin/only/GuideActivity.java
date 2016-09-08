package com.liuguilin.only;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


/**
 * 引导
 * Created by LGL on 2016/3/25.
 */
public class GuideActivity extends Activity{

    private static final int TIMEOUT = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case TIMEOUT:
                    startActivity(new Intent(GuideActivity.this,LocationActivity.class));
                    finish();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        handler.sendEmptyMessageDelayed(TIMEOUT,3000);
    }
}
