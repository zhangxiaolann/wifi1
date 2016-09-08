package com.liuguilin.only.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.liuguilin.only.R;
import com.liuguilin.only.utils.L;

/*
 *  项目名：  Only
 *  包名：    com.liuguilin.only.view
 *  文件名:   CircularMenuView
 *  创建者:   LGL
 *  创建时间:  2016/7/4 14:04
 *  描述：    圆形手势菜单
 */
public class CircularMenuView extends View {

    private OnRorateListenser onRorateListenser;
    private float tempDegress, tempDegress1;
    private String[] outerString ;
    private String[] innerString ;
    // 保存内外圆的数组
    private PointCirCleInner[] innerPointCirCle;
    private PointCirCleOut[] outerPointCirCle;
    private Context mContext;
    // 屏幕的宽和高
    private int width, height;
    private DisplayMetrics displayMetrics;
    // 对应的画笔对象
    private Paint linePaint, textPaint, criPaint;

    // 圆心的坐标
    private int circleX, cirCleY;
    // 半径
    private int radiusInner, radiusOuter;

    public CircularMenuView(Context context) {
        super(context);
        initView();
    }

    public CircularMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CircularMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化布局
     */
    public void initView() {
        mContext = getContext();

        outerString = new String[]{mContext.getString(R.string.item_1),mContext.getString(R.string.item_2),mContext.getString(R.string.item_3),
                mContext.getString(R.string.item_4),mContext.getString(R.string.item_5),mContext.getString(R.string.item_6),mContext.getString(R.string.item_7),
                mContext.getString(R.string.item_8),mContext.getString(R.string.item_9),mContext.getString(R.string.item_10),mContext.getString(R.string.item_11),
                mContext.getString(R.string.item_12)};
        innerString = new String[] {mContext.getString(R.string.item_13),mContext.getString(R.string.item_14),mContext.getString(R.string.item_15),
                mContext.getString(R.string.item_16),mContext.getString(R.string.item_17),mContext.getString(R.string.item_18)};

        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        /*
         * 两种获取屏幕宽和高方法
         * Display display = manager.getDefaultDisplay();
         * width = display.getWidth();
         * height = display.getHeight();
		 */
        displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        circleX = width / 2 - 5;
        cirCleY = height / 2 - 20;
        radiusInner = width / 3 - 50;
        radiusOuter = width / 2 - 50;
        // 创建画笔

        linePaint = new Paint();

        textPaint = new Paint();

        criPaint = new Paint();

        // 设置画笔的属性
        // 设置绘画的颜色
        linePaint.setColor(Color.parseColor("#B4CDE6"));
        // 设置空心的图案
        linePaint.setStyle(Paint.Style.STROKE);
        // 消除锯齿
        linePaint.setAntiAlias(true);
        // 画线段的粗线
        linePaint.setStrokeWidth(3.0f);
        // 设置颜色
        textPaint.setColor(Color.WHITE);
        // 设置绘画的内容居中
        textPaint.setTextAlign(Paint.Align.CENTER);
        // 消除锯齿
        textPaint.setAntiAlias(true);
        // 设置字体的大小
        textPaint.setTextSize(15.f);

        criPaint.setColor(Color.GRAY);
        // 填充
        criPaint.setStyle(Paint.Style.FILL);
        criPaint.setAntiAlias(true);

        drawCircleText();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        // 绘画的线圈
        canvas.drawCircle(circleX, cirCleY, radiusInner, linePaint);

        canvas.drawCircle(circleX, cirCleY, radiusOuter, linePaint);

        PointCirCleInner cleInner;
        for (int i = 0; i < 6; i++) {
            cleInner = innerPointCirCle[i];
            canvas.drawCircle(cleInner.cirCle_X, cleInner.cirCle_Y, 35,
                    criPaint);
            canvas.drawText(innerString[i], cleInner.cirCle_X,
                    cleInner.cirCle_Y + 8, textPaint);
        }

        PointCirCleOut cirCleOut;
        for (int i = 0; i < 12; i++) {

            cirCleOut = outerPointCirCle[i];
            canvas.drawCircle(cirCleOut.cirCle_X, cirCleOut.cirCle_Y, 35,
                    criPaint);
            canvas.drawText(outerString[i], cirCleOut.cirCle_X,
                    cirCleOut.cirCle_Y + 8, textPaint);
        }

    }

