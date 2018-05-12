package com.hu.tyler.dontdinealone;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hu.tyler.dontdinealone.models.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LobbyActivity extends AppCompatActivity {

    UserModel user;

    // Reference to Cloud Firestore Database
    private FirebaseFirestore db;

    // Reference to Firestore Document
    private DocumentReference userPreferenceRef;

    // List items
    private String[] groupSizes;
    private String[] diningHallsFormatted;
    private String[] diningHallsDatabaseNames;

    // Checked items (these are global so that selects remain checked)
    private boolean[] checkedGroupSizes;
    private boolean[] checkedDiningHalls;

    private boolean hasChosenGroupSizes;
    private boolean hasChosenDiningHalls;

    // User items
    private Map<String, Boolean> userPreferences = new HashMap<String, Boolean>();

    Button buttonPreferences;
    Button buttonMatch;

    //    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
//        progressDialog = new ProgressDialog(this)*/

        user = UserModel.getInstance();

        //Check if user is not logged in
        if (!user.isSignedIn()) {
            //Closing this activity
            finish();
            //Starting Main activity
            startActivity(new Intent(this, MainActivity.class));
        }

        // Initialize Cloud Firestore database
        db = FirebaseFirestore.getInstance();
        userPreferenceRef = db.collection(user.getUid()).document("Preference");

        // List items are retrieved from "app/res/values/strings.xml"
        groupSizes = getResources().getStringArray(R.array.groupSizes);
        diningHallsFormatted = getResources().getStringArray(R.array.diningHallsFormatted);
        diningHallsDatabaseNames = getResources().getStringArray(R.array.diningHallsDatabaseNames);

        checkedGroupSizes = new boolean[groupSizes.length];
        checkedDiningHalls = new boolean[diningHallsFormatted.length];

        hasChosenGroupSizes = false;
        hasChosenDiningHalls = false;

        /* TODO: This will be useful if we move this to userRegistration, to preload db fields and default values.
        // Initializing user preference maps.
        for (int i = 0; i < groupSizes.length; i++) {
            userPreferences.put(groupSizes[i], false);
        }
        for (int i = 0; i < diningHallsDatabaseNames.length; i++) {
            userPreferences.put(diningHallsDatabaseNames[i], false);
        }
        */
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        user = null;
    }
    // Presenter Methods ---------------------------------------------

    public void setDinePreferences(View v) {
        // TODO: Will cancelable = false help?
        // These are asynchronous listeners which means they won't wait for selections to be made to
        // return control, so we need to check that diningHallChoices and groupSizeChoices contain
        // values when we use them.
        selectGroupSizes();
    }

    // Selection menu for group size choices
    public void selectGroupSizes(){
        final AlertDialog groupSizeSelection;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_group_sizes_label);
        builder.setMultiChoiceItems(groupSizes, checkedGroupSizes, new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                checkedGroupSizes[i] = isChecked;
            }
        });
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hasChosenGroupSizes = false;
                for (int j = 0; j < checkedGroupSizes.length; j++) {
                    userPreferences.put(groupSizes[j], checkedGroupSizes[j]);
                    hasChosenGroupSizes |= checkedGroupSizes[j];
                }
                if (hasChosenGroupSizes == false) {
                    Toast.makeText(LobbyActivity.this, "Please make a selection", Toast.LENGTH_SHORT).show();
                    selectGroupSizes();
                } else {
                    // This is here so that we only progress if we are OK with our groupSize selection
                    selectDiningHalls();
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
    public void selectDiningHalls() {

        final AlertDialog diningHallSelection;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_dining_halls_label);
        builder.setMultiChoiceItems(diningHallsFormatted, checkedDiningHalls, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                checkedDiningHalls[i] = isChecked;
            }
        });
        // Do we want preferences local to session or persistent? Chose persistent for now.
        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                hasChosenDiningHalls = false;
                for (int j = 0; j < checkedDiningHalls.length; j++) {
                    userPreferences.put(diningHallsDatabaseNames[j], checkedDiningHalls[j]);
                    hasChosenDiningHalls |= checkedDiningHalls[j];
                }
                if (hasChosenDiningHalls == false) {
                    Toast.makeText(LobbyActivity.this, "Please make a selection", Toast.LENGTH_SHORT).show();
                    selectDiningHalls();
                } else {
                    uploadPreferences();
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

    public void uploadPreferences() {
        userPreferenceRef.set(userPreferences)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(LobbyActivity.this, "Preferences saved...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LobbyActivity.this, "Save error: " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void startMatching(View v){
        // checks if user has entered preferences yet since listeners are asynchronous
        if (!(hasChosenGroupSizes && hasChosenDiningHalls)) {
            setDinePreferences(v);
        }
        Toast.makeText(this, "Matching.", Toast.LENGTH_SHORT).show();
        //TODO: begin matching logic
    }

    // Navigation Methods --------------------------------------------

    public void goToEditProfileActivity(View v)
    {
//        progressDialog.setMessage("Loading Profile...");
//        progressDialog.show();
        Intent x = new Intent(this, EditProfileActivity.class);
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

    // Runnables -----------------------------------------------------

}
