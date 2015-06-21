package com.cngu.shades.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cngu.shades.R;
import com.cngu.shades.presenter.ShadesPresenter;

public class ShadesFragment extends Fragment {
    private static final String TAG = "ShadesFragment";
    private static final boolean DEBUG = true;

    private ShadesPresenter presenter;

    public ShadesFragment() {
        // Android Fragments require an explicit public default constructor for re-creation
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shades, container, false);

        Button startShadesButton = (Button) view.findViewById(R.id.start_shades_button);
        startShadesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onShadesFabClicked();
            }
        });

        Button stopShadesButton = (Button) view.findViewById(R.id.stop_shades_button);
        stopShadesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    public void registerPresenter(ShadesPresenter presenter) {
        if (presenter == null) {
            throw new IllegalArgumentException("presenter cannot be null");
        }
        this.presenter = presenter;

        if (DEBUG) Log.i(TAG, "Registered Presenter");
    }

    public void startService(Intent service) {
        getActivity().startService(service);
    }
}
