package com.forradical.binzee.collectionforlisab.activities.main.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forradical.binzee.collectionforlisab.R;
import com.forradical.binzee.collectionforlisab.activities.photodetail.PhotoDetailActivity;
import com.forradical.binzee.collectionforlisab.base.FoxFragment;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.views.adapters.ImageViewPagerAdapter;
import com.forradical.binzee.collectionforlisab.views.adapters.MainGalleryRecyclerViewAdapter;
import com.forradical.binzee.collectionforlisab.views.viewpager.CyclingImageViewPager;
import com.forradical.binzee.collectionforlisab.views.viewpager.ViewPagerPointer;
import com.kogitune.activity_transition.ActivityTransitionLauncher;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * TODO 寻找合适的PhotoView
 */
public class MainFragment extends FoxFragment implements MainContract.View ,MainGalleryRecyclerViewAdapter.OnItemClickListener {
    @BindView(R.id.cycling_view_pager)
    CyclingImageViewPager cyclingViewPager;
    @BindView(R.id.rv_gallery)
    RecyclerView rvGallery;
    @BindView(R.id.view_pointer_field)
    ViewPagerPointer viewPointer;
    Unbinder unbinder;

    private List<ImageBean> data;

    private MainContract.Presenter mPresenter;

    @Override
    protected int onInflateLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        mPresenter = new Presenter(this);
        data = getTestData();


        //TODO 测试瀑布流效果
        final List<ImageBean> pagerData = new ArrayList<>();
        pagerData.add(data.get(0));
        pagerData.add(data.get(1));
        pagerData.add(data.get(2));
        pagerData.add(data.get(3));
        pagerData.add(data.get(4));
        cyclingViewPager.setData(getContext(), pagerData, true);
        cyclingViewPager.beginCycling();
        cyclingViewPager.setPointer(viewPointer, false);
        cyclingViewPager.setOnItemClickListener(new ImageViewPagerAdapter.OnItemClickListener() {
            @Override
            public void onClicked(View target, ImageBean bean) {
                final Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
                Bundle bundle = new Bundle();

                bundle.putParcelable("data", bean);
                bundle.putInt("position", 0);
                intent.putExtra("params", bundle);
                ActivityTransitionLauncher.with(getActivity()).from(target).launch(intent);
            }
        });

        MainGalleryRecyclerViewAdapter adapter = new MainGalleryRecyclerViewAdapter(getContext(), data);
        adapter.setOnItemClickListener(this);
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
        for (int i = 0; i <= count; i++) {
            list.add(beans.get(random2.nextInt(4)));
        }

        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void showPictures(List<ImageBean> dataList) {

    }

    @Override
    public void showAddPictureWindow() {

    }

    @Override
    public void notice(String text) {

    }

//    RecyclerView子项点击事件↓

    @OnClick(R.id.fab_add)
    public void onViewClicked() {
        //TODO 浮动按钮点击
    }

    @Override
    public void onImageClick(View target, int position) {
        final Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) data);
        bundle.putInt("position", position);
        intent.putExtra("params", bundle);
        ActivityTransitionLauncher.with(getActivity()).from(target).launch(intent);
    }

    @Override
    public void onMoreClick(ImageBean bean) {
        //TODO 文字详情页
    }
}
