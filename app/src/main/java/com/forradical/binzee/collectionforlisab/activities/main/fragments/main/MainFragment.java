package com.forradical.binzee.collectionforlisab.activities.main.fragments.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.forradical.binzee.collectionforlisab.R;
import com.forradical.binzee.collectionforlisab.activities.addphoto.AddPhotoActivity;
import com.forradical.binzee.collectionforlisab.activities.photodetail.PhotoDetailActivity;
import com.forradical.binzee.collectionforlisab.base.FoxActivity;
import com.forradical.binzee.collectionforlisab.base.FoxFragment;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.utils.DatabaseHelper;
import com.forradical.binzee.collectionforlisab.utils.FileUtil;
import com.forradical.binzee.collectionforlisab.views.CustomDialogFragment;
import com.forradical.binzee.collectionforlisab.views.GlideImageEngine;
import com.forradical.binzee.collectionforlisab.views.PictureDetailDialog;
import com.forradical.binzee.collectionforlisab.views.adapters.ImageViewPagerAdapter;
import com.forradical.binzee.collectionforlisab.views.adapters.MainGalleryRecyclerViewAdapter;
import com.forradical.binzee.collectionforlisab.views.viewpager.CyclingImageViewPager;
import com.forradical.binzee.collectionforlisab.views.viewpager.ViewPagerPointer;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * TODO 寻找合适的PhotoView
 */
public class MainFragment extends FoxFragment implements MainContract.View, MainGalleryRecyclerViewAdapter.OnItemClickListener {
    private static final int REQUEST_CODE_CHOOSE = 0x001;

    @BindView(R.id.cycling_view_pager)
    CyclingImageViewPager cyclingViewPager;
    @BindView(R.id.rv_gallery)
    RecyclerView rvGallery;
    @BindView(R.id.view_pointer_field)
    ViewPagerPointer viewPointer;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    @BindView(R.id.data_holder)
    TextView dataHolder;

    private int breakPosition = 1;

    private List<ImageBean> data;

    private MainContract.Presenter mPresenter;
    private MainGalleryRecyclerViewAdapter adapter;

