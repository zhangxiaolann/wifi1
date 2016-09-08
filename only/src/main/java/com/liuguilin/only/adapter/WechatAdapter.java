package com.liuguilin.only.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.liuguilin.only.R;
import com.liuguilin.only.bean.WechatBean;
import com.liuguilin.only.cache.LruImageCache;
import com.liuguilin.only.utils.L;
import com.liuguilin.only.utils.PicassoUtils;

import java.util.List;

public class WechatAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater inflater;
	private List<WechatBean> mList;

	private NetworkImageView networkImageView;

	private RequestQueue mQueue;

	private WechatBean bean;

	public WechatAdapter(Context mContext, List<WechatBean> mList) {
		this.mContext = mContext;
		this.mList = mList;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoldwe vHoldwe = null;

		if (convertView == null) {
			vHoldwe = new ViewHoldwe();
			convertView = inflater.inflate(R.layout.wechatist_item, null);
			vHoldwe.iv_url = (ImageView) convertView
					.findViewById(R.id.iv_url);
			vHoldwe.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			vHoldwe.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
			convertView.setTag(vHoldwe);
		} else {
			vHoldwe = (ViewHoldwe) convertView.getTag();
		}

		bean = mList.get(position);
		//使用Picasso解析
		L.i("-----------"+bean.getUrl());
		if(!TextUtils.isEmpty(bean.getUrl())){
			PicassoUtils.loadImageViewSize(mContext,bean.getUrl(),180,100,vHoldwe.iv_url);
		}else{
			vHoldwe.iv_url.setBackgroundResource(R.drawable.iv_error);
		}

		vHoldwe.tv_title.setText(bean.getTitle());
		vHoldwe.tv_type.setText(bean.getType());

		return convertView;
	}

	class ViewHoldwe {
		private ImageView iv_url;
		private TextView tv_title;
		private TextView tv_type;
	}

}
