package com.example.imagescan;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.sql.Myapp;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class ShowImageActivity extends Activity {
	private GridView mGridView;
	private List<String> list;
	private ChildAdapter adapter;
	private int length;
	private ProgressDialog pb;
	String response;
	String filename;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_image_activity);

		mGridView = (GridView) findViewById(R.id.child_grid);
		list = getIntent().getStringArrayListExtra("data");

		adapter = new ChildAdapter(this, list, mGridView);
		mGridView.setAdapter(adapter);
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				
				AlertDialog.Builder builder = new Builder(ShowImageActivity.this);
				builder.setItems(new String[]{"发送","查看"}, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						switch (arg1) {
						case 0:
							if (Myapp.list.size() == 0) {
								Toast.makeText(ShowImageActivity.this, "没有找到已连接的设备！",
										Toast.LENGTH_LONG).show();
								return;
							}
							filename = getFileName(list.get(position));
							File file = new File(list.get(position));
							try {
								FileInputStream fis = new FileInputStream(file);
								length = fis.available();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							pb = new ProgressDialog(ShowImageActivity.this);
							pb.setMax(length);
							// 设置对话框的标题
							pb.setTitle("任务完成度");
							// 设置对话框显示的内容
							pb.setMessage(getFileName(list.get(position)));
							// 设置对话框不能用"取消"按钮关闭
							pb.setCancelable(true);
							// 设置对话框的进度条风格
							pb.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
							// 设置对话框的进度条是否显示进度
							pb.setIndeterminate(false);
							pb.show();

							Thread sendThread = new Thread(new Runnable() {
								@Override
								public void run() {

									response = SendFile(filename,
											list.get(position),
											Myapp.list.get(0).get("ip"),
											9999, length);
									handler.sendEmptyMessage(123);
								}
							});
							sendThread.start();
							break;
						case 1:
							File file1 = new File(list.get(position));
							if (file1 != null && file1.isFile() == true) {
								Intent intent = new Intent();
								intent.setAction(android.content.Intent.ACTION_VIEW);
								intent.setDataAndType(Uri.fromFile(file1), "image/*");
								ShowImageActivity.this.startActivity(intent);
							}
							break;

						default:
							break;
						}
					}
				});
				builder.show();
				
			}
		});
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 123) {
				Toast.makeText(ShowImageActivity.this, response,
						Toast.LENGTH_LONG).show();

				int len = length / 1024 / 1024;
				int len2 = length / 1024;
				if (len == 0) {
					SQLiteDatabase db = Myapp.db.getWritableDatabase();
					String sql = "insert into note(filename, size, time, type, model) values('"+filename+"', '"+len2+"KB"+"', '"+getTime()+"', '发送', '"+Myapp.list.get(0).get("name")+"')";
					db.execSQL(sql);
					
					String sql1 = "insert into allsize(allsize) values('"+len2+"')";
					db.execSQL(sql1);
				}
				else {
					SQLiteDatabase db = Myapp.db.getWritableDatabase();
					String sql = "insert into note(filename, size, time, type, model) values('"+filename+"', '"+len+"M"+"', '"+getTime()+"', '发送', '"+Myapp.list.get(0).get("name")+"')";
					db.execSQL(sql);
					
					String sql1 = "insert into allsize(allsize) values('"+len2+"')";
					db.execSQL(sql1);
				}
			}
		};
	};

	private String getFileName(String url) {
		int index = url.lastIndexOf("/") + 1;
		return url.substring(index);

	}

	synchronized public String SendFile(String fileName, String path,
			String ipAddress, int port, int length) {
		int finished = 0;
		try {
			Socket name = new Socket(ipAddress, port);
			OutputStream outputName = name.getOutputStream();
			OutputStreamWriter outputWriter = new OutputStreamWriter(outputName);
			BufferedWriter bwName = new BufferedWriter(outputWriter);
			bwName.write(fileName + "x" + length);
			bwName.close();
			outputWriter.close();
			outputName.close();
			name.close();

			Socket data = new Socket(ipAddress, port);
			OutputStream outputData = data.getOutputStream();
			FileInputStream fileInput = new FileInputStream(path);
			int size;
			byte[] buffer = new byte[4028];
			while ((size = fileInput.read(buffer)) != -1) {
				outputData.write(buffer, 0, size);
				finished = finished + size;
				Log.e("upload", finished + "");
				if (finished < length) {
					pb.setProgress(finished);
				} else {
					pb.dismiss();
				}
			}
			outputData.close();
			fileInput.close();
			data.close();
			return fileName + "发送完成";
		} catch (Exception e) {
			return "连接已断开，传输失败,请重新连接!";
		}
	}

	private String getTime() {
		Calendar now;
		SimpleDateFormat fmt;

		now = Calendar.getInstance();
		fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return fmt.format(now.getTime());
	}

}
