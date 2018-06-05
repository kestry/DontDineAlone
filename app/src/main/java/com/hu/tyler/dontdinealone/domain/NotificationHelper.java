package com.hu.tyler.dontdinealone.domain;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import com.hu.tyler.dontdinealone.MatchedActivity;
import com.hu.tyler.dontdinealone.R;

public class NotificationHelper {

    // Displays the match notification
    public static void showMatchNotification(Activity lobbyActivity){
        showNotification(lobbyActivity, "Match Found!", "You have a match waiting.");
    }

    //Displays the message notification.
    public static void showMessageNotification(Activity matchedActivity){
        showNotification(matchedActivity, "New Message", "You have a new chat message waiting");
    }

    // Helper method that creates notification from supplied parameters
    public static void showNotification(Activity activity, String title, String message){
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibration = {1000};
        PendingIntent matchedActivityPendingIntent = createPendingIntent(activity.getBaseContext());

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity.getApplicationContext());
        Notification notification = new NotificationCompat.Builder(activity.getApplicationContext())
                .setSmallIcon(R.drawable.peach)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(matchedActivityPendingIntent)
                .setSound(sound)
                .setVibrate(vibration)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(null,0, notification);
    }

    // Helper method creates PendingIntent that allows the notification click to bring us to
    // the current running instance of MatchedActivity
    private static PendingIntent createPendingIntent(Context baseContext) {
        Intent matchedActivityIntent = new Intent(baseContext, MatchedActivity.class);
        matchedActivityIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return PendingIntent.getActivity(baseContext, 0, matchedActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
