package fragment;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wificonnect.qiezhi.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.video_item;
import tools.connect_tools;

;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment2 extends Fragment {
    public List<video_item> list = new ArrayList<>();
    private ListView lv_movie;
    private FileInputStream fileInputStream;
    private ProgressDialog progress;
    private String path;
    RelativeLayout relative_layout;
    private View view;

    public fragment2() {
        // Required empty public constructor
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                relative_layout.setVisibility(View.GONE);
                lv_movie.setVisibility(View.VISIBLE);
                myadapter adapter = new myadapter();
                lv_movie.setAdapter(adapter);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment2, container, false);


        return view;
    }

    private boolean mHasLoadedOnce = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isVisibleToUser && !mHasLoadedOnce) {
                lv_movie = (ListView) view.findViewById(R.id.lv_movie);
                relative_layout = (RelativeLayout) view.findViewById(R.id.relative_layout);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getVideoFile();
                    }
                }).start();
                // async http request here
                lv_movie.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        SharedPreferences pref = getActivity().getSharedPreferences("judge", getActivity().MODE_PRIVATE);
                        boolean connected = pref.getBoolean("connected", false);
                        if (!connected) {
                            Toast.makeText(getContext(), "请先连接设备", Toast.LENGTH_SHORT).show();
                        } else {
                            video_item video_item = list.get(position);
                            path = video_item.getPath();
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
                lv_movie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        video_item video_item = list.get(position);
                        path = video_item.getPath();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        File file = new File(path);
                        String strend = "";
                        if (path.toLowerCase().endsWith(".mp4")) {
                            strend = "mp4";
                        } else if (path.toLowerCase().endsWith(".3gp")) {
                            strend = "3gp";
                        } else if (path.toLowerCase().endsWith(".mov")) {
                            strend = "mov";
                        } else if (path.toLowerCase().endsWith(".wmv")) {
                            strend = "wmv";
                        }
                        intent.setDataAndType(Uri.fromFile(file), "video/" + strend);
                        startActivity(intent);

                    }
                });

                Log.e("aaa", "setUserVisibleHint");

                mHasLoadedOnce = true;
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("tag", path);
                String response = connect_tools.SendFile(getContext(), getFileName(path),
                        path,
                        connect_tools.getip(getActivity()), fileInputStream.available(), progress);
                System.out.println("信息" + response);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.toString());
            }
        }
    };


    /**
     * 获得视频文件
     */

    public void getVideoFile() {// 获得视频文件
        list.clear();
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);
        while (cursor.moveToNext()) {

            String title = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            String path = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            long duration = cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));//视频时间
            long size = cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));//视频大小
            long thumbNailsId = cursor.getLong(cursor.getColumnIndex("_ID"));
            Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(cr, thumbNailsId,
                    MediaStore.Video.Thumbnails.MICRO_KIND, null);//获取视频第一帧画面
            video_item video_item = new video_item(bitmap, size, duration, title, path);
            list.add(video_item);
        }
        cursor.close();
        handler.sendEmptyMessage(1);

    }

//    /**
//     * 获得视频第一帧
//     *
//     * @param bitmap
//     * @return
//     */

//    public Bitmap getVidioBitmap(Bitmap bitmap, String uri) {
//
//        Uri originalUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        ContentResolver cr = getActivity().getContentResolver();
//        Cursor cursor = cr.query(originalUri, null, null, null, null);
//        while (cursor.moveToNext()) {
//            long thumbNailsId = cursor.getLong(cursor.getColumnIndex("_ID"));
//            bitmap = MediaStore.Video.Thumbnails.getThumbnail(cr, thumbNailsId,
//                    MediaStore.Video.Thumbnails.MICRO_KIND, null);
//        }
//        return bitmap;
//    }

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
                        R.layout.frag2_video, null);
                arg1.setTag(holder);
            } else {
                holder = (viewholder) arg1.getTag();
            }
            // 找到布局里面的控件
            holder.title = (TextView) arg1.findViewById(R.id.title);
            holder.duration = (TextView) arg1.findViewById(R.id.duration);
            holder.size = (TextView) arg1.findViewById(R.id.size);
            holder.img = (ImageView) arg1.findViewById(R.id.tu);


            long aa = list.get(arg0).getDuration();
            long bb = list.get(arg0).getSize();

            holder.title.setText(list.get(arg0).getTitle() + "");
            holder.duration.setText(aa / 1000 / 60 + ":" + aa / 1000 % 60);
            holder.size.setText(bb / 1024 / 1024 + "MB");
            holder.img.setImageBitmap(list.get(arg0).getBitmap());

            return arg1;
        }
    }

    class viewholder {
        TextView title;
        TextView duration;
        TextView size;
        ImageView img;
    }

    /**
     * 切割字符串
     *
     * @param url
     * @return
     */
    private String getFileName(String url) {
        int index = url.lastIndexOf("/") + 1;
        return url.substring(index);

    }


}
