package com.forradical.binzee.collectionforlisab.activities.main.fragments.main;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forradical.binzee.collectionforlisab.R;
import com.forradical.binzee.collectionforlisab.base.FoxActivity;
import com.forradical.binzee.collectionforlisab.base.FoxFragment;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.views.adapters.MainGalleryRecyclerViewAdapter;
import com.forradical.binzee.collectionforlisab.views.viewpager.CyclingImageViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainFragment extends FoxFragment implements MainContract.View {
    @BindView(R.id.cycling_view_pager)
    CyclingImageViewPager cyclingViewPager;
    @BindView(R.id.rv_gallery)
    RecyclerView rvGallery;

    private MainContract.Presenter mPresenter;

    @Override
    protected void create(Bundle savedInstanceState) {
        mPresenter = new Presenter(this);
        List<ImageBean> list = getTestData();


        //TODO 测试瀑布流效果
        List<ImageBean> pagerData = new ArrayList<>();
        pagerData.add(list.get(0));
        pagerData.add(list.get(1));
        pagerData.add(list.get(2));
        pagerData.add(list.get(3));
        pagerData.add(list.get(4));
        cyclingViewPager.setData(getContext(), pagerData, true);
        cyclingViewPager.beginCycling();

        MainGalleryRecyclerViewAdapter adapter = new MainGalleryRecyclerViewAdapter(getContext(), list);
        rvGallery.setAdapter(adapter);
        rvGallery.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvGallery.setNestedScrollingEnabled(false);
        rvGallery.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
    }

    /**
     * 获取测试数据
     * TODO 删除
     */
    private List<ImageBean> getTestData() {
        List<ImageBean> beans = new ArrayList<>();
        ImageBean bean = new ImageBean(null);
        bean.setId(R.drawable.cat1);
        bean.setTitle("芝麻");
        beans.add(bean);
        bean = new ImageBean(null);
        bean.setId(R.drawable.mylove2);
        bean.setTitle("可爱的根号");
        beans.add(bean);
        bean = new ImageBean(null);
        bean.setId(R.drawable.devil);
        bean.setTitle("神的祈愿");
        beans.add(bean);
        bean = new ImageBean(null);
        bean.setId(R.drawable.frame_pic);
        bean.setTitle("漂亮的根号");
        beans.add(bean);

        List<ImageBean> list = new ArrayList<>();
        Random random1 = new Random(200609);
        Random random2 = new Random(19980102);
        int count = random1.nextInt(20) + 10;
        for (int i = 0; i <= count; i++){
            list.add(beans.get(random2.nextInt(4)));
        }

        return list;
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
