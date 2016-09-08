package fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wificonnect.qiezhi.R;
import com.wificonnect.qiezhi.filter_file_activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bean.liulang;

/**
 * A simple {@link Fragment} subclass.
 */
public class liulang_fragment extends Fragment {

    private final int img[] = new int[]{R.drawable.ng_style, R.drawable.ni_style, R.drawable.lo_style,
            R.drawable.nj_style, R.drawable.nh_style, R.drawable.nk_style};
    private final String name[] = new String[]{"应用", "图片", "音乐", "视频", "文档", "文件"};
    private View view;
    private GridView gridView;
    List<liulang> list;
    private TextView sendLiuliang;
    private TextView receiveLiuliang;

    private long receivesize;
    private long sendsize;
    private tools.circle_view circle_view;

    public liulang_fragment() {

    }


    public static String getPrintSize(long size) {
        size = size / 1024 / 1024;
        size = size * 100;
        return String.valueOf((size / 100)) + "."
                + String.valueOf((size % 100)) + "-MB";
    }//字节转换

    public int getnum(String name) {
        String[] split = name.split("-");
        double s = Double.parseDouble(split[0]);
        return (int) s;
    }

    /**
     * 初始化
     */
    private void assignViews() {
        gridView = (GridView) view.findViewById(R.id.liulan_grid);
        circle_view = (tools.circle_view) view.findViewById(R.id.circleview);
    }


    private boolean mHasLoadedOnce = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (this.isVisible()) {

            // we check that the fragment is becoming visible
            if (isVisibleToUser && !mHasLoadedOnce) {
                SharedPreferences pref = getActivity().getSharedPreferences("judge",
                        getActivity().MODE_PRIVATE);
                receivesize = pref.getLong("receivesize", 0);
                String re_Size = getPrintSize(receivesize);
                int re = getnum(re_Size);

                sendsize = pref.getLong("sendsize", 0);
                String se_Size = getPrintSize(sendsize);
                int se = getnum(se_Size);
                circle_view.setprogress(se, re);
                mHasLoadedOnce = true;
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.liulang_fragment, container, false);
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "shanchuan");
        if (!file.exists()) {
            file.mkdir();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assignViews();
        list = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {
            liulang liu = new liulang(img[i], name[i]);
            list.add(liu);
        }

        myadapter m = new myadapter();
        gridView.setAdapter(m);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), filter_file_activity.class);
                intent.putExtra("titile_name", name[position]);
                intent.putExtra("id", position);
                startActivity(intent);
            }
        });

        View title = getActivity().findViewById(R.id.liulan_title);
        Button button = (Button) title.findViewById(R.id.back_button);
        button.setVisibility(View.GONE);
        TextView textView = (TextView) title.findViewById(R.id.title_tv);
        textView.setText("传输记录");
    }

    /**
     * 适配器
     */
    private class myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();// 返回多少条数据
        }

        @Override
        public Object getItem(int arg0) {
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            // 创建布局解析器，解析布局文件
            viewHolder holder = null;
            if (arg1 == null) {
                holder = new viewHolder();
                arg1 = LayoutInflater.from(getActivity()).inflate(
                        R.layout.liu_icos, null);

                arg1.setTag(holder);
            } else {
                holder = (viewHolder) arg1.getTag();
            }
            // 找到布局里面的控件
            holder.icon = (ImageView) arg1.findViewById(R.id.img_liu);
            holder.des = (TextView) arg1.findViewById(R.id.name_liu);
//            holder.size = (TextView) arg1.findViewById(R.id.size_liu);

            holder.icon.setImageResource(list.get(arg0).getImg());
            holder.des.setText(list.get(arg0).getDes());
//            holder.size.setText(list.get(arg0).getSize());
            return arg1;
        }
    }

    class viewHolder {
        ImageView icon;
        TextView des;
//        TextView size;
    }
}
