package com.liuguilin.only;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

/**
 * 设置界面
 * Created by LGL on 2016/5/8.
 */
public class SettingActivity extends BaseActivity{

    private ListView mListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
    }
}
