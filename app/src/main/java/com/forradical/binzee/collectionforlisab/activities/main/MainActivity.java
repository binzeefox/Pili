package com.forradical.binzee.collectionforlisab.activities.main;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.forradical.binzee.collectionforlisab.R;
import com.forradical.binzee.collectionforlisab.base.FoxActivity;

public class MainActivity extends FoxActivity implements IMainContract.View{

    private IMainContract.Presenter mPresenter;

    @Override
    protected int onInflateLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        mPresenter = new MainPresenter(this);
    }

    @Override
    public FoxActivity getFoxActivity() {
        return this;
    }
}
