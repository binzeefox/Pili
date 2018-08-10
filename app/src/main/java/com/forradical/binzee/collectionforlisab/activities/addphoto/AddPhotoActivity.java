package com.forradical.binzee.collectionforlisab.activities.addphoto;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.forradical.binzee.collectionforlisab.R;
import com.forradical.binzee.collectionforlisab.base.FoxActivity;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.utils.CommonUtil;
import com.forradical.binzee.collectionforlisab.utils.DatabaseHelper;
import com.forradical.binzee.collectionforlisab.views.AddingPager;
import com.forradical.binzee.collectionforlisab.views.adapters.AddingPagerAdapter;
import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class AddPhotoActivity extends FoxActivity implements AddPhotoContract.View {

    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    @BindView(R.id.adding_pager)
    AddingPager addingPager;

    private ExitActivityTransition exitTransition;
    private Presenter mPresenter;
    private List<ImageBean> data;
    private boolean isUpdate;

    @Override
    protected int onInflateLayout() {
        return R.layout.activity_add_photo;
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        exitTransition = ActivityTransition.with(getIntent()).to(fabAdd).duration(175).start(savedInstanceState);
        mPresenter = new Presenter(this);
        List<String> rawData = mParams.getStringArrayList("data");
        List<ImageBean> imageData = mParams.getParcelableArrayList("image_data");
        isUpdate = imageData != null;
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("图片详情");
        readyPagers(rawData, imageData);
    }

    /**
     * 显示ViewPager内容
     *
     * @param data 路径列表
     */
    private void readyPagers(@Nullable List<String> data, List<ImageBean> imageData) {
        if (data == null) {
            getDialogHelper()
                    .cancelable(false)
                    .title("错误")
                    .message("未知错误，数据丢失")
                    .positiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            exitTransition.exit(AddPhotoActivity.this);
                        }
                    }).show(getSupportFragmentManager());
            return;
        }
        final int max = data.size();
        if (max == 1){
            getSupportActionBar().setTitle("图片详情");
        }else {
            getSupportActionBar().setTitle("图片详情\0(1/" + max + ")");
        }
        addingPager.setData(this, data, imageData);
        addingPager.addOnPageChangeListener(new CommonUtil.SimpleOnPagerChangeListener() {
            @Override
            public void onPageSelected(int position) {
                toolbar.setTitle("图片详情\0("+ (position + 1) +"/"+ max + ")");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    @Override
    protected Toolbar setToolbar() {
        int transparent = getResources().getColor(android.R.color.transparent);
        Toolbar toolbar = super.setToolbar();
//        toolbar.setBackgroundColor(transparent);
//        getWindow().setStatusBarColor(0x4000);
        return toolbar;
    }

    @Override
    public void onBackPressed() {
        getDialogHelper()
                .cancelable(false)
                .title("提示")
                .message("尚未保存，是否退出？")
                .negativeButton("取消", cancelListener)
                .positiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                exitTransition.exit(AddPhotoActivity.this);
            }
        }).show(getSupportFragmentManager());
    }

    @OnClick(R.id.fab_add)
    public void onViewClicked() {
        AddingPagerAdapter adapter = addingPager.getAdapter();
        data = adapter.getSavingData();
        if (adapter.isFinished()){
            if (isUpdate) {
                ImageBean bean = data.get(0);
                bean.update(bean.getId());
                finish();
            } else {
                mPresenter.savePhotos(data);
            }
        }else {
            getDialogHelper()
                    .title("提示")
                    .message("图片名称不能为空")
                    .positiveButton("确定", cancelListener)
                    .show(getSupportFragmentManager());
        }
    }

    @Override
    public void showAddingDialog(Observable<Integer> progressOb) {
        final int max = data.size();
        String progress = "0/" + max;
        ViewGroup view = (ViewGroup) getLayoutInflater().inflate(R.layout.progress_view_layout, null);
        final TextView progressField = view.findViewById(R.id.dialog_progress_text);
        final ProgressBar progressBar = view.findViewById(R.id.dialog_progress_bar);
        progressField.setText(progress);
        progressBar.setMax(max);
        progressBar.setProgress(0);

        getDialogHelper()
                .title("保存中")
                .view(view)
                .cancelable(false)
                .show(getSupportFragmentManager());
        progressOb.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                dContainer.add(d);
            }

            @Override
            public void onNext(Integer integer) {
                String progress = "" + integer + "/" + max;
                progressBar.setProgress(integer);
                progressField.setText(progress);
            }

            @Override
            public void onError(Throwable e) {
                int failed = ((DatabaseHelper.SaveImagesException) e).failCount;
                getSnackbar("" + failed + "张图片保存失败").show();
                dismissDialog();
                finish();
            }

            @Override
            public void onComplete() {
                getSnackbar("保存成功").show();
                dismissDialog();
                finish();
            }
        });
    }

    /**
     * 复用取消监听
     */
    private DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };
}
