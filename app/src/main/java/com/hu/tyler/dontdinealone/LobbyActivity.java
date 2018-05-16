package com.hu.tyler.dontdinealone;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hu.tyler.dontdinealone.data.UserMatchInfoRepo;
import com.hu.tyler.dontdinealone.data.UserProfileRepo;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;
import com.hu.tyler.dontdinealone.domain.User;
import com.hu.tyler.dontdinealone.util.Callback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LobbyActivity extends AppCompatActivity {

    private User user;
    private UserMatchInfoRepo repo;
    private UserProfileRepo profileRepo; // DELETE

    ///Tyler Edits: 5/15
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference onlineUsers = db.collection("Online"); // for group items
    TextView hiTxt;
    OnlineUser u;
    ///////////////////

    // List items
    private String[] diningHallsFormatted;

    // Checked items (these are global so that selects remain checked)
    private boolean[] isPreferredGroupSizes;
    private boolean[] isPreferredDiningHalls;

    private boolean hasChosenGroupSizes;
    private boolean hasChosenDiningHalls;

    Button buttonPreferences;
    Button buttonMatch;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);


        user = User.getInstance();
        repo = UserMatchInfoRepo.getInstance();
        profileRepo = UserProfileRepo.getInstance();  // DELETE
        removeDups();
        //Check if user is not logged in
        if (!user.isSignedIn(new SignedInCallback())) {
            //Close this activity
            finish();
            //Start Main activity
            startActivity(new Intent(this, MainActivity.class));
        }


