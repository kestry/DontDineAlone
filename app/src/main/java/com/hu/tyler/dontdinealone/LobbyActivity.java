package com.hu.tyler.dontdinealone;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.hu.tyler.dontdinealone.data.model.Chat;
import com.hu.tyler.dontdinealone.data.model.Collections;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.entity.OnlineUser;
import com.hu.tyler.dontdinealone.data.model.Documents;
import com.hu.tyler.dontdinealone.data.model.MatchPreferences;
import com.hu.tyler.dontdinealone.domain.OnlineService;
import com.hu.tyler.dontdinealone.domain.PrimitiveArrayService;
import com.hu.tyler.dontdinealone.domain.UserStatusService;
import com.hu.tyler.dontdinealone.net.Session;
import com.hu.tyler.dontdinealone.net.Writer;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;
import com.hu.tyler.dontdinealone.util.Callback;
import com.hu.tyler.dontdinealone.util.NullCallback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LobbyActivity extends AppCompatActivity {

    private Collections collections;
    private Documents documents;
    private boolean findingMatch = false; //variable to indicate on going search
    private boolean goingToMatching = false; // prevents MatchingActivity from running twice
    private CollectionReference onlineUsers; // for group items
    private CollectionReference matchedUsers;
    private TextView hiTxt;
    private OnlineUser user; // object to hold user info
    private EventListener<QuerySnapshot> onlineUsersEventListener;
    private ListenerRegistration onlineUsersListenerRegistration;

    // List items
    private String[] groupSizesFormatted;
    private String[] diningHallsFormatted;
    MatchPreferences matchPreferences;

    Button buttonMatch;
    Button buttonMatchOld;

    String transitionID = "0"; //this will hold the document ID for 2 matches TODO: What is a transitionId exactly?
    ProgressBar progressBar;
    Button emailButt = null;

    // Lifecycle Methods -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Session.setActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        buttonMatch = findViewById(R.id.buttonMatch); //assigning variable to button
        progressBar = findViewById(R.id.progressBarForLobbyActivity);
        progressBar.setVisibility(View.GONE);
        collections = Collections.getInstance();
        documents = Documents.getInstance();

        onlineUsers = collections.getOnlineUsersCRef();
        matchedUsers = collections.getMatchedCRef();

        SignedInCallback signedInCallback = new SignedInCallback();
        signedInCallback.onSuccess();

        onlineUsersEventListener = getNewOnlineEventListener();
        groupSizesFormatted = getResources().getStringArray(R.array.groupSizesFormatted);
        diningHallsFormatted = getResources().getStringArray(R.array.diningHallsFormatted);

        matchPreferences = Entity.matchPreferences;

        user = Entity.onlineUser;

        emailButt = findViewById(R.id.emailButton);
        emailButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });


    }

    public void sendEmail()
    {
        /* Create the Intent */
        try{
            openGmail(this, "support@dontdinealone.com","Bug/Abuse Report", "Dear Dont Done Alone Team,\n");
        }
        catch(Exception e)
        {
            Toast.makeText(this, "Error Loading Email Application," +
                    "please send your problem to support@dontdinealone.com", Toast.LENGTH_SHORT).show();
        }
    }

    public void openGmail(Activity activity, String email, String subject, String content) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(emailIntent);
    }

    ///////////TYLERS EDITS/////////
    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.GONE);
        user = Entity.onlineUser;
        if (onlineUsersListenerRegistration == null) {
            onlineUsersListenerRegistration = beginOnlineUsersListener();
        }
        OnlineService.goOnline(NullCallback.getInstance());
    }

    @Override
    protected void onResume(){
        super.onResume();
        progressBar.setVisibility(View.GONE);
        user = Entity.onlineUser;

        if (onlineUsersListenerRegistration == null) {
            onlineUsersListenerRegistration = beginOnlineUsersListener();
        }
        OnlineService.goOnline(NullCallback.getInstance());
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(Entity.authUser.isSignedIn()) {
            if (findingMatch == false) {
                OnlineService.goOffline();
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (onlineUsersListenerRegistration != null) {
            onlineUsersListenerRegistration.remove();
        }
        if (Entity.authUser.isSignedIn() && findingMatch == true) {
            OnlineService.goOffline();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!Entity.authUser.isSignedIn()) {
            return;
        }
    }

    // Presenter Methods -------------------------------------------------------------------------
    //This is triggered during startup
    EventListener<QuerySnapshot> getNewOnlineEventListener() {
        return new EventListener<QuerySnapshot>() {
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
                    data += "Name: " + otherUser.getName()  +
                            "\nStatus Code: " + otherUser.getStatus() +
                             "\n\n";
                }
                TextView onlineCount = findViewById(R.id.onlineCount);
                onlineCount.setText(data);
            }
        };

    }

    ListenerRegistration beginOnlineUsersListener() {
        return onlineUsers.addSnapshotListener(onlineUsersEventListener);
    }

    public void startMatching(View v) {
        if(user == null) {
            return; //
        }
        //TODO: begin matching logic
        if (findingMatch) {

            // Send Cancels
            Writer w = new Writer((short)0x03);
            w.write8((byte)1);
            Session.getCon().send(w);
            findingMatch = false;
            UserStatusService.updateToOnline();
            buttonMatch.setText("Start Matching");
            buttonMatch.setBackgroundColor(Color.parseColor("#FF9900"));
            return;
        }
        queueUser();
    }

    public void queueUser() {
        if(!Session.isConnected())
        {
            Session.getCon().start();
        }
        findingMatch = true;
        UserStatusService.updateToQueued();
        buttonMatch.setBackgroundColor(Color.parseColor("#FF4081"));
        buttonMatch.setText("Stop Matching");
        if(Session.isConnected())
            doMatch();
        else
            Session.setConnected(true);
    }

    public void doMatch()
    {
        Writer w = new Writer((short)0x02);
        w.writeStr(user.getDocumentId());
        w.writeStr(user.getName());
        for(boolean b : matchPreferences.getGroupSizePreferences())
            w.write16((short)(b == true ? 1 : 0));
        for(boolean b : matchPreferences.getDiningHallPreferences())
            w.write16((short)(b == true ? 1 : 0));
        Session.getCon().send(w);
    }


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
                    data += "Name: " + otherUser.getName() +"\n" +
                            "\nStatus Code: " + otherUser.getStatus() + "\n\n";
                }
                TextView onlineCount = findViewById(R.id.onlineCount);
                onlineCount.setText(data);
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
        // We need to copy the persistent alert's boolean array
        final boolean[] groupSizePreferences = PrimitiveArrayService
                .makeBooleanArrayFromList(matchPreferences.getGroupSizePreferences());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_group_sizes_label);
        builder.setMultiChoiceItems(groupSizesFormatted, groupSizePreferences,
                new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                groupSizePreferences[i] = isChecked;
            }
        });
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                matchPreferences.setGroupSizePreferencesFromArray(groupSizePreferences);
                if (matchPreferences.hasChosenAGroupSizePreference()) {
                    // Since we made a selection, we are ok to continue selecting next preference.
                    checkBoxDiningHallPreferences();
                } else {
                    Toast.makeText(LobbyActivity.this, "Please make a selection", Toast.LENGTH_SHORT).show();
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
        final boolean[] diningHallPreferences = PrimitiveArrayService
                .makeBooleanArrayFromList(matchPreferences.getDiningHallPreferences());
        final AlertDialog diningHallSelection;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_dining_halls_label);
        builder.setMultiChoiceItems(diningHallsFormatted, diningHallPreferences,
                new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                diningHallPreferences[i] = isChecked;
            }
        });
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                matchPreferences.setDiningHallPreferencesFromArray(diningHallPreferences);
                if (matchPreferences.hasChosenADiningHallPreference()) {
                    // Store the preferences.
                    saveMatchPreferences();
                } else {
                    Toast.makeText(LobbyActivity.this, "Please make a selection", Toast.LENGTH_SHORT).show();
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
        documents.getUserMatchPreferencesDocRef().set(Entity.matchPreferences)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        //progressDialog.dismiss();

                        Toast.makeText(LobbyActivity.this,
                                "Match preferences saved successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        //progressDialog.dismiss();

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
        if(Session.getCon() != null) {
            try {
                Session.getCon().close();
                Session.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Entity.authUser.signOut();
        Toast.makeText(this, "Logged Out.", Toast.LENGTH_SHORT).show();
        //Next time the app opens go to MainActivity
        Intent x = new Intent(this, MainActivity.class);
        finish();
        startActivity(x);
        //Terminate the current activity
    }

    public void goToMatchingActivity() {
        Intent myIntent = new Intent(this, MatchedActivity.class);
        finish();
        myIntent.putExtra("key", transitionID); //Optional parameters
        myIntent.putExtra("name", user.getName()); //Optional parameters
        this.startActivity(myIntent);

    }

    // Callbacks ---------------------------------------------------------------------------------

    final class StoreCallback implements Callback {

        @Override
        public void onSuccess() {
            progressBar.setVisibility(View.GONE);
            //progressDialog.dismiss();
            Toast.makeText(LobbyActivity.this, "Preferences saved. Beginning matching..", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Exception e) {
            progressBar.setVisibility(View.GONE);
            //progressDialog.dismiss();

            Log.w("XXX", "Save error: ", e);
            Toast.makeText(LobbyActivity.this, "Preference save failed", Toast.LENGTH_SHORT).show();
            Toast.makeText(LobbyActivity.this, "Preference Save Error: " + e, Toast.LENGTH_LONG).show();
        }
    }

    final class SignedInCallback implements Callback {

        @Override
        public void onSuccess() {
            progressBar.setVisibility(View.GONE);
            //progressDialog.dismiss();
            ////////////Tyler's Edits

            hiTxt = findViewById(R.id.textViewTitle);
            String g = "Welcome " + Entity.user.getDisplayName();
            hiTxt.setText(g);
            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.US);
            String date = s.format(new Date());
            if(Entity.authUser == null)
                return;
            loadOnlineUsers();
            //Entity.con.setActivity(activity);
            //Entity.con.start();
            ///////////////////// End of Tylers Edit
        }

        @Override
        public void onFailure(Exception e) {
            progressBar.setVisibility(View.GONE);
            //progressDialog.dismiss();
        }

        public MatchedActivity createMatchedActivity(){
            return new MatchedActivity();
        }
    }
}
