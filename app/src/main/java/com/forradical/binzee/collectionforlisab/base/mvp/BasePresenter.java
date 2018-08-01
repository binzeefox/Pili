package com.forradical.binzee.collectionforlisab.base.mvp;

public abstract class BasePresenter<T extends IBaseView> implements IBasePresenter {
    protected T view;

    public BasePresenter(T view){
        onBind(view);
    }

    protected void onBind(T view){
        this.view = view;
    }
}
