package com.forradical.binzee.collectionforlisab.activities.addphoto;

import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.utils.DatabaseHelper;

import java.util.List;

import io.reactivex.Observable;

class Model implements AddPhotoContract.Model {

    @Override
    public Observable<Integer> savePhotos(List<ImageBean> beanList) {
        return DatabaseHelper.saveImages(beanList);
    }
}
