package fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wificonnect.qiezhi.R;
import com.wificonnect.qiezhi.received_activity;
import com.wificonnect.qiezhi.send_activity;

/**
 * A simple {@link Fragment} subclass.
 */
public class main_fragment extends Fragment implements View.OnClickListener {

    private Button iSend;
    private Button iReceive;

    LinearLayout touxiang;
    ImageView quanquan;
    private View view;

    private void assignViews() {
        quanquan = (ImageView) view.findViewById(R.id.quanquan);
        quanquan.setOnClickListener(this);
        touxiang = (LinearLayout) view.findViewById(R.id.touxiang);
        touxiang.setOnClickListener(this);
        iSend = (Button) view.findViewById(R.id.i_send);
        iSend.setOnClickListener(this);
        iReceive = (Button) view.findViewById(R.id.i_receive);
        iReceive.setOnClickListener(this);

    }

    public main_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.main_fragment, container, false);
        assignViews();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quanquan:
                break;
            case R.id.touxiang:
                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                drawer.openDrawer(navigationView);
                break;
            case R.id.i_send:
                startActivity(new Intent(getContext(), send_activity.class));
                break;
            case R.id.i_receive:
                startActivity(new Intent(getContext(), received_activity.class));
                break;

        }
    }
}
