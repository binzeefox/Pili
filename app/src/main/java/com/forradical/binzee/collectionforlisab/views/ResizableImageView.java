package com.forradical.binzee.collectionforlisab.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ResizableImageView extends android.support.v7.widget.AppCompatImageView {
    public float ratio = -1F;

    public ResizableImageView(Context ctx, float ratio){
        super(ctx);
        this.ratio = ratio;
    }

    public ResizableImageView(Context context) {
        super(context);
    }

    public ResizableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Drawable d = getDrawable();
        int width = MeasureSpec.getSize(widthMeasureSpec); //高度根据使得图片的宽度充满屏幕计算而得
        if (d != null) {
            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
            setMeasuredDimension(width, height);
        } else if (ratio > 0){
            int height = (int) (width * ratio);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
