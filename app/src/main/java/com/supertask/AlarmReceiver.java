package com.supertask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by pratik on 19/10/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final String ACTION_FOR_ALARM_PENDING_INTENT = "com.supertask.action.ALARM_RECEIVED";
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // set the alarm again
            Log.d(TAG, "From reboot");
            Util.setAlarm(context);
        } else if (intent.getAction().equals(ACTION_FOR_ALARM_PENDING_INTENT)) {
            Log.d(TAG, "From pending intent");
            Util.setImageChoiceForToday(context);
        }
    }
}
