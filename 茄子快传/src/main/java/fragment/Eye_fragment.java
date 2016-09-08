package fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wificonnect.qiezhi.R;

import java.util.ArrayList;
import java.util.List;

import adapter.Find_tab_Adapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Eye_fragment extends Fragment {
    private TabLayout tabLayout;                            //定义TabLayout
    private ViewPager viewpager;                             //定义viewPager
    private FragmentStatePagerAdapter fAdapter;                               //定义adapter
    private List<Fragment> list_fragment;                                //定义要装fragment的列表
    private List<String> list_title;                                     //tab名称列表
    Eye_child1_frag child1_frag;
    Eye_child2_frag child2_frag;
    Eye_child3_frag child3_frag;

    private View view;

    public Eye_fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.eye_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View title = view.findViewById(R.id.title_eye);
        Button button = (Button) title.findViewById(R.id.back_button);
        TextView textview = (TextView) title.findViewById(R.id.title_tv);
        button.setVisibility(View.GONE);
        textview.setText("发现");
        initControls();
    }



    private void initControls() {

        tabLayout = (TabLayout) view.findViewById(R.id.eye_TabLayout);
        viewpager = (ViewPager) view. findViewById(R.id.eye_viewpager);

        //初始化各fragment
        child1_frag=new Eye_child1_frag();
        child2_frag=new Eye_child2_frag();
        child3_frag=new Eye_child3_frag();


        //将fragment装进列表中
        list_fragment = new ArrayList<>();
        list_fragment.add(child1_frag);
        list_fragment.add(child2_frag);
        list_fragment.add(child3_frag);


        viewpager.setOffscreenPageLimit(list_fragment.size());//状态保存不销毁viewpager状态

        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        list_title = new ArrayList<>();
        list_title.add("科技在线");
        list_title.add("微信精选");
        list_title.add("奇闻轶事");


        viewpager.setOffscreenPageLimit(list_fragment.size());//状态保存不销毁viewpager状态

        //设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(2)));



        fAdapter = new Find_tab_Adapter(getActivity().getSupportFragmentManager(), list_fragment, list_title);

        //viewpager加载adapter
        viewpager.setAdapter(fAdapter);
        //tab_FindFragment_title.setViewPager(vp_FindFragment_pager);
        //TabLayout加载viewpager
        tabLayout.setupWithViewPager(viewpager);
        //tab_FindFragment_title.set

        viewpager.setCurrentItem(1);
    }
}
