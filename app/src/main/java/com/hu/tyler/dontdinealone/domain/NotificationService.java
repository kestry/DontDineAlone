/* USAGE INFORMATION
    To start the service you must create an intent, putExtra() all required data for that notification
    type, and pass the intent to startService() in the onPause() method.
    Pass the same intent to the stopService() method in onResume() to kill the service.
 */

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.hu.tyler.dontdinealone.LobbyActivity;
import com.hu.tyler.dontdinealone.MatchedActivity;
import com.hu.tyler.dontdinealone.R;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.model.Collections;
import com.hu.tyler.dontdinealone.data.model.Documents;
import com.hu.tyler.dontdinealone.data.entity.OnlineUser;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;

// Service for running in the background while the app is closed to listen for database changes and show notifications.
public class NotificationService extends Service {

    public static final int MATCH_NOTIFICATION = 0;
    public static final int MESSAGE_NOTIFICATION = 1;
    public static final String NOTIFICATION_TYPE = "NOTIFICATION_TYPE";
    public static final String CHAT_ID  = "CHAT_ID";

    private Collections collections;
    private Documents documents;
    private DocumentReference thisUserRef;
    private CollectionReference matchedRef;
    private CollectionReference myChatRef;
    private ListenerRegistration thisUserListener;
    private ListenerRegistration chatListener;
    private String chatID;
    private boolean firstSnapshot;

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
        matchedRef = collections.getMatchedCRef();
        firstSnapshot = true;
    }

    // Selects appropriate listener to start based on value passed through startService() via intent.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int notificationType = intent.getIntExtra(NOTIFICATION_TYPE, -1);
        switch (notificationType){
            case MATCH_NOTIFICATION:
                startMatchListener();
                break;
            case MESSAGE_NOTIFICATION:
                chatID = intent.getStringExtra(CHAT_ID);
                myChatRef = matchedRef.document(chatID).collection("Chatty");
                startMessageListener();
                break;
            default:
                Log.w("TAG", "Needs a valid notification type when starting NotificationService.");
                break;
        }
        return START_NOT_STICKY;
    }

    // Listens for when this OnlineUser status turns to "matched"
    public void startMatchListener() {
        // We'll probably need to change what we're listening to once we finalize how matching works.
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
                        showMatchNotification();
                        //Stops the background service after notification is pushed, may or may not want this later on.
                        stopSelf();
                    }
                }
            }
        });
    }

    // Displays the match notification
    public void showMatchNotification() {
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
                .setVibrate(vibration)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        int notificationId = 0;
        notificationManager.notify(notificationId, mBuilder.build());
    }

    // Listens for when new chat documents are added to our chat collection.
    public void startMessageListener() {
        //We'll probably need to change what we're listening to once we finalize how matching works.
        chatListener = myChatRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }
                if(firstSnapshot){
                    firstSnapshot = false;
                }else{
                    showMessageNotification();
                    stopSelf();
                }
                /*
                for(DocumentChange dc : snapshots.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED) {
                        showMessageNotification();
                        stopSelf();
                    }
                }
                */
            }
        });
    }

    // Displays the message notification
    public void showMessageNotification() {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibration = {1000};

        Intent matchingActivityIntent = new Intent(this, MatchedActivity.class);
        matchingActivityIntent.putExtra("key", chatID); //Optional parameters
        matchingActivityIntent.putExtra("name", Entity.user.getDisplayName()); //Optional parameters

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(matchingActivityIntent);
        PendingIntent matchingActivityPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.peach)
                .setContentTitle("New Message!")
                .setContentText("You have a new chat message.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(matchingActivityPendingIntent)
                .setSound(sound)
                .setVibrate(vibration)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        int notificationId = 0;
        notificationManager.notify(notificationId, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        if (thisUserListener != null){
            thisUserListener.remove();
        }
        if (chatListener != null){
            chatListener.remove();
        }
    }
}