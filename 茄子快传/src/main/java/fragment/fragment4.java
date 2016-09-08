package fragment;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wificonnect.qiezhi.R;
import com.wificonnect.qiezhi.ShowImageActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import adapter.GroupAdapter;
import imagescan.ImageBean;

public class fragment4 extends Fragment {
    List<String> chileList = new ArrayList<>();
    private HashMap<String, List<String>> mGruopMap = new HashMap<>();
    private List<ImageBean> list = new ArrayList<ImageBean>();
    private GroupAdapter adapter;
    private GridView mGroupGridView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, container, false);

        mGroupGridView = (GridView) view.findViewById(R.id.main_grid);

        getImages();

        mGroupGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {//点击进入子图片浏览
                List<String> childList = mGruopMap.get(list.get(position)
                        .getFolderName());
                Intent mIntent = new Intent(getActivity(),
                        ShowImageActivity.class);
                mIntent.putStringArrayListExtra("data",
                        (ArrayList<String>) childList);
                startActivity(mIntent);

            }
        });
        return view;
    }


    private void getImages() {
        chileList.removeAll(chileList);
        mGruopMap.clear();
        list.removeAll(list);
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = getActivity().getContentResolver();
                //查询contentresolver
                Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                while (mCursor.moveToNext()) {
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    String parentName = new File(path).getParentFile()
                            .getName();
                    if (!mGruopMap.containsKey(parentName)) {//此映射包含对于指定键的映射关系，则返回 true。
                        chileList = new ArrayList<String>();
                        chileList.add(path);
                        mGruopMap.put(parentName, chileList);
                    } else {
                        mGruopMap.get(parentName).add(path);
                    }
                }

                mCursor.close();
                mHandler.sendEmptyMessage(1);
            }
        }).start();

    }//得到图片


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter = new GroupAdapter(getActivity(), list = subGroupOfImage(mGruopMap), mGroupGridView);
                    mGroupGridView.setAdapter(adapter);//设置adapter
                    break;
            }
        }

    };


    /**
     * @param mGruopMap
     * @return
     */
    private List<ImageBean> subGroupOfImage(HashMap<String, List<String>> mGruopMap) {
        List<ImageBean> list = new ArrayList<ImageBean>();
        if (mGruopMap.size() == 0) {

            return list;
        }


        Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet()
                .iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            ImageBean mImageBean = new ImageBean();
            String key = entry.getKey();
            List<String> value = entry.getValue();
            mImageBean.setFolderName(key);
            mImageBean.setImageCounts(value.size());
            mImageBean.setTopImagePath(value.get(0));

            list.add(mImageBean);
        }

        return list;

    }

}
