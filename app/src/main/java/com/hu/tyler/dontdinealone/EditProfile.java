package com.hu.tyler.dontdinealone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    // Keys for Cloud Firestore
    private static final String KEY_DISPLAY_NAME = "display_name";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_ANIMAL = "animal";
    private static final String KEY_ID = "id";

    //Firebase auth object
    private FirebaseAuth firebaseAuth;

    //Reference to Cloud Firestore Database
    private FirebaseFirestore db;

    EditText editTextDisplayName;
    EditText editTextGender;
    EditText editTextAnimal;
    int x = 14; //total number of avatars
    ImageView avaBtn[] = new ImageView[x]; //this is for the avatars.
    ImageView currentAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //Check if user is not logged in
        if (firebaseAuth.getCurrentUser() == null) {
            //Closing this activity
            finish();
            //Starting Main activity
            startActivity(new Intent(this, MainActivity.class));
        }

        // Initialize Cloud Firestore database
        db = FirebaseFirestore.getInstance();

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
                    Toast.makeText(EditProfile.this, "#" + j, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    // Saves user info.
    public void saveUserInfo(View v) {
        editTextDisplayName = findViewById(R.id.editTextDisplayName);
        editTextGender = findViewById(R.id.editTextGender);
        editTextAnimal = findViewById(R.id.editTextAnimal);

        String name = editTextDisplayName.getText().toString().trim();
        String gender = editTextGender.getText().toString().trim();
        String animal = editTextAnimal.getText().toString().trim();

        // Keys are fields in our database, Values are the user's info.
        Map<String, Object> profile = new HashMap<>();
        profile.put(KEY_DISPLAY_NAME, name);
        profile.put(KEY_GENDER, gender);
        profile.put(KEY_ANIMAL, animal);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        //Alternative: db.collection("Profiles/user.getUid()).set(profile)...
        //However the way below is more intuitive, because we can chain nest collections and documents.
        db.collection("Profiles").document(user.getUid()).set(profile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfile.this, "Profile saved...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "Profile error: " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Goes back to the dine page.
    public void backToDine(View v)
    {
        Intent x = new Intent(this, Dine.class);
        startActivity(x);
        //Terminate the current activity
        finish();
    }
}
