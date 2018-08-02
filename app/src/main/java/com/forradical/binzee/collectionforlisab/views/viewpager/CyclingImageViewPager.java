package com.forradical.binzee.collectionforlisab.views.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.forradical.binzee.collectionforlisab.base.FoxActivity;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.utils.RxHelp;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CyclingImageViewPager extends ViewPager {
    private ImageViewPagerAdapter mAdapter;

    public CyclingImageViewPager(@NonNull Context context) {
        super(context, null);
    }

    public CyclingImageViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置数据
     * @param context 上下文
     * @param data  数据
     */
    public void setData(Context context, List<ImageBean> data){
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
        if (mAdapter == null)
            throw new IllegalStateException("Call 'setData' first!!!");
        Disposable disposable = Observable
                .interval(3000L, 4500L, TimeUnit.MILLISECONDS)
                .compose(RxHelp.<Long>applySchedulers())
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

    public void setRound(boolean isRound){
        mAdapter.setRoundImage(isRound);
    }
}
