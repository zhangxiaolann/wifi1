package com.liuguilin.only.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.liuguilin.only.R;
import com.liuguilin.only.WebViewActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的博客
 * Created by LGL on 2016/5/4.
 */
public class BlogFragment extends android.support.v4.app.Fragment {

    private ListView mBlogListView;
    //adapter
    private ArrayAdapter<String> adapter;
    //标题
    private List<String> titleText = new ArrayList<>();
    //链接
    private List<String> blogUrl = new ArrayList<>();
    //博客链接
    private String Url = "http://blog.csdn.net/qq_26787115/article/details/50485166";
    //下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;

    public static final int HTMLGET = 10;
    public static final int UPDATE_ADAPTER = 11;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HTMLGET:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //子线程解析
                            try {
                                //文档类
                                Document doc = Jsoup.connect(Url).get();
                                Elements el = doc.select("div.markdown_views>blockquote>p").select("a");
                                for (Element s : el) {
                                    titleText.add(s.text());
                                    blogUrl.add(s.attr("href"));
                                }
                                handler.sendEmptyMessage(UPDATE_ADAPTER);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case UPDATE_ADAPTER:
                    //初始化Adapter
                    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, titleText);
                    mBlogListView.setAdapter(adapter);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, null);
        findView(view);
        return view;
    }

    /**
     * 初始化
     *
     * @param view
     */
    private void findView(View view) {
        mBlogListView = (ListView) view.findViewById(R.id.mBlogListView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        //解析
        handler.sendEmptyMessage(HTMLGET);

        //设置下拉刷新的颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        //下拉监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //关闭刷新
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        //点击事件
        mBlogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), WebViewActivity.class);
                Bundle b = new Bundle();
                b.putString("title", titleText.get(position));
                b.putString("url", blogUrl.get(position));
                i.putExtras(b);
                startActivity(i);
            }
        });
    }
}
