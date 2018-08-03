package com.forradical.binzee.collectionforlisab.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.forradical.binzee.collectionforlisab.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public abstract class FoxFragment extends Fragment {

    protected ViewGroup rootView;
    protected CompositeDisposable dContainer;
    Unbinder unbinder;

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_base, container, false);

        View contentView = getLayoutInflater().inflate(onInflateLayout(), null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.addView(contentView, params);
        unbinder = ButterKnife.bind(this, rootView);
        create(savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (!dContainer.isDisposed())
            dContainer.dispose();
    }

    protected abstract void create(Bundle savedInstanceState);

    protected abstract int onInflateLayout();
}
