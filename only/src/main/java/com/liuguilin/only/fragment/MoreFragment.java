package com.liuguilin.only.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liuguilin.only.R;
import com.liuguilin.only.view.CircularMenuView;

/**
 * 更多精彩
 * Created by LGL on 2016/5/4.
 */
public class MoreFragment extends android.support.v4.app.Fragment implements CircularMenuView.OnRorateListenser {

    //圆形菜单
    private CircularMenuView mCircleMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, null);
        findView(view);
        return view;
    }

    /**
     * 初始化
     *
     * @param view
     */
    private void findView(View view) {
        mCircleMenu = (CircularMenuView) view.findViewById(R.id.mCircleMenu);
        mCircleMenu.setOnRorateListenser(this);

    }

    @Override
    public void onCircleInnerLinstener(CircularMenuView.PointCirCleInner pointCirCle) {
        Toast.makeText(getActivity(), "当前点击是:" + pointCirCle.cirCle_name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCircleOuterLinstener(CircularMenuView.PointCirCleOut pointCirCle) {
        Toast.makeText(getActivity(), "当前点击是:" + pointCirCle.cirCle_name, Toast.LENGTH_SHORT).show();
    }
}
