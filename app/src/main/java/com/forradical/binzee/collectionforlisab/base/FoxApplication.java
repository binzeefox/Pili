package com.forradical.binzee.collectionforlisab.base;

import android.app.Activity;
import android.app.Application;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.utils.ActivityCollector;
import com.tencent.bugly.Bugly;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * 自定义Application
 *
 * 集成自定义的Activity管理器，内涵模拟返回栈
 * 可以批量销毁activity
 */
public class FoxApplication extends LitePalApplication {

    private static final String APP_ID = "b1417ceffe";
    private static FoxApplication mInstance;
    public static Snackbar mSnackbar;

    private static final List<ImageBean> fullList = new ArrayList<>();

    // 自定义Activity管理器
    private final static ActivityCollector mCollector = ActivityCollector.get();

    public static FoxApplication getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Bugly.init(this, APP_ID, true);
        LitePal.initialize(this);
    }

//    /**
//     * 同步添加多条信息
//     */
//    public synchronized static void addImageBeans(ImageBean... imageBeans){
//        fullList.addAll(Arrays.asList(imageBeans));
//    }
//
//    /**
//     * 同步删除信息
//     */
//    public synchronized static void removeImageBeans(ImageBean... imageBeans){
//        fullList.removeAll(Arrays.asList(imageBeans));
//    }
//
//    /**
//     * 同步获取信息
//     */
//    public synchronized static ImageBean getFullListItem(int position){
//        return fullList.get(position);
//    }

    /**
     * 获取对象
     */
    public static List<ImageBean> getFullList(){
        synchronized (fullList){
            Log.d("FoxApplication", "fullList.size = " + fullList.size());
            return fullList;
        }
    }

    /**
     * 模拟返回栈注册
     */
    public void registerActivity(Activity activity){
        mCollector.list(activity);
    }

    /**
     * 模拟返回栈注销
     */
    public void unRegisterActivity(Activity activity){
        mCollector.delist(activity);
    }

    /**
     * 获取Activity管理器
     */
    public ActivityCollector getActivityCollector() {
        return mCollector;
    }

    /**
     * 存入全局缓存数据
     * @param key   键
     * @param value 值
     */
    public static void putGlobalData(String key, Object value){
        mCollector.putGlobalData(key, value);
    }

    /**
     * 获取全局缓存数据
     * @param key   键
     * @param <T>   数据类型
     * @return  数据
     */
    public static <T> T getGlobalData(String key){
        return mCollector.getGlobalData(key);
    }
}
