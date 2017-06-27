package com.example.peter.sugar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(MainActivity.LOG_TAG, "BootCompletedReceiver: onReceive()");
        TimeManager.initProfiles(context);
    }
}
