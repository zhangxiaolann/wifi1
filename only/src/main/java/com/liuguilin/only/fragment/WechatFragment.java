package com.liuguilin.only.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.liuguilin.only.R;
import com.liuguilin.only.WebViewActivity;
import com.liuguilin.only.adapter.WechatAdapter;
import com.liuguilin.only.bean.WechatBean;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信精选
 * Created by LGL on 2016/5/4.
 */
public class WechatFragment extends android.support.v4.app.Fragment {

    //刷新时间
    public static final int REFRESH_DELAY = 4000;

    private ListView mListView;

    private List<WechatBean> mList = new ArrayList<WechatBean>();

    private WechatAdapter adapter;

    private List<String> urlList = new ArrayList<String>();

    private List<String> titleList = new ArrayList<String>();

    //下拉刷新
    private PullToRefreshView mPullToRefreshView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat, null);
        findView(view);
        return view;
    }

    /**
     * 初始化
     *
     * @param view
     */
    private void findView(View view) {

        mListView = (ListView) view.findViewById(R.id.list_view);

        getNews();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(getActivity(), WebViewActivity.class);
                Bundle b = new Bundle();
                b.putString("title", titleList.get(position));
                b.putString("url", urlList.get(position));
                i.putExtras(b);
                startActivity(i);
            }
        });


        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });
    }

    private void getNews() {

        // key
        String url = "http://v.juhe.cn/weixin/query?key=78f723dccf85aea324a3cf0daac97f35";

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 成功
                    @Override
                    public void onResponse(String json) {
                        // Log.i("json", json);
                        Volley_news(json);
                    }
                }, new Response.ErrorListener() {
            // 失败
            @Override
            public void onErrorResponse(VolleyError errorLog) {
            }
        });

        queue.add(request);
    }

    /**
     * 解析Json
     *
     * @param json
     */
    private void Volley_news(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonresult = jsonObject.getJSONObject("result");
            JSONArray jArray = jsonresult.getJSONArray("list");

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jb = (JSONObject) jArray.get(i);
                WechatBean bean = new WechatBean();
                bean.setTitle(jb.getString("title"));
                bean.setType(jb.getString("source"));
                bean.setUrl(jb.getString("firstImg"));
                mList.add(bean);

                urlList.add(jb.getString("url"));
                titleList.add(jb.getString("title"));
            }
            adapter = new WechatAdapter(getActivity(), mList);
            mListView.setAdapter(adapter);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
