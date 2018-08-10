package com.forradical.binzee.collectionforlisab.activities.main.fragments.main;

import android.net.Uri;

import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.base.mvp.IBaseModel;
import com.forradical.binzee.collectionforlisab.base.mvp.IBasePresenter;
import com.forradical.binzee.collectionforlisab.base.mvp.IBaseView;

import java.util.List;

import io.reactivex.Observable;

interface MainContract {
    interface View extends IBaseView{
        void refresh(List<ImageBean> dataList);
        void notice(String text);
        void onLoading();
        void onLoaded();
    }

    interface Presenter extends IBasePresenter{
        void requestPictures();
        void showDetail(ImageBean bean);
        void addPicture(ImageBean bean);
        void deletePicture(ImageBean bean);
    }

    interface Model extends IBaseModel{
        Observable<List<ImageBean>> getPictures();
        boolean addPicture(ImageBean bean);
        void deletePicture(ImageBean bean);
    }
}
