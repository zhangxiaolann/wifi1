package fragment;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wificonnect.qiezhi.R;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import tools.AppInfo;
import tools.AppInfoProvider;
import tools.connect_tools;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment3 extends Fragment {

    private GridView appview;
    private List<AppInfo> list = null;
    private int length;
    private ProgressDialog pb;
    String response;
    String filename;
    int position1;
    private View view;
    RelativeLayout relative_layout_fra3;


    public fragment3() {
        // Required empty public constructor
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                relative_layout_fra3.setVisibility(View.GONE);
                appview.setVisibility(View.VISIBLE);
                myadapter adapter = new myadapter();
                appview.setAdapter(adapter);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment3, container, false);
        return view;

    }

    private boolean mHasLoadedOnce = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (this.isVisible()) {

            // we check that the fragment is becoming visible
            if (isVisibleToUser && !mHasLoadedOnce) {
                appview = (GridView) view.findViewById(R.id.appview);
                relative_layout_fra3 = (RelativeLayout) view.findViewById(R.id.relative_layout_fra3);

                // async http request here
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new AppInfoProvider(getActivity());
                        list = AppInfoProvider.getAllApps();
                        handler.sendEmptyMessage(1);
                    }
                }).start();

                appview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        SharedPreferences pref = getActivity().getSharedPreferences("judge", getActivity().MODE_PRIVATE);
                        boolean connected = pref.getBoolean("connected", false);
                        if (!connected) {
                            Toast.makeText(getContext(), "请先连接设备", Toast.LENGTH_SHORT).show();
                        } else {
                            position1 = position;

                            filename = list.get(position).getAppName() + ".apk";
                            File file = new File(list.get(position).getPackagePath());
                            try {
                                FileInputStream fis = new FileInputStream(file);
                                length = fis.available();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            pb = new ProgressDialog(getActivity());
                            pb.setMax(length / 1024 / 1024);
                            // 设置对话框的标题
                            pb.setTitle("任务完成度");
                            // 设置对话框显示的内容
                            pb.setMessage(list.get(position).getAppName());
                            // 设置对话框不能用"取消"按钮关闭
                            pb.setCancelable(true);
                            // 设置对话框的进度条风格
                            pb.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            // 设置对话框的进度条是否显示进度
                            pb.setIndeterminate(false);
                            pb.show();
                            new Thread(runnable).start();
                        }
                        return false;
                    }
                });
                mHasLoadedOnce = true;
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            response = connect_tools.SendFile(getContext(), filename, list.get(position1).getPackagePath(),
                    connect_tools.getip(getActivity()),
                    length, pb);

        }
    };


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
                        R.layout.frag3_apps, null);
                arg1.setTag(holder);
            } else {
                holder = (viewHolder) arg1.getTag();
            }
            holder.icon = (ImageView) arg1.findViewById(R.id.icon);
            // 找到布局里面的控件
            holder.appName = (TextView) arg1.findViewById(R.id.appName);

            holder.icon.setImageDrawable(list.get(arg0).getIcon());
            holder.appName.setText(list.get(arg0).getAppName());
            // codesize.setText(list.get(arg0).getCodesize()+"");
            return arg1;
        }
    }

    class viewHolder {
        ImageView icon;
        TextView appName;
    }

}
