package com.forradical.binzee.collectionforlisab.activities.addphoto;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.forradical.binzee.collectionforlisab.R;
import com.forradical.binzee.collectionforlisab.base.FoxActivity;
import com.forradical.binzee.collectionforlisab.base.FoxApplication;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.utils.DatabaseHelper;
import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class AddPhotoActivity extends FoxActivity implements AddPhotoContract.View {

    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;

    private ExitActivityTransition exitTransition;
    private Presenter mPresenter;
    private List<ImageBean> data;

    @Override
    protected int onInflateLayout() {
        return R.layout.activity_add_photo;
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        exitTransition = ActivityTransition.with(getIntent()).to(fabAdd).duration(175).start(savedInstanceState);
        mPresenter = new Presenter(this);
        List<String> rawData = mParams.getStringArrayList("data");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        readyPagers(rawData);
    }

    /**
     * 显示ViewPager内容
     * @param data 路径列表
     */
    private void readyPagers(List<String> data) {
        if (data == null) {
            //TODO 判空
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        exitTransition.exit(this);
    }

    @OnClick(R.id.fab_add)
    public void onViewClicked() {
        //TODO 确定按钮
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
}
