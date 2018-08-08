package com.forradical.binzee.collectionforlisab.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;

public class ImageUtil {
    private static final RequestOptions roundOptions;
    private static final UsefulTransformation transformation;

    static {
        transformation = new UsefulTransformation(UsefulTransformation.OPTION_ROUND);
        transformation.setRoundRadius(4);
        roundOptions = new RequestOptions()
                .transforms(transformation);
    }

    private RequestBuilder<Drawable> mGlide;
    private boolean isRound = false;

    public ImageUtil(Context ctx, String path) {
        mGlide = Glide.with(ctx).load(path);
    }

    public ImageUtil(Context ctx, int resource) {
        mGlide = Glide.with(ctx).load(resource);
    }

    /**
     * 显示图片
     *
     * @param view 目标部件
     */
    public void show(ImageView view) {
        RequestBuilder<Drawable> glide = mGlide.clone();
        if (isRound) {
            glide = mGlide.apply(roundOptions);
        }
        glide.into(view);
    }

    public void setRound(boolean isRound) {
        this.isRound = isRound;
    }
}
