package com.forradical.binzee.collectionforlisab.utils;

import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;

public class CommonUtil {

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
