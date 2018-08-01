package com.forradical.binzee.collectionforlisab.utils;

import android.app.Activity;
import android.util.LruCache;

import java.util.Stack;

/**
 * 活动管理器
 */
public class ActivityCollector {

    /**
     * 全局的存放数据，在任何一个地方都能取到
     * 最多存放8条信息
     */
    private final LruCache<String, Object> globalDatas;
    private static ActivityCollector mInstance = new ActivityCollector();
    private static final Stack<Activity> mActivityStack = new Stack<>();

    private ActivityCollector(){
        globalDatas = new LruCache<String, Object>(8){
            @Override
            protected int sizeOf(String key, Object value) {
                return 1;
            }
        };
    }

    /**
     * 静态获取单例
     */
    public static ActivityCollector get(){
        synchronized (ActivityCollector.class) {
            return mInstance;
        }
    }

//    ******↑构造方法
//    ******↓公共方法

    /**
     * 获取模拟返回栈
     */
    public static Stack<Activity> getActivityStack() {
        return mActivityStack;
    }

    /**
     * 添加活动
     */
    public void list(Activity activity){
        mActivityStack.push(activity);
    }

    /**
     * 移除活动
     */
    public void delist(Activity activity){
        mActivityStack.remove(activity);
    }

    /**
     * 获取栈顶Activity
     */
    public Activity getTopActivity(){
        return mActivityStack.peek();
    }

    /**
     * 杀死活动
     */
    public void kill(){
        kill(1);
    }

    /**
     * 杀死所有活动
     */
    public void killAll(){
        kill(mActivityStack.size());
    }

    /**
     * 杀死一定数量活动
     * @param count 从栈顶起需要杀死的数量
     */
    public void kill(int count){
        if (count <= 0)
            return;
        if (count > mActivityStack.size())
            count = mActivityStack.size();
        while (count != 0){
            mActivityStack.pop().finish();
            count--;
        }
    }

    /**
     * 添加全局数据
     * @param key   键
     * @param value 值
     */
    public void putGlobalData(String key, Object value){
        globalDatas.put(key, value);
    }

    /**
     * 获取全局数据
     * @param key   键
     * @param <T>   数据类型
     * @return  数据
     */
    public <T> T getGlobalData(String key){
        return (T) globalDatas.get(key);
    }
}
