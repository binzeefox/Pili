package com.forradical.binzee.collectionforlisab.activities.main;

import android.os.Bundle;

import com.forradical.binzee.collectionforlisab.views.viewpager.CyclingImageViewPager;
import com.forradical.binzee.collectionforlisab.R;
import com.forradical.binzee.collectionforlisab.base.FoxActivity;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends FoxActivity implements IMainContract.View {

    @BindView(R.id.view_pager)
    CyclingImageViewPager viewPager;

    private IMainContract.Presenter mPresenter;
    private List<ImageBean> beans;  //图片列表

    @Override
    protected int onInflateLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        mPresenter = new MainPresenter(this);
        beans = new ArrayList<>();

        setImages(R.drawable.cat1, R.drawable.devil, R.drawable.frame_pic, R.drawable.mylove2);
        viewPager.setData(this, beans);
        viewPager.beginCycling();
    }

    private void setImages(int... ids) {
        for (long id : ids) {
            ImageBean bean = new ImageBean(null);
            bean.setCreateTime(id);
            beans.add(bean);
        }
    }

    @Override
    public FoxActivity getFoxActivity() {
        return this;
    }

}
