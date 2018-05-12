package com.hu.tyler.dontdinealone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hu.tyler.dontdinealone.models.DatabaseModel;
import com.hu.tyler.dontdinealone.models.UserModel;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private UserModel user;
    private DatabaseModel dbModel;

    EditText editTextDisplayName;
    EditText editTextGender;
    EditText editTextAnimal;
    int x = 14; //total number of avatars
    ImageView avaBtn[] = new ImageView[x]; //this is for the avatars.
    ImageView currentAvatar;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        user = UserModel.getInstance();
        dbModel = DatabaseModel.getInstance();

        progressDialog = new ProgressDialog(this);

        //Check if user is not logged in
        if (!user.isSignedIn()) {
            //Closing this activity
            finish();
            //Starting Main activity
            startActivity(new Intent(this, MainActivity.class));
        }

        editTextDisplayName = findViewById(R.id.editTextDisplayName);
        editTextGender = findViewById(R.id.editTextGender);
        editTextAnimal = findViewById(R.id.editTextAnimal);

        loadProfile();

        //Didn't figure out how to dynamically set buttons, so the below is temporary
        avaBtn[0] = findViewById(R.id.ava1);
        avaBtn[1] = findViewById(R.id.ava2);
        avaBtn[2] = findViewById(R.id.ava3);
        avaBtn[3] = findViewById(R.id.ava4);
        avaBtn[4] = findViewById(R.id.ava5);
        avaBtn[5] = findViewById(R.id.ava6);
        avaBtn[6] = findViewById(R.id.ava7);
        avaBtn[7] = findViewById(R.id.ava8);
        avaBtn[8] = findViewById(R.id.ava9);
        avaBtn[9] = findViewById(R.id.ava10);
        avaBtn[10] = findViewById(R.id.ava11);
        avaBtn[11] = findViewById(R.id.ava12);
        avaBtn[12] = findViewById(R.id.ava13);
        avaBtn[13] = findViewById(R.id.ava14);

        currentAvatar = avaBtn[0];

        for(int i = 0; i < x; i++)
        {
            final int j = i;  // can't pass in a dynamic int to the class below, so had to do this.
            avaBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentAvatar.setBackgroundColor(00000000);
                    avaBtn[j].setBackgroundColor(Color.parseColor("#FF4081"));
                    currentAvatar = avaBtn[j];
                    Toast.makeText(EditProfileActivity.this, "#" + j, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        user = null;
        dbModel = null;
    }

    // Presenter Methods ---------------------------------------------

    public void saveProfile(final View v) {
        dbModel.setDisplayName(editTextDisplayName.getText().toString().trim());
        dbModel.setGender(editTextGender.getText().toString().trim());
        dbModel.setAnimal(editTextAnimal.getText().toString().trim());

        progressDialog.setMessage("Saving Profile...");
        progressDialog.show();
        dbModel.storeProfile(new StoreSuccessRunnable(), new StoreFailureRunnable());
    }

    public void loadProfile() {
        // Sets the editText field with our db profile info.
        editTextDisplayName.setText(dbModel.getDisplayName());
        editTextGender.setText(dbModel.getGender());
        editTextAnimal.setText(dbModel.getAnimal());
    }

    public void goToLobbyActivity(View v)
    {
        goToLobbyActivity();
    }

    // Navigation Methods --------------------------------------------

    // Goes back to the lobby page.
    public void goToLobbyActivity()
    {
        finish();
        startActivity(new Intent(this, LobbyActivity.class));
    }

    // Runnables -----------------------------------------------------

    final class StoreSuccessRunnable implements Runnable {

        @Override
        public void run() {
            progressDialog.dismiss();

            Toast.makeText(EditProfileActivity.this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
            goToLobbyActivity();
        }
    }

    final class StoreFailureRunnable implements Runnable {

        @Override
        public void run() {
            progressDialog.dismiss();

            Log.w("XXX", "Save error: ", dbModel.getException());
            Toast.makeText(EditProfileActivity.this, "Profile save failed", Toast.LENGTH_SHORT).show();
            Toast.makeText(EditProfileActivity.this, "Profile Save Error: " + dbModel.getException(), Toast.LENGTH_LONG).show();
        }
    }
}
