package com.cngu.shades.activity;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cngu.shades.R;
import com.cngu.shades.fragment.ShadesFragment;
import com.cngu.shades.helper.FilterCommandFactory;
import com.cngu.shades.helper.FilterCommandSender;
import com.cngu.shades.model.SettingsModel;
import com.cngu.shades.presenter.ShadesPresenter;

public class ShadesActivity extends AppCompatActivity {
    private static final String TAG = "ShadesActivity";
    private static final boolean DEBUG = true;
    private static final String FRAGMENT_TAG_SHADES = "cngu.fragment.tag.SHADES";

    private SharedPreferences mSharedPreferences;
    private ShadesPresenter mPresenter;
    private SettingsModel mSettingsModel;

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

        // Wire MVP classes
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSettingsModel = new SettingsModel(getResources(), mSharedPreferences);
        FilterCommandFactory filterCommandFactory = new FilterCommandFactory(this);
        FilterCommandSender filterCommandSender = new FilterCommandSender(this);

        mPresenter = new ShadesPresenter(view, mSettingsModel, filterCommandFactory, filterCommandSender);
        view.registerPresenter(mPresenter);

        // Make Presenter listen to settings changes
        mSettingsModel.setOnSettingsChangedListener(mPresenter);

        findViewById(R.id.debug_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String power = mSettingsModel.getShadesPowerState() ? "true" : "false";
                String debug = mSettingsModel.getShadesPauseState() ? "true" : "false";
                Log.d(TAG, String.format("POWER: %s PAUSE: %s", power, debug));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSettingsModel.openSettingsChangeListener();
        mPresenter.onStart();
    }

    @Override
    protected void onStop() {
        mSettingsModel.closeSettingsChangeListener();
        super.onStop();
    }
}
