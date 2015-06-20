package com.cngu.shades.presenter;

import android.util.Log;

import com.cngu.shades.fragment.ShadesFragment;

public class ShadesPresenter {
    private static final String TAG = "ShadesPresenter";
    private static final boolean DEBUG = true;

    private ShadesFragment view;

    public ShadesPresenter(ShadesFragment view) {
        if (view == null) {
            throw new IllegalArgumentException("view cannot be null");
        }

        this.view = view;
        this.view.registerPresenter(this);

        if (DEBUG) Log.i(TAG, "Registered View");
    }


}
