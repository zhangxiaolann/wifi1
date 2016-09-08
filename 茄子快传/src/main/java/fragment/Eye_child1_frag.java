package fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.wificonnect.qiezhi.R;
import com.wificonnect.qiezhi.Webview_activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bean.NewsBean;
import tools.BitmapCache;
import tools.Juhedate;
import tools.LoadListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Eye_child1_frag extends Fragment implements LoadListView.IloadInterface, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新
    private View view;
    List<NewsBean> list = new ArrayList<>();
    MyAdaptre myAdaptre;
    LoadListView loadlistview;
    int pager = 1;

    public Eye_child1_frag() {
        // Required empty public constructor
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (myAdaptre == null) {
                    loadlistview = (LoadListView) view.findViewById(R.id.loadlistview1);
                    loadlistview.setInterface(Eye_child1_frag.this);
                    myAdaptre = new MyAdaptre(list, getContext());
                    loadlistview.setAdapter(myAdaptre);
                    swipeRefreshLayout.setRefreshing(false);

                    loadlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            NewsBean newsBean = list.get(position);
                            String Url = newsBean.getUrl();
                            Intent intent = new Intent(getContext(), Webview_activity.class);
                            intent.putExtra("url", Url);
                            intent.putExtra("name", "科技在线");
                            startActivity(intent);
                        }
                    });
                } else {
                    myAdaptre.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);//下拉刷新控件
                    loadlistview.loadComplete();//隐藏底部布局
                }
            } else if (msg.what == 2) {

                swipeRefreshLayout.setRefreshing(false);//下拉刷新控件
                myAdaptre.notifyDataSetChanged();
            } else if (msg.what == 3) {
                loadlistview.loadComplete();//隐藏底部布局
                myAdaptre.notifyDataSetChanged();

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.eye_child1_frag, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip1);
        swipeRefreshLayout.setOnRefreshListener(Eye_child1_frag.this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        json_re();//加载数据
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    /**
     * 分页加载
     */
    @Override
    public void onLoad() {
        Log.e("TAG", "分页");
        new Thread(new Runnable() {
            @Override
            public void run() {
                pager++;
                Object getnews = Juhedate.getnews(pager + "");
                try {

                    JSONArray jsonArray = new JSONArray(getnews.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String ctime = object.getString("ctime");//发布时间
                        String description = object.getString("description");//来源
                        String url = object.getString("url");//新闻地址
                        String title = object.getString("title");//新闻标题
                        String picUrl = object.getString("picUrl");//图片地址
                        NewsBean newsBean = new NewsBean(ctime, title, description, url, picUrl);
                        list.add(newsBean);
                    }
                    handler.sendEmptyMessage(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        Log.e("TAG", "下拉刷新");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object getnews = Juhedate.getnews(1 + "");
                if (myAdaptre!=null){
                    list.clear();
                }
                try {
                    JSONArray jsonArray = new JSONArray(getnews.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String ctime = object.getString("ctime");//发布时间
                        String description = object.getString("description");//来源
                        String url = object.getString("url");//新闻地址
                        String title = object.getString("title");//新闻标题
                        String picUrl = object.getString("picUrl");//图片地址
                        NewsBean newsBean = new NewsBean(ctime, title, description, url, picUrl);
                        list.add(newsBean);
                    }
                    handler.sendEmptyMessage(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 第一次加载数据
     */
    public void json_re() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Object getnews = Juhedate.getnews("1");
                try {

                    JSONArray jsonArray = new JSONArray(getnews.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String ctime = object.getString("ctime");//发布时间
                        String description = object.getString("description");//来源
                        String url = object.getString("url");//新闻地址
                        String title = object.getString("title");//新闻标题
                        String picUrl = object.getString("picUrl");//图片地址
                        NewsBean newsBean = new NewsBean(ctime, title, description, url, picUrl);
                        list.add(newsBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message mesage = Message.obtain();
                mesage.what = 1;
                handler.sendMessage(mesage);

            }
        }).start();

    }

    /**
     * 下面是适配器
     */
    class MyAdaptre extends BaseAdapter {

        private List<NewsBean> alist;
        private ViewHolder holder;
        private RequestQueue queue;
        private ImageLoader imageLoader;
        private LayoutInflater inflater;

        public MyAdaptre(List<NewsBean> alist, Context context) {
            queue = Volley.newRequestQueue(context);
            imageLoader = new ImageLoader(queue, new BitmapCache());
            inflater = LayoutInflater.from(context);
            this.alist = alist;
        }

        @Override
        public int getCount() {
            return alist.size();
        }

        @Override
        public Object getItem(int position) {
            return alist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

//        public void OnDataChange(List<WeChartInfo> alist) {
//            this.alist = alist;
//            this.notifyDataSetChanged();
//        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.eye_chart_item, null);
                holder.tv1 = (TextView) view.findViewById(R.id.tv1);
                holder.llnear_anima= (LinearLayout) view.findViewById(R.id.llnear_anima);
                holder.tv2 = (TextView) view.findViewById(R.id.tv2);
                holder.tv3 = (TextView) view.findViewById(R.id.tv3);
                holder.iv = (NetworkImageView) view.findViewById(R.id.iv);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.list_anim);
            holder.llnear_anima.setAnimation(animation);
            String url = alist.get(i).getPicUrl();
            if (url != null && !url.equals("") && !url.equals("null")) {
//                holder.iv.setDefaultImageResId(R.mipmap.ic_launcher);
                holder.iv.setVisibility(View.VISIBLE);
                holder.iv.setErrorImageResId(R.mipmap.ic_launcher);
                holder.iv.setImageUrl(url, imageLoader);
            } else {
                holder.iv.setVisibility(View.GONE);
            }
            String title = alist.get(i).getTitle();
            holder.tv1.setText(title);
            String source = alist.get(i).getDescription();
            holder.tv2.setText(source);
            String ctime = alist.get(i).getCtime();
            holder.tv3.setText(ctime);
            return view;
        }
    }

    public class ViewHolder {
        TextView tv1, tv2, tv3;
        LinearLayout llnear_anima;
        NetworkImageView iv;
    }

}
