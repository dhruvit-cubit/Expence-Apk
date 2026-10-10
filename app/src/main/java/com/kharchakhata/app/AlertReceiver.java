package com.kharchakhata.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/** Shows the notification when an EMI alarm goes off. */
public class AlertReceiver extends BroadcastReceiver {

    private static final String CHANNEL = "emi_alerts";

    @Override
    public void onReceive(Context c, Intent intent) {
        try {
            NotificationManager nm = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder b;
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel ch = new NotificationChannel(CHANNEL, "EMI alerts", NotificationManager.IMPORTANCE_HIGH);
                nm.createNotificationChannel(ch);
                b = new Notification.Builder(c, CHANNEL);
            } else {
                b = new Notification.Builder(c);
            }

            Intent open = new Intent(c, MainActivity.class);
            PendingIntent tap = PendingIntent.getActivity(c, 0, open,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            b.setSmallIcon(R.drawable.ic_stat_kk)
                    .setContentTitle(intent.getStringExtra("title"))
                    .setContentText(intent.getStringExtra("body"))
                    .setContentIntent(tap)
                    .setAutoCancel(true);

            nm.notify(intent.getIntExtra("id", 0), b.build());
        } catch (Exception ignored) {
        }
    }
}
