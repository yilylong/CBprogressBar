package com.zhl.rx.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhl.rx.R;

/**
 * 描述：
 * Created by zhaohl on 2018-11-27.
 */
public class ImageProgressBar extends View {
    private Paint paint;
    private Bitmap bgBitmap;
    private int bgResID = -1;
    private int roundConer;
    private int max = 100;
    private float progress = 0;
    private int coverColor = 0x77E6E9E7;
    private int mode = 0;
    private PorterDuff.Mode styleMode = PorterDuff.Mode.DST_IN;
    public ImageProgressBar(Context context) {
        this(context,null);
    }

    public ImageProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public ImageProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.ImageProgressBar);
        bgResID = array.getResourceId(R.styleable.ImageProgressBar_imagebg,-1);
        roundConer = (int) array.getDimension(R.styleable.ImageProgressBar_roundConer,5);
        coverColor = array.getColor(R.styleable.ImageProgressBar_coverColor,coverColor);
        mode = array.getInt(R.styleable.ImageProgressBar_styleMode,coverColor);
        switch (mode){
            case 0:
                styleMode = PorterDuff.Mode.DST_IN;
                break;
            case 1:
                styleMode = PorterDuff.Mode.SRC_IN;
                break;
        }
        array.recycle();
        if(bgResID!=-1){
            generateImageBg(bgResID);
        }else{
            throw new RuntimeException("need a image bg!");
        }
        setLayerType(View.LAYER_TYPE_SOFTWARE,paint);// 关掉硬件加速 否则 paint.setXfermode 无效
    }

    public void setImageBg(Bitmap bgBitmap){
        this.bgBitmap = bgBitmap;
        invalidate();
    }
    public void setImageBg(int resID){
        this.bgResID = resID;
        generateImageBg(bgResID);
        invalidate();
    }

    private void generateImageBg(int res) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        bgBitmap = BitmapFactory.decodeResource(getResources(),res,options);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawImageBg(canvas);
        drawProgress(canvas);
    }

    private void drawProgress(Canvas canvas) {
        paint.setColor(coverColor);
        paint.setXfermode(new PorterDuffXfermode(styleMode));
        canvas.drawRect(new Rect((int) ((progress/max)*getWidth()),0,getWidth(),getHeight()),paint);
        paint.setXfermode(null);
    }

    private void drawImageBg(Canvas canvas) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);// 这里不重新初始化一个画笔刷新后绘出的底图会出问题
        bgBitmap = Bitmap.createScaledBitmap(bgBitmap,getWidth(),getHeight(),true);
        canvas.drawRoundRect(new RectF(0,0,getWidth(),getHeight()),roundConer,roundConer,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bgBitmap,0,0,paint);
    }


    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if (progress > max) {
            progress = max;
        } else {
            this.progress = progress;
            postInvalidate();
        }
    }
}
