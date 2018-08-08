package com.forradical.binzee.collectionforlisab.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.forradical.binzee.collectionforlisab.R;
import com.zhihu.matisse.engine.ImageEngine;

public class GlideImageEngine implements ImageEngine {


    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholder)//这里可自己添加占位图
//                .error(R.drawable.error)//这里可自己添加出错图
                .override(resize, resize);
        Glide.with(context)
                .asBitmap()  // some .jpeg files are actually gif
                .load(uri)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {

    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        RequestOptions options = new RequestOptions()
                .override(resizeX, resizeY)
                .priority(Priority.HIGH);
        Glide.with(context)
                .load(uri)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {

    }

    @Override
    public boolean supportAnimatedGif() {
        return false;
    }
}
