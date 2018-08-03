package com.forradical.binzee.collectionforlisab.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.forradical.binzee.collectionforlisab.R;
import com.forradical.binzee.collectionforlisab.activities.main.MainActivity;
import com.forradical.binzee.collectionforlisab.base.FoxActivity;
import com.forradical.binzee.collectionforlisab.views.CustomDialogFragment;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

public class SplashActivity extends FoxActivity {

    @BindView(R.id.iv_splash)
    ImageView ivSplash;
    private boolean isTiming;

    @Override
    protected int onInflateLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);

        isTiming = true;
        Glide.with(this).load(R.drawable.devil).into(ivSplash);
        Observable timer = Observable
                .timer(2500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        navigate(MainActivity.class);
                        finish();
                    }
                });
        dContainer.add(timer.subscribe());
    }

    /**
     * 不显示工具栏
     */
    @Override
    protected boolean isShowToolbar() {
        return false;
    }
}
