package com.forradical.binzee.collectionforlisab.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;

public class ImageUtil {
//    public static final String HOLDER_URL = "https://fakeimg.pl/200x100/?retina=1&text=PIMI%20!%20!&font=noto";

    private RequestBuilder<Drawable> mGlide;
    private RequestOptions roundOptions;

    public ImageUtil(Context ctx, String path){
        mGlide = Glide.with(ctx).load(path);

        UsefulTransformation transformation = new UsefulTransformation(ctx, UsefulTransformation.OPTION_ROUND);
        transformation.setRoundRadius(20);
        roundOptions = new RequestOptions()
                .transforms(transformation);
    }

    public ImageUtil(Context ctx, int resource){
        mGlide = Glide.with(ctx).load(resource);

        UsefulTransformation transformation = new UsefulTransformation(ctx, UsefulTransformation.OPTION_ROUND);
        transformation.setRoundRadius(20);
        roundOptions = new RequestOptions()
                .transforms(transformation);
    }

    /**
     * 显示圆角图片
     * @param view  目标部件
     */
    public void showRoundImage(ImageView view){
        mGlide.apply(roundOptions).into(view);
    }

    /**
     * 显示图片
     * @param view  目标部件
     */
    public void show(ImageView view){
        mGlide.into(view);
        mGlide = null;
    }
}
