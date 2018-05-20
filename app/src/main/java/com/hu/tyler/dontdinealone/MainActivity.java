package com.hu.tyler.dontdinealone;
/*This is basically the login activity, when the app first boots up this is where it goes
* */

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.util.Callback;
import com.hu.tyler.dontdinealone.util.NullCallback;

public class MainActivity extends AppCompatActivity {

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

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPW = findViewById(R.id.editTextPW);
    }

    @Override
    public void onStart() {
        super.onStart();
        //If the user is already logged in, go directly to lobby.
        if (Entity.authUser.isSignedIn(NullCallback.getInstance())) {
            Toast.makeText(this, "Previously Logged In: " + Entity.authUser.getEmail(), Toast.LENGTH_SHORT).show();
            goToLobbyActivity();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // Presenter Methods ---------------------------------------------

    public void login(View v) {
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
// END OF UNCOMMENT

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logging You In...");
        progressDialog.show();

        Entity.authUser.signIn(email, password, new LoginCallback());
//        Test statment
        Toast.makeText(this, "After user.signIn", Toast.LENGTH_SHORT).show();

    }

    // Navigation Methods --------------------------------------------

    public void goToRegisterActivity(View v) {
        finish();
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }

    void goToLobbyActivity() {
        finish();
        startActivity(new Intent(getApplicationContext(), LobbyActivity.class));
    }

    // Callbacks -----------------------------------------------------

    final class LoginCallback implements Callback {

        @Override
        public void onSuccess() {
            progressDialog.dismiss();

            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

            //TODO: Uncomment for future releases
//          if (user.isEmailVerified()) {
//              Toast.makeText(MainActivity.this, "Email is verified", Toast.LENGTH_SHORT).show();
                goToLobbyActivity();
//          } else {
//              Toast.makeText(MainActivity.this, "Email is not verified", Toast.LENGTH_SHORT).show();
//          }
//END OF UNCOMMENT

        }

        @Override
        public void onFailure(Exception e) {
            progressDialog.dismiss();

            Log.w("XXX", "signInWithEmail:failure ", e);
            Log.w("XXX", "Failed Email: " + editTextEmail.getText().toString().trim());
            Toast.makeText(MainActivity.this, "Login Error: " + e, Toast.LENGTH_LONG).show();
        }
    }
}
