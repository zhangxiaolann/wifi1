package com.liuguilin.only;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguilin.only.fragment.BlogFragment;
import com.liuguilin.only.fragment.GirlFragment;
import com.liuguilin.only.fragment.GithubFragment;
import com.liuguilin.only.fragment.MoreFragment;
import com.liuguilin.only.fragment.NewsFragment;
import com.liuguilin.only.fragment.UserFragment;
import com.liuguilin.only.fragment.WechatFragment;
import com.liuguilin.only.utils.FunctionUtils;
import com.liuguilin.only.utils.NetWorkUtils;
import com.liuguilin.only.view.CustomDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //城市标记
    public static final String KEY_PICKED_CITY = "picked_city";
    //ToolsBar
    private Toolbar toolbar;
    //HeaderView
    private View headerView;
    //侧滑菜单
    private DrawerLayout drawer;
    //分享Dialog
    private CustomDialog dialog_share, dialog_finish;
    private LinearLayout cancel_share, qq_firend_share, qq_qzone_share, wechat_share, wechatmoments_share, line_share,
            img_share, erweima_share, other_share;
    //窗口
    private PopupWindow pop;
    //退出标记
    private long exitTime = 0;

    private ImageView mIvIcon1;
    private ImageView mIvIcon2;
    private ImageView mIvIcon3;
    private ImageView mIvIcon4;
    private ImageView mIvIcon5;
    private FloatingActionButton mIvIcon6;

    //popuwindow
    private TextView mRadar;    //雷达
    private TextView mScan;     //扫一扫
    private TextView mWebsite;  //访问官网
    private TextView mDream;    //关于梦

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isNetWork();
        initView();

    }

    /**
     * 初始化控件
     */
    private void initView() {

        //跳转头条新闻
        initPagerContent(new NewsFragment());

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //头部
        headerView = navigationView.getHeaderView(0);
        headerView.findViewById(R.id.iv_circle).setOnClickListener(this);
        headerView.findViewById(R.id.tv_github).setOnClickListener(this);


        //初始化图片
        mIvIcon1 = (ImageView) findViewById(R.id.iv_icon1);
        mIvIcon2 = (ImageView) findViewById(R.id.iv_icon2);
        mIvIcon3 = (ImageView) findViewById(R.id.iv_icon3);
        mIvIcon4 = (ImageView) findViewById(R.id.iv_icon4);
        mIvIcon5 = (ImageView) findViewById(R.id.iv_icon5);
        mIvIcon6 = (FloatingActionButton) findViewById(R.id.iv_icon6);

        mIvIcon1.setOnClickListener(this);
        mIvIcon2.setOnClickListener(this);
        mIvIcon3.setOnClickListener(this);
        mIvIcon4.setOnClickListener(this);
        mIvIcon5.setOnClickListener(this);
        mIvIcon6.setOnClickListener(this);

        showImage(false);



    }

    /**
     * 是否显示菜单
     *
     * @param isShow
     */
    private void showImage(boolean isShow) {
        if (isShow) {
            mIvIcon1.setVisibility(View.VISIBLE);
            mIvIcon2.setVisibility(View.VISIBLE);
            mIvIcon3.setVisibility(View.VISIBLE);
            mIvIcon4.setVisibility(View.VISIBLE);
            mIvIcon5.setVisibility(View.VISIBLE);
        } else {
            mIvIcon1.setVisibility(View.GONE);
            mIvIcon2.setVisibility(View.GONE);
            mIvIcon3.setVisibility(View.GONE);
            mIvIcon4.setVisibility(View.GONE);
            mIvIcon5.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //加载视图
            View view = getLayoutInflater().inflate(R.layout.pop_layout, null);
            mRadar = (TextView) view.findViewById(R.id.tv_leida);
            mScan = (TextView) view.findViewById(R.id.tv_saomiao);
            mWebsite = (TextView) view.findViewById(R.id.tv_guanwang);
            mDream = (TextView) view.findViewById(R.id.tv_about);

            mRadar.setOnClickListener(this);
            mScan.setOnClickListener(this);
            mWebsite.setOnClickListener(this);
            mDream.setOnClickListener(this);
            //实例化
            pop = new PopupWindow(view, 300, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, true);
            pop.setOutsideTouchable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop.dismiss();
                }
            });
            //类似switch功能
            if (pop.isShowing()) {
                pop.dismiss();
            } else {
                //显示
                pop.showAsDropDown(findViewById(R.id.action_settings));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_toutiao) {
            //跳转头条新闻
            initPagerContent(new NewsFragment());
        } else if (id == R.id.nav_wechat) {
            //跳转微信精选
            initPagerContent(new WechatFragment());
        } else if (id == R.id.nav_blog) {
            //跳转我的博客
            initPagerContent(new BlogFragment());
        } else if (id == R.id.nav_more) {
            //跳转更多精彩
            initPagerContent(new MoreFragment());
        } else if (id == R.id.nav_girl) {
            //跳转福利
            initPagerContent(new GirlFragment());
        } else if (id == R.id.nav_share) {
            //跳转分享
            showShareDialog();
        } else if (id == R.id.nav_setting) {
            //跳转设置
            startActivity(new Intent(this, SettingActivity.class));
        }
        //关闭侧滑动画
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 分享Dialog
     */
    private void showShareDialog() {
        dialog_share = new CustomDialog(this, 0, 0, R.layout.dialog_share, R.style.Theme_dialog, Gravity.BOTTOM, R.style.pop_anim_style);
        dialog_share.show();
        cancel_share = (LinearLayout) dialog_share.findViewById(R.id.cancel_share);//取消
        cancel_share.setOnClickListener(MainActivity.this);
        qq_firend_share = (LinearLayout) dialog_share.findViewById(R.id.qq_firend_share);//qq好友
        qq_firend_share.setOnClickListener(MainActivity.this);
        qq_qzone_share = (LinearLayout) dialog_share.findViewById(R.id.qq_qzone_share);//qq空间
        qq_qzone_share.setOnClickListener(MainActivity.this);
        wechat_share = (LinearLayout) dialog_share.findViewById(R.id.wechat_share);//微信
        wechat_share.setOnClickListener(MainActivity.this);
        wechatmoments_share = (LinearLayout) dialog_share.findViewById(R.id.wechatmoments_share);//微信朋友圈
        wechatmoments_share.setOnClickListener(MainActivity.this);
        line_share = (LinearLayout) dialog_share.findViewById(R.id.line_share);//复制链接
        line_share.setOnClickListener(MainActivity.this);
        img_share = (LinearLayout) dialog_share.findViewById(R.id.img_share);//图片分享
        img_share.setOnClickListener(MainActivity.this);
        erweima_share = (LinearLayout) dialog_share.findViewById(R.id.erweima_share);//分享二维码
        erweima_share.setOnClickListener(MainActivity.this);
        other_share = (LinearLayout) dialog_share.findViewById(R.id.other_share);//其他分享
        other_share.setOnClickListener(MainActivity.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //跳转到我的信息
            case R.id.iv_circle:
                initPagerContent(new UserFragment());
                //关闭侧滑动画
                drawer.closeDrawer(GravityCompat.START);
                break;
            //跳转到我的Github
            case R.id.tv_github:
                //关闭侧滑动画
                drawer.closeDrawer(GravityCompat.START);
                initPagerContent(new GithubFragment());
                break;
            case R.id.iv_icon1:
                Toast.makeText(this, "icon1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_icon2:
                Toast.makeText(this, "icon2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_icon3:
                Toast.makeText(this, "icon3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_icon4:
                Toast.makeText(this, "icon4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_icon5:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.iv_icon6:
                if (mIvIcon1.getVisibility() == View.GONE) {
                    showImage(true);
                    //点击最外层icon，展开icon动画
                    showIcon();
                } else {
                    showImage(false);
                }

                break;
            case R.id.cancel_share://取消分享
                dialog_share.dismiss();
                break;
            case R.id.qq_firend_share://qq好友分享
                Toast.makeText(getApplicationContext(), "qq好友分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.qq_qzone_share://qq空间分享
                Toast.makeText(getApplicationContext(), "qq空间分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.wechat_share://微信分享
                Toast.makeText(getApplicationContext(), "微信分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.wechatmoments_share://微信朋友圈分享
                Toast.makeText(getApplicationContext(), "微信朋友圈分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.line_share://复制链接
                Toast.makeText(getApplicationContext(), "复制链接", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_share://图片分享
                Toast.makeText(getApplicationContext(), "图片分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.erweima_share://分享二维码
                Toast.makeText(getApplicationContext(), "分享二维码", Toast.LENGTH_SHORT).show();
                break;
            case R.id.other_share://其他分享
                Toast.makeText(getApplicationContext(), "其他分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_leida: //雷达
                Toast.makeText(getApplicationContext(), "雷达", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_saomiao:   //扫一扫
                Toast.makeText(getApplicationContext(), "扫一扫", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_guanwang:  //访问官网
                Toast.makeText(getApplicationContext(), "访问官网", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_about: //关于梦
                Toast.makeText(getApplicationContext(), "关于梦", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    /**
     * 动画实现，因为除了沿X,Y轴的图标，另外3个都有角度，所有，要有三角函数计算
     * 使图标位移距离相等，实现扇形效果
     */
    @SuppressLint("NewApi")
    private void showIcon() {

        //设置动画时间
        int duration = 500;
        //动画距离,屏幕宽度的60%
        float distance = getScreenWidth() * 0.6f;
        //相邻ImageView运动角度式22.5度
        float angle1 = (float) (22.5f * Math.PI / 180);
        float angle2 = (float) (45f * Math.PI / 180);
        float angle3 = (float) (67.5f * Math.PI / 180);

        //icon1
        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("TranslationX", 0f, distance);
        //icon2
        PropertyValuesHolder p21 = PropertyValuesHolder.ofFloat("TranslationX", 0f, (float) (distance * Math.cos(angle1)));
        PropertyValuesHolder p22 = PropertyValuesHolder.ofFloat("TranslationY", 0f, -(float) (distance * Math.sin(angle1)));
        //icon3
        PropertyValuesHolder p31 = PropertyValuesHolder.ofFloat("TranslationX", 0f, (float) (distance * Math.cos(angle2)));
        PropertyValuesHolder p32 = PropertyValuesHolder.ofFloat("TranslationY", 0f, -(float) (distance * Math.sin(angle2)));
        //icon4
        PropertyValuesHolder p41 = PropertyValuesHolder.ofFloat("TranslationX", 0f, (float) (distance * Math.cos(angle3)));
        PropertyValuesHolder p42 = PropertyValuesHolder.ofFloat("TranslationY", 0f, -(float) (distance * Math.sin(angle3)));
        //icon5
        PropertyValuesHolder p5 = PropertyValuesHolder.ofFloat("TranslationY", 0f, -distance);

        ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(mIvIcon1, p1).setDuration(duration);
        ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(mIvIcon2, p21, p22).setDuration(duration);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(mIvIcon3, p31, p32).setDuration(duration);
        ObjectAnimator animator4 = ObjectAnimator.ofPropertyValuesHolder(mIvIcon4, p41, p42).setDuration(duration);
        ObjectAnimator animator5 = ObjectAnimator.ofPropertyValuesHolder(mIvIcon5, p5).setDuration(duration);

        //添加自由落体效果插值器
        animator1.setInterpolator(new BounceInterpolator());
        animator2.setInterpolator(new BounceInterpolator());
        animator3.setInterpolator(new BounceInterpolator());
        animator4.setInterpolator(new BounceInterpolator());
        animator5.setInterpolator(new BounceInterpolator());

        //启动动画
        animator1.start();
        animator2.start();
        animator3.start();
        animator4.start();
        animator5.start();
    }


    /**
     * 竖屏时获取屏幕宽度，横屏时，获取高度
     *
     * @return
     */
    public int getScreenWidth() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int x = outMetrics.widthPixels;
        int y = outMetrics.heightPixels;
        return x > y ? y : x;
    }

    /**
     * 加载fragment
     *
     * @param fragment
     */
    private void initPagerContent(Fragment fragment) {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        //会话
        android.support.v4.app.FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.myContent, fragment);
        ft.commit();
    }

    /**
     * 监听按钮
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //如果按退出
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                exitAPP();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 退出应用
     */
    private void exitAPP() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一次退出Only", Toast.LENGTH_LONG).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }

    }

    /**
     * 判断网络是够连接
     */
    private void isNetWork() {
        if (!NetWorkUtils.isConnected(this)) {
            new AlertDialog.Builder(this).setTitle(getString(R.string.network_connection_error))
                    .setMessage(getString(R.string.is_go_netwotk_setting)).setPositiveButton(getString(R.string.intent_setting), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FunctionUtils.toSettingNetWork(MainActivity.this);
                }
            }).setNegativeButton(getString(R.string.finish), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            }).show();
        }
    }
}
