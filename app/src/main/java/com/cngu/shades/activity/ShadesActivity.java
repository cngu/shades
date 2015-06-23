package com.cngu.shades.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cngu.shades.R;
import com.cngu.shades.fragment.ShadesFragment;
import com.cngu.shades.helpers.FilterCommandFactory;
import com.cngu.shades.helpers.FilterCommandSender;
import com.cngu.shades.presenter.ShadesPresenter;

public class ShadesActivity extends AppCompatActivity {
    private static final String TAG = "ShadesActivity";
    private static final boolean DEBUG = true;
    private static final String FRAGMENT_TAG_SHADES = "cngu.fragment.tag.SHADES";

    private ShadesPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shades);

        FragmentManager fragmentManager = getFragmentManager();

        ShadesFragment view;

        // Only create and attach a new fragment on the first Activity creation.
        // On Activity re-creation, retrieve the existing fragment stored in the FragmentManager.
        if (savedInstanceState == null) {
            if (DEBUG) Log.i(TAG, "onCreate - First creation");

            view = new ShadesFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, view, FRAGMENT_TAG_SHADES)
                    .commit();
        } else {
            if (DEBUG) Log.i(TAG, "onCreate - Re-creation");

            view = (ShadesFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG_SHADES);
        }

        // Connect MVP View and Presenter
        FilterCommandFactory filterCommandFactory = new FilterCommandFactory(this);
        FilterCommandSender filterCommandSender = new FilterCommandSender(this);
        mPresenter = new ShadesPresenter(view, filterCommandFactory, filterCommandSender);
    }
}
