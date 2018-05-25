package com.hu.tyler.dontdinealone.domain;

import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.hu.tyler.dontdinealone.LobbyActivity;
import com.hu.tyler.dontdinealone.R;
import com.hu.tyler.dontdinealone.data.model.Collections;
import com.hu.tyler.dontdinealone.data.model.Documents;
import com.hu.tyler.dontdinealone.data.entity.OnlineUser;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;

// Service for running in the background while the app is closed to listen for database changes and show notifications.
public class NotificationService extends Service {

    private Collections collections;
    private Documents documents;
    private DocumentReference thisUserRef;
    ListenerRegistration thisUserListener;

    // Returns null because we don't use it.
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        collections = Collections.getInstance();
        documents = Documents.getInstance();
        thisUserRef = documents.getOnlineUserDocRef();
    }

    // Starts listening for when this users OnlineUser status is matched
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //We'll probably need to change what we're listening to once we finalize how matching works.
        thisUserListener = thisUserRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    OnlineUser thisUser = snapshot.toObject(OnlineUser.class);
                    if(thisUser.getStatus().equals(DatabaseStatuses.User.matched)){
                        showNotification();
                        //Stops the background service after notification is pushed, may or may not want this later on.
                        stopSelf();
                    }
                }
            }
        });
        return START_NOT_STICKY;
    }

    public void showNotification(){
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibration = {1000};

        Intent lobbyActivityIntent = new Intent(this, LobbyActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(lobbyActivityIntent);
        PendingIntent lobbyActivityPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.peach)
                .setContentTitle("Match Found!")
                .setContentText("You have a match waiting.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(lobbyActivityPendingIntent)
                .setSound(sound)
                .setVibrate(vibration);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        int notificationId = 0;
        notificationManager.notify(notificationId, mBuilder.build());

    }

    @Override
    public void onDestroy() {
        thisUserListener.remove();
    }
}