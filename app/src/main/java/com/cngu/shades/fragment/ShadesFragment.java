package com.cngu.shades.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
        mShadesFab.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                mShadesFab.removeOnLayoutChangeListener(this);

                // Obtain a reference to the internal PreferenceFragment ListView
                ListView prefFragListView = (ListView) getActivity().findViewById(android.R.id.list);

                // Add a bottom padding to the ListView to accommodate the FAB
                int paddingTop = prefFragListView.getPaddingTop();
                int paddingLeft = prefFragListView.getPaddingLeft();
                int paddingRight = prefFragListView.getPaddingRight();
                int paddingBottom = prefFragListView.getPaddingBottom() +
                        prefFragListView.getBottom() - mShadesFab.getTop();

                prefFragListView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            }
        });

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
