package com.hu.tyler.dontdinealone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.util.Callback;

public class RegisterActivity extends AppCompatActivity {

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

        editTextEmail = findViewById(R.id.xxxxReg);
        editTextPw = findViewById(R.id.xxxxPW);
        editTextPwc = findViewById(R.id.xxxxPWC);

        progressDialog = new ProgressDialog(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // Presenter Methods ---------------------------------------------

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
        Entity.authUser.register(email, password, new RegisterCallback());
    }

    // Navigation Methods --------------------------------------------

    public void goToMainActivity(View v) {
        Toast.makeText(this, "Back to Login.", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    // Callbacks -----------------------------------------------------

    final class RegisterCallback implements Callback {

        @Override
        public void onSuccess() {
            progressDialog.dismiss();

            Toast.makeText(RegisterActivity.this,
                    "Registered Successfully: Check your Email for Confirmation", Toast.LENGTH_LONG).show();
            Intent x = new Intent(RegisterActivity.this, MainActivity.class);

            startActivity(x);
        }

        @Override
        public void onFailure(Exception e) {
            progressDialog.dismiss();

            Log.d("XXX", "mail " + email);
            Log.d("XXX", "pw " + password);

            Log.w("XXX", "Registration error: ", e);
            Toast.makeText(RegisterActivity.this, "Registration Error: " + e, Toast.LENGTH_LONG).show();
        }
    }
}
