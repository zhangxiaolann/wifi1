package fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wificonnect.qiezhi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class setup_fragment extends Fragment {

    private TextView te;
    private LinearLayout lin, lin1;
    private View view;

    public setup_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.setup_fragment, container, false);
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        te = (TextView) view.findViewById(R.id.te);
        lin = (LinearLayout) view.findViewById(R.id.lin);
        lin1 = (LinearLayout) view.findViewById(R.id.lin1);
        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("设置存储位置");
                builder.setMessage("内部存储设备\n/内部存储设备/QieZi");
                builder.setPositiveButton("确定", null);
                builder.create();
                builder.show();
            }
        });
        lin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("这是一个信息");
                builder.setMessage("确定清空所有缓存吗?");
                builder.setPositiveButton("确定", null);
                builder.setNegativeButton("取消", null);
                builder.create();
                builder.show();

            }
        });

    }
}
