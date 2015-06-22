package com.cngu.shades.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.cngu.shades.R;
import com.cngu.shades.presenter.ShadesPresenter;

public class ShadesFragment extends PreferenceFragment {
    private static final String TAG = "ShadesFragment";
    private static final boolean DEBUG = true;

    private ShadesPresenter mPresenter;

    public ShadesFragment() {
        // Android Fragments require an explicit public default constructor for re-creation
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

    public void registerPresenter(ShadesPresenter presenter) {
        if (presenter == null) {
            throw new IllegalArgumentException("presenter cannot be null");
        }
        mPresenter = presenter;

        if (DEBUG) Log.i(TAG, "Registered Presenter");
    }
}
