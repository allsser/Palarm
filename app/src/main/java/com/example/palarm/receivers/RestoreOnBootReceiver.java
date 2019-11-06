package com.example.palarm.receivers;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.palarm.data.AlarmData;
import com.example.palarm.data.TimerData;
import com.example.palarm.palarm;


public class RestoreOnBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        palarm alarmio = (palarm) context.getApplicationContext();
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for (AlarmData alarm : alarmio.getAlarms()) {
            if (alarm.isEnabled)
                alarm.set(context, manager);
        }

        for (TimerData timer : alarmio.getTimers()) {
            if (timer.getRemainingMillis() > 0)
                timer.setAlarm(context, manager);
        }
    }
}
