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
    Button registerBtn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    public void registering()
    {
        mail = findViewById(R.id.xxxreg);
        pw = findViewById(R.id.xxxxPW);

        //Ensure that the email entered is proper length
        if(mail.length() < 8)
        {
            Toast.makeText(this,"Email length is too short", Toast.LENGTH_SHORT).show();
            return;
        }
        //Take the last 8 chars of the string to ensure it's ucsc.edu:
        String email = mail.getText().toString().trim();
        email = email.substring(email.length()-8,email.length());
        if(!email.equalsIgnoreCase("ucsc.edu"))
        {
            Toast.makeText(this,"Registration is restricted to UCSC Domain", Toast.LENGTH_SHORT).show();
            return;
        }


        if(TextUtils.isEmpty(pw.getText().toString().trim())){
            Toast.makeText(this, "Please Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        //Show the progress bar
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        //Try and Register:
        firebaseAuth.createUserWithEmailAndPassword(mail.getText().toString().trim(), pw.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                    Intent x = new Intent(Register.this, Dine.class);
                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                    x.putExtra("email", mail.getText().toString().trim());
                    x.putExtra("PW", pw.getText().toString().trim());
                    startActivity(x);
                }
                else{
                    Log.d("XXX", "mail " + mail.getText().toString().trim() );
                    Log.d("XXX", "pw " + pw.getText().toString().trim() );
                    Log.w("XXX", "signInWithEmail:failure", task.getException());
                    Toast.makeText(Register.this, "Error :" + task.getException(), Toast.LENGTH_LONG).show();
                    Log.d("XXX", "firebaseAuth.getCurrentUser() =  "+ firebaseAuth.getCurrentUser() );
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
