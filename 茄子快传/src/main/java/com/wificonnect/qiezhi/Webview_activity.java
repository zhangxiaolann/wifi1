package com.wificonnect.qiezhi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import tools.LJWebView;

public class Webview_activity extends Activity {


    private LJWebView mLJWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mLJWebView =  (LJWebView) findViewById(R.id.web);
        View view = findViewById(R.id.Web_include);
        Button button = (Button) view.findViewById(R.id.back_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        TextView textView = (TextView) view.findViewById(R.id.title_tv);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        textView.setText(name);
        String url = intent.getStringExtra("url");

        mLJWebView.setBarHeight(8);
        mLJWebView.setClickable(true);
        mLJWebView.setUseWideViewPort(true);
        mLJWebView.setSupportZoom(true);
        mLJWebView.setBuiltInZoomControls(true);
        mLJWebView.setJavaScriptEnabled(true);
        mLJWebView.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mLJWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                view.loadUrl(url);
                return true;
            }
        });

        mLJWebView.loadUrl(url);
    }


    float x, x1;
    float y, y1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();

                break;
            case MotionEvent.ACTION_UP:
                x1 = event.getX();
                y1 = event.getY();
                if (x1 - x > 100 && Math.abs(y1 - y) < 300) {
                    System.out.println("向右滑动");
                    finish();

                }
//                else if (x - x1 > 100 && Math.abs(y1 - y) < 300) {
//                    System.out.println("向左滑动");
//
//                }
                break;
        }


        return false;
    }

    @Override
    public void onBackPressed() {

        finish();

    }
}
