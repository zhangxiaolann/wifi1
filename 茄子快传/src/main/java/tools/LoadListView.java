package tools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.wificonnect.qiezhi.R;

public class LoadListView extends ListView implements AbsListView.OnScrollListener {
    View footer;//底部布局
    int totalItemCount;//总数量
    int lastVisibleItem;//最后一个可见的Item
    boolean isLoading;//判断是否正在加载
    private IloadInterface mIloadInterface;

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadListView(Context context) {
        super(context);
        initView(context);
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 添加底部加载布局到ListView中
     *
     * @param context 上下文对象
     */
    private void initView(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.footer_layout, null);
        //第一次进来下面正在加载是隐藏的
        footer.findViewById(R.id.footview_linear).setVisibility(View.GONE);
        this.addFooterView(footer);
        this.setOnScrollListener(this);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (this.totalItemCount == lastVisibleItem &&
                scrollState == SCROLL_STATE_IDLE) {
            if (!isLoading) {
                isLoading = true;
                footer.findViewById(R.id.footview_linear).setVisibility(View.VISIBLE);
                //加载更多数据
                mIloadInterface.onLoad();
            }

        }
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        this.lastVisibleItem = visibleItemCount + firstVisibleItem;
        this.totalItemCount = totalItemCount;
    }


    public void setInterface(IloadInterface iloadInterface) {
        this.mIloadInterface = iloadInterface;
    }

    //加载更多数据的回调方法
    public interface IloadInterface {
        void onLoad();
    }

    /**
     * 加载完毕
     */
    public void loadComplete() {
        isLoading = false;
        footer.findViewById(R.id.footview_linear).setVisibility(View.GONE);
    }
}


