package com.forradical.binzee.collectionforlisab.activities.main.fragments.main;

import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.utils.DatabaseHelper;
import com.forradical.binzee.collectionforlisab.utils.ImageUtil;

import java.util.List;

import io.reactivex.Observable;

class Model implements MainContract.Model {

    @Override
    public Observable<List<ImageBean>> getPictures() {
        return DatabaseHelper.getAllImage();
    }

    @Override
    public boolean addPicture(ImageBean bean) {
        return DatabaseHelper.saveImage(bean);
    }

    @Override
    public void deletePicture(ImageBean bean) {
        DatabaseHelper.deletePicture(bean);
    }
}
