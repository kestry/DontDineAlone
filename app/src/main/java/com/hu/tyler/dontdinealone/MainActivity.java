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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 69;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    EditText mail;
    EditText pw;

    //Base on what button is pressed, it goes here
    public void login(View v) {
        switch (v.getId())
        {
            case R.id.login:
                trylogin();
                break;

            case R.id.register:
                Intent x = new Intent(MainActivity.this, Register.class);
                startActivity(x);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user.getEmail();
            Toast.makeText(this, "Previously Log In: " + email, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Dine.class));
        }
    }

    public void trylogin()
    {
        String email = mail.getText().toString().trim();
        String password = pw.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Logging You In...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified())
                    {
                        Toast.makeText(MainActivity.this, "Email is verified" , Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(), Dine.class));
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"Email is not verified", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Log.w("XXX", "signInWithEmail:failure", task.getException());
                Toast.makeText(MainActivity.this, "signInWithEmail:failure" + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mail = findViewById(R.id.etEmail);
        pw = findViewById(R.id.etPW);

    }

}
