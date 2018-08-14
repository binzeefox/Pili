package com.forradical.binzee.collectionforlisab.activities.main.fragments.main;

import android.util.Log;

import com.forradical.binzee.collectionforlisab.base.FoxActivity;
import com.forradical.binzee.collectionforlisab.base.FoxApplication;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.base.mvp.BasePresenter;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

class Presenter extends BasePresenter<MainContract.View, MainContract.Model> implements MainContract.Presenter {

    public Presenter(MainContract.View view) {
        super(view);
        model = new Model();
    }

    @Override
    public void requestPictures() {
        view.onLoading();
        model.getPictures().subscribe(new Observer<List<ImageBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
                dContainer.add(d);
            }

            @Override
            public void onNext(List<ImageBean> beanList) {
                Collections.reverse(beanList);
                FoxApplication.getFullList().clear();
                FoxApplication.getFullList().addAll(beanList);
                Log.d("MainFragment.Presenter", "onNext:" + beanList.toString());
                view.refresh();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("MainFragment.Presenter", "onError:");
                view.onLoaded();
            }

            @Override
            public void onComplete() {
                Log.d("MainFragment.Presenter", "onComplete:");
                view.onLoaded();
            }
        });
    }

    @Override
    public void showDetail(ImageBean bean) {
        //TODO 进入图片详情
    }

    @Override
    public void addPicture(ImageBean bean) {
        boolean isOkay = model.addPicture(bean);
        view.notice(isOkay ? "添加图片成功" : "添加图片失败");
    }

    @Override
    public void deletePicture(ImageBean bean) {
        model.deletePicture(bean);
    }
}
