package com.forradical.binzee.collectionforlisab.utils;

import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class DatabaseHelper {

    /**
     * 获取数据库全部图片
     */
    public static Observable<List<ImageBean>> getAllImage() {
        return Observable.create(new ObservableOnSubscribe<List<ImageBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ImageBean>> emitter) throws Exception {
                emitter.onNext(LitePal.findAll(ImageBean.class));
                emitter.onComplete();
            }
        }).compose(RxHelp.<List<ImageBean>>applySchedulers());
    }

    /**
     * 根据标签获取图片
     */
    public static Observable<List<ImageBean>> getImageFromType(final String... types) {
        return Observable.create(new ObservableOnSubscribe<List<ImageBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ImageBean>> emitter) throws Exception {
                List<ImageBean> raw = LitePal.findAll(ImageBean.class);
                List<ImageBean> result = new ArrayList<>();
                for (ImageBean bean : raw) {
                    if (result.contains(bean))
                        continue;
                    for (String type : types) {
                        if (bean.isMatchType(type))
                            result.add(bean);
                    }
                }

                emitter.onNext(result);
                emitter.onComplete();
            }
        }).compose(RxHelp.<List<ImageBean>>applySchedulers());
    }

    /**
     * 保存单个图片信息
     *
     * @param bean 图片信息
     * @return 是否成功
     */
    public static boolean saveImage(ImageBean bean) {
        return bean.save();
    }

    /**
     * 更新图片
     *
     * @param bean 图片信息
     * @return 受影响的行数
     */
    public static int updateImage(ImageBean bean) {
        return bean.update(bean.getId());
    }

    /**
     * 批量保存图片
     * @param beanList 图片列表
     */
    public static Observable<Integer> saveImages(final List<ImageBean> beanList) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                int progress = 0;
                int fail = 0;
                for (ImageBean bean : beanList) {
                    if (bean.save()) {
                        progress++;
                        emitter.onNext(progress);
                    } else {
                        fail++;
                    }
                }
                if (fail == 0)
                    emitter.onComplete();
                else
                    emitter.onError(new SaveImagesException(fail));
            }
        }).compose(RxHelp.<Integer>applySchedulers());
    }

//    ******↓自定义异常

    public static class SaveImagesException extends Exception {
        public int failCount;

        SaveImagesException(int failCount){
            this.failCount = failCount;
        }
    }
}
