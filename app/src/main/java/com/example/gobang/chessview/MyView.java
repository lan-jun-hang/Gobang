package com.example.gobang.chessview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gobang.R;

import java.util.ArrayList;

public class MyView extends View {

    private Context mContext;

    private Paint mPaint;  // 画笔
    private Bitmap wPieces; // 白棋子
    private Bitmap bPieces; // 黑棋子

    //棋子坐标集合 坐标类型
    private ArrayList<Point> wPoints;
    private ArrayList<Point> bPoints;

    private int mPanelWith;     // 棋盘宽度（和高度一样）
    private float mLineHeight;  // 棋盘每一行高度
    private static final int MAX_LINE = 10; // 棋盘有多少行
    private float radioPiece = 1.0f * 3 / 4;

    private IsChessWin mIsChessWin;
    private boolean gameOverMethod; // 是否结束
    private boolean wChessStart = true; // 是否白棋先走

    public Activity mActivity;

    public MyView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();

        // 设置画笔颜色
        mPaint.setColor(Color.BLACK);
        // 设置抗锯齿
        mPaint.setAntiAlias(true);
        // 设置防抖动
        mPaint.setDither(true);
        // 设置画笔风格为空心
        mPaint.setStyle(Paint.Style.STROKE);
        // 获取棋子
        wPieces = BitmapFactory.decodeResource(getResources(), R.mipmap.stone_w2);
        bPieces = BitmapFactory.decodeResource(getResources(), R.mipmap.stone_b1);
        // 棋子坐标点集合初始化
        wPoints = new ArrayList<>();
        bPoints = new ArrayList<>();
    }

    //计算棋盘大小，自定义view尺寸规则，使其为最大正方形
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获取屏幕宽度、高度
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        // 由于棋盘是正方形，因为要选取正方形最大值，就要以屏幕长和宽最小值为准
        int size = Math.min(widthSize, heightSize);
        // 设置棋盘实际长和宽
        setMeasuredDimension(size, size);
    }

    // view 尺寸改变会调用这个方法
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWith = w; // 棋盘宽度
        mLineHeight = mPanelWith * 1.0f / MAX_LINE;  // 一行高度：宽度 / 10行
        int piecesWidth = (int) (mLineHeight * radioPiece);
        // 对棋子图片进行缩放
        wPieces = Bitmap.createScaledBitmap(wPieces, piecesWidth, piecesWidth, true);
        bPieces = Bitmap.createScaledBitmap(bPieces, piecesWidth, piecesWidth, true);
    }

    // 绘制图形
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);  // 自定义方法 绘制棋盘
        drawPieces(canvas); // 自定义方法 绘制棋子

        mIsChessWin = new IsChessWin(mContext);
        gameOverMethod = mIsChessWin.isGameOverMethod(wPoints, bPoints);
        if (gameOverMethod) {  // 游戏结束
            showDialog(); // 显示对话框
        }

    }


    /**
     * 绘制棋盘
     *
     * @param canvas
     */
    private void drawBoard(Canvas canvas) {
        int w = mPanelWith; // 棋盘宽度
        float lineHeight = mLineHeight; // 每行高度
        for (int i = 0; i < MAX_LINE; i++) {
            int startX = (int) (lineHeight / 2); // 棋盘横线距离手机屏幕左侧小距离
            int endX = (int) (w - lineHeight / 2);// 终点X横坐标为宽度减去半个lineHeight（棋盘空格宽度）
            int y = (int) ((0.5 + i) * lineHeight); // 在原有基础上移动lineHeight倍的距离，初始距离为：0.5*lineHeight即为间距

            // 画横线
            canvas.drawLine(startX, y, endX, y, mPaint);
            // 画纵线，坐标相反
            canvas.drawLine(y, startX, y, endX, mPaint);
        }
    }

    /**
     * 绘制棋子
     * 从存储数据的动态数组中找到坐标，画出对应棋子
     *
     * @param canvas
     */
    private void drawPieces(Canvas canvas) {
        // 白棋
        for (int i = 0; i < wPoints.size(); i++) {
            Point point = wPoints.get(i);
            // 移动的距离为：1 / 2 - radioPoeces/2 = (1 - radioPoeces) / 2;
            canvas.drawBitmap(wPieces, (point.x + 1.0f / 2 - radioPiece / 2) * mLineHeight, (point.y + (1 - radioPiece) / 2) * mLineHeight, null);
        }
        // 黑棋
        for (int i = 0; i < bPoints.size(); i++) {
            Point point = bPoints.get(i);
            canvas.drawBitmap(bPieces, (point.x + (1 - radioPiece) / 2) * mLineHeight, (point.y + (1 - radioPiece) / 2) * mLineHeight, null);
        }
    }

    /**
     * 弹出对话框
     */
    private void showDialog() {
        String res = mIsChessWin.whiteWinFlag() ? "白棋获胜！" : "黑棋获胜！";
        new AlertDialog.Builder(mContext)
                .setMessage("恭喜" + res + "是否再来一局？")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Restart();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }


    public void Restart() {
        wPoints.clear();
        bPoints.clear();
        gameOverMethod = false;
        invalidate(); // view 恢复，系统方法
    }

    /**
     * 屏幕的触摸事件，获取坐标集合
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gameOverMethod = mIsChessWin.isGameOverMethod(wPoints, bPoints);
        if (gameOverMethod) {
            showDialog();
            return false;
        }
        int action = event.getAction();
        // 屏幕中手指按下再抬起的事件
        if (action == MotionEvent.ACTION_UP) {
            // 获取x,y轴坐标
            int x = (int) event.getX();
            int y = (int) event.getY();
            // 根据手指触碰的坐标 计算出棋子在棋盘的坐标
            Point point = new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
            // 如果当前黑棋或者白棋的坐标点已经在集合里面（重复落点），则坐标点无效
            if (wPoints.contains(point) || bPoints.contains(point)) {
                return false;
            }
            // 判断是否白棋先走，否则添加黑棋坐标
            if (wChessStart) {
                wPoints.add(point);
            } else {
                bPoints.add(point);
            }
            wChessStart = !wChessStart; // 这回白棋先走，下回黑棋先走
            invalidate(); // 刷新view
            return true;
        }
        return true;
    }


    /**
     * 当view因为某种原因（比如系统回收）销毁时，保存状态
     * onSaveInstanceState的调用遵循一个原则，即当系统“未经你许可“时销毁了activity
     * 则onSaveInstanceState被调用，提供一个让你保存你的数据的机会
     * <p>
     *
     * @return
     */
    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAMEOVER = "instance_GameOver";
    private static final String INSTANCE_WHITARRAY = "instance_whiteArray";
    private static final String INSTANCE_BLACKARRAY = "instance_blackArray";

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        // 保存系统默认状态
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        // 保存是否结束游戏的值
        bundle.putBoolean(INSTANCE_GAMEOVER, gameOverMethod);
        // 保存白棋的坐标点的集合
        bundle.putParcelableArrayList(INSTANCE_WHITARRAY, wPoints);
        // 黑棋点坐标集合
        bundle.putParcelableArrayList(INSTANCE_BLACKARRAY, bPoints);
        return bundle;
    }

    /**
     * 取出保存的值
     *
     * @param state
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            gameOverMethod = bundle.getBoolean((INSTANCE_GAMEOVER));
            wPoints = Bundle.EMPTY.getParcelableArrayList(INSTANCE_WHITARRAY);
            bPoints = Bundle.EMPTY.getParcelableArrayList(INSTANCE_BLACKARRAY);
            return;
        }
        super.onRestoreInstanceState(state);

    }


}
