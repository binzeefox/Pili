package com.forradical.binzee.collectionforlisab.utils;

import android.app.Application;
import android.util.Log;

import com.forradical.binzee.collectionforlisab.base.FoxApplication;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Fixme 数据库无法在Observable中查询？？？代码不健硕，需要修改
 */
public class DatabaseHelper {

    /**
     * 获取数据库全部图片
     */
    public static Observable<List<ImageBean>> getAllImage() {
        final List<ImageBean> beanList = LitePal.findAll(ImageBean.class);
        return Observable.create(new ObservableOnSubscribe<List<ImageBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ImageBean>> emitter) {
                emitter.onNext(beanList);
//                List<ImageBean> beanList = LitePal.findAll(ImageBean.class);
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
        if (bean.save()){
            FoxApplication.getFullList().add(0, bean);
            Log.d("DatabaseHelper", "fullList.size = " + FoxApplication.getFullList().size());
            return true;
        }
        return false;
    }

    /**
     * 更新图片
     *
     * @param bean 图片信息
     * @return 受影响的行数
     */
    public static int updateImage(ImageBean bean) {
        int count = bean.update(bean.getId());
        if (count > 0) {
            int position = 0;
            for (ImageBean b : FoxApplication.getFullList()) {
                if (bean.getPath().equals(b.getPath())) {
                    position = FoxApplication.getFullList().indexOf(b);
                    FoxApplication.getFullList().remove(b);
                    break;
                }
            }
            FoxApplication.getFullList().add(position, bean);
            return count;
        }
        return 0;
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
                        Log.d("DatabaseHelper", "保存成功");
                        progress++;
                        FoxApplication.getFullList().add(0,bean);
                        emitter.onNext(progress);
                    } else {
                        fail++;
                        Log.d("DatabaseHelper", "保存成功");
                    }
                }
                if (fail == 0)
                    emitter.onComplete();
                else
                    emitter.onError(new SaveImagesException(fail));
            }
        }).compose(RxHelp.<Integer>applySchedulers());
    }

    /**
     * 删除
     */
    public static void deletePicture(ImageBean bean){
        FoxApplication.getFullList().remove(bean);
        FileUtil.deleteBeanFile(bean);
        bean.delete();
    }



//    ******↓自定义异常

    public static class SaveImagesException extends Exception {
        public int failCount;

        SaveImagesException(int failCount){
            this.failCount = failCount;
        }
    }
}
