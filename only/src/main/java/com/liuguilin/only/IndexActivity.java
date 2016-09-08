package com.liuguilin.only;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.liuguilin.only.utils.PicassoUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 首页
 * Created by LGL on 2016/3/25.
 */
public class IndexActivity extends Activity {

    private Handler handler;
    //知乎日报接口
    private String url = "http://news-at.zhihu.com/api/4/start-image/1080*1776";

    private ImageView iv_index;
    private TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        initView();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);// 睡眠2000
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = hand.obtainMessage();
                hand.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 初始化View
     */
    private void initView() {
        iv_index = (ImageView) findViewById(R.id.iv_index);
        tv_content = (TextView) findViewById(R.id.tv_content);

        //解析json
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 成功
                    @Override
                    public void onResponse(String json) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            tv_content.setText(jsonObject.getString("text"));
                            PicassoUtils.loadImageViewSize(IndexActivity.this,jsonObject.getString("img"),720,1280,iv_index);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            // 失败
            @Override
            public void onErrorResponse(VolleyError errorLog) {
            }
        });
        queue.add(request);
    }


    Handler hand = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (isFristRun()) {
                // 如果是第一次启动程序则进入引导界面
                Intent intent = new Intent(IndexActivity.this,
                        GuideActivity.class);
                startActivity(intent);
            } else {
                // 如果不是第一次启动则进入主页
                Intent intent = new Intent(IndexActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }
            finish();
        };
    };

    // 判断是否是第一次启动程序 利用 SharedPreferences 将数据保存在本地
    private boolean isFristRun() {
        // 实例化SharedPreferences对象（第一步）
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                "share", MODE_PRIVATE);
        // 实例化SharedPreferences.Editor对象（第二步）
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        Editor editor = sharedPreferences.edit();
        if (!isFirstRun) {
            return false;
        } else {
            // 保存数据 （第三步）
            editor.putBoolean("isFirstRun", false);
            // 提交当前数据 （第四步）
            editor.commit();
            return true;
        }
    }

    // 禁止返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();
    }
}
