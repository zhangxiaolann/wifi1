package com.wificonnect.qiezhi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bean.file_item;

public class filter_file_activity extends Activity {

    private String titile_name;
    private int iid;
    private ListView listView;
    private File fileDirectory;
    private myadapter myadapter;
    List<file_item> list = new ArrayList<>();

    /*
    *初始化
     */
    private void assignViews() {
        listView = (ListView) findViewById(R.id.filter_listview);
        View view = findViewById(R.id.title_filter);
        Button button = (Button) view.findViewById(R.id.back_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView textView = (TextView) view.findViewById(R.id.title_tv);
        textView.setText(titile_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_file_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Intent intent = getIntent();//接收值
        titile_name = intent.getStringExtra("titile_name");
        iid = intent.getIntExtra("id", 0);
        assignViews();


        fileDirectory = Environment.getExternalStorageDirectory();//获取存储地址
        fileDirectory = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "shanchuan");
        showfile(fileDirectory, iid);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//点击
                file_item file_item = list.get(position);
                String path = file_item.getPath();
                File file1 = new File(path);
                openFile(file1);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                file_item file_item = list.get(position);
                final String path = file_item.getPath();
                final File file = new File(path);
                AlertDialog.Builder builder = new AlertDialog.Builder(filter_file_activity.this);
                builder.setTitle("提示！！");
                builder.setIcon(R.drawable.lw);
                builder.setMessage("确定要删除 " + file.getName() + "？？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        file.delete();
                        showfile(fileDirectory, iid);

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
                return true;
            }
        });

    }
  /*
  展示文件
 */

    private void showfile(File file, int id) {
        File[] listFile = file.listFiles();

        if (list.size() > 0) {
            list.clear();
        }
        for (int i = 0; i < listFile.length; i++) {
            file_item fileitem = new file_item();
            String name = listFile[i].getName();
            if (name.indexOf(".") == -1) {

            } else {
                String[] names = name.split("\\.");
                if (names[1].equals("apk") && id == 0) {
                    fileitem.setPath(listFile[i].getAbsolutePath());
                    fileitem.setDrawable_int(R.drawable.ng);
                    fileitem.setFile_name(name);
                    list.add(fileitem);
                } else if (names[1].equals("jpg") && id == 1) {
                    fileitem.setPath(listFile[i].getAbsolutePath());
                    fileitem.setDrawable_int(R.drawable.ni);
                    fileitem.setFile_name(name);
                    list.add(fileitem);
                } else if (names[1].equals("mp3") && id == 2) {
                    fileitem.setPath(listFile[i].getAbsolutePath());
                    fileitem.setDrawable_int(R.drawable.lo);
                    fileitem.setFile_name(name);
                    list.add(fileitem);
                } else if ((names[1].equals("mp4") || names[1].equals("3gp") || names[1].equals("mov") || names[1].equals("wmv")) && id == 3) {
                    fileitem.setPath(listFile[i].getAbsolutePath());
                    fileitem.setDrawable_int(R.drawable.nj);
                    fileitem.setFile_name(name);
                    list.add(fileitem);
                } else if (names[1].equals("txt") && id == 4) {
                    fileitem.setPath(listFile[i].getAbsolutePath());
                    fileitem.setDrawable_int(R.drawable.nh);
                    fileitem.setFile_name(name);
                    list.add(fileitem);
                } else if (id == 5) {
                    fileitem.setPath(listFile[i].getAbsolutePath());
                    if (names[1].equals("apk")) {
                        fileitem.setDrawable_int(R.drawable.ng);
                    } else if (names[1].equals("jpg")) {
                        fileitem.setDrawable_int(R.drawable.ni);
                    } else if (names[1].equals("mp3")||names[1].equals("ape")||names[1].equals("wmv")) {
                        fileitem.setDrawable_int(R.drawable.lo);
                    } else if ((names[1].equals("mp4") || names[1].equals("3gp") || names[1].equals("mov") || names[1].equals("wmv"))) {
                        fileitem.setDrawable_int(R.drawable.nj);
                    } else if (names[1].equals("txt")) {
                        fileitem.setDrawable_int(R.drawable.nh);
                    }else {
                        fileitem.setDrawable_int(R.drawable.nh);
                    }

                    fileitem.setFile_name(name);
                    list.add(fileitem);
                }
                if (myadapter == null) {

                    myadapter = new myadapter(list, this);
                    listView.setAdapter(myadapter);
                } else {
                    myadapter.notifyDataSetChanged();
                }

            }
        }
    }

    /**
     * 打开文件
     *
     * @param file
     */
    private void openFile(File file) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        //跳转
        startActivity(intent);

    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    private String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
    /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    /**
     * 数组
     */
    private final String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    /*
    *适配器
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
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);

                convertView.setTag(viewHolder);
            } else {

                viewHolder = (ViewHolder) convertView.getTag();
            }
            file_item item = list.get(position);
            viewHolder.textView.setText(item.getFile_name());
            viewHolder.imageView.setImageResource(item.getDrawable_int());
            return convertView;
        }

        public class ViewHolder {
            ImageView imageView;
            TextView textView;
        }
    }
}
