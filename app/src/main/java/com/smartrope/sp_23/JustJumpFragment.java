package com.smartrope.sp_23;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class JustJumpFragment extends Fragment {
    JustJumpTraining justJumpTraining;
    TextView
            tvCounter,
            tvTimer,
            tvKcal,
            tvRPM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_just_jump, container, false);
        tvCounter = view.findViewById(R.id.tvCounter);
        tvKcal = view.findViewById(R.id.tvKcal);
        tvRPM = view.findViewById(R.id.tvRPM);
        tvTimer = view.findViewById(R.id.tvTimer);
        justJumpTraining = new JustJumpTraining();

        return view;
    }


}