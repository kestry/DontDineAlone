package com.hu.tyler.dontdinealone;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Dine extends AppCompatActivity {

    private ArrayList<String> diningHallChoices;
    private ArrayList<String> groupSizeChoices;
    // These are global so selections remained checked
    private boolean[] checkedItemsHalls;
    private boolean[] checkedItemsGroups;

    //    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dine);
//        progressDialog = new ProgressDialog(this)*/
        diningHallChoices = new ArrayList<>();
        groupSizeChoices  = new ArrayList<>();
        checkedItemsHalls = new boolean[6];
        checkedItemsGroups = new boolean[6];
    }

    public void goToEditProfile(View v)
    {
//        progressDialog.setMessage("Loading Profile...");
//        progressDialog.show();
        Intent x = new Intent(this, EditProfile.class);
        startActivity(x);
    }
    public void logout(View v) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Logged Off.", Toast.LENGTH_SHORT).show();
        //Next time the app opens go to MainActivity
        Intent x = new Intent(this, MainActivity.class);
        startActivity(x);
        //Terminate the current activity
        finish();
    }

    public void startMatching(View v){
        // checks if user has entered preferences yet since listeners are asynchronous
        if(diningHallChoices.size() == 0 || groupSizeChoices.size() == 0){
            Toast.makeText(this, "Select Preferences before matching.", Toast.LENGTH_SHORT).show();
        }
        //begin matching logic
    }

    public void setPreferences(View v){
        // These are asynchronous listeners which means they won't wait for selections to be made to
        // return control, so we need to check that diningHallChoices and groupSizeChoices contain
        // values when we use them.
        selectDiningHalls();
        selectGroupSizes();
    }

    // Selection menu for dining hall choices. Change the database names or formatted names as you see fit to match your database
    // Selections are saved in diningHallChoices instance variable
    public void selectDiningHalls(){
        diningHallChoices = new ArrayList<>();
        String[] diningHallsFormatted = {"Nine & Ten", "Cowell/Stevenson", "Crown/Merill", "Porter/Kresge", "Rachel Carson/Oaks"};
        final String[] diningHallsDatabaseNames = {"Nine&Ten", "CowellStevenson", "CrownMerill", "PorterKresge", "RachelCarsonOaks"};

        final AlertDialog diningHallSelection;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Dining Halls");
        builder.setMultiChoiceItems(diningHallsFormatted, checkedItemsHalls, new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                checkedItemsHalls[i] = isChecked;
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(int j = 0; j < checkedItemsHalls.length; j++){
                    if(checkedItemsHalls[j]){
                        diningHallChoices.add(diningHallsDatabaseNames[j]);
                    }
                }
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        diningHallSelection = builder.create();
        diningHallSelection.show();
    }

    // Selection menu for group size choices
    // Selections are saved in groupSizeChoices instance variable
    public void selectGroupSizes(){
        groupSizeChoices = new ArrayList<>();
        final String[] groupSizes = {"2", "3", "4", "5", "6"};
        final AlertDialog groupSizeSelection;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Preferred Group Sizes");
        builder.setMultiChoiceItems(groupSizes, checkedItemsGroups, new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                checkedItemsGroups[i] = isChecked;
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(int j = 0; j < checkedItemsGroups.length; j++){
                    if(checkedItemsGroups[j]){
                        groupSizeChoices.add(groupSizes[j]);
                    }
                }
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        groupSizeSelection = builder.create();
        groupSizeSelection.show();
    }
}
