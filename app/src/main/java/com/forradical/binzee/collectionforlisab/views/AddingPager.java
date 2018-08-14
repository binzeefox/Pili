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

    public void setData(@NonNull final FoxActivity ctx, final List<ImageBean> imageBeans){
        AddingPagerAdapter adapter = new AddingPagerAdapter(ctx, imageBeans);
        setAdapter(adapter);
    }

    @Nullable
    @Override
    public AddingPagerAdapter getAdapter() {
        return (AddingPagerAdapter) super.getAdapter();
    }
}
