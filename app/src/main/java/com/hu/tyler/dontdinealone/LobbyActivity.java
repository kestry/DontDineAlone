package com.hu.tyler.dontdinealone;

/* Jean -
 * Duplicates should be fixed now with the unique Firebase UID being used for online users
 * but people will still show up as online on forced quits.
 */

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hu.tyler.dontdinealone.data.model.Chat;
import com.hu.tyler.dontdinealone.data.model.Collections;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.entity.OnlineUser;
import com.hu.tyler.dontdinealone.data.model.Documents;
import com.hu.tyler.dontdinealone.data.model.MatchPreferences;
import com.hu.tyler.dontdinealone.domain.ArrayService;
import com.hu.tyler.dontdinealone.domain.NotificationService;
import com.hu.tyler.dontdinealone.domain.OnlineService;
import com.hu.tyler.dontdinealone.domain.QueueService;
import com.hu.tyler.dontdinealone.domain.UserStatusService;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;
import com.hu.tyler.dontdinealone.util.Callback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LobbyActivity extends AppCompatActivity {

    private Collections collections;
    private Documents documents;
    private boolean findingMatch = false; //variable to indicate on going search
    private boolean goingToMatching = false; // prevents MatchingActivity from running twice
    ///Tyler Edits: 5/15
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference onlineUsers; // for group items
    private CollectionReference matchedUsers;
    TextView hiTxt;
    OnlineUser user; // object to hold user info
    ///////////////////

    // List items
    private String[] diningHallsFormatted;
    MatchPreferences matchPreferences;
    boolean[] groupSizePreferences;
    boolean[] diningHallPreferences;

    Button buttonMatch;
    String transitionID = "0"; //this will hold the document ID for 2 matches TODO: What is a transitionId exactly?
    private ProgressDialog progressDialog;

    // Lifecycle Methods -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        buttonMatch = findViewById(R.id.buttonMatch); //assigning variable to button

        collections = Collections.getInstance();
        documents = Documents.getInstance();

        onlineUsers = collections.getOnlineUsersCRef();
        matchedUsers = collections.getMatchedCRef();

        //Check if user is not logged in
        if (!Entity.authUser.isSignedIn(new SignedInCallback())) {
            //Close this activity
            finish();
            //Start Main activity
            startActivity(new Intent(this, MainActivity.class));
        }

        diningHallsFormatted = getResources().getStringArray(R.array.diningHallsFormatted);

        matchPreferences = Entity.matchPreferences;

        Toast.makeText(LobbyActivity.this,
                "matchPref GS length" + matchPreferences.getGroupSizePreferences().size(), Toast.LENGTH_SHORT).show();
        Toast.makeText(LobbyActivity.this,
                "matchPref DH length" + matchPreferences.getDiningHallPreferences().size(), Toast.LENGTH_SHORT).show();
        groupSizePreferences = ArrayService
                .makeArrayFromList(matchPreferences.getGroupSizePreferences());
        diningHallPreferences = ArrayService
                .makeArrayFromList(matchPreferences.getDiningHallPreferences());

        progressDialog = new ProgressDialog(this);
        // List items are retrieved from "app/res/values/strings.xml"

        user = Entity.onlineUser;
    }

    ///////////TYLERS EDITS/////////
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("XXX", "transitionID @ onStart: " + transitionID);
        user = Entity.onlineUser;
        onlineUsers.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                Log.d("XXX", "transitionID @ onStart @ addSnapshotListener: " + transitionID);

                if(e!=null){
                    // We encountered an exception.
                    return;
                }

                String data = "Online Users:\n\n";
                List<DocumentSnapshot> snaps = queryDocumentSnapshots.getDocuments();
                int loopCounter = 0;
                for(DocumentSnapshot snap : snaps)
                {
                    OnlineUser otherUser = snap.toObject(OnlineUser.class);
                    Log.d("XXX", "for loop users" + ++loopCounter + ": " + otherUser.getName());
                    otherUser.setDocumentId(snap.getId());
                    boolean isValidUserAndChatId = user != null && snap.get("chatID") != null;
                    if(isValidUserAndChatId) {
                        boolean isOurUser = snap.getId().equals(user.getDocumentId());
                        if(isOurUser) {
                            // User has been matched
                            if(!snap.get("chatID").toString().equals("0")) {
                                transitionID = snap.get("chatID").toString();
                                if(goingToMatching == false)
                                {
                                    goingToMatching = true;// prevents 2x execution
                                    goToMatchingActivity();

                                }
                            }
                        }
                    }
                    data += otherUser.getName() +"\n" + otherUser.getEmail() +
                            "\nStatus Code: " + otherUser.getStatus() +
                            "\nDocID: " + otherUser.getDocumentId() + "\n\n";
                }
                TextView onlineCount = findViewById(R.id.onlineCount);
                onlineCount.setText(data);
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(findingMatch == false && user != null) {
            onlineUsers.document(user.getDocumentId()).delete();
        }
        //Starts the NotificationService in the background.
        Intent notificationService = new Intent(this, NotificationService.class);
        startService(notificationService);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent notificationService = new Intent(this, NotificationService.class);
        stopService(notificationService);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(user == null)
            return;
        QueueService.leaveQueue();
        onlineUsers.document(user.getDocumentId()).delete();
    }

    // Presenter Methods -------------------------------------------------------------------------

    public void startMatching(View v) {
        if(user == null) {
            return; //
        }
        //TODO: begin matching logic
        if (findingMatch) {
            QueueService.leaveQueue();
            OnlineService.goBackOnline();
            findingMatch = false;
            buttonMatch.setText("Start Matching");
            buttonMatch.setBackgroundColor(Color.parseColor("#FF9900"));
            return;
        }
        //setMatchPreferences(v);
        queueUser();
    }

    public void queueUser() {
        findingMatch = true;
        buttonMatch.setBackgroundColor(Color.parseColor("#FF4081"));
        buttonMatch.setText("Stop Matching");
        QueueService.enterQueue(new StoreCallback());
        //lookingFortheHungry();
    }

    protected void lookingFortheHungry() {
        // Look for other people and we can distinguish them b/c their status code is "waiting".
        onlineUsers.whereEqualTo("status", DatabaseStatuses.User.queued)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        List<DocumentSnapshot> snaps = documentSnapshots.getDocuments();
                        for(DocumentSnapshot snap : snaps) {
//                            remove the element that has a identical email -- TODO: What does this mean?
                            String otherId = snap.getId();
                            String ourId = user.getDocumentId();

                            boolean foundOtherUser = !otherId.equalsIgnoreCase(ourId);
                            if(foundOtherUser) {
                                Toast.makeText(LobbyActivity.this,
                                        "Match Found!", Toast.LENGTH_SHORT).show();
                                //TODO: probably should check whether someone already changed your status before proceeding further.
                                /*
                                //PRECAUTION SLOWDOWNS -- TODO: What does this mean?
                                boolean foundMatchAlready = user.getStatus() == DatabaseStatuses.User.matched;
                                if(transitionID != "0" || foundMatchAlready) {
                                    return;
                                }
                                ////////////END OF EXTRA PRECAUTIONS
                                */
                                UserStatusService.updateEverywhere(
                                        documents.getOnlineUserDocRef(),
                                        DatabaseStatuses.User.matched);

                                //Get the user we are matched with
                                final DocumentReference otherDocRef = db.collection("Online").document(otherId);
                                Log.d("XXX","otherId.equals(ourId):" + otherId.equals(ourId));
                                Log.d("XXX", "id: "+ otherId + " Matched Email: " + snap.getString("email"));

                                SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.US);
                                String date = s.format(new Date());
                                final Chat mchat = new Chat(user.getName(),"Ready",2, date);
                                 matchedUsers.add(mchat).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                     @Override
                                     public void onSuccess(DocumentReference documentReference) {
                                         mchat.setDocumentId(documentReference.getId());
                                         transitionID = mchat.getDocumentId();
                                         Log.d("XXX", "TransitionID @ lookingFortheHungry" + transitionID);
                                         otherDocRef.update("chatID", transitionID); //update the chatID of the matched person
                                         user.setNewDoc(transitionID);
                                         onlineUsers.document(user.getDocumentId()).update("chatID", transitionID);
                                         findingMatch = false; //no longer finding matches
                                         goingToMatching = true; //transitioning to a new activity
                                         goToMatchingActivity();
                                     }
                                 });
                            }
                        }
                    }
                });
    }

    public void goToMatchingActivity() {
        Intent myIntent = new Intent(this, MatchedActivity.class);
        finish();
        myIntent.putExtra("key", transitionID); //Optional parameters
        myIntent.putExtra("name", user.getName()); //Optional parameters

        this.startActivity(myIntent);

    }
    /////////////////////////////End of Tylers Edit


    public void loadOnlineUsers() {
        onlineUsers.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                String data = "Online Users:\n\n";

                Toast.makeText(LobbyActivity.this, "Inside loadOnlineUsers().Sucess", Toast.LENGTH_SHORT).show();
                List<DocumentSnapshot> snaps = documentSnapshots.getDocuments();
                Toast.makeText(LobbyActivity.this, "# of people online" + snaps.size(), Toast.LENGTH_SHORT).show();
                int loopCount = 0;
                for (DocumentSnapshot snap : snaps) {
                    Log.d("XXX", "for loop " + ++loopCount);
                    OnlineUser otherUser = snap.toObject(OnlineUser.class);
                    otherUser.setDocumentId(snap.getId());
                    data += otherUser.getName() +"\n" + otherUser.getEmail() +
                            "\nStatus Code: " + otherUser.getStatus() +
                            "\nDocID: " + otherUser.getDocumentId() + "\n\n";
                }
                TextView onlineCount = findViewById(R.id.onlineCount);
                onlineCount.setText(data);
//                for(QueryDocumentSnapshot querySnap: snaps){
//                    OnlineUser otherUser = documentSnapshot.toObject(OnlineUser.class);
//                }
            }
        });
    }

    public void setMatchPreferences(View v) {
        // TODO: Will cancelable = false help?
        // These are asynchronous listeners which means they won't wait for selections to be made to
        // return control, so we need to check that diningHallChoices and groupSizeChoices contain
        // values when we use them.
        checkBoxGroupSizePreferences();
    }

    // Selection menu for group size choices
    public void checkBoxGroupSizePreferences() {
        final AlertDialog groupSizeSelection;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_group_sizes_label);
        builder.setMultiChoiceItems(DatabaseKeys.Preference.GROUP_SIZES, groupSizePreferences,
                new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                groupSizePreferences[i] = isChecked;
                matchPreferences.setGroupSizePreferenceAt(i, isChecked);
            }
        });
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (matchPreferences.hasChosenAGroupSizePreference()) {
                    // Since we made a selection, we are ok to continue selecting next preference.
                    checkBoxDiningHallPreferences();
                } else {
                    Toast.makeText(LobbyActivity.this,
                            "Please make a selection", Toast.LENGTH_SHORT).show();
                    // We call our function again since the user didn't make a selection.
                    checkBoxGroupSizePreferences();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) { }
        });
        groupSizeSelection = builder.create();
        groupSizeSelection.show();
    }

    // Selection menu for dining hall choices.
    public void checkBoxDiningHallPreferences() {

        final AlertDialog diningHallSelection;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_dining_halls_label);
        builder.setMultiChoiceItems(diningHallsFormatted, diningHallPreferences,
                new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                diningHallPreferences[i] = isChecked;
                matchPreferences.setDiningHallPreferenceAt(i, isChecked);
            }
        });
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (matchPreferences.hasChosenADiningHallPreference()) {
                    // Store the preferences.
                    saveMatchPreferences();
                } else {
                    Toast.makeText(LobbyActivity.this,
                            "Please make a selection", Toast.LENGTH_SHORT).show();
                    // We call our function again since the user didn't make a selection.
                    checkBoxDiningHallPreferences();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) { }
        });
        diningHallSelection = builder.create();
        diningHallSelection.show();
    }

    public void saveMatchPreferences() {
        documents.getUserMatchPreferencesDocRef().set(matchPreferences)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();

                        Toast.makeText(LobbyActivity.this,
                                "Match preferences saved successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();

                        Log.w("XXX", "Save error: ", e);
                        Toast.makeText(LobbyActivity.this,
                                "Failed to save match preferences", Toast.LENGTH_SHORT).show();
                        Toast.makeText(LobbyActivity.this,
                                "Error: " + e, Toast.LENGTH_LONG).show();
                    }
                });

    }

    // Navigation Methods ------------------------------------------------------------------------

    public void goToEditProfileActivity(View v) {
//        progressDialog.setMessage("Loading Profile...");
//        progressDialog.show();
        Intent x = new Intent(this, EditProfileActivity.class);
        finish();
        startActivity(x);
    }

    public void goToMainActivity(View v) {
        Entity.authUser.signOut();
        Toast.makeText(this, "Logged Out.", Toast.LENGTH_SHORT).show();
        //Next time the app opens go to MainActivity
        Intent x = new Intent(this, MainActivity.class);
        finish();
        startActivity(x);
        //Terminate the current activity
    }

    // Callbacks ---------------------------------------------------------------------------------

    final class StoreCallback implements Callback {

        @Override
        public void onSuccess() {
            progressDialog.dismiss();
            Toast.makeText(LobbyActivity.this, "Preferences saved. Beginning matching..", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Exception e) {
            progressDialog.dismiss();

            Log.w("XXX", "Save error: ", e);
            Toast.makeText(LobbyActivity.this, "Preference save failed", Toast.LENGTH_SHORT).show();
            Toast.makeText(LobbyActivity.this, "Preference Save Error: " + e, Toast.LENGTH_LONG).show();
        }
    }

    final class SignedInCallback implements Callback {

        @Override
        public void onSuccess() {
            progressDialog.dismiss();
            ////////////Tyler's Edits

            hiTxt = findViewById(R.id.textViewTitle);
            String g = "Welcome " + Entity.user.getDisplayName();
            hiTxt.setText(g);
            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.US);
            String date = s.format(new Date());
            if(Entity.authUser == null)
                return;
            loadOnlineUsers();
            ///////////////////// End of Tylers Edit
        }

        @Override
        public void onFailure(Exception e) {
        }
    }
}
