package com.example.palarm.adapters;

import android.content.Context;

import com.example.palarm.fragments.BasePagerFragment;
import com.example.palarm.interfaces.FragmentInstantiator;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SimplePagerAdapter extends FragmentStatePagerAdapter {

    private FragmentInstantiator[] fragments;

    public SimplePagerAdapter(Context context, FragmentManager fm, FragmentInstantiator... fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public BasePagerFragment getItem(int position) {
        return fragments[position].newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments[position].getTitle(position);
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

}
