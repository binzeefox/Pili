package com.forradical.binzee.collectionforlisab.activities.main.fragments.main;

import android.util.Log;

import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.base.mvp.BasePresenter;

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
                view.refresh(beanList);
                Log.d("Presenter.java", "加载到的内容" + beanList.toString());
            }

            @Override
            public void onError(Throwable e) {
                view.onLoaded();
            }

            @Override
            public void onComplete() {
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
}
