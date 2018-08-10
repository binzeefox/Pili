package com.forradical.binzee.collectionforlisab.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

import com.forradical.binzee.collectionforlisab.base.FoxActivity;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.utils.FileUtil;
import com.forradical.binzee.collectionforlisab.utils.RxHelp;
import com.forradical.binzee.collectionforlisab.views.adapters.AddingPagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

public class AddingPager extends ViewPager {

    public AddingPager(@NonNull Context context) {
        super(context);
    }

    public AddingPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(@NonNull final FoxActivity ctx, final List<String> rawData, final List<ImageBean> imageBeans){
        final List<String> data = new ArrayList<>();

        ctx.showLoadingDialog("请稍后", null);
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                Log.d("AddingPagerAdapter", "处理图片");
                for (String path : rawData) {
                    String newPath = FileUtil.copyImage(new File(path));
                    data.add(newPath);
                }
                Log.d("AddingPagerAdapter", "处理完成");
                emitter.onComplete();
            }
        }).compose(RxHelp.applySchedulers())
                .subscribe(new RxHelp.CompleteObserver<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        ctx.dContainer.add(d);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ctx.dismissDialog();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("AddingPagerAdapter", "注销弹窗");
                        ctx.dismissDialog();
                        AddingPagerAdapter adapter = new AddingPagerAdapter(ctx, data);
                        adapter.setImageData(imageBeans);
                        setAdapter(adapter);
                    }
                });
    }

    @Nullable
    @Override
    public AddingPagerAdapter getAdapter() {
        return (AddingPagerAdapter) super.getAdapter();
    }
}
