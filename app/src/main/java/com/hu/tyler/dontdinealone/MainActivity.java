package com.hu.tyler.dontdinealone;
/*This is basically the login activity, when the app first boots up this is where it goes
* */

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    EditText editTextEmail;
    EditText editTextPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove titleBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPW = findViewById(R.id.editTextPW);
    }

    //Check if the user is already logged in.
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user.getEmail();
            Toast.makeText(this, "Previously Log In: " + email, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Dine.class));
        }
    }

    //This method goes to the Register page.
    public void goToRegister(View v) {
        Intent x = new Intent(MainActivity.this, Register.class);
        finish();
        startActivity(x);
    }

    //This method logs the user in
    public void trylogin(View v) {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPW.getText().toString();

        //Ensure that the email entered is proper length
        if (editTextEmail.length() < 8) {
            Toast.makeText(this, "Email length is too short", Toast.LENGTH_SHORT).show();
            return;
        }

        //TODO: Uncomment this for final release
        //Take the last 8 chars of the string to ensure it's ucsc.edu:
//        String domainCheck = email.substring(email.length() - 8, email.length());
//        if (!domainCheck.equalsIgnoreCase("ucsc.edu")) {
//            Toast.makeText(this, "Registration is restricted to UCSC Domain", Toast.LENGTH_SHORT).show();
//            return;
//        }
//END OF UNCOMMENT
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Logging You In...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    //TODO: Delete the 2 lines below later.
                        finish();
                        startActivity(new Intent(getApplicationContext(), Dine.class));


                        //TODO: Uncomment for future releases
//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                    if (user.isEmailVerified()) {
//                        Toast.makeText(MainActivity.this, "Email is verified", Toast.LENGTH_SHORT).show();
//                        finish();
//                        startActivity(new Intent(getApplicationContext(), Dine.class));
//                    } else {
//                        Toast.makeText(MainActivity.this, "Email is not verified", Toast.LENGTH_SHORT).show();
//                    }
                    //END OF UNCOMMENT

                } else {
                    Log.w("XXX", "signInWithEmail:failure ", task.getException());
                    Log.w("XXX", "Failed Email: " + editTextEmail.getText().toString().trim());
                    Toast.makeText(MainActivity.this, "Error: " + task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
