package com.forradical.binzee.collectionforlisab.views.adapters;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.forradical.binzee.collectionforlisab.R;
import com.forradical.binzee.collectionforlisab.base.FoxActivity;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.utils.CommonUtil;
import com.forradical.binzee.collectionforlisab.utils.DateUtil;
import com.forradical.binzee.collectionforlisab.utils.ImageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.gujun.android.taggroup.TagGroup;

public class AddingPagerAdapter extends PagerAdapter {
    private List<ViewGroup> viewList;   // 装有展示图片的部件
    private final ImageBean[] result;
    private List<String> pathList;
    private Context mContext;

    public AddingPagerAdapter(final FoxActivity ctx, final List<String> pathList) {
        mContext = ctx;
        result = new ImageBean[pathList.size()];
        viewList = new ArrayList<>();
        this.pathList = pathList;
        convertViews(ctx);
    }

    /**
     * 处理Views
     */
    private void convertViews(Activity ctx) {
        // 为每一张图片准备一个View
        for (int i = 0; i < pathList.size(); i++) {
            ViewGroup view = (ViewGroup) LayoutInflater.from(ctx).inflate(R.layout.adding_pager_adapter_layout, null);
            String path = pathList.get(i);
            result[i] = new ImageBean(path);
            viewList.add(view);
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        String path = pathList.get(position);
        ViewGroup view = viewList.get(position);
        container.addView(view);
        ImageView thumbField = view.findViewById(R.id.iv_adding_thumb);
        TextInputEditText titleField = view.findViewById(R.id.edt_adding_title);
        Button createTimeBtn = view.findViewById(R.id.btn_adding_create_time);
        EditText commentField = view.findViewById(R.id.edt_adding_comment);
        TagGroup tagGroupField = view.findViewById(R.id.tg_tag_field);

        new ImageUtil(mContext, path).setBlue(true).show(thumbField);
        handleFields(titleField, createTimeBtn, commentField, tagGroupField, position);
        return view;
    }

    /**
     * 处理单个View布局
     */
    private void handleFields(TextInputEditText titleField
            , final Button createTimeBtn, EditText commentField, TagGroup tagGroupField, final int position) {

        //标题
        String[] pathSplit = pathList.get(position).split("/");
        String name = pathSplit[pathSplit.length - 1];
        titleField.setText(name);
        titleField.addTextChangedListener(new CommonUtil.SimpleTextWatch() {
            @Override
            public void afterTextChanged(Editable editable) {
                result[position].setTitle(editable.toString());
            }
        });

        //创建时间
        createTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar d = Calendar.getInstance(Locale.CHINA);
                Date myDate = new Date();
                d.setTime(myDate);
                int year = d.get(Calendar.YEAR);
                int month = d.get(Calendar.MONTH);
                int day = d.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month++;
                        String createDate = "" + year + "-" + month + "-" + day;
                        createTimeBtn.setText(createDate);
                        result[position].setCreateTime(DateUtil.dateToMill(createDate));
                    }
                }, year, month, day).show();
            }
        });

        //备注
        commentField.addTextChangedListener(new CommonUtil.SimpleTextWatch() {
            @Override
            public void afterTextChanged(Editable editable) {
                result[position].setComment(editable.toString());
            }
        });

        //标签
        tagGroupField.setOnTagChangeListener(new TagGroup.OnTagChangeListener() {
            @Override
            public void onAppend(TagGroup tagGroup, String tag) {
                List<String> tagList = Arrays.asList(tagGroup.getTags());
                result[position].getTypeList().addAll(tagList);
            }

            @Override
            public void onDelete(TagGroup tagGroup, String tag) {
                List<String> tagList = Arrays.asList(tagGroup.getTags());
                result[position].getTypeList().addAll(tagList);
            }
        });
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public int getCount() {
        return pathList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    /**
     * 获取所有处理过的信息
     */
    public List<ImageBean> getSavingData(){
        return Arrays.asList(result);
    }

    /**
     * 返回是否全部处理过
     */
    public boolean isFinished(){
        for (ImageBean bean : result){
            if (bean.getTitle() == null){
                return false;
            }
        }
        return true;
    }
}
