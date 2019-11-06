package com.example.palarm.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.afollestad.aesthetic.AestheticActivity;
import com.example.palarm.R;
import com.example.palarm.fragments.BaseFragment;
import com.example.palarm.fragments.HomeFragment;
import com.example.palarm.fragments.SplashFragment;
import com.example.palarm.palarm;
import com.example.palarm.receivers.TimerReceiver;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.AlarmClock;

import java.lang.ref.WeakReference;

//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // SectionsPagerAdapter는 각각의 ViewPager에 뿌려질 데이터를 담고 있다.
//        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
//
//        ViewPager viewPager = findViewById(R.id.view_pager);
//        viewPager.setAdapter(sectionsPagerAdapter);
//
//        TabLayout tabs = findViewById(R.id.tabs);
//        tabs.setupWithViewPager(viewPager);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
//}
public class MainActivity extends AestheticActivity implements FragmentManager.OnBackStackChangedListener, palarm.ActivityListener {

    public static final String EXTRA_FRAGMENT = "com.example.palarm.MainActivity.EXTRA_FRAGMENT";
    public static final int FRAGMENT_TIMER = 0;
    public static final int FRAGMENT_STOPWATCH = 2;

    private palarm palarm;
    private WeakReference<BaseFragment> fragmentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        palarm = (palarm) getApplicationContext();
        palarm.setListener(this);

        if (savedInstanceState == null) {
            BaseFragment fragment = createFragmentFor(getIntent());
            if (fragment == null)
                return;

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, fragment)
                    .commit();

            fragmentRef = new WeakReference<>(fragment);
        } else {
            BaseFragment fragment;

            if (fragmentRef == null || (fragment = fragmentRef.get()) == null)
                fragment = new HomeFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();

            fragmentRef = new WeakReference<>(fragment);
        }

        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (isActionableIntent(intent)) {
            FragmentManager manager = getSupportFragmentManager();
            BaseFragment newFragment = createFragmentFor(intent);
            BaseFragment fragment = fragmentRef != null ? fragmentRef.get() : null;

            if (newFragment == null || newFragment.equals(fragment)) // check that fragment isn't already displayed
                return;

            if (newFragment instanceof HomeFragment && manager.getBackStackEntryCount() > 0) // clear the back stack
                manager.popBackStack(manager.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

            FragmentTransaction transaction = manager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_up_sheet, R.anim.slide_out_up_sheet, R.anim.slide_in_down_sheet, R.anim.slide_out_down_sheet)
                    .replace(R.id.fragment, newFragment);

            if (fragment instanceof HomeFragment && !(newFragment instanceof HomeFragment))
                transaction.addToBackStack(null);

            fragmentRef = new WeakReference<>(newFragment);
            transaction.commit();
        }
    }
    /**
     * Return a fragment to display the content provided by
     * the passed intent.
     *
     * @param intent    The intent passed to the activity.
     * @return          An instantiated fragment corresponding
     *                  to the passed intent.
     */
    @Nullable
    private BaseFragment createFragmentFor(Intent intent) {
        BaseFragment fragment = fragmentRef != null ? fragmentRef.get() : null;
        int fragmentId = intent.getIntExtra(EXTRA_FRAGMENT, -1);

        switch (fragmentId) {

            default:
                if (Intent.ACTION_MAIN.equals(intent.getAction()) || intent.getAction() == null)
                    return new SplashFragment();

                Bundle args = new Bundle();
                args.putString(HomeFragment.INTENT_ACTION, intent.getAction());

                BaseFragment newFragment = new HomeFragment();
                newFragment.setArguments(args);
                return newFragment;
        }
    }

    /**
     * Determine if something needs to be done as a result
     * of the intent being sent to the activity - which has
     * a higher priority than any fragment that is currently
     * open.
     *
     * @param intent    The intent passed to the activity.
     * @return          True if a fragment should be replaced
     *                  with the action that this intent entails.
     */
    private boolean isActionableIntent(Intent intent) {
        return intent.hasExtra(EXTRA_FRAGMENT)
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && (AlarmClock.ACTION_SHOW_ALARMS.equals(intent.getAction())
                || AlarmClock.ACTION_SET_TIMER.equals(intent.getAction()))
                || AlarmClock.ACTION_SET_ALARM.equals(intent.getAction())
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && (AlarmClock.ACTION_SHOW_TIMERS.equals(intent.getAction()))));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (palarm != null)
            palarm.setListener(null);

        palarm = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState != null ? outState : new Bundle());
    }

    @Override
    protected void onPause() {
        super.onPause();
        palarm.stopCurrentSound();
    }

    @Override
    public void onBackStackChanged() {
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        fragmentRef = new WeakReference<>(fragment);
    }

    @Override
    public void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, 0);
    }

    @Override
    public FragmentManager gettFragmentManager() {
        return getSupportFragmentManager();
    }
}
