package com.forradical.binzee.collectionforlisab.activities.main.fragments.main;

import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.base.mvp.IBaseModel;
import com.forradical.binzee.collectionforlisab.base.mvp.IBasePresenter;
import com.forradical.binzee.collectionforlisab.base.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

public interface MainContract {
    interface View extends IBaseView{
        void showPictures(List<ImageBean> dataList);
        void showAddWindow();
        void notice(String text);
    }

    interface Presenter extends IBasePresenter{
        void requestPictures();
        void showDetail(ImageBean bean);
        void addPicture(ImageBean bean);
    }

    interface Model extends IBaseModel{
        Observable<List<ImageBean>> getPictures();
        boolean addPicture(ImageBean bean);
    }
}
