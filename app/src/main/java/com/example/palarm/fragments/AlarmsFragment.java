package com.example.palarm.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.aesthetic.Aesthetic;
import com.example.palarm.R;
import com.example.palarm.adapters.AlarmsAdapter;
import com.example.palarm.interfaces.ContextFragmentInstantiator;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.Disposable;

public class AlarmsFragment extends BasePagerFragment {

    private RecyclerView recyclerView;
    private View empty;

    private AlarmsAdapter alarmsAdapter;

    private Disposable colorAccentSubscription;
    private Disposable colorForegroundSubscription;
    private Disposable textColorPrimarySubscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler, container, false);
        recyclerView = v.findViewById(R.id.recycler);
        empty = v.findViewById(R.id.empty);
        ((TextView) v.findViewById(R.id.emptyText)).setText(R.string.msg_alarms_empty);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        alarmsAdapter = new AlarmsAdapter(getAlarmio(), recyclerView, getFragmentManager());
        recyclerView.setAdapter(alarmsAdapter);

        colorAccentSubscription = Aesthetic.Companion.get()
                .colorAccent()
                .subscribe(integer -> alarmsAdapter.setColorAccent(integer));

        colorForegroundSubscription = Aesthetic.Companion.get()
                .colorCardViewBackground()
                .subscribe(integer -> alarmsAdapter.setColorForeground(integer));

        textColorPrimarySubscription = Aesthetic.Companion.get()
                .textColorPrimary()
                .subscribe(integer -> alarmsAdapter.setTextColorPrimary(integer));

        onChanged();
        return v;
    }

    @Override
    public void onDestroyView() {
        colorAccentSubscription.dispose();
        colorForegroundSubscription.dispose();
        textColorPrimarySubscription.dispose();
        super.onDestroyView();
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.title_alarms);
    }

    @Override
    public void onAlarmsChanged() {
        if (recyclerView != null && alarmsAdapter != null) {
            recyclerView.post(() -> alarmsAdapter.notifyDataSetChanged());

            onChanged();
        }
    }

    @Override
    public void onTimersChanged() {
        if (recyclerView != null && alarmsAdapter != null) {
            recyclerView.post(() -> alarmsAdapter.notifyDataSetChanged());

            onChanged();
        }
    }

    private void onChanged() {
        if (empty != null && alarmsAdapter != null)
            empty.setVisibility(alarmsAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }

    public static class Instantiator extends ContextFragmentInstantiator {

        public Instantiator(Context context) {
            super(context);
        }

        @Override
        public String getTitle(Context context, int position) {
            return context.getString(R.string.title_alarms);
        }

        @Nullable
        @Override
        public BasePagerFragment newInstance(int position) {
            return new AlarmsFragment();
        }
    }

}
