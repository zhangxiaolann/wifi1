package com.wificonnect.qiezhi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.Find_tab_Adapter;
import fragment.fragment1;
import fragment.fragment2;
import fragment.fragment3;
import fragment.fragment4;
import fragment.fragment5;
import wifi.WifiLib;
import wifi.WifiLibInitializer;

/*
这个页面是发送页面的主要的activity里面放fragment布局
 */
public class send_activity extends FragmentActivity {
    private TabLayout tabLayout;                            //定义TabLayout
    private ViewPager viewpager;                             //定义viewPager
    private FragmentStatePagerAdapter fAdapter;                               //定义adapter
    private List<Fragment> list_fragment;                                //定义要装fragment的列表
    private List<String> list_title;                                     //tab名称列表
    private fragment1 fragment1;
    private fragment2 fragment2;
    private fragment3 fragment3;
    private fragment4 fragment4;
    private fragment5 fragment5;
    WifiLib wifiLib;
    LayoutInflater Inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View view = findViewById(R.id.title);
        Button back_botton = (Button) view.findViewById(R.id.back_button);
        TextView title_lianjie = (TextView) view.findViewById(R.id.title_lianjie);
        title_lianjie.setVisibility(View.VISIBLE);
        title_lianjie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(send_activity.this, is_Sending.class));
            }
        });
        initControls();
        Inflater = LayoutInflater.from(this);
        back_botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_lianjie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(send_activity.this, is_Sending.class));

            }
        });
        WifiLibInitializer.init(this);
        wifiLib = WifiLib.getInstance();



        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(send_activity.this);
        builder.setTitle("传输小提示！！");
        builder.setIcon(R.drawable.lw);
        builder.setMessage("1.传输前请先连接设备\n2.对方设备显示已经连接即可传输\n3.单击可打开选择的文件\n4.长按可发送选择的文件");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        builder.create();
        builder.show();
    }

    @Override
    public void onBackPressed() {
        SharedPreferences pref = getSharedPreferences("judge", MODE_PRIVATE);
        boolean connected = pref.getBoolean("connected", false);
        if (!connected) {
            super.onBackPressed();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setIcon(R.drawable.lw);
            builder.setMessage("是否退出传输？？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    wifiLib.closeWifi();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create();
            builder.show();

        }

    }

    private void initControls() {

        tabLayout = (TabLayout) findViewById(R.id.TabLayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        //初始化各fragment
        fragment1 = new fragment1();
        fragment2 = new fragment2();
        fragment3 = new fragment3();
        fragment4 = new fragment4();
        fragment5 = new fragment5();


        //将fragment装进列表中
        list_fragment = new ArrayList<>();
        list_fragment.add(fragment1);
        list_fragment.add(fragment2);
        list_fragment.add(fragment3);
        list_fragment.add(fragment4);
        list_fragment.add(fragment5);


        viewpager.setOffscreenPageLimit(list_fragment.size());//状态保存不销毁viewpager状态

        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        list_title = new ArrayList<>();
        list_title.add("文件");
        list_title.add("视频");
        list_title.add("应用");
        list_title.add("图片");
        list_title.add("音乐");

        viewpager.setOffscreenPageLimit(list_fragment.size());//状态保存不销毁viewpager状态

        //设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(3)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(4)));


        fAdapter = new Find_tab_Adapter(getSupportFragmentManager(), list_fragment, list_title);

        //viewpager加载adapter
        viewpager.setAdapter(fAdapter);
        //tab_FindFragment_title.setViewPager(vp_FindFragment_pager);
        //TabLayout加载viewpager
        tabLayout.setupWithViewPager(viewpager);
        //tab_FindFragment_title.set
    }


}
