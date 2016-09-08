package com.wificonnect.qiezhi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xys.libzxing.zxing.encoding.EncodingUtils;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import bean.file_item;
import service.Receive_service;
import tools.WhewView;
import wifi.WifiApManager;
import wifi.WifiLib;
import wifi.WifiLibInitializer;

/*
接收頁面
 */
public class received_activity extends Activity implements View.OnClickListener {
    private WifiLib wifiLib;
    private ImageView imageView;
    private TextView info;
    private String ip;
    private WhewView whewView;
    ProgressBar progressBar;
    FrameLayout frameLayout;
    LinearLayout linearLayout;
    TextView connect_name;
    ListView list_con;

    private File fileDirectory;
    private File[] listFile;
    List<file_item> list = new ArrayList<>();
    private myadapter myadapter;
    private int leng;
    private ImageView code_image;//二维码展示
    private RelativeLayout relative_rece;//水波纹界面
    private Button receive_zx;//扫码发送

    boolean isfirst = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.receive_zx:
                if (!isfirst) {
                    receive_zx.setText("等待连接");
                    Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.lw);
                    Bitmap bitamp = EncodingUtils.createQRCode("My_kuaichuan-12345678", 300, 300, bitmap);
                    code_image.setImageBitmap(bitamp);
                    code_image.setVisibility(View.VISIBLE);
                    relative_rece.setVisibility(View.GONE);
                    isfirst = true;
                } else {
                    receive_zx.setText("扫码连接");
                    code_image.setVisibility(View.GONE);
                    relative_rece.setVisibility(View.VISIBLE);
                    isfirst = false;
                }

                break;
        }
    }

    private void assignViews() {
        receive_zx = (Button) findViewById(R.id.receive_zx);
        receive_zx.setOnClickListener(this);
        relative_rece = (RelativeLayout) findViewById(R.id.relative_rece);
        code_image = (ImageView) findViewById(R.id.code_image);
        list_con = (ListView) findViewById(R.id.list_con);
        connect_name = (TextView) findViewById(R.id.connect_name);
        linearLayout = (LinearLayout) findViewById(R.id.LinearLayout);
        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        progressBar = (ProgressBar) findViewById(R.id.connect_pb);
        View view = findViewById(R.id.title);
        TextView textView = (TextView) view.findViewById(R.id.title_tv);
        Button button = (Button) view.findViewById(R.id.back_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tuichu();
            }
        });
        whewView = (WhewView) findViewById(R.id.whewview);
        textView.setText("接收文件");
        info = (TextView) findViewById(R.id.info);
        imageView = (ImageView) findViewById(R.id.imageView);
    }//初始化数据

    public void tuichu() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setIcon(R.drawable.lw);
        builder.setMessage("是否关闭热点并退出？？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                wifiLib.closeWifiAp();
                stopService(new Intent(received_activity.this, Receive_service.class));//关闭接收服务
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.received_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        assignViews();//初始化数据
        setWifi();//开启热点
        whewView.start();//开启水波纹咻一咻的效果
        new Thread(new Runnable() {
            @Override
            public void run() {
                receive_udp();
            }
        }).start();//开启udp接收者接收udp，如果连接上了就能接收到
        list_con.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int length = listFile.length;
                fileDirectory = listFile[length - position - 1];
                openFile(fileDirectory);
            }
        });
        list_con.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(received_activity.this);
                builder.setTitle("提示");
                builder.setIcon(R.drawable.lw);
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
                        Toast.makeText(received_activity.this, "点击了取消", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create();
                builder.show();
                return true;
            }
        });

    }


    //查找当前文件路径的子文件夹，并显示在listview列表上
    public void show_file_list(File file) {

        //判断当前文件目录下是否有子文件

        if (file.isDirectory()) {
            listFile = file.listFiles();// 返回一个抽象路径名数组，这些路径名表示此抽象路径名表示的目录中的文件
            if (list.size() > 0) {
                list.clear();
            }
            for (int i = 0; i < listFile.length; i++) {
                file_item item = new file_item();//实例化对象


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
                            item.setDrawable_int(R.drawable.mv);
                        } else if (names[1].equals("mp3")) {
                            item.setDrawable_int(R.drawable.lo);
                        } else {
                            item.setDrawable_int(R.drawable.le);
                        }
                    }


                }
                item.setFile_name(listFile[i].getName());
                list.add(0, item);


            }

            if (myadapter == null) {

                myadapter = new myadapter(list, this);
                list_con.setAdapter(myadapter);
            } else {
                myadapter.notifyDataSetChanged();
            }
        } else {


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

    public void setWifi() {
        WifiLibInitializer.init(this);
        wifiLib = WifiLib.getInstance();
        wifiLib.closeWifi();
        wifiLib.createAccessPoint(WifiApManager.WifiApType.TYPE_WPA_PSK, "My_kuaichuan", "12345678");
    }//开启热点

    @Override
    public void onBackPressed() {
        tuichu();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                info.setText("连接成功，等待发送文件");
                code_image.setVisibility(View.GONE);
                relative_rece.setVisibility(View.VISIBLE);
                startService(new Intent(received_activity.this, Receive_service.class));//连接成功开启服务接收数据

                ////////注册广播接收数据更新发过来的实时进度条
                MyRceiver1 myRceiver1 = new MyRceiver1();
                IntentFilter filter1 = new IntentFilter();
                filter1.addAction("com.rec.leng");//数据总长度
                received_activity.this.registerReceiver(myRceiver1, filter1);

                MyRceiver2 myRceiver2 = new MyRceiver2();
                IntentFilter filter2 = new IntentFilter();
                filter2.addAction("com.rec.total");//数据实时进度
                received_activity.this.registerReceiver(myRceiver2, filter2);
                whewView.stop();//动画停止
                whewView.setVisibility(View.GONE);
                new Thread(runnable).start();//接收成功就回传udp通知发送端可以发送了
            }


        }
    };

    boolean flag = true;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            while (flag) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendudp(ip, "11111");
            }
        }
    };//


    public void sendudp(String ip, String text) {
        // 创建发送端Socket对象
        // DatagramSocket()
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            // 创建数据，并把数据打包
            // DatagramPacket(byte[] buf, int length, InetAddress address, int port)
            // 创建数据
            byte[] bys = text.getBytes();
            // 长度
            int length = bys.length;
            // IP地址对象
            InetAddress address = InetAddress.getByName(ip);
            // 端口
            int port = 54321;
            DatagramPacket dp = new DatagramPacket(bys, length, address, port);

            // 调用Socket对象的发送方法发送数据包
            // public void send(DatagramPacket p)
            ds.send(dp);

            // 释放资源
            ds.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//发送udp广播通知已经接收到了广播


    public void receive_udp() {
        // 创建接收端的Socket对象
        DatagramSocket ds = null;
        boolean flag = true;
        try {
            ds = new DatagramSocket(10086);
            while (flag) {
                // 创建一个包裹
                byte[] bys = new byte[1024];
                DatagramPacket dp = new DatagramPacket(bys, bys.length);
                // 接收数据
                ds.receive(dp);
                // 解析数据
                ip = dp.getAddress().getHostAddress();
                String received_udpdate = new String(dp.getData(), 0, dp.getLength());

                System.out.println(received_udpdate);

                if (received_udpdate.equals("1")) {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    flag = false;
                }
                System.out.println("from " + ip + " data is : " + received_udpdate);
            }
            // 释放资源
            // 接收端应该一直开着等待接收数据，是不需要关闭
            ds.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//接收发送过来的广播

    public static String getPrintSize(long size) {
        size = size / 1024 / 1024;
        size = size * 100;
        return String.valueOf((size / 100)) + "."
                + String.valueOf((size % 100)) + "-MB";
    }//字节转换

    //广播1接收数据的长度
    public class MyRceiver1 extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            flag = false;
            frameLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);


            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                //获取shanchuanw文件夹目录
                fileDirectory = Environment.getExternalStorageDirectory();
                fileDirectory = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "shanchuan");
                show_file_list(fileDirectory);
            }


            Bundle extras = intent.getExtras();
            leng = extras.getInt("leng");
            String fileName = extras.getString("fileName");
            connect_name.setText(fileName);
            progressBar.setMax(leng / 1024 / 1024);

            //接收节省的流量
            SharedPreferences pref = getSharedPreferences("judge",
                    MODE_PRIVATE);
            long receivesize = pref.getLong("receivesize", 0);
            long rece_size = receivesize + leng;
            SharedPreferences.Editor editor = getSharedPreferences("judge",
                    MODE_PRIVATE).edit();
            editor.putLong("receivesize", rece_size);
            editor.commit();
        }
    }

    //广播2接收不停发送过来的实时进度
    public class MyRceiver2 extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            int total = extras.getInt("total");
            progressBar.setProgress(total / 1024 / 1024);
            if (total == leng) {
//                myadapter.notifyDataSetChanged();
                progressBar.setProgress(0);
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    //获取shanchuanw文件夹目录
                    fileDirectory = Environment.getExternalStorageDirectory();
                    fileDirectory = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "shanchuan");
                    show_file_list(fileDirectory);
                }
            }

        }
    }

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