    /**
     * 创建内圆的点
     */
    public void createInnerPoint() {

        int startAngle = 0;
        // 创建数组
        innerPointCirCle = new PointCirCleInner[6];
        // 每次生成位置转换的角度的位置
        int degress = 360 / 6;
        // 创建每一个点
        for (int i = 0; i < innerPointCirCle.length; i++) {
            // 每一个点
            PointCirCleInner cirCleInner = new PointCirCleInner();
            cirCleInner.cirCle_angle = startAngle;
            startAngle += degress;

            if (startAngle > 360 || startAngle < -360) {
                startAngle %= 360;
            }

            cirCleInner.cirCle_name = innerString[i];

            innerPointCirCle[i] = cirCleInner;
        }

        calculatePointPosition1(6);
    }

    /**
     * 创建外圆的点点
     */
    public void createOuterPoint() {

        outerPointCirCle = new PointCirCleOut[12];

        int startAngle = 0;

        int degress = 360 / 12;

        for (int i = 0; i < outerPointCirCle.length; i++) {

            PointCirCleOut cirCleOut = new PointCirCleOut();

            cirCleOut.cirCle_angle = startAngle;

            startAngle += degress;

            if (startAngle > 360 || startAngle < -360) {
                startAngle %= 360;
            }

            cirCleOut.cirCle_name = outerString[i];
            outerPointCirCle[i] = cirCleOut;
        }
        calculatePointPosition2(12);
    }

    /**
     * 计算点所在的位置
     */
    public void calculatePointPosition1(int type) {

        PointCirCleInner pointCirCle;

        int radius = radiusInner;

        for (int i = 0; i < 6; i++) {

            pointCirCle = innerPointCirCle[i];

            pointCirCle.cirCle_X = (float) (circleX + radius * (Math.cos(pointCirCle.cirCle_angle * Math.PI / 180)));

            pointCirCle.cirCle_Y = (float) (cirCleY + radius * (Math.sin(pointCirCle.cirCle_angle * Math.PI / 180)));

        }

    }

    public void calculatePointPosition2(int type) {

        PointCirCleOut pointCirCle;

        int radius = radiusOuter;

        for (int i = 0; i < 12; i++) {

            pointCirCle = outerPointCirCle[i];

            pointCirCle.cirCle_X = (float) (circleX + radius
                    * (Math.cos(pointCirCle.cirCle_angle * Math.PI / 180)));

            pointCirCle.cirCle_Y = (float) (cirCleY + radius
                    * (Math.sin(pointCirCle.cirCle_angle * Math.PI / 180)));

        }

    }

    /**
     * 圆点的属性
     *
     * @author Administrator
     */
    public class PointCirCleInner {

        // 点的坐标
        public float cirCle_X;
        public float cirCle_Y;
        // 名称
        public String cirCle_name;
        // 角度
        public float cirCle_degress;
        // 起始角度
        public float cirCle_angle;
        // 是否被点中
        public boolean isChecked = false;
        // 当前点击的位置与点的左边（距离）
        public float cirCle_dx;
        public float cirCle_dy;
    }

    public class PointCirCleOut {

        // 点的坐标
        public float cirCle_X;
        public float cirCle_Y;
        // 名称
        public String cirCle_name;
        // 角度
        public float cirCle_degress;
        // 起始角度
        public float cirCle_angle;
        // 是否被点中
        public boolean isChecked = false;
        // 当前点击的位置与点的左边（距离）
        public float cirCle_dx;
        public float cirCle_dy;
    }

    /**
     * 绘制圆形球与文字
     */
    public void drawCircleText() {

        createInnerPoint();

        createOuterPoint();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 不做任何操作：：：
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:

                L.e("X=" + event.getX(), "Y=" + event.getY());
                // 计算手指滑动的角度,并且重新计算各个球的坐标
                resetPositionAngle2(event.getX(), event.getY(), 12);
                resetPositionAngle1(event.getX(), event.getY(), 6);

                // 重新放置各个球的位置
                calculatePointPosition1(6);
                calculatePointPosition2(12);

                // 执行

                invalidate();

                break;

            case MotionEvent.ACTION_UP:

                // 判断点击的位置是否在所绘制的图案上，进行事件处理

                checkClickCircle1(event.getX(), event.getY(), 6);
                checkClickCircle2(event.getX(), event.getY(), 12);

                tempDegress = 0;

                tempDegress1 = 0;
                // 重新放置各个球的位置
                calculatePointPosition1(6);
                calculatePointPosition2(12);

                // 执行

                invalidate();

                break;

        }

