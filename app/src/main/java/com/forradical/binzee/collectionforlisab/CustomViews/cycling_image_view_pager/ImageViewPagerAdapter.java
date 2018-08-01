package com.forradical.binzee.collectionforlisab.CustomViews.cycling_image_view_pager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.utils.ImageUtil;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class ImageViewPagerAdapter extends PagerAdapter {
    private List<ImageView> imgViews;   // 装有展示图片的部件
    private Context mContext;   // 上下文
    private List<ImageBean> data;   // 真正显示的所有图片的列表
    private OnItemClickListener listener;   // 点击监听器

    private boolean isRound = false;    // 是否圆角

    public ImageViewPagerAdapter(Context context, List<ImageBean> imageList){
        mContext = context;
        imgViews = new ArrayList<>();
        data = new ArrayList<>();
        convertViews(imageList);
    }

    /**
     * 处理Views
     * @param imageList 原始图片列表
     */
    private void convertViews(List<ImageBean> imageList) {
        data.add(imageList.get(imageList.size() - 1));
        data.addAll(imageList);
        data.add(imageList.get(0));


        // 为每一张图片准备一个ImageViews
        for (ImageBean bean : data){
            ImageView view = new ImageView(mContext);
            view.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgViews.add(view);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final ImageBean bean = data.get(position);
        ImageView view = imgViews.get(position);
        container.addView(view);

        ImageUtil imageUtil = new ImageUtil(mContext, bean.getPath());
        if (isRound)
            imageUtil.showRoundImage(view);
        else
            imageUtil.show(view);
        Glide.with(mContext).load(bean.getPath()).into(view);
        if (listener != null)
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClicked(bean);
                }
            });

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(imgViews.get(position));
    }

//    ******↓公共方法

    /**
     * 设置子项点击事件
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    /**
     * 设置是否圆角
     */
    public void setRoundImage(boolean isRound){
        this.isRound = isRound;
    }

//    ******↓点击监听

    public interface OnItemClickListener{
        void onClicked(ImageBean bean);
    }
}
