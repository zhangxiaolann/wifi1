package fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wificonnect.qiezhi.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.file_item;
import tools.connect_tools;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment1 extends Fragment {

    ListView listView;
    private File fileDirectory;
    private File[] listFile;//文件数组
    List<file_item> list = new ArrayList<>();//集合放文件对象
    private myadapter myadapter;
    private FileInputStream fileInputStream;
    private ProgressDialog progress;

    public fragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment1, container, false);//引入布局

        listView = (ListView) view.findViewById(R.id.listview);

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            //获取sd卡的根目录
            fileDirectory = Environment.getExternalStorageDirectory();
            Log.e("eeee",fileDirectory.toString());
            show_file_list(fileDirectory);
        }
//listview的点击事件点击发送
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    File file = fileDirectory.getParentFile();
                    System.out.println(file.toString());
                    if (file.toString().equals("/storage/emulated")) {
                        Toast.makeText(getContext(), "已经到根目录", Toast.LENGTH_SHORT).show();
                    } else {

                        fileDirectory = file;
                        show_file_list(fileDirectory);
                    }
                } else {
                    fileDirectory = listFile[position - 1];
                    show_file_list(fileDirectory);
                }

            }
        });

        //长按事件删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("简单对话框");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("确定删除？？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = listFile[position];
                        file.delete();
                        show_file_list(fileDirectory);

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "点击了取消", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create();
                builder.show();
                return true;
            }
        });
        return view;
    }

    //查找当前文件路径的子文件夹，并显示在listview列表上
    public void show_file_list(File file) {

        //判断当前文件目录下是否有子文件

        if (file.isDirectory()) {
            listFile = file.listFiles();// 返回一个抽象路径名数组，这些路径名表示此抽象路径名表示的目录中的文件
            if (list.size() > 0) {
                list.clear();
            }
            for (int i = -1; i < listFile.length; i++) {
                file_item item = new file_item();//实例化对象

                if (i == -1) {
                    item.setDrawable_int(R.drawable.ll);
                    item.setFile_name("...");
                    list.add(item);
                } else {
                    if (listFile[i].isDirectory()) {
                        //是文件时设置图标
                        item.setDrawable_int(R.drawable.ll);
                    } else {
                        String name = listFile[i].getName();

                        if (name.indexOf(".") == -1) {
                            item.setDrawable_int(R.drawable.le);
                        } else {
                            String[] names = name.split("\\.");
                            if (names[1].equals("txt")) {
                                item.setDrawable_int(R.drawable.m_);
                            } else if (names[1].equals("zip")) {
                                item.setDrawable_int(R.drawable.me);
                            } else if (names[1].equals("jpg")) {
                                item.setDrawable_int(R.drawable.lq);
                            } else if (names[1].equals("mp4")) {
                                item.setDrawable_int(R.drawable.lo);
                            } else if (names[1].equals("mp3")) {
                                item.setDrawable_int(R.drawable.mv);
                            } else {
                                item.setDrawable_int(R.drawable.le);
                            }
                        }


                    }
                    item.setFile_name(listFile[i].getName());
                    list.add(item);

                }
            }

            if (myadapter == null) {

                myadapter = new myadapter(list, getContext());
                listView.setAdapter(myadapter);
            } else {
                myadapter.notifyDataSetChanged();
            }
        } else {
            SharedPreferences pref = getActivity().getSharedPreferences("judge", getActivity().MODE_PRIVATE);
            boolean connected = pref.getBoolean("connected", false);
            if (!connected) {
                Toast.makeText(getContext(), "请先连接设备", Toast.LENGTH_SHORT).show();
            } else {


                /////////传输//////传输/////传输////传输/传输/////传输////传输//
                progress = new ProgressDialog(getContext());
                progress.setTitle("任务完成度");

                try {

                    fileInputStream = new FileInputStream(fileDirectory);
                    progress.setMessage(fileDirectory.getName());
                    progress.setMax(fileInputStream.available()/1024/1024);
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
        }
    }

    /*
    开启线程发送文件
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                String response = connect_tools.SendFile(getContext(), fileDirectory.getName(),
                        fileDirectory.getPath(),
                        connect_tools.getip(getActivity()),
                         fileInputStream.available(), progress);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.toString());
            }
        }
    };





    /**
     * listview的适配器
     */
    class myadapter extends BaseAdapter {
        List<file_item> list;
        ViewHolder viewHolder;
        private LayoutInflater mLayoutInflater;

        public myadapter(List<file_item> list, Context context) {
            this.list = list;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.frag1_file, null);//填充view对象
                viewHolder.textView = (TextView) convertView
                        .findViewById(R.id.textView);
                viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.LinearLayout_list);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);

                convertView.setTag(viewHolder);
            } else {

                viewHolder = (ViewHolder) convertView.getTag();
            }
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.list_anim);
            viewHolder.layout.setAnimation(animation);

            file_item item = list.get(position);
            viewHolder.textView.setText(item.getFile_name());
            viewHolder.imageView.setImageResource(item.getDrawable_int());
            return convertView;
        }

        public class ViewHolder {
            LinearLayout layout;
            ImageView imageView;
            TextView textView;
        }
    }



}
