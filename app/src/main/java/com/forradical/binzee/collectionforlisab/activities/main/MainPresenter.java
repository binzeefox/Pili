package com.forradical.binzee.collectionforlisab.activities.main;

import com.forradical.binzee.collectionforlisab.base.mvp.BasePresenter;

public class MainPresenter extends BasePresenter<IMainContract.View, IMainContract.Model> implements IMainContract.Presenter {

    public MainPresenter(IMainContract.View view) {
        super(view);
        model = new MainModel();
    }
}