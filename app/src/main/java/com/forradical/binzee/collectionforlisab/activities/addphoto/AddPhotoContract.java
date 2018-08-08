package com.forradical.binzee.collectionforlisab.activities.addphoto;

import android.media.Image;

import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.base.mvp.IBaseModel;
import com.forradical.binzee.collectionforlisab.base.mvp.IBasePresenter;
import com.forradical.binzee.collectionforlisab.base.mvp.IBaseView;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;

public interface AddPhotoContract {
    interface View extends IBaseView{
        void showAddingDialog(Observable<Integer> progressOb);
    }

    interface Presenter extends IBasePresenter{
        void savePhotos(List<ImageBean> beanList);
    }

    interface Model extends IBaseModel{
        Observable<Integer> savePhotos(List<ImageBean> beanList);
    }
}
