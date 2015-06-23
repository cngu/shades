package com.cngu.shades.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.widget.Space;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cngu.shades.R;
import com.cngu.shades.presenter.ShadesPresenter;

public class ShadesFragment extends PreferenceFragment {
    private static final String TAG = "ShadesFragment";
    private static final boolean DEBUG = true;

    private ShadesPresenter mPresenter;
    private Button mShadesFab;

    public ShadesFragment() {
        // Android Fragments require an explicit public default constructor for re-creation
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v = super.onCreateView(inflater, container, savedInstanceState);

        mShadesFab = (Button) getActivity().findViewById(R.id.shades_fab);
        mShadesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onShadesFabClicked();
            }
        });

        // NOTE: Wait until layout to find the padding and x/y locations. For some reason, onStart()
        //       and onResume() both occur before the FAB is measured.
        final ViewTreeObserver viewTreeObserver = mShadesFab.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // Must obtain the most up-to-date ViewTreeObserver here
                    mShadesFab.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    // Obtain a reference to the internal PreferenceFragment ListView
                    final ListView prefFragListView = (ListView) getActivity().findViewById(android.R.id.list);

                    // Add a bottom padding to the ListView to accommodate the FAB
                    int paddingTop = prefFragListView.getPaddingTop();
                    int paddingLeft = prefFragListView.getPaddingLeft();
                    int paddingRight = prefFragListView.getPaddingRight();
                    int paddingBottom = prefFragListView.getPaddingBottom() +
                            prefFragListView.getBottom() - mShadesFab.getTop();

                    // frameworks/base/core/res/res/layout/preference_list_fragment.xml already
                    // disables clipToPadding for us, but we disable it again in case this changes
                    // in future versions.
                    prefFragListView.setClipToPadding(false);
                    prefFragListView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                    prefFragListView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
                }
            });
        }

        return v;
    }

    public void registerPresenter(ShadesPresenter presenter) {
        if (presenter == null) {
            throw new IllegalArgumentException("presenter cannot be null");
        }
        mPresenter = presenter;

        if (DEBUG) Log.i(TAG, "Registered Presenter");
    }
}
