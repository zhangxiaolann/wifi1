package com.liuguilin.only.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.liuguilin.only.R;
import com.liuguilin.only.WebViewActivity;
import com.liuguilin.only.adapter.GithubAdapter;
import com.liuguilin.only.bean.GithubBean;
import com.liuguilin.only.view.ExplosionField;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的Github
 * Created by LGL on 2016/5/11.
 */
public class GithubFragment extends android.support.v4.app.Fragment {


    //列表
    private ListView mListView;
    //Adapter
    private GithubAdapter adapter;

    //数据
    private String[] name_data = {"Only", "Coding-Developer-Book"};
    private String[] web_data = {"https://github.com/LiuGuiLinAndroid/Only", "https://github.com/LiuGuiLinAndroid/Coding-Developer-Book"};

    //实体类
    private GithubBean bean;
    //装载数据
    private List<GithubBean> mList = new ArrayList<GithubBean>();
    // 实例化粒子动画
    private ExplosionField explosionField;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_github, null);
        findView(view);
        return view;
    }

    /**
     * 初始化
     *
     * @param view
     */
    private void findView(View view) {

        explosionField = new ExplosionField(getActivity());
        // 绑定哪个控件哪个控件就有效果，如果需要整个layout，只要绑定根布局的id即可
        explosionField.addListener(view.findViewById(R.id.iv_circle));

        mListView = (ListView) view.findViewById(R.id.mListView);
        for (int i = 0; i < name_data.length; i++) {
            bean = new GithubBean();
            bean.setName(name_data[i]);
            bean.setWebAddress(web_data[i]);
            mList.add(bean);
        }
        adapter = new GithubAdapter(getActivity(), mList);
        mListView.setAdapter(adapter);

        //点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), WebViewActivity.class);
                Bundle b = new Bundle();
                b.putString("title", name_data[position]);
                b.putString("url", web_data[position]);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }
}