    @Override
    protected int onInflateLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        mPresenter = new Presenter(this);
        refresh(null);
//        data = getTestData();


//        //TODO 测试瀑布流效果
//        final List<ImageBean> pagerData = new ArrayList<>();
//        pagerData.add(data.get(0));
//        pagerData.add(data.get(1));
//        pagerData.add(data.get(2));
//        pagerData.add(data.get(3));
//        pagerData.add(data.get(4));
//        cyclingViewPager.setImageData(getContext(), pagerData, true);
//        cyclingViewPager.setPointer(viewPointer, false);
//        cyclingViewPager.setOnItemClickListener(new ImageViewPagerAdapter.OnItemClickListener() {
//            @Override
//            public void onClicked(View target, ImageBean bean) {
//                final Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
//                Bundle bundle = new Bundle();
//
//                bundle.putParcelable("data", bean);
//                bundle.putInt("position", 0);
//                intent.putExtra("params", bundle);
//                ActivityTransitionLauncher.with(getActivity()).from(target).launch(intent);
//            }
//        });
//
//        MainGalleryRecyclerViewAdapter adapter = new MainGalleryRecyclerViewAdapter(getContext(), data);
//        adapter.setOnItemClickListener(this);
//        rvGallery.setAdapter(adapter);
//        rvGallery.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        rvGallery.setNestedScrollingEnabled(false);
//        rvGallery.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.requestPictures();
        cyclingViewPager.setCurrentItem(breakPosition);
        cyclingViewPager.beginCycling();
        adapter.notifyDataSetChanged();
        checkNoData();
    }

    /**
     * 判断是否没有数据
     */
    private void checkNoData() {
        boolean noData = data == null || data.isEmpty();
        dataHolder.setVisibility(noData ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        cyclingViewPager.stopCycling();
        breakPosition = cyclingViewPager.getCurrentItem();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            ArrayList<String> pathList = new ArrayList<>();
            for (Uri uri : Matisse.obtainResult(data)) {
                pathList.add(FileUtil.getImageFileFromUri(getActivity(), uri).getAbsolutePath());
            }
            final Intent intent = new Intent(getActivity(), AddPhotoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("data", pathList);
            intent.putExtra("params", bundle);
            ActivityTransitionLauncher.with(getActivity()).from(fabAdd).launch(intent);
        }
    }

//    /**
//     * 获取测试数据
//     * TODO 删除
//     */
//    private List<ImageBean> getTestData() {
//        List<ImageBean> beans = new ArrayList<>();
//        ImageBean bean = new ImageBean(null);
//        bean.setId(R.drawable.cat1);
//        bean.setTitle("芝麻");
//        beans.add(bean);
//        bean = new ImageBean(null);
//        bean.setId(R.drawable.mylove2);
//        bean.setTitle("可爱的根号");
//        beans.add(bean);
//        bean = new ImageBean(null);
//        bean.setId(R.drawable.devil);
//        bean.setTitle("神的祈愿");
//        beans.add(bean);
//        bean = new ImageBean(null);
//        bean.setId(R.drawable.frame_pic);
//        bean.setTitle("漂亮的根号");
//        beans.add(bean);
//
//        List<ImageBean> list = new ArrayList<>();
//        Random random1 = new Random(200609);
//        Random random2 = new Random(19980102);
//        int count = random1.nextInt(20) + 10;
//        for (int i = 0; i <= count; i++) {
//            list.add(beans.get(random2.nextInt(4)));
//        }
//
//        return list;
//    }

    @Override
    public void refresh(List<ImageBean> dataList) {
        if (data != null)
            data.clear();
        data = dataList;
        List<ImageBean> pagerData = new ArrayList<>();
        if (data == null || data.isEmpty()) {
            pagerData = null;
            viewPointer.setVisibility(View.GONE);
        } else {
            Collections.reverse(data);
            viewPointer.setVisibility(View.VISIBLE);
            for (int i = 0; i < 5 && i < dataList.size(); i++) {
                ImageBean bean = data.get(i);
                pagerData.add(bean);
            }
        }
        cyclingViewPager.setData(getContext(), pagerData, true);
        cyclingViewPager.notifyDataSetChanged();
        if (data != null && !data.isEmpty()) {
            cyclingViewPager.setPointer(viewPointer, false);
        }
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

        if (adapter == null) {
            adapter = new MainGalleryRecyclerViewAdapter(getContext(), data);
            adapter.setOnItemClickListener(this);
            rvGallery.setAdapter(adapter);
            rvGallery.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            rvGallery.setNestedScrollingEnabled(false);
            rvGallery.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
        } else {
            adapter.setDataList(data);
            adapter.notifyDataSetChanged();
        }
        checkNoData();
    }

    @Override
    public void notice(String text) {
        getSnackbar(text).show();
    }

    @Override
    public void onLoading() {
        ((FoxActivity) getActivity()).showLoadingDialog("加载中", null);
    }

    @Override
    public void onLoaded() {
        ((FoxActivity) getActivity()).dismissDialog();
    }

    @OnClick(R.id.fab_add)
    public void onViewClicked() {
        openChooser();
    }

//    RecyclerView子项点击事件↓

    /**
     * 图片点击
     */
    @Override
    public void onImageClick(View target, int position) {
        final Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) data);
        bundle.putInt("position", position);
        intent.putExtra("params", bundle);
        ActivityTransitionLauncher.with(getActivity()).from(target).launch(intent);
    }

    /**
     * 图片更多点击
     */
    @Override
    public void onMoreClick(View target, final ImageBean bean) {
//        mPresenter.showDetail(bean);
        PopupMenu menu = new PopupMenu(getActivity(), target);
        menu.getMenuInflater()
                .inflate(R.menu.menu_photo_detail, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_delete:
                        CustomDialogFragment.get(getContext())
                                .title("请确认")
                                .message("是否删除照片" + bean.getTitle() + "？")
                                .positiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DatabaseHelper.deletePicture(bean);
                                        data.remove(bean);
                                        mPresenter.deletePicture(bean);
                                    }
                                })
                                .negativeButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show(getFragmentManager());
                        refresh(data);
                        break;
                    case R.id.menu_detail:
                        onDetail(bean);
                        break;
                }
                return true;
            }
        });
        menu.show();
    }

    /**
     * 进入详情
     */
    private void onDetail(final ImageBean bean) {
        PictureDetailDialog.get(getActivity(), bean)
                .negativeButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<String> pathList = new ArrayList<>();
                        ArrayList<ImageBean> imageBeans = new ArrayList<>();
                        pathList.add(bean.getPath());
                        imageBeans.add(bean);
                        final Intent intent = new Intent(getContext(), AddPhotoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("data", pathList);
                        bundle.putParcelableArrayList("image_data", imageBeans);
                        intent.putExtra("params", bundle);
                        startActivity(intent);
                    }
                })
                .show(getActivity().getSupportFragmentManager());
    }

    /**
     * 开启图片选择
     */
    private void openChooser() {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG), false)
                .showSingleMediaType(true)
                .theme(R.style.image_picker_theme) //选择主题 默认是蓝色主题，Matisse_Dracula为黑色主题
                .countable(true)
                .maxSelectable(9)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .capture(true)//选择照片时，是否显示拍照
                .captureStrategy(new CaptureStrategy(true
                        , "com.forradical.binzee.collectionforlisab.PROVIDER"))
                .imageEngine(new GlideImageEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }
}
