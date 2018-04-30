package com.hu.tyler.dontdinealone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Dine extends AppCompatActivity {

    Button logoffBtn, editProfile;

//    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dine);
//        progressDialog = new ProgressDialog(this);
        logoffBtn = findViewById(R.id.button2);
        logoffBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                logoff();
            }
        });

        editProfile = findViewById(R.id.dineProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPersonalProfile();
            }
        });
    }

    private void editPersonalProfile()
    {
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
        Intent x = new Intent(this, EditProfile.class);
        startActivity(x);
    }
    public void logoff() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Logged Off.", Toast.LENGTH_SHORT).show();
        //Next time the app opens go to MainActivity
        Intent x = new Intent(this, MainActivity.class);
        startActivity(x);
        //Terminate the current activity
        finish();
    }
}
