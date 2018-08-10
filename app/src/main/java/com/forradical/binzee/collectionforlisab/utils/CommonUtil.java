package com.forradical.binzee.collectionforlisab.utils;

import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;

public class CommonUtil {

    public static class SimpleDialogConfirmListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    /**
     * 建议文字监听
     */
    public static class SimpleTextWatch implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    /**
     * 简易Pager监听器
     */
    public static abstract class SimpleOnPagerChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
