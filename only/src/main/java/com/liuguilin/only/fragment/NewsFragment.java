package com.liuguilin.only.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuguilin.only.R;
import com.liuguilin.only.adapter.ContentFragmentAdapter;
import com.liuguilin.only.transformer.VerticalStackTransformer;
import com.liuguilin.only.view.OrientedViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 今日头条
 * Created by LGL on 2016/5/4.
 */
public class NewsFragment extends android.support.v4.app.Fragment {

    private OrientedViewPager mOrientedViewPager;
    private ContentFragmentAdapter mContentFragmentAdapter;
    private List<android.support.v4.app.Fragment> mFragments = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, null);
        findView(view);
        return view;
    }

    /**
     * 初始化view
     * @param view
     */
    private void findView(View view) {
        mOrientedViewPager = (OrientedViewPager) view.findViewById(R.id.view_pager);

        //制造数据
        for (int i = 0; i < 10; i++) {
            mFragments.add(CardFragment.newInstance(i + 1));
        }

        mContentFragmentAdapter = new ContentFragmentAdapter(getFragmentManager(),mFragments);
        //设置viewpager的方向为竖直
        mOrientedViewPager.setOrientation(OrientedViewPager.Orientation.VERTICAL);
        //设置limit
        mOrientedViewPager.setOffscreenPageLimit(4);
        //设置transformer
        mOrientedViewPager.setPageTransformer(true, new VerticalStackTransformer(getActivity()));
        mOrientedViewPager.setAdapter(mContentFragmentAdapter);

    }

}
