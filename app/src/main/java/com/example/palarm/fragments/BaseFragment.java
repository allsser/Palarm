package com.example.palarm.fragments;

import android.os.Bundle;

import com.example.palarm.palarm;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment implements palarm.AlarmioListener {

    private palarm palarm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        palarm = (palarm) getContext().getApplicationContext();
        palarm.addListener(this);
    }

    @Override
    public void onDestroy() {
        palarm.removeListener(this);
        palarm = null;
        super.onDestroy();
    }

    @Nullable
    protected palarm getAlarmio() {
        return palarm;
    }

    public void notifyDataSetChanged() {
        // Update the info displayed in the fragment.
    }

    @Override
    public void onAlarmsChanged() {
        // Update any alarm-dependent data.
    }

    @Override
    public void onTimersChanged() {
        // Update any timer-dependent data.
    }
}
