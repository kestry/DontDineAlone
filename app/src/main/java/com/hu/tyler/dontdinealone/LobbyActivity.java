package com.hu.tyler.dontdinealone;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hu.tyler.dontdinealone.models.DatabaseKeys;
import com.hu.tyler.dontdinealone.models.DatabaseModel;
import com.hu.tyler.dontdinealone.models.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LobbyActivity extends AppCompatActivity {

    private UserModel user;
    private DatabaseModel db;

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

        user = UserModel.getInstance();

        //Check if user is not logged in
        if (!user.isSignedIn()) {
            //Close this activity
            finish();
            //Start Main activity
            startActivity(new Intent(this, MainActivity.class));
        }

        // Initialize Cloud Firestore database
        db = DatabaseModel.getInstance();

        progressDialog = new ProgressDialog(this);

        // List items are retrieved from "app/res/values/strings.xml"
        diningHallsFormatted = getResources().getStringArray(R.array.diningHallsFormatted);

        isPreferredGroupSizes = new boolean[DatabaseKeys.Preference.groupSizes.length];
        isPreferredDiningHalls = new boolean[DatabaseKeys.Preference.diningHalls.length];

        hasChosenGroupSizes = false;
        hasChosenDiningHalls = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        user = null;
        db = null;
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
        builder.setMultiChoiceItems(DatabaseKeys.Preference.groupSizes, isPreferredGroupSizes, new DialogInterface.OnMultiChoiceClickListener(){
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
                    db.setGroupSizePreference(j, isPreferredGroupSizes[j]);
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
                    db.setDiningHallPreference(j, isPreferredDiningHalls[j]);
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
        db.storePreference(new StoreSuccessRunnable(), new StoreFailureRunnable());
    }

    public void startMatching(View v){
        // checks if user has entered preferences yet since listeners are asynchronous
        if (!(hasChosenGroupSizes && hasChosenDiningHalls)) {
            setMatchPreferences(v);
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

    final class StoreSuccessRunnable implements Runnable {

        @Override
        public void run() {
            progressDialog.dismiss();

            Toast.makeText(LobbyActivity.this, "Preferences saved successfully", Toast.LENGTH_SHORT).show();
        }
    }

    final class StoreFailureRunnable implements Runnable {

        @Override
        public void run() {
            progressDialog.dismiss();

            Log.w("XXX", "Save error: ", db.getException());
            Toast.makeText(LobbyActivity.this, "Preference save failed", Toast.LENGTH_SHORT).show();
            Toast.makeText(LobbyActivity.this, "Preference Save Error: " + db.getException(), Toast.LENGTH_LONG).show();
        }
    }
}