//        Toast.makeText(LobbyActivity.this, "Test1: Hello " + profileRepo.get(DatabaseKeys.Profile.DISPLAY_NAME), Toast.LENGTH_SHORT).show();  // DELETE

        progressDialog = new ProgressDialog(this);
         // Delete duplicates from crashes
        // List items are retrieved from "app/res/values/strings.xml"

        diningHallsFormatted = getResources().getStringArray(R.array.diningHallsFormatted);
        isPreferredGroupSizes = new boolean[DatabaseKeys.Preference.GROUP_SIZES.length];
        isPreferredDiningHalls = new boolean[DatabaseKeys.Preference.DINING_HALLS.length];

        hasChosenGroupSizes = false;
        hasChosenDiningHalls = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }




    ///////////TYLERS EDITS/////////
    @Override
    protected void onStart() {
        super.onStart();

        onlineUsers.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                if(e!=null){
                    return;
                }

                String data = "Online Users:\n\n";
                Toast.makeText(LobbyActivity.this, "Inside loadOnlineUsers().Sucess", Toast.LENGTH_SHORT).show();
                List<DocumentSnapshot> x= queryDocumentSnapshots.getDocuments();
                Toast.makeText(LobbyActivity.this, "Online x size" + x.size(), Toast.LENGTH_SHORT).show();
                for(int i = 0; i < x.size(); i++)
                {
                    OnlineUser j = x.get(i).toObject(OnlineUser.class);
                    Log.d("XXX", "for loop users"+i + ": " + j.getname());
                    j.setDocumentId(x.get(i).getId());
                    data += j.getname() +"\n" + user.getEmail() +
                            "\nStatus Code: " + j.getstatus() +
                            "\nDocID: " + j.getDocumentId() + "\n\n";
                }
                TextView onlineCount = findViewById(R.id.onlineCount);
                onlineCount.setText(data);
            }
        });
    }


    /////////////////////////////
    @Override
    public void onDestroy() {
        super.onDestroy();
        user = null;
        repo = null;
        DocumentReference ref =  onlineUsers.document(u.getDocumentId());
        ref.delete();
    }


    //Removes duplicate accounts log in with the same email...
    private void removeDups(){
         onlineUsers.whereEqualTo("email", user.getEmail()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        List<DocumentSnapshot> x= documentSnapshots.getDocuments();

                        for(int i = 0; i < x.size(); i++)
                        {
                            //remove the element that has a identical email
//                            if(x.get(i).getId() != u.getDocumentId() )
//                            {
                                Log.d("XXX", "Deleted Duplicate: " + x.get(i).getString("email") +
                                "id: " + x.get(i).getId());
                                db.collection("Online").document(x.get(i).getId()).delete();
//                            }
                        }
                    }
                });
    }

    public void loadOnlineUsers(){
        onlineUsers.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                String data = "Online Users:\n\n";

                Toast.makeText(LobbyActivity.this, "Inside loadOnlineUsers().Sucess", Toast.LENGTH_SHORT).show();
                List<DocumentSnapshot> x= documentSnapshots.getDocuments();
                Toast.makeText(LobbyActivity.this, "Online x size" + x.size(), Toast.LENGTH_SHORT).show();
                for(int i = 0; i < x.size(); i++)
                {
                    Log.d("XXX", "for loop "+i);
                    OnlineUser j = x.get(i).toObject(OnlineUser.class);
                    j.setDocumentId(x.get(i).getId());
                    data += j.getname() +"\n" + j.getEmail() +
                            "\nStatus Code: " + j.getstatus() +
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

    // Presenter Methods ---------------------------------------------

    public void setMatchPreferences(View v) {
        // TODO: Will cancelable = false help?
        // These are asynchronous listeners which means they won't wait for selections to be made to
        // return control, so we need to check that diningHallChoices and groupSizeChoices contain
        // values when we use them.
        checkBoxGroupSizePreferences();
    }

    // Selection menu for group size choices
    public void checkBoxGroupSizePreferences(){
        final AlertDialog groupSizeSelection;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_group_sizes_label);
        builder.setMultiChoiceItems(DatabaseKeys.Preference.GROUP_SIZES, isPreferredGroupSizes, new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                isPreferredGroupSizes[i] = isChecked;
            }
        });
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hasChosenGroupSizes = false;
                for (int j = 0; j < isPreferredGroupSizes.length; j++) {
                    repo.set(DatabaseKeys.Preference.GROUP_SIZES[j], isPreferredGroupSizes[j]);
                    hasChosenGroupSizes |= isPreferredGroupSizes[j];
                }
                // We only progress if the user chose at least one group
                if (hasChosenGroupSizes == true) {
                    // We choose dining halls next.
                    checkBoxDiningHallPreferences();
                } else {
                    Toast.makeText(LobbyActivity.this, "Please make a selection", Toast.LENGTH_SHORT).show();
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

    // Selection menu for dining hall choices. Change the database names or formatted names as you see fit to match your database
    public void checkBoxDiningHallPreferences(){

        final AlertDialog diningHallSelection;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_dining_halls_label);
        builder.setMultiChoiceItems(diningHallsFormatted, isPreferredDiningHalls, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                isPreferredDiningHalls[i] = isChecked;
            }
        });
        // Do we want preferences local to session or persistent? Chose persistent for now.
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                hasChosenDiningHalls = false;
                for (int j = 0; j < isPreferredDiningHalls.length; j++) {
                    repo.set(DatabaseKeys.Preference.DINING_HALLS[j], isPreferredDiningHalls[j]);
                    hasChosenDiningHalls |= isPreferredDiningHalls[j];
                }
                // We only store preferences if we've also chosen at least one dining hall
                if (hasChosenDiningHalls == true) {
                    storeMatchPreferences();
                } else {
                    Toast.makeText(LobbyActivity.this, "Please make a selection", Toast.LENGTH_SHORT).show();
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

    public void storeMatchPreferences() {
        repo.store(new StoreCallback());
    }

    public void startMatching(View v){


        // checks if user has entered preferences yet since listeners are asynchronous
        //if (!(hasChosenGroupSizes && hasChosenDiningHalls)) {
//            setMatchPreferences(v);
        //}
        //TODO: begin matching logic

    }

    // Navigation Methods --------------------------------------------

    public void goToEditProfileActivity(View v)
    {
//        progressDialog.setMessage("Loading Profile...");
//        progressDialog.show();
        Intent x = new Intent(this, EditProfileActivity.class);
        finish();
        startActivity(x);
    }

    public void goToMainActivity(View v) {
        user.signOut();
        Toast.makeText(this, "Logged Out.", Toast.LENGTH_SHORT).show();
        //Next time the app opens go to MainActivity
        Intent x = new Intent(this, MainActivity.class);
        finish();
        startActivity(x);
        //Terminate the current activity
    }

    // Callbacks -----------------------------------------------------

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
            Toast.makeText(LobbyActivity.this, "Test2: Hello " + profileRepo.get(DatabaseKeys.Profile.DISPLAY_NAME), Toast.LENGTH_SHORT).show();  // DELETE


            hiTxt = findViewById(R.id.textViewTitle);
            hiTxt.setText("Welcome " + profileRepo.get(DatabaseKeys.Profile.DISPLAY_NAME));
            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
            String date = s.format(new Date());

            //Lets get online users
            u = new OnlineUser(user.getEmail(),(String) profileRepo.get(DatabaseKeys.Profile.DISPLAY_NAME),
                    (String) profileRepo.get( DatabaseKeys.Profile.ANIMAL),1 , date);
            onlineUsers.add(u).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    u.setDocumentId(documentReference.getId());
                    Log.d("XXX", "DocID for this instance: " + u.getDocumentId());
                    loadOnlineUsers();
                }
            });

            ///////////////////// End of Tylers Edit
        }

        @Override
        public void onFailure(Exception e) {
        }
    }
}
