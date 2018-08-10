package com.forradical.binzee.collectionforlisab.views.adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.forradical.binzee.collectionforlisab.activities.photodetail.PhotoDetailActivity;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.utils.ImageUtil;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.util.ArrayList;
import java.util.List;

public class DetailViewPagerAdapter extends PagerAdapter {
    private List<PhotoView> imgViews;   // 装有展示图片的部件
    private Context mContext;
    private List<ImageBean> data;

    public DetailViewPagerAdapter(Context ctx, List<ImageBean> datas) {
        super();
        mContext = ctx;
        this.data = datas;
        imgViews = new ArrayList<>();
        convertViews();
    }

    public ImageBean getBeanByPosition(int position){
        return data.get(position);
    }

    /**
     * 处理Views
     */
    private void convertViews() {
        // 为每一张图片准备一个ImageViews
        for (ImageBean bean : data) {
            PhotoView view = new PhotoView(mContext);
            PhotoViewAttacher attacher = new PhotoViewAttacher(view);
            attacher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((PhotoDetailActivity) mContext).onBackPressed();
                }
            });
            view.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imgViews.add(view);
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(imgViews.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final ImageBean bean = data.get(position);
        ImageView view = imgViews.get(position);
        container.addView(view);

        //如果Path为空，显示占位图
        if (bean.getPath() != null) {
            ImageUtil imageUtil = new ImageUtil(mContext, bean.getPath());
            imageUtil.show(view);
        } else {
            //TODO 测试数据，应改为 bean.getPath();
            ImageUtil imageUtil = new ImageUtil(mContext, bean.getId());
            imageUtil.show(view);
        }
        return view;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
