package com.forradical.binzee.collectionforlisab.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.forradical.binzee.collectionforlisab.R;
import com.forradical.binzee.collectionforlisab.base.litepal.ImageBean;
import com.forradical.binzee.collectionforlisab.utils.CommonUtil;
import com.forradical.binzee.collectionforlisab.utils.DateUtil;

import java.util.List;

import me.gujun.android.taggroup.TagGroup;

public class PictureDetailDialog extends CustomDialogFragment {

    /**
     * 静态获取
     */
    public static CustomDialogFragment get(Context ctx, ImageBean bean) {
        View rootView = LayoutInflater.from(ctx).inflate(R.layout.dialog_detail_layout, null);
        setupViews(rootView, bean);
        return CustomDialogFragment.get(ctx)
                .title(bean.getTitle())
                .view(rootView)
                .positiveButton("返回", new CommonUtil.SimpleDialogConfirmListener());
    }

    /**
     * 准备布局
     */
    public static void setupViews(View rootView, ImageBean bean) {
        long createTime = bean.getCreateTime();
        List<String> tags = bean.getTypeList();
        String comment = bean.getComment();

        TextView createTimeField = rootView.findViewById(R.id.create_time_field);
        TagGroup tagGroupField = rootView.findViewById(R.id.tag_group_field);
        TextView commentField = rootView.findViewById(R.id.comment_field);

        createTimeField.setText(DateUtil.millToDate(createTime));
        tagGroupField.setTags(tags);
        if (comment != null)
            commentField.setText(comment);
        else
            commentField.setText("空");
    }
}
