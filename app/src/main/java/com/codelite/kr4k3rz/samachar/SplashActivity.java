package com.codelite.kr4k3rz.samachar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codelite.kr4k3rz.samachar.util.FeedLists;
import com.orhanobut.hawk.Hawk;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Hawk.init(getBaseContext()).build();
        initFeedList();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        boolean checked = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("pushNotification", true);
        Log.i("TAG", "checked value : " + checked);

        if (checked) {
            scheduleAlarm();
            Log.i("TAG", "  Alarm scheduled");

        } else {
            Log.i("TAG", "  Alarm scheduled Canceled");
            cancelAlarm();

        }
        finish();
    }


    private void initFeedList() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            Hawk.put("updatedData", FeedLists.feedsListSetup());   //<---- Setups your feed into database
            // mark first time has run.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
        }


    }


    private void scheduleAlarm() {
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, 0,
                intent, 0);
        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis + AlarmManager.INTERVAL_HALF_HOUR,
                AlarmManager.INTERVAL_HOUR, pIntent);
    }

    private void cancelAlarm() {
        Intent intent = new Intent(getBaseContext(), MyAlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(getBaseContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);

    }

}