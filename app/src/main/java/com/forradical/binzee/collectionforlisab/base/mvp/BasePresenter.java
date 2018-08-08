package com.forradical.binzee.collectionforlisab.base.mvp;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BasePresenter<V extends IBaseView, M extends IBaseModel> implements IBasePresenter {
    protected V view;
    protected M model;
    protected CompositeDisposable dContainer;

    public BasePresenter(V view) {
        dContainer = new CompositeDisposable();
        onBind(view);
    }

    protected void onBind(V view) {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        if (dContainer != null && !dContainer.isDisposed())
            dContainer.dispose();
    }
}
