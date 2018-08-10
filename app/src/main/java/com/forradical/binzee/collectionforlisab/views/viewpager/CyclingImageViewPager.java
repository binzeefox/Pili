package com.forradical.binzee.collectionforlisab.views.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.forradical.binzee.collectionforlisab.base.FoxActivity;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.utils.RxHelp;
import com.forradical.binzee.collectionforlisab.views.adapters.ImageViewPagerAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CyclingImageViewPager extends ViewPager {
    private ImageViewPagerAdapter mAdapter;
    private boolean mIsScrolling = false;
    private ViewPagerPointer mPointer;
    private Disposable timer;

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
    public void setData(Context context, List<ImageBean> data, boolean isRound){
        mAdapter = new ImageViewPagerAdapter(context, data, isRound);
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
                int max = mAdapter.getCount();
                int position = getCurrentItem();

                //滑动结束
                if (state == ViewPager.SCROLL_STATE_IDLE){
                    mIsScrolling = false;
                    if (position == 0) {
                        position = max - 2;
                        setCurrentItem(position, false);
                    }
                    if (position == max - 1) {
                        position = 1;
                        setCurrentItem(position, false);
                    }
                }
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    mIsScrolling = true;
                }
                if(state == ViewPager.SCROLL_STATE_SETTLING){
                    if (mPointer != null)
                        mPointer.setCurrent(position);
                }
            }
        });
    }

    /**
     * 开启循环
     */
    public void beginCycling(){
        if (mAdapter == null)
//            throw new IllegalStateException("Call 'setImageData' first!!!");
            return;
        timer = Observable
                .interval(3000L, 4500L, TimeUnit.MILLISECONDS)
                .compose(RxHelp.<Long>applySchedulers())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (mIsScrolling){
                            return;
                        }
                        int cur = getCurrentItem();
                        int max = getAdapter().getCount();
                        cur++;
                        if (cur >= max)
                            cur = 0;
                        setCurrentItem(cur);
                    }
                });
        ((FoxActivity)getContext()).dContainer.add(timer);
    }

    /**
     * 停止循环
     */
    public void stopCycling(){
        if (timer != null && !timer.isDisposed())
            timer.dispose();
    }

    /**
     * 设置监听器
     */
    public void setOnItemClickListener(ImageViewPagerAdapter.OnItemClickListener listener){
        mAdapter.setOnItemClickListener(listener);
    }

    /**
     * 设置图片治时期
     * @param pointer   指示器
     */
    public void setPointer(ViewPagerPointer pointer, boolean clickable){
        mPointer = pointer;
        mPointer.setUpViewPager(this, true);
        mPointer.setClickable(clickable);
    }

    public void notifyDataSetChanged(){
        mAdapter.notifyDataSetChanged();
    }
}
