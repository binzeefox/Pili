package com.forradical.binzee.collectionforlisab.CustomViews.cycling_image_view_pager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

import com.forradical.binzee.collectionforlisab.base.FoxActivity;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CyclingImageViewPager extends ViewPager {
    private ImageViewPagerAdapter mAdapter;

    public CyclingImageViewPager(@NonNull Context context, List<ImageBean> data) {
        super(context);
        mAdapter = new ImageViewPagerAdapter(context, data);
        setAdapter(mAdapter);

        setCurrentItem(1, false);
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int rawMax = mAdapter.getCount();

                if (state == 0){
                    int position = getCurrentItem();
                    if (position == 0) {
                        position = rawMax - 2;
                        setCurrentItem(position, false);
                    }
                    if (position == rawMax - 1) {
                        position = 1;
                        setCurrentItem(position, false);
                    }
                }
            }
        });
    }

    /**
     * 开启循环
     */
    public void beginCycling(){
        Disposable disposable = Observable
                .interval(3000L, 4500L, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        int cur = getCurrentItem();
                        int max = getAdapter().getCount();
                        cur++;
                        if (cur >= max)
                            cur = 0;
                        setCurrentItem(cur);
                    }
                });
        ((FoxActivity)getContext()).dContainer.add(disposable);
    }
}
