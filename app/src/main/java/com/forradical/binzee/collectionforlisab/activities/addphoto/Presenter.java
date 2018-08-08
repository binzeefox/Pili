package com.forradical.binzee.collectionforlisab.activities.addphoto;

import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.base.mvp.BasePresenter;

import java.util.List;

class Presenter extends BasePresenter<AddPhotoContract.View, AddPhotoContract.Model> implements AddPhotoContract.Presenter {

    public Presenter(AddPhotoContract.View view) {
        super(view);
        model = new Model();
    }

    @Override
    public void savePhotos(List<ImageBean> beanList) {
        view.showAddingDialog(model.savePhotos(beanList));
    }
}
