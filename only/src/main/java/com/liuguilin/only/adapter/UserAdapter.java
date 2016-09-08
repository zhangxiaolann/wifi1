package com.liuguilin.only.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuguilin.only.R;

/**
 * 个人信息
 * Created by LGL on 2016/6/1.
 */
public class UserAdapter extends BaseAdapter {

    private String[] data = {"姓名：刘桂林", "性别：男", "生日：1995.10.05", "职业：Android工程师"};
    private Context mContext;
    private LayoutInflater inflater;

    public UserAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.user_item,null);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.iv.setBackgroundResource(R.mipmap.ic_launcher);
        viewHolder.tv.setText(data[position]);

        return convertView;
    }


    class ViewHolder{
        private ImageView iv;
        private TextView tv;
    }
}
