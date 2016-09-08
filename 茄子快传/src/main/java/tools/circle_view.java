package tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/*
 自定义环形显示流量视图
 文字显示

 画一个环形，在外面画一个空心大圆灰白色的
 里面画一个实心小圆，再画一个带灰白色的框
 在里面画两个扇形，总共360度
 */
public class circle_view extends View {

    private Paint paint;
    int send = 0;//发送的流量占总节省流量的百分比乘以360度
    int send_size;//发送的流量
    int receive_size;//接收的流量
    private int all_size;//总节省的流量

    public circle_view(Context context) {
        super(context);
        paint = new Paint();
    }


    public circle_view(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public circle_view(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }


    /**
     * 传入显示的百分比
     */
    public void setprogress(int send_size, int receive_size) {
        this.send_size = send_size;
        this.receive_size = receive_size;
        all_size = send_size + receive_size;
        double aaa = ((double) send_size / (double) all_size) * 360;
        send = (int) aaa;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();//得到给的宽度
        int h = getHeight();//得到给的高度

        RectF oval = new RectF(w / 4 - h / 4, h / 4, w / 4 + h / 4, (3 * h) / 4);//创建一个矩形用于画圆弧（圆弧是在矩形的内部内切圆）

        //发送的流量的圆弧
        paint.setAntiAlias(true);//抗锯齿
        paint.setColor(Color.parseColor("#FF03A9F4"));//发送画扇形蓝色
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(oval, -90, (send * 1f) / 360 * 360f, true, paint);//画圆弧
        canvas.drawRect(w / 2, h / 2 - 40, w / 2 + 20, h / 2 - 20, paint);//右边的小正方形


        //接收的流量的圆弧
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#FF79FFAA"));//接收画扇形绿色
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(oval, (send * 1f) / 360 * 360f - 90, ((360 - send) * 1f) / 360 * 360f, true, paint);//画圆弧
        canvas.drawRect(w / 2, h / 2 + 20, w / 2 + 20, h / 2 + 40, paint);//右边的小正方形

        //字体
        paint.reset();
        paint.setColor(Color.parseColor("#FF000000"));
        paint.setTextSize(30);
        canvas.drawText("累计接收:" + receive_size + "MB", w / 2 + 80, h / 2 + 40, paint);
        canvas.drawText("累计发送:" + send_size + "MB", w / 2 + 80, h / 2 - 20, paint);


        paint.reset();
        paint.setColor(Color.parseColor("#FF000000"));
        paint.setTextSize(50);
        canvas.drawText("节省流量:" + all_size + "MB", w / 12, h / 4 - 50, paint);


        //内实心白圆
        paint.reset();
        paint.setColor(Color.WHITE);//白饼
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawCircle(w / 4, h / 2, h / 4 - 30, paint);
        //内空心灰框
        paint.reset();
        paint.setColor(Color.parseColor("#FFC9C9C9"));//内圈
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(w / 4, h / 2, h / 4 - 30, paint);
        //外空心灰框
        paint.reset();
        paint.setColor(Color.parseColor("#FFC9C9C9"));//外圈灰色
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(w / 4, h / 2, h / 4, paint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }
}
