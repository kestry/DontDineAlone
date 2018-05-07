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

//    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dine);
//        progressDialog = new ProgressDialog(this)*/
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
}
