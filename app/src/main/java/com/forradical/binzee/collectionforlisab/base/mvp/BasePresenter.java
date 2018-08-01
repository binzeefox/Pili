package com.forradical.binzee.collectionforlisab.base.mvp;

public abstract class BasePresenter<V extends IBaseView, M extends IBaseModel> implements IBasePresenter {
    protected V view;
    protected M model;

    public BasePresenter(V view){
        onBind(view);
    }

    protected void onBind(V view){
        this.view = view;
    }
}
