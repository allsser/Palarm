package com.example.palarm.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.afollestad.aesthetic.Aesthetic;
import com.example.palarm.R;
import com.example.palarm.adapters.SimplePagerAdapter;
import com.example.palarm.data.SoundData;
import com.example.palarm.fragments.sound.AlarmSoundChooserFragment;
import com.example.palarm.fragments.sound.FileSoundChooserFragment;
import com.example.palarm.fragments.sound.RingtoneSoundChooserFragment;
import com.example.palarm.interfaces.SoundChooserListener;
import com.example.palarm.palarm;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

public class SoundChooserDialog extends DialogFragment implements SoundChooserListener {

    private SoundChooserListener listener;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                if (params != null) {
                    params.windowAnimations = R.style.SlideDialogAnimation;
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_sound_chooser, container, false);

        Aesthetic.Companion.get()
                .colorPrimary()
                .take(1)
                .subscribe(integer -> view.setBackgroundColor(integer));

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);

        AlarmSoundChooserFragment alarmFragment = new AlarmSoundChooserFragment();
        RingtoneSoundChooserFragment ringtoneFragment = new RingtoneSoundChooserFragment();
        FileSoundChooserFragment fileFragment = new FileSoundChooserFragment();

        alarmFragment.setListener(this);
        ringtoneFragment.setListener(this);
        fileFragment.setListener(this);

        viewPager.setAdapter(new SimplePagerAdapter(
                getContext(), getChildFragmentManager(),
                new AlarmSoundChooserFragment.Instantiator(view.getContext(), this),
                new RingtoneSoundChooserFragment.Instantiator(view.getContext(), this),
                new FileSoundChooserFragment.Instantiator(view.getContext(), this))
        );

        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    public void setListener(SoundChooserListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSoundChosen(SoundData sound) {
        if (listener != null)
            listener.onSoundChosen(sound);

        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (view != null)
            ((palarm) view.getContext().getApplicationContext()).stopCurrentSound();
    }
}
