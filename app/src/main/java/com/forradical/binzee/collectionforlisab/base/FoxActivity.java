package com.forradical.binzee.collectionforlisab.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.forradical.binzee.collectionforlisab.views.CustomDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 活动基类。
 * <p>
 * 封装onCreate和动态权限获取
 * 封装了一个弹窗Fragment
 * 封装跳转
 */
public abstract class FoxActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 0x00;
    private CustomDialogFragment dialogHelper;
    public CompositeDisposable dContainer;

//    ******↓生命周期

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFoxApplication().registerActivity(this);
        dContainer = new CompositeDisposable();
        setContentView(onInflateLayout());
        //TODO ButterKnife Binder here...
        ButterKnife.bind(this);

        create(savedInstanceState);
        //Check and request permission
        List<String> permissionList = getPermissionList(onCheckPermission());
        if (permissionList != null)
            requestSelfPermissions(checkSelfPermissions(permissionList));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getFoxApplication().unRegisterActivity(this);
        if (dContainer != null && !dContainer.isDisposed()) {//rx_java注意isDisposed是返回是否取消订阅
            dContainer.dispose();
            dContainer.clear();
            dContainer = null;
        }
    }

//    ******↓公共方法

    /**
     * 无参无返回跳转
     * @param target 目标Activity
     */
    protected void navigate(Class<? extends Activity> target) {
        navigate(target, null);
    }

    /**
     * 无返回跳转
     * @param params 参数
     * @param target 目标Activity
     */
    protected void navigate(Class<? extends Activity> target, Bundle params) {
        Intent intent = new Intent(this, target);
        if (params != null)
            intent.putExtra("params", params);
        startActivity(intent);
    }

    /**
     * 返回值跳转
     * @param target 目标Activity
     * @param requestCode   请求码
     */
    protected void navigateForResult(Class<? extends Activity> target, int requestCode){
        navigateForResult(target, null, requestCode);
    }

    /**
     * 返回值跳转
     * @param target 目标Activity
     * @param params 参数
     * @param requestCode   请求码
     */
    protected void navigateForResult(Class<? extends Activity> target, Bundle params, int requestCode){
        Intent intent = new Intent(this, target);
        if (params != null)
            intent.putExtra("params", params);
        startActivityForResult(intent, requestCode);
    }

//    ******↓继承方法

    /**
     * 加载布局
     *
     * @return 布局资源id
     */
    protected abstract int onInflateLayout();

    /**
     * 代理onCreate
     */
    protected abstract void create(Bundle savedInstanceState);

    /**
     * Application类型转换
     *
     * @return 定制的Application
     */
    protected FoxApplication getFoxApplication() {
        return (FoxApplication) super.getApplication();
    }

    /**
     * 获取需要检查的权限
     *
     * @return 权限数组
     */
    protected String[] onCheckPermission() {
        return null;
    }

    /**
     * 权限请求结果
     *
     * @param failedList 尚未通过的权限
     */
    protected void onSelfPermissionResult(List<String> failedList) {
        if (failedList.size() == 0)
            return;
        StringBuilder sb = new StringBuilder();
        for (String permission : failedList) {
            if (!sb.toString().isEmpty())
                sb.append("和");
            sb.append(permission).append("\n");
        }
        sb.append("获取失败");

        CustomDialogFragment.get(this)
                .cancelable(true)
                .title("权限获取失败")
                .message(sb.toString())
                .show(getSupportFragmentManager());
    }

//    ******↓权限相关

    /**
     * 检查动态权限
     *
     * @return 尚未通过的权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    private List<String> checkSelfPermissions(List<String> permissions) {
        List<String> failedList = new ArrayList<>();
        for (String permission : permissions)
            if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(permission))
                failedList.add(permission);
        return failedList;
    }

    /**
     * 请求动态权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void requestSelfPermissions(List<String> permissions) {
        if (permissions == null)
            return;
        String[] requests = new String[permissions.size()];
        for (int i = 0; i < permissions.size(); i++)
            requests[i] = permissions.get(i);
        requestPermissions(requests, PERMISSION_CODE);
    }

    /**
     * 权限格式转换
     */
    private List<String> getPermissionList(String[] permissions) {
        if (permissions == null)
            return null;
        return Arrays.asList(permissions);
    }

    /**
     * 权限回调
     */
    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> failedList = new ArrayList<>();

        for (int i = 0; i < permissions.length; i++)
            if (PackageManager.PERMISSION_GRANTED != grantResults[i])
                failedList.add(permissions[i]);
        onSelfPermissionResult(failedList);
    }

//    ******↓弹窗相关

    /**
     * 获取弹窗构造器
     */
    protected CustomDialogFragment getDialogHelper() {
        if (dialogHelper == null)
            dialogHelper = CustomDialogFragment.get(this);
        return dialogHelper;
    }

    /**
     * 显示网络加载Dialog
     */
    protected CustomDialogFragment showLoadingDialog(String title, AlertDialog.OnClickListener cancelClickListener) {
        if (dialogHelper == null)
            getDialogHelper();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ProgressBar bar = new ProgressBar(this);
        bar.setPadding(0, 30, 0, 30);
        bar.setLayoutParams(params);
        bar.setIndeterminate(true);
        dialogHelper
                .title(title)
                .cancelable(false)
                .view(bar);
        if (cancelClickListener != null)
            dialogHelper.negativeButton("取消", cancelClickListener);
        dialogHelper.show(getSupportFragmentManager());
        return dialogHelper;
    }

    /**
     * 注销弹窗
     */
    protected void dismissDialog() {
        if (dialogHelper == null)
            return;
        dialogHelper.dismiss();
        dialogHelper = null;
    }
}
