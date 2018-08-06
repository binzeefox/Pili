package com.forradical.binzee.collectionforlisab.activities.main;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.forradical.binzee.collectionforlisab.R;
import com.forradical.binzee.collectionforlisab.activities.main.fragments.fifth.FifthFragment;
import com.forradical.binzee.collectionforlisab.activities.main.fragments.fourth.FourthFragment;
import com.forradical.binzee.collectionforlisab.activities.main.fragments.main.MainFragment;
import com.forradical.binzee.collectionforlisab.activities.main.fragments.second.SecondFragment;
import com.forradical.binzee.collectionforlisab.activities.main.fragments.serach.SerachFragment;
import com.forradical.binzee.collectionforlisab.base.FoxActivity;
import com.forradical.binzee.collectionforlisab.views.CustomDialogFragment;
import com.forradical.binzee.collectionforlisab.views.adapters.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FoxActivity {

    private static final List<Fragment> pagesList;
    private static final List<Integer> tabList;

    static {
        pagesList = new ArrayList<>();
        // TODO 首页的所有Fragment
        pagesList.add(new MainFragment());
        pagesList.add(new SecondFragment());
        pagesList.add(new SerachFragment());
        pagesList.add(new FourthFragment());
        pagesList.add(new FifthFragment());

        tabList = new ArrayList<>();
        tabList.add(R.id.item1);
        tabList.add(R.id.item2);
        tabList.add(R.id.item3);
        tabList.add(R.id.item4);
        tabList.add(R.id.item5);
    }

    @BindView(R.id.main_view_pager)
    ViewPager mainViewPager;
    @BindView(R.id.tab_bar)
    RadioGroup tabBar;

    @Override
    protected int onInflateLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), pagesList);
        mainViewPager.setAdapter(adapter);
        mainViewPager.setNestedScrollingEnabled(true);
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                tabBar.check(tabList.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        tabBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int position = tabList.indexOf(i);
                mainViewPager.setCurrentItem(position, true);
            }
        });
    }

    @Override
    protected boolean isFullScreen() {
        return false;
    }

    @Override
    protected String[] onCheckPermission() {
        return new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }

    @Override
    protected void onSelfPermissionResult(final List<String> failedList) {
        if (!failedList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String permission : failedList) {
                if (!sb.toString().isEmpty())
                    sb.append("和");
                sb.append(permission).append("\n");
            }
            sb.append("获取失败,应用即将退出");

            CustomDialogFragment.get(this)
                    .cancelable(true)
                    .title("权限获取失败")
                    .message(sb.toString())
                    .cancelable(false)
                    .positiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            System.exit(0);
                        }
                    })
                    .negativeButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestSelfPermissions(failedList);
                            dismissDialog();
                        }
                    })
                    .show(getSupportFragmentManager());
        }
    }
}
