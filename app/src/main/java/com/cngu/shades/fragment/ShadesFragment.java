package com.cngu.shades.fragment;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;

import com.cngu.shades.R;
import com.cngu.shades.presenter.ShadesPresenter;

public class ShadesFragment extends PreferenceFragment {
    private static final String TAG = "ShadesFragment";
    private static final boolean DEBUG = true;

    private ShadesPresenter mPresenter;
    private FloatingActionButton mShadesFab;
    private int mShadesFabIconResId = -1;

    public ShadesFragment() {
        // Android Fragments require an explicit public default constructor for re-creation
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        String openOnStartupKey = getString(R.string.pref_key_always_open_on_startup);
        String resumeAfterRebootPrefKey= getString(R.string.pref_key_keep_running_after_reboot);

        PreferenceScreen prefScreen = getPreferenceScreen();
        final CheckBoxPreference openOnStartupPref = (CheckBoxPreference) prefScreen.findPreference(openOnStartupKey);
        final CheckBoxPreference resumeAfterRebootPref = (CheckBoxPreference) prefScreen.findPreference(resumeAfterRebootPrefKey);

        openOnStartupPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                boolean checked = openOnStartupPref.isChecked();
                if (checked) {
                    resumeAfterRebootPref.setEnabled(false);
                } else {
                    resumeAfterRebootPref.setEnabled(true);
                }

                return false;
            }
        });

        resumeAfterRebootPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                boolean checked = resumeAfterRebootPref.isChecked();
                if (checked) {
                    openOnStartupPref.setEnabled(false);
                } else {
                    openOnStartupPref.setEnabled(true);
                }

                return false;
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = super.onCreateView(inflater, container, savedInstanceState);

        mShadesFab = (FloatingActionButton) getActivity().findViewById(R.id.shades_fab);
        mShadesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onShadesFabClicked();
            }
        });
        setShadesFabIcon(mShadesFab, mShadesFabIconResId);

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
                            //prefFragListView.getBottom() - mShadesFab.getTop();
                            mShadesFab.getBottom() - mShadesFab.getTop() - 20;

                    // frameworks/base/core/res/res/layout/preference_list_fragment.xml already
                    // disables clipToPadding for us, but we disable it again in case this changes
                    // in future versions.
                    prefFragListView.setClipToPadding(false);
                    prefFragListView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                    prefFragListView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

                    // Reduce the padding on the fab
                    float paddingFactor = 0.75f;
                    paddingLeft = (int) (mShadesFab.getPaddingLeft() * paddingFactor);
                    paddingTop = (int) (mShadesFab.getPaddingTop() * paddingFactor);
                    paddingRight = (int) (mShadesFab.getPaddingRight() * paddingFactor);
                    paddingBottom = (int) (mShadesFab.getPaddingBottom() * paddingFactor);
                    mShadesFab.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                }
            });
        }

        return v;
    }

    public void registerPresenter(@NonNull ShadesPresenter presenter) {
        mPresenter = presenter;

        if (DEBUG) Log.i(TAG, "Registered Presenter");
    }

    public void setShadesFabIcon(int drawableResId) {
        mShadesFabIconResId = drawableResId;
        setShadesFabIcon(mShadesFab, mShadesFabIconResId);
    }

    private void setShadesFabIcon(FloatingActionButton fab, int drawableResId) {
        if (fab != null && drawableResId != -1) {
            fab.setImageResource(drawableResId);
        }
    }
}
