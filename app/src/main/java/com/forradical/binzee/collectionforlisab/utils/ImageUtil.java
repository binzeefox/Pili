package com.forradical.binzee.collectionforlisab.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.forradical.binzee.collectionforlisab.base.FoxApplication;

public class ImageUtil {
    private static final RequestOptions roundOptions;
    private static final RequestOptions blueOptions;
    private static final UsefulTransformation roundTransformation;
    private static final UsefulTransformation blueTransformation;

    static {
        roundTransformation = new UsefulTransformation(UsefulTransformation.OPTION_ROUND);
        roundTransformation.setRoundRadius(4);
        blueTransformation = new UsefulTransformation(UsefulTransformation.OPTION_BLUE);
        blueTransformation.setBlueRadius(10);
        roundOptions = new RequestOptions()
                .transforms(roundTransformation);
        blueOptions = new RequestOptions()
                .transform(blueTransformation);
    }

    private RequestBuilder<Drawable> mGlide;
    private boolean isRound = false;
    private boolean isBlue = false;

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
        } else if (isBlue){
            glide = mGlide.apply(blueOptions);
        }
        glide.into(view);
    }

    public ImageUtil setRound(boolean isRound) {
        this.isRound = isRound;
        isBlue = !isRound;
        return this;
    }

    public ImageUtil setBlue(boolean isBlue) {
        this.isBlue = isBlue;
        isRound = !isBlue;
        return this;
    }
}
