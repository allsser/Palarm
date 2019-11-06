package com.example.palarm.interfaces;

import com.example.palarm.fragments.BasePagerFragment;

import androidx.annotation.Nullable;

public interface FragmentInstantiator {
    @Nullable
    BasePagerFragment newInstance(int position);
    @Nullable
    String getTitle(int position);
}
