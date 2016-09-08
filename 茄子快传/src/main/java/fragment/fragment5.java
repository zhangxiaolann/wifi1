package fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wificonnect.qiezhi.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import bean.Music1;
import tools.GetMusicInfo;
import tools.connect_tools;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment5 extends Fragment {
    private List<Music1> list = null;
    private ListView lv_ge;
    private FileInputStream fileInputStream;
    private ProgressDialog progress;
    private String path;
    private View view;
    private RelativeLayout relative_layout_fra5;

    public fragment5() {
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                // 实例化
                lv_ge.setVisibility(View.VISIBLE);
                relative_layout_fra5.setVisibility(View.GONE);
                myadapter adapter = new myadapter();
                // 绑定适配器
                lv_ge.setAdapter(adapter);
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment5, container, false);
        return view;
    }

    private boolean mHasLoadedOnce = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (this.isVisible()) {

            // we check that the fragment is becoming visible
            if (isVisibleToUser && !mHasLoadedOnce) {
                lv_ge = (ListView) view.findViewById(R.id.lv_ge);
                relative_layout_fra5 = (RelativeLayout) view.findViewById(R.id.relative_layout_fra5);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 获取手机里面的音乐信息
                        new GetMusicInfo(getActivity());
                        // 把所有的音乐信息赋给了list
                        list = GetMusicInfo.getAllMusic();
                        handler.sendEmptyMessage(1);
                    }
                }).start();
                lv_ge.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        SharedPreferences pref = getActivity().getSharedPreferences("judge", getActivity().MODE_PRIVATE);
                        boolean connected = pref.getBoolean("connected", false);
                        if (!connected) {
                            Toast.makeText(getContext(), "请先连接设备", Toast.LENGTH_SHORT).show();
                        } else {
                            Music1 music1 = list.get(position);
                            path = music1.path;
                            /////////传输//////传输/////传输////传输/传输/////传输////传输//
                            progress = new ProgressDialog(getContext());
                            progress.setTitle("任务完成度");
                            try {
                                fileInputStream = new FileInputStream(path);
                                progress.setMessage(getFileName(path));
                                progress.setMax(fileInputStream.available() / 1024 / 1024);
                                progress.setCancelable(true);//设置不能使用取消按钮
                                progress.setIndeterminate(false); // 设置对话框的进度条是否显示进度
                                progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                progress.show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Log.e("tag", "找不到文件");
                                Toast.makeText(getContext(), "找不到文件", Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("tag", "io异常");
                                progress.dismiss();
                                Toast.makeText(getContext(), "读取文件异常", Toast.LENGTH_SHORT).show();
                            }
                            new Thread(runnable).start();
                        }
                        return true;
                    }
                });
                lv_ge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Music1 music1 = list.get(position);
                        path = music1.path;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        File file = new File(path);
                        intent.setDataAndType(Uri.fromFile(file), "audio/mp3");
                        startActivity(intent);
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
            try {

                String response = connect_tools.SendFile(getContext(), getFileName(path), path,
                        connect_tools.getip(getActivity()),
                        fileInputStream.available(), progress);
                System.out.println("信息" + response);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.toString());
            }
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
            viewholder holder = null;
            if (arg1 == null) {
                holder = new viewholder();
                arg1 = LayoutInflater.from(getActivity()).inflate(
                        R.layout.frag5_music, null);

                arg1.setTag(holder);
            } else {
                holder = (viewholder) arg1.getTag();
            }
            // 找到布局里面的控件
            holder.title = (TextView) arg1.findViewById(R.id.title);
            holder.duration = (TextView) arg1.findViewById(R.id.duration);
//            holder.path = (TextView) arg1.findViewById(R.id.path);
            holder.size = (TextView) arg1.findViewById(R.id.size);

            int cc = Integer.parseInt(list.get(arg0).duration + "");
            int dd = Integer.parseInt(list.get(arg0).size + "");

            holder.title.setText(list.get(arg0).title);
            holder.duration.setText(cc / 1000 / 60 + ":" + cc / 1000 % 60);
//            holder.path.setText(list.get(arg0).path);
            holder.size.setText(dd / 1024 / 1024 + "MB");
            return arg1;
        }
    }

    class viewholder {
        TextView title;
        TextView duration;
        //        TextView path;
        TextView size;
    }

    private String getFileName(String url) {
        int index = url.lastIndexOf("/") + 1;
        return url.substring(index);

    }

}
