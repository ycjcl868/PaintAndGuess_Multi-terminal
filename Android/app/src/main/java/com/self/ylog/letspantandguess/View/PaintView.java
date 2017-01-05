package com.self.ylog.letspantandguess.View;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.self.ylog.letspantandguess.R;
import com.self.ylog.letspantandguess.Utils.Size;
import com.self.ylog.letspantandguess.Utils.UserInfo;

import java.util.ArrayList;



public class PaintView extends View implements OnTouchListener{

    // judge your fingers' tremble
    public static final float TOUCH_TOLERANCE = 4;
    // judge long pressed
    public static final long TOUCH_LONG_PRESSED = 500;
    Paint mPaint = new Paint();
    Paint mEraserPaint = new Paint();
    private Path mPath;
    private Paint mBitmapPaint;
    private int width;
    private int height;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Context context;
    PaintView paintView;

    public static boolean IsPaint = true;
    private float mX, mY;

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Init_Paint(UserInfo.PaintColor, UserInfo.PaintWidth);
        Init_Eraser(UserInfo.Ereaser_color,UserInfo.EraserWidth);
        //获取设备宽高
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = manager.getDefaultDisplay().getWidth();
        height = manager.getDefaultDisplay().getHeight();

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        setOnTouchListener(this);

//        getSize();
    }

    public PaintView(Context context) {
        super(context);
        this.context = context;
        Init_Paint(UserInfo.PaintColor, UserInfo.PaintWidth);
        Init_Eraser(UserInfo.Ereaser_color,UserInfo.EraserWidth);
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = manager.getDefaultDisplay().getWidth();
        height = manager.getDefaultDisplay().getHeight();

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        setOnTouchListener(this);
    }

//    public void getSize(){
//        final Size msize=new Size();
//
//        final PaintView paintView;
//        paintView=(PaintView) findViewById(R.id.paint_view);
//        ViewTreeObserver vto2 = paintView.getViewTreeObserver();
//        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                paintView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
////                Toast.makeText(getContext(),"imageView.getHeight()+\",\"+imageView.getWidth()",Toast.LENGTH_LONG).show();
////                Log.i("test",paintView.getHeight()+" "+paintView.getWidth());
//                msize.setmX(paintView.getWidth());
//                msize.setmY(paintView.getHeight());
//                Log.i("test",msize.getmX()+" "+msize.getmY());
//            }
//        });
//    }

    // init paint
    private void Init_Paint(int color, int width) {
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(width);
    }

    // init eraser
    private void Init_Eraser(int color,int width) {
        mEraserPaint.setAntiAlias(true);
        mEraserPaint.setDither(true);
        mEraserPaint.setStrokeWidth(width);
        mEraserPaint.setStyle(Paint.Style.STROKE);
        mEraserPaint.setStrokeJoin(Paint.Join.ROUND);
        mEraserPaint.setStrokeCap(Paint.Cap.SQUARE);
        // The most important
//        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    public void IsPaint(Boolean IsPaint){
        this.IsPaint=IsPaint;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action_dowm(x,y);
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                action_move(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                action_up(x,y);
                invalidate();
                break;
        }

        return true;
    }

    public void action_dowm(int x, int y){
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        Log.i("point",mX+" "+mY);
    }

    public void action_move(int x, int y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
        Log.i("point",mX+" "+mY);
    }

    public void action_up(int x, int y){
        Log.i("point",mX+" "+mY);
        mPath.lineTo(mX, mY);
        if(IsPaint)
            mCanvas.drawPath(mPath, mPaint);
        else
            mCanvas.drawPath(mPath, mEraserPaint);
        mPath.reset();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mEraserPaint.setColor(Color.WHITE);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        if(IsPaint)
            canvas.drawPath(mPath, mPaint);
        else
            canvas.drawPath(mPath, mEraserPaint);
    }

    public void clean() {
        mBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

}
