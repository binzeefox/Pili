package com.forradical.binzee.collectionforlisab.activities.main.fragments.main;

import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.base.mvp.BasePresenter;

import java.util.List;
import java.util.Observable;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class Presenter extends BasePresenter<MainContract.View, MainContract.Model> implements MainContract.Presenter {

    public Presenter(MainContract.View view) {
        super(view);
        model = new Model();
    }

    @Override
    public void requestPictures() {
        Disposable task = model.getPictures().subscribe(new Consumer<List<ImageBean>>() {
            @Override
            public void accept(List<ImageBean> imageBeanList) throws Exception {
                view.showPictures(imageBeanList);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                view.notice("获取图片失败");
            }
        });
        dContainer.add(task);
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
