package fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import bean.WeChartInfo;
import tools.BitmapCache;
import tools.Juhedate;
import tools.LoadListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Eye_child2_frag extends Fragment implements LoadListView.IloadInterface, SwipeRefreshLayout.OnRefreshListener {

    int toppager = 1;
    final String MAXSIZE = "10";

    private View view;
    private LoadListView loadlistview;
    MyAdaptre myAdaptre;
    private List<WeChartInfo> alist = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新
    private Object getwechartlist;//下载的数据


    public Eye_child2_frag() {
        // Required empty public constructor
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
                toppager++;
                getwechartlist = Juhedate.getwechartlist(toppager + "", MAXSIZE);
                try {
                    JSONObject jsonObject = new JSONObject(getwechartlist.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String firstImg = object.getString("firstImg");
                        String id = object.getString("id");
                        String source = object.getString("source");
                        String title = object.getString("title");
                        String url = object.getString("url");
                        String mark = object.getString("mark");
                        WeChartInfo weChartInfo = new WeChartInfo(firstImg, id, source, url, title, mark);
                        alist.add(weChartInfo);
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

                getwechartlist = Juhedate.getwechartlist(1 + "", MAXSIZE);
                if (myAdaptre != null) {
                    alist.clear();
                }
                try {
                    JSONObject jsonObject = new JSONObject(getwechartlist.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String firstImg = object.getString("firstImg");
                        String id = object.getString("id");
                        String source = object.getString("source");
                        String title = object.getString("title");
                        String url = object.getString("url");
                        String mark = object.getString("mark");
                        WeChartInfo weChartInfo = new WeChartInfo(firstImg, id, source, url, title, mark);
                        alist.add(i, weChartInfo);
                    }
                    handler.sendEmptyMessage(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hander_info(msg);

        }
    };

    /**
     * hander内的方法
     *
     * @param msg
     */
    public void hander_info(Message msg) {
        if (msg.what == 1) {
            if (myAdaptre == null) {
                loadlistview = (LoadListView) view.findViewById(R.id.loadlistview);
                loadlistview.setInterface(Eye_child2_frag.this);
                myAdaptre = new MyAdaptre(alist, getContext());
                loadlistview.setAdapter(myAdaptre);
                swipeRefreshLayout.setRefreshing(false);

                loadlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        WeChartInfo weChartInfo = alist.get(position);
                        String weChartInfoUrl = weChartInfo.getUrl();
                        Intent intent = new Intent(getContext(), Webview_activity.class);
                        intent.putExtra("url", weChartInfoUrl);
                        intent.putExtra("name", "微信精选");
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.eye_child2_frag, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip);
        swipeRefreshLayout.setOnRefreshListener(Eye_child2_frag.this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        json_re();
        return view;
    }


    /**
     * 第一次加载数据
     */
    public void json_re() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                getwechartlist = Juhedate.getwechartlist(toppager + "", MAXSIZE);
                try {
                    JSONObject jsonObject = new JSONObject(getwechartlist.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String firstImg = object.getString("firstImg");
                        String id = object.getString("id");
                        String source = object.getString("source");
                        String title = object.getString("title");
                        String url = object.getString("url");
                        String mark = object.getString("mark");
                        WeChartInfo weChartInfo = new WeChartInfo(firstImg, id, source, url, title, mark);
                        alist.add(weChartInfo);
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

        private List<WeChartInfo> alist;
        private ViewHolder holder;
        private RequestQueue queue;
        private ImageLoader imageLoader;
        private LayoutInflater inflater;

        public MyAdaptre(List<WeChartInfo> alist, Context context) {
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
                holder.llnear_anima = (LinearLayout) view.findViewById(R.id.llnear_anima);
                holder.tv1 = (TextView) view.findViewById(R.id.tv1);
                holder.tv2 = (TextView) view.findViewById(R.id.tv2);
                holder.tv3 = (TextView) view.findViewById(R.id.tv3);
                holder.iv = (NetworkImageView) view.findViewById(R.id.iv);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.list_anim);
            holder.llnear_anima.setAnimation(animation);
            String url = alist.get(i).getFirstImg();
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
            String source = alist.get(i).getSource();
            holder.tv2.setText(source);
            holder.tv3.setVisibility(View.GONE);
            return view;
        }
    }

    public class ViewHolder {
        TextView tv1, tv2, tv3;
        NetworkImageView iv;
        LinearLayout llnear_anima;
    }

}
