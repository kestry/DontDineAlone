package com.hu.tyler.dontdinealone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText mail;
    EditText pw;
    EditText pwc;
    Button registerBtn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    public void registering() {
        mail = findViewById(R.id.xxxreg);
        pw = findViewById(R.id.xxxxPW);
        pwc = findViewById(R.id.xxxxPWC);
        // Noticed we were calling these a lot so I made them strings.
        // Declared final so the inner class can access them. - Cody
        final String email = mail.getText().toString().trim();
        final String password = pw.getText().toString().trim();
        String passwordConfirm = pwc.getText().toString().trim();
        String domain;

        /* Got this to work by changing text= to hint= in the XML for EditText - Cody

        //set email to empty text when clicked
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail.getText().clear();
            }
        });

        //set email field to empty text when clicked
        pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.getText().clear();
            }
        });
        */

        //Ensure that the email entered is proper length
        if (mail.length() < 8) {
            Toast.makeText(this, "Email length is too short", Toast.LENGTH_SHORT).show();
            return;
        }
        //Take the last 8 chars of the string to ensure it's ucsc.edu:
        domain = email.substring(email.length() - 8, email.length());
        if (!domain.equalsIgnoreCase("ucsc.edu")) {
            Toast.makeText(this, "Registration is restricted to UCSC Domain", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        //Verify that passwords entered match
        if(!password.equals(passwordConfirm)){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        //Show the progress bar
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        //Try and Register:
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "Registered Successfully: Check your Email for" +
                            " Confirmation", Toast.LENGTH_LONG).show();
                    Intent x = new Intent(Register.this, MainActivity.class);
                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                    //Log out immediately to prevent illegal sign in without email confirmation
                    FirebaseAuth.getInstance().signOut();
                    //Puts the email in a string, perhaps we can use to transfer to main email box
                    x.putExtra("email", email);
                    startActivity(x);
                } else {
                    Log.d("XXX", "mail " + email);
                    Log.d("XXX", "pw " + password);

                    Log.w("XXX", "signInWithEmail:failure", task.getException());
                    Toast.makeText(Register.this, "Error :" + task.getException(), Toast.LENGTH_LONG).show();
                    Log.d("XXX", "firebaseAuth.getCurrentUser() =  " + firebaseAuth.getCurrentUser());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        registerBtn = findViewById(R.id.registerAccount);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                registering();
            }
        });
    }
}
