package com.forradical.binzee.collectionforlisab.activities.main.fragments.main;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forradical.binzee.collectionforlisab.R;
import com.forradical.binzee.collectionforlisab.base.FoxActivity;
import com.forradical.binzee.collectionforlisab.base.FoxFragment;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.views.viewpager.CyclingImageViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainFragment extends FoxFragment implements MainContract.View{
    @BindView(R.id.cycling_view_pager)
    CyclingImageViewPager cyclingViewPager;
    @BindView(R.id.rv_gallery)
    RecyclerView rvGallery;

    private MainContract.Presenter mPresenter;

    @Override
    protected void create(Bundle savedInstanceState) {
        mPresenter = new Presenter(this);
//        mPresenter.requestPictures();

        List<ImageBean> list = new ArrayList<>();
        //TODO 测试瀑布流效果
    }

    @Override
    protected int onInflateLayout() {
        return R.layout.fragment_main;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
    }

    @Override
    public void showPictures(List<ImageBean> dataList) {

    }

    @Override
    public void showAddWindow() {

    }

    @Override
    public void notice(String text) {

    }
}
