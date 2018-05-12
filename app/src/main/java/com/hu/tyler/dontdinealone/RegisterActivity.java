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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hu.tyler.dontdinealone.models.UserModel;

public class RegisterActivity extends AppCompatActivity {

    UserModel user;

    //Reference to Cloud Firestore Database
    private FirebaseFirestore db;

    //Reference to Firestore Document
    private DocumentReference userProfileRef;

    EditText editTextEmail;
    EditText editTextPw;
    EditText editTextPwc;
    private ProgressDialog progressDialog;

    String email;
    String password;
    String passwordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        user = UserModel.getInstance();

        editTextEmail = findViewById(R.id.xxxxReg);
        editTextPw = findViewById(R.id.xxxxPW);
        editTextPwc = findViewById(R.id.xxxxPWC);

        progressDialog = new ProgressDialog(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        user = null;
    }

    public void register(View v) {

        email = editTextEmail.getText().toString().trim();
        password = editTextPw.getText().toString().trim();
        passwordConfirm = editTextPwc.getText().toString().trim();
        String domain;

        //Ensure that the email entered is proper length
        if (email.length() < 8) {
            Toast.makeText(this, "Email length is too short", Toast.LENGTH_SHORT).show();
            return;
        }

        //TODO:Uncomment for Later Releases
        //Take the last 8 chars of the string to ensure it's ucsc.edu:
//        domain = email.substring(email.length() - 8, email.length());
//        if (!domain.equalsIgnoreCase("ucsc.edu")) {
//            Toast.makeText(this, "Registration is restricted to UCSC Domain", Toast.LENGTH_SHORT).show();
//            return;
//        }
        //END OF UNCOMMENT

        //Verify if a password was entered
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        //Verify that passwords entered match
        if (!password.equals(passwordConfirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        //Show the progress bar
        progressDialog.setMessage("Registering Account...");
        progressDialog.show();

        //Try and Register:
        user.register(email, password, new RegisterSuccessRunnable(), new RegisterFailureRunnable());
    }


    public void goToMainActivity(View v) {
        Toast.makeText(this, "Back to Login.", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    class RegisterSuccessRunnable implements Runnable {

        @Override
        public void run() {
            progressDialog.dismiss();

            Toast.makeText(RegisterActivity.this,
                    "Registered Successfully: Check your Email for Confirmation", Toast.LENGTH_LONG).show();
            Intent x = new Intent(RegisterActivity.this, MainActivity.class);

            startActivity(x);
        }
    }

    class RegisterFailureRunnable implements Runnable {

        @Override
        public void run() {
            progressDialog.dismiss();

            Log.d("XXX", "mail " + email);
            Log.d("XXX", "pw " + password);

            Log.w("XXX", "Registration error: ", user.getException());
            Toast.makeText(RegisterActivity.this, "Registration Error: " + user.getException(), Toast.LENGTH_LONG).show();
        }
    }
}
