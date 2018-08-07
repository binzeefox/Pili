package com.forradical.binzee.collectionforlisab.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.forradical.binzee.collectionforlisab.base.FoxApplication;

public class PopupUtil {

    protected Snackbar getSnackbar(View view, CharSequence message){
        if (FoxApplication.mSnackbar != null){
            FoxApplication.mSnackbar.setText(message).setAction(null, null);
        }else {
            FoxApplication.mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        }
        return FoxApplication.mSnackbar;
    }

    protected Snackbar getSnackbar(View view, int resource){
        if (FoxApplication.mSnackbar != null){
            FoxApplication.mSnackbar.setText(resource).setAction(null, null);
        }else {
            FoxApplication.mSnackbar = Snackbar.make(view, resource, Snackbar.LENGTH_LONG);
        }
        return FoxApplication.mSnackbar;
    }
}
