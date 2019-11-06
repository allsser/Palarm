package com.example.palarm.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.palarm.activities.AlarmActivity;
import com.example.palarm.data.TimerData;
import com.example.palarm.palarm;

public class TimerReceiver extends BroadcastReceiver {

    public static final String EXTRA_TIMER_ID = "james.alarmio.EXTRA_TIMER_ID";

    @Override
    public void onReceive(Context context, Intent intent) {
        palarm palarm = (palarm) context.getApplicationContext();
        TimerData timer = palarm.getTimers().get(intent.getIntExtra(EXTRA_TIMER_ID, 0));
        palarm.removeTimer(timer);

        Intent ringer = new Intent(context, AlarmActivity.class);
        ringer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ringer.putExtra(AlarmActivity.EXTRA_TIMER, timer);
        context.startActivity(ringer);
    }
}
