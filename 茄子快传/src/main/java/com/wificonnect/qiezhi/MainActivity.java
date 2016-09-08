package com.wificonnect.qiezhi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;
import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fragment.Eye_fragment;
import fragment.liulang_fragment;
import fragment.main_fragment;
import fragment.setup_fragment;


public class MainActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    FrameLayout framelayout;
    LinearLayout main_ll;
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    int imageResId[] = {R.drawable.eye_tab, R.drawable.home_tab, R.drawable.quanzi_tab};
    private String[] mTitles = new String[]{"发现", "传输", "记录"};
    private List<Fragment> list_fragment;                                //定义要装fragment的列表

    private NavigationView navigationView;

    ///fragment
    setup_fragment setup_frag;
    main_fragment main_frag;
    liulang_fragment liulang_frag;
    Eye_fragment eye_frag;


    Handler handler = new Handler();
    private ImageView userLogo;
    private Tencent tencent;
    private TextView login;

    //初始化
    private void assignViews() {
        framelayout = (FrameLayout) findViewById(R.id.framelayout);
        main_ll = (LinearLayout) findViewById(R.id.main_ll);

        //实例化fragment
        setup_frag = new setup_fragment();
        main_frag = new main_fragment();
        liulang_frag = new liulang_fragment();
        eye_frag = new Eye_fragment();

        //侧滑
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = getLayoutInflater().inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(headerView);
        userLogo = (ImageView) headerView.findViewById(R.id.imageView1);
        login = (TextView) headerView.findViewById(R.id.login);
        userLogo.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //QQ登录
        tencent = Tencent.createInstance("1105401765", getApplicationContext());
        assignViews();
        initViews();

        //第一次进入应用将connected传入false，当连接到设备就会变成true，才可以发送数据
        SharedPreferences.Editor editor = getSharedPreferences("judge",
                MODE_PRIVATE).edit();
        editor.putBoolean("connected", false);
        editor.commit();

    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();      //调用双击退出函数
        }
        return false;
    }

    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    //侧滑的点击事件
    @SuppressWarnings("h")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            main_ll.setVisibility(View.VISIBLE);
            framelayout.setVisibility(View.GONE);

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(MainActivity.this, set_up_activity.class));
                }
            }.start();


        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //replacefragment
    public void initFragment(Fragment fragment) {
        FragmentManager f = getSupportFragmentManager();
        FragmentTransaction transaction = f.beginTransaction();
        transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.in_from_left, R.anim.out_from_right);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView1://用户登录
                tencent.login(MainActivity.this, "all", listener);//登录,第二个参数代表权限
                break;
        }
    }

    private IUiListener listener = new IUiListener() {
        @Override
        public void onComplete(Object response) {
            try {
                JSONObject object = new JSONObject(response + "");
                int ret = object.getInt("ret");
                if (ret == 0) {
                    //登录成功
//                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    String openID = object.getString("openid");
                    String accessToken = object.getString("access_token");
                    String expires = object.getString("expires_in");
                    tencent.setOpenId(openID);
                    tencent.setAccessToken(accessToken, expires);
                    Log.e("TAG", openID + "--" + accessToken + "--" + expires);
                    UserInfo userInfo = new UserInfo(MainActivity.this, tencent.getQQToken());//获取用户信息
                    userInfo.getUserInfo(new MyIUiListener());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    };

    private class MyIUiListener implements IUiListener {
        @Override
        public void onComplete(Object obj) {//得到用户信息
            try {
                JSONObject object = new JSONObject(obj + "");
//                String ret = object.getString("ret");
//                String is_lost = object.getString("is_lost");
                String nickname = object.getString("nickname");//网名
                login.setText(nickname);
//                String gender = object.getString("gender");
//                String province = object.getString("province");
//                String city = object.getString("city");
//                String figureurl = object.getString("figureurl");//头像地址
//                String figureurl_1 = object.getString("figureurl_1");//头像地址
//                String figureurl_2 = object.getString("figureurl_2");//头像地址
//                String figureurl_qq_1 = object.getString("figureurl_qq_1");//头像地址
                String figureurl_qq_2 = object.getString("figureurl_qq_2");//头像地址
                ImageOptions options = new ImageOptions.Builder()
                        .setSize(DensityUtil.dip2px(80), DensityUtil.dip2px(80))
                        .setFadeIn(true)//淡入效果
                        .setCircular(true)//展示为圆形
                        .build();
                x.image().bind(userLogo, figureurl_qq_2, options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //QQ登录回调
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, listener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initViews() {

        mTablayout = (TabLayout) findViewById(R.id.tabLayout_main);
        mViewPager = (ViewPager) findViewById(R.id.viewPager_main);
        list_fragment = new ArrayList<>();
        list_fragment.add(eye_frag);
        list_fragment.add(main_frag);
        list_fragment.add(liulang_frag);


        SampleFragmentPagerAdapter pagerAdapter =
                new SampleFragmentPagerAdapter(getSupportFragmentManager(), this);

        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(3);//状态保存不销毁viewpager状态


        mTablayout.setupWithViewPager(mViewPager);

        for (int i = 0; i < mTablayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTablayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }

        mViewPager.setCurrentItem(1);
        mTablayout.getTabAt(1).getCustomView().setSelected(true);


    }


    public class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter {
        private Context context;


        public View getTabView(int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.news_title);
            tv.setText(mTitles[position]);
            ImageView img = (ImageView) v.findViewById(R.id.imageView);
            img.setImageResource(imageResId[position]);
            return v;
        }

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return list_fragment.size();
        }

        @Override
        public Fragment getItem(int position) {
            return list_fragment.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}

