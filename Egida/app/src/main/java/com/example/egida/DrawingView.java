package com.example.egida;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.security.NoSuchAlgorithmException;

public class DrawingView extends View {
    public int width;
    public  int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint;
    private Paint circlePaint;
    private Path circlePath;

    private Path mPath;
    private Paint mPaint;

    private void setup(){
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLACK);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
    }

    private void init(@NonNull AttributeSet set){}

    public DrawingView(Context context) {
        super(context);

        setup();
    }

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);

        init(attrs);

        setup();
    }


    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);

        setup();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    private static String seed = "";

    public static String getKey() throws NoSuchAlgorithmException {
        ShaEncoder encoder = new ShaEncoder(seed);
        return encoder.sha256EncodeInput().substring(0, 16);
    }

    private float mX, mY;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath( mPath,  mPaint);
        canvas.drawPath( circlePath,  circlePaint);
    }

    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) throws NoSuchAlgorithmException {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        Log.i("bruh", getKey());
    }

    private void touch_move(float x, float y) throws NoSuchAlgorithmException {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;

            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
        Integer tmp = Math.round(dx) + Math.round(dy);

        ShaEncoder encoder = new ShaEncoder(tmp.toString());

        seed += encoder.sha256EncodeInput().substring(0, 3);
        if (seed.length() > 16)
            seed.substring(0, 16);
        Log.i("bruh", seed);
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        circlePath.reset();
        mCanvas.drawPath(mPath,  mPaint);
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    touch_start(x, y);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                try {
                    touch_move(x, y);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }
}