        return super.dispatchTouchEvent(event);
    }

    private void checkClickCircle1(float x, float y, int type) {
        // TODO Auto-generated method stub
        float distances = 0;

        for (PointCirCleInner mPointCirCle : innerPointCirCle) {

            distances = (float) Math.sqrt(((x - mPointCirCle.cirCle_X) * (x - mPointCirCle.cirCle_X) + (y - mPointCirCle.cirCle_Y) * (y - mPointCirCle.cirCle_Y)));

            if (distances < 35) {
                mPointCirCle.isChecked = true;
            } else {
                mPointCirCle.isChecked = false;
            }

        }

        for (PointCirCleInner mPointCirCle : innerPointCirCle) {
            if (mPointCirCle.isChecked) {
                onRorateListenser.onCircleInnerLinstener(mPointCirCle);
            }
        }
    }

    private void checkClickCircle2(float x, float y, int type) {
        // TODO Auto-generated method stub
        float distances = 0;

        for (PointCirCleOut mPointCirCle : outerPointCirCle) {

            distances = (float) Math.sqrt(((x - mPointCirCle.cirCle_X) * (x - mPointCirCle.cirCle_X) + (y - mPointCirCle.cirCle_Y) * (y - mPointCirCle.cirCle_Y)));

            if (distances < 35) {
                mPointCirCle.isChecked = true;
            } else {
                mPointCirCle.isChecked = false;
            }

        }

        for (PointCirCleOut mPointCirCle : outerPointCirCle) {
            if (mPointCirCle.isChecked) {
                onRorateListenser.onCircleOuterLinstener(mPointCirCle);
            }
        }
    }

    /**
     * 重置球的角度
     *
     * @param x 移动后当前的X位置
     * @param y 移动后当前的Y位置
     * @param i 判定是内圆还是外圆
     */
    private void resetPositionAngle1(float x, float y, int i) {
        // TODO Auto-generated method stub
        // 计算滑动的角度
        float degress = logicAngle1(x, y);
        int type = i;

        // 重置各个球的位置
        for (int k = 0; k < type; k++) {


            innerPointCirCle[k].cirCle_angle += degress;

            if (innerPointCirCle[k].cirCle_angle > 360) {

                innerPointCirCle[k].cirCle_angle -= 360;

            } else if (innerPointCirCle[k].cirCle_angle < 0) {

                innerPointCirCle[k].cirCle_angle += 360;

            }

        }
    }

    private void resetPositionAngle2(float x, float y, int i) {
        // TODO Auto-generated method stub
        // 计算滑动的角度
        float degress = logicAngle2(x, y);

        int type = i;

        // 重置各个球的位置
        for (int k = 0; k < type; k++) {


            outerPointCirCle[k].cirCle_angle += degress;

            if (outerPointCirCle[k].cirCle_angle > 360) {

                outerPointCirCle[k].cirCle_angle -= 360;

            } else if (outerPointCirCle[k].cirCle_angle < 0) {

                outerPointCirCle[k].cirCle_angle += 360;

            }

        }
    }

    /**
     * 计算滑动的角度
     *
     * @param x
     * @param y
     * @return
     */
    private float logicAngle1(float x, float y) {
        // TODO Auto-generated method stub
        float resultAngle = 0;
        // 获取当前球到圆心的距离
        float distances = (float) Math.sqrt(((x - circleX) * (x - circleX) + (y - cirCleY) * (y - cirCleY)));
        // 根反玄函数可以得到旋转的度数
        float degress = (float) (Math.acos((x - circleX) / distances) * 180 / Math.PI);

        if (y < cirCleY) {
            // 说明在圆心之上位置

            degress = -degress;
        }

        // 为了防止首次tempDegress初始值为0时 的情况
        if (tempDegress != 0) {

            resultAngle = degress - tempDegress;
        }

        tempDegress = degress;

        return resultAngle;
    }

    private float logicAngle2(float x, float y) {
        // TODO Auto-generated method stub
        float resultAngle = 0;
        // 获取当前球到圆心的距离
        float distances = (float) Math.sqrt(((x - circleX) * (x - circleX) + (y - cirCleY) * (y - cirCleY)));
        // 根反玄函数可以得到旋转的度数
        float degress = (float) (Math.acos((x - circleX) / distances) * 180 / Math.PI);

        if (y < cirCleY) {
            // 说明在圆心之上位置

            degress = -degress;
        }

        // 为了防止首次tempDegress初始值为0时 的情况
        if (tempDegress1 != 0) {

            resultAngle = degress - tempDegress1;
        }

        tempDegress1 = degress;

        return resultAngle;
    }

    public interface OnRorateListenser {
        public void onCircleInnerLinstener(PointCirCleInner pointCirCle);

        public void onCircleOuterLinstener(PointCirCleOut pointCirCle);
    }

    public void setOnRorateListenser(OnRorateListenser onRorateListenser) {
        this.onRorateListenser = onRorateListenser;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            return false;
        }
        return true;
    }

}
