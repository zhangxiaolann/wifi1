package com.liuguilin.only.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liuguilin.only.R;
import com.liuguilin.only.bean.GithubBean;

import java.util.List;

/**
 * github项目列表
 * Created by LGL on 2016/5/8.
 */
public class GithubAdapter extends BaseAdapter {

    private Context mContext;
    private List<GithubBean> mList;

    private LayoutInflater mInflater;
    private GithubBean bean;

    //字体
    private Typeface fontFace;


    //构造方法
    public GithubAdapter(Context mContext, List<GithubBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fontFace = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/YYG.TTF");
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHodler;

        if (convertView == null) {

            viewHodler = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_github, null);
            viewHodler.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            viewHodler.tv_web = (TextView) convertView
                    .findViewById(R.id.tv_web);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHolder) convertView.getTag();
        }

        bean = mList.get(position);
        viewHodler.tv_name.setText(bean.getName());
        viewHodler.tv_name.setTypeface(fontFace);
        viewHodler.tv_web.setText(bean.getWebAddress());
        viewHodler.tv_web.setTypeface(fontFace);

        return convertView;
    }

    class ViewHolder {
        private TextView tv_name;
        private TextView tv_web;
    }
}
