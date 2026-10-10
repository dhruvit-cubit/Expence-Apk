package com.kharchakhata.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONObject;

/** Keeps the list of EMI alerts and sets one alarm for each of them. */
public class AlertScheduler {

    private static final String PREFS = "kk_alerts";
    private static final String KEY = "list";

    /** Replaces all earlier alerts with the new list sent by the web page. */
    public static void apply(Context c, String json) {
        cancelAll(c);
        SharedPreferences sp = c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putString(KEY, json).apply();
        scheduleAll(c, json);
    }

    /** Sets the alarms again, for example after the phone restarts. */
    public static void restore(Context c) {
        SharedPreferences sp = c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = sp.getString(KEY, null);
        if (json != null) {
            scheduleAll(c, json);
        }
    }

    private static void scheduleAll(Context c, String json) {
        try {
            JSONArray list = new JSONArray(json);
            AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
            long now = System.currentTimeMillis();
            for (int i = 0; i < list.length(); i++) {
                JSONObject o = list.getJSONObject(i);
                long at = o.getLong("at");
                if (at <= now) {
                    continue;
                }
                PendingIntent pi = pending(c, o.getInt("id"), o.optString("title"), o.optString("body"));
                if (Build.VERSION.SDK_INT >= 23) {
                    am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, pi);
                } else {
                    am.set(AlarmManager.RTC_WAKEUP, at, pi);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static void cancelAll(Context c) {
        SharedPreferences sp = c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String old = sp.getString(KEY, null);
        if (old == null) {
            return;
        }
        try {
            JSONArray list = new JSONArray(old);
            AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
            for (int i = 0; i < list.length(); i++) {
                PendingIntent pi = pending(c, list.getJSONObject(i).getInt("id"), "", "");
                am.cancel(pi);
                pi.cancel();
            }
        } catch (Exception ignored) {
        }
    }

    private static PendingIntent pending(Context c, int id, String title, String body) {
        Intent i = new Intent(c, AlertReceiver.class);
        i.putExtra("id", id);
        i.putExtra("title", title);
        i.putExtra("body", body);
        return PendingIntent.getBroadcast(c, id, i,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }
}
