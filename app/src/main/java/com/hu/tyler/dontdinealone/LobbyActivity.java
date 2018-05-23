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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hu.tyler.dontdinealone.data.Collections;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.entity.OnlineUser;
import com.hu.tyler.dontdinealone.data.Documents;
import com.hu.tyler.dontdinealone.domain.OnlineService;
import com.hu.tyler.dontdinealone.domain.QueueService;
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
    private int findingMatch = 0; //variable to indicate on going search
    private int goingToMatching = 0; // prevents MatchingActivity from running twice
    ///Tyler Edits: 5/15
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference onlineUsers; // for group items
    private CollectionReference matchedUsers;
    private DocumentReference tempUse;
    TextView hiTxt;
    OnlineUser u; // object to hold user info
    ///////////////////

    // List items
    private String[] diningHallsFormatted;

    private boolean[] groupSizePreferences;
    private boolean[] diningHallPreferences;

    Button buttonMatch;
    String transitionID = "0"; //this will hold the document ID for 2 matches
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

        groupSizePreferences = Entity.matchPreferences.getGroupSizePreferences();
        diningHallPreferences = Entity.matchPreferences.getDiningHallPreferences();

        progressDialog = new ProgressDialog(this);
        // List items are retrieved from "app/res/values/strings.xml"

        diningHallsFormatted = getResources().getStringArray(R.array.diningHallsFormatted);

        u = Entity.onlineUser;
    }

    ///////////TYLERS EDITS/////////
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("XXX", "transitionID @ onStart: " + transitionID);
        u = Entity.onlineUser;
        onlineUsers.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                Log.d("XXX", "transitionID @ onStart @ addSnapshotListener: " + transitionID);

                if(e!=null){
                    return;
                }

                String data = "Online Users:\n\n";
                List<DocumentSnapshot> x = queryDocumentSnapshots.getDocuments();
                for(int i = 0; i < x.size(); i++)
                {
                    OnlineUser j = x.get(i).toObject(OnlineUser.class);
                    Log.d("XXX", "for loop users"+i + ": " + j.getName());
                    j.setDocumentId(x.get(i).getId());
                    if(u != null && x.get(i).get("chatID") != null) // Check object validity before proceeding matching
                    if(x.get(i).getId().equals(u.getDocumentId()))
                    {
                        //User has been match
                        if(!x.get(i).get("chatID").toString().equals("0"))
                        {
                            transitionID = x.get(i).get("chatID").toString();
                            if(goingToMatching == 0)
                            {
                                goingToMatching = 1;// prevents 2x execution
                                goToMatchingActivity();

                            }
                        }
                    }
                    data += j.getName() +"\n" + j.getEmail() +
                            "\nStatus Code: " + j.getStatus() +
                            "\nDocID: " + j.getDocumentId() + "\n\n";
                }
                TextView onlineCount = findViewById(R.id.onlineCount);
                onlineCount.setText(data);
            }
        });

        //OnlineService.goOnline();

    }

    @Override
    protected void onPause(){
        super.onPause();
        if(findingMatch == 0)
        {if (u != null)
            onlineUsers.document(u.getDocumentId()).update("status", DatabaseStatuses.User.offline);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(u == null)
            return;
        QueueService.leaveQueue();
        onlineUsers.document(u.getDocumentId()).delete();
    }

    // Presenter Methods -------------------------------------------------------------------------

    public void startMatching(View v) {
        if(u == null) {
            return; //
        }
        //TODO: begin matching logic
        if (findingMatch != 0) {
            QueueService.leaveQueue();
            OnlineService.goBackOnline();
            findingMatch = 0;
            buttonMatch.setText("Start Matching");
            buttonMatch.setBackgroundColor(Color.parseColor("#FF9900"));
            return;
        }
        setMatchPreferences(v);
    }

    public void queueUser() {
        onlineUsers.document(u.getDocumentId()).update("status", DatabaseStatuses.User.queued);
        onlineUsers.document(u.getDocumentId()).update("queueTimestamp", FieldValue.serverTimestamp());
        QueueService.enterQueue(new StoreCallback());
        findingMatch = 1;
        buttonMatch.setBackgroundColor(Color.parseColor("#FF4081"));
        buttonMatch.setText("Stop Matching");
        lookingFortheHungry();
    }

    protected void lookingFortheHungry() {
        // Look for other people and we can distinguishe them b/c there status code is "waiting".
        onlineUsers.whereEqualTo("status", DatabaseStatuses.User.queued)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        List<DocumentSnapshot> x= documentSnapshots.getDocuments();
                        for(int i = 0; i < x.size(); i++)
                        {
//                            remove the element that has a identical email
                            String id = x.get(i).getId();
                            String uId = u.getDocumentId();

                            if(!id.equalsIgnoreCase(uId))
                            {
                                Toast.makeText(LobbyActivity.this, "Match Found!", Toast.LENGTH_SHORT).show();
                                //TODO: probably should check whether someone already changed your status before proceeding further.]
                                //PRECAUTION SLOWDOWNS
                                if(transitionID != "0" )
                                    return;
                                if(u.getStatus() == DatabaseStatuses.User.matched)
                                    return;
                                ////////////END OF EXTRA PRECAUTIONS


                                onlineUsers.document(u.getDocumentId()).update("status", DatabaseStatuses.User.matched); //update my status
                                db.collection("Online").document(x.get(i).getId()).update("status", DatabaseStatuses.User.matched);

                                //Get the user we are matched with
                                tempUse = db.collection("Online").document(x.get(i).getId());
                                Log.d("XXX","x.get(i).getId().equals(u.getDocumentId():" + x.get(i).getId().equals(u.getDocumentId()));
                                Log.d("XXX", "id: "+ x.get(i).getId()+ " Matched Email: " + x.get(i).getString("email"));

                                SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.US);
                                String date = s.format(new Date());
                                final Chat mchat = new Chat(u.getName(),"Ready",2,date);
                                 matchedUsers.add(mchat).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                     @Override
                                     public void onSuccess(DocumentReference documentReference) {
                                         mchat.setDocumentId(documentReference.getId());
                                         transitionID = mchat.getDocumentId();
                                         Log.d("XXX", "TransitionID @ lookingFortheHungry" + transitionID);
                                         tempUse.update("chatID",transitionID); //update the chatID of the matched person
                                         u.setNewDoc(transitionID);
                                         onlineUsers.document(u.getDocumentId()).update("chatID", transitionID);
                                         findingMatch = 0; //no longer finding matches
                                         goingToMatching = 1; //transitioning to a new activity
                                         goToMatchingActivity();
                                     }
                                 });
                            }
                        }
                    }
                });
    }

    public void goToMatchingActivity() {
        Intent myIntent = new Intent(this, MatchingActivty.class);
        finish();
        myIntent.putExtra("key", transitionID); //Optional parameters
        myIntent.putExtra("name", u.getName()); //Optional parameters

        this.startActivity(myIntent);

    }
    /////////////////////////////End of Tylers Edit


    public void loadOnlineUsers() {
        onlineUsers.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                String data = "Online Users:\n\n";

                Toast.makeText(LobbyActivity.this, "Inside loadOnlineUsers().Sucess", Toast.LENGTH_SHORT).show();
                List<DocumentSnapshot> x= documentSnapshots.getDocuments();
                Toast.makeText(LobbyActivity.this, "# of people online" + x.size(), Toast.LENGTH_SHORT).show();
                for(int i = 0; i < x.size(); i++)
                {
                    Log.d("XXX", "for loop "+i);
                    OnlineUser j = x.get(i).toObject(OnlineUser.class);
                    j.setDocumentId(x.get(i).getId());
                    data += j.getName() +"\n" + j.getEmail() +
                            "\nStatus Code: " + j.getStatus() +
                            "\nDocID: " + j.getDocumentId() + "\n\n";
                }
                TextView onlineCount = findViewById(R.id.onlineCount);
                onlineCount.setText(data);
//                for(QueryDocumentSnapshot documentSnapshot: x){
//                    OnlineUser x = documentSnapshot.toObject(OnlineUser.class);
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
                Entity.matchPreferences.setGroupSizePreferenceAt(i, isChecked);
            }
        });
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Entity.matchPreferences.hasChosenAGroupSizePreference()) {
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
            public void onClick(DialogInterface dialogInterface, int i) {
            }
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
                Entity.matchPreferences.setDiningHallPreferenceAt(i, isChecked);
            }
        });
        // Do we want preferences local to session or persistent? Chose persistent for now.
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (Entity.matchPreferences.hasChosenADiningHallPreference()) {
                    // We queue now that we finished selecting preferences.
                    queueUser();
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
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        diningHallSelection = builder.create();
        diningHallSelection.show();
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
