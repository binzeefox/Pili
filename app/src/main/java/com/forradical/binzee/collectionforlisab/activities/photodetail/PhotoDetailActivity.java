package com.forradical.binzee.collectionforlisab.activities.photodetail;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.forradical.binzee.collectionforlisab.R;
import com.forradical.binzee.collectionforlisab.base.FoxActivity;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.views.adapters.DetailViewPagerAdapter;
import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PhotoDetailActivity extends FoxActivity {

    @BindView(R.id.vp_detail)
    ViewPager vpDetail;
    @BindView(R.id.tv_pointer)
    TextView tvPointer;
    private ExitActivityTransition exitTransition;
    private List<ImageBean> data;

    @Override
    protected int onInflateLayout() {
        return R.layout.activity_photo_detail;
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        exitTransition = ActivityTransition.with(getIntent()).to(vpDetail).duration(175).start(savedInstanceState);

        data = mParams.getParcelableArrayList("data");
        if (data == null) {
            data = new ArrayList<>();
            data.add((ImageBean) mParams.getParcelable("data"));
        }
        initViews();
    }

    /**
     * 初始化界面
     */
    private void initViews() {
        int position = mParams.getInt("position", 0);
        tvPointer.setVisibility(data.size() > 1 ? View.VISIBLE : View.GONE);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(data.get(position).getTitle());
        String sb = String.valueOf(position + 1) +
                "/" +
                data.size();
        tvPointer.setText(sb);
        DetailViewPagerAdapter adapter = new DetailViewPagerAdapter(this, data);
        vpDetail.setAdapter(adapter);
        vpDetail.setCurrentItem(position, false);
        vpDetail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String sb = String.valueOf(position + 1) +
                        "/" +
                        data.size();
                tvPointer.setText(sb);
                toolbar.setTitle(data.get(position).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    protected Toolbar configToolbar(Toolbar toolbar) {
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        return toolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        if (item.getItemId() == R.id.menu_more) {
            //TODO 跳转至详情页
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        exitTransition.exit(this);
    }
}
