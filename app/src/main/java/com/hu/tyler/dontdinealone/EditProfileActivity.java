package com.hu.tyler.dontdinealone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.model.Documents;
import com.hu.tyler.dontdinealone.data.entity.User;
import com.hu.tyler.dontdinealone.domain.ViewService;
import com.hu.tyler.dontdinealone.util.Callback;

public class EditProfileActivity extends AppCompatActivity {

    Documents documents;

    User user = Entity.user;

    EditText editTextDisplayName;
    EditText editTextGender;
    EditText editTextAnimal;

    ProgressBar progressBar;

    // Lifecycle Methods -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editTextDisplayName = findViewById(R.id.editTextDisplayNameInEditProfile);
        editTextGender = findViewById(R.id.editTextGender);
        editTextAnimal = findViewById(R.id.editTextAnimal);

        documents = Documents.getInstance();

        progressBar = findViewById(R.id.progressBarInEditProfileActivity);

        SignedInCallback signedInCallback = new SignedInCallback();
        signedInCallback.onSuccess();
    }

    // Presenter Methods -------------------------------------------------------------------------

    /**
     * Unhighlights the current user avatar.
     */
    void unhighlight() {
        View view = ViewService.getView(user.getAvatarViewName(), this);
        view.setBackgroundColor(00000000);
    }

    /**
     * Sets and highlights the clicked avatar and unhighlights the previous avatar.
     */
    void highlight(View v) {
        unhighlight();
        v.setBackgroundColor(Color.parseColor("#FF4081"));
        String avatarViewName = ViewService.getViewName(v);
        user.setAvatarViewName(avatarViewName);
        Toast.makeText(EditProfileActivity.this,
                avatarViewName, Toast.LENGTH_SHORT).show();
    }

    public void saveProfile(final View v) {
        Entity.user.setDisplayName(editTextDisplayName.getText().toString().trim());
        Entity.user.setGender(editTextGender.getText().toString().trim());
        Entity.user.setAnimal(editTextAnimal.getText().toString().trim());

        //progressDialog.setMessage("Saving Profile...");
        //progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        documents.getUserDocRef().set(Entity.user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(EditProfileActivity.this,
                                "Profile saved successfully", Toast.LENGTH_SHORT).show();
                        goToLobbyActivity();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);

                        Log.w("XXX", "Save error: ", e);
                        Toast.makeText(EditProfileActivity.this,
                                "Failed to save profile", Toast.LENGTH_SHORT).show();
                        Toast.makeText(EditProfileActivity.this,
                                "Error: " + e, Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void loadProfile() {
        // Sets the editText field with our db profile info.
        editTextDisplayName.setText(Entity.user.getDisplayName());
        editTextGender.setText(Entity.user.getGender());
        editTextAnimal.setText(Entity.user.getAnimal());
    }

    // Navigation Methods ------------------------------------------------------------------------

    public void goToLobbyActivity(View v)
    {
        goToLobbyActivity();
    }


    // Goes back to the lobby page.
    public void goToLobbyActivity()
    {
        finish();
        startActivity(new Intent(this, LobbyActivity.class));
    }

    // Callbacks ---------------------------------------------------------------------------------

    final class StoreCallback implements Callback {

        @Override
        public void onSuccess() {
            progressBar.setVisibility(View.GONE);

            Toast.makeText(EditProfileActivity.this,
                    "Profile saved successfully", Toast.LENGTH_SHORT).show();
            goToLobbyActivity();
        }

        @Override
        public void onFailure(Exception e) {
            progressBar.setVisibility(View.GONE);

            Log.w("XXX", "Save error: ", e);
            Toast.makeText(EditProfileActivity.this,
                    "Profile save failed", Toast.LENGTH_SHORT).show();
            Toast.makeText(EditProfileActivity.this,
                    "Profile save error: " + e, Toast.LENGTH_LONG).show();
        }
    }

    final class SignedInCallback implements Callback {

        @Override
        public void onSuccess() {
            progressBar.setVisibility(View.GONE);

            // Load user's saved profile.
            loadProfile();
            Toast.makeText(EditProfileActivity.this,
                    "Profile loaded successfully", Toast.LENGTH_SHORT).show();

            // Set user's avatar to default if none was previously set.
            if (user.getAvatarViewName() == null) {
                String defaultAvatarViewName = ViewService.getViewName(findViewById(R.id.ava1));
                user.setAvatarViewName(defaultAvatarViewName);
            }
            // Highlight user's avatar.
            View avatarView = ViewService.getView(user.getAvatarViewName(), EditProfileActivity.this);
            highlight(avatarView);
        }

        @Override
        public void onFailure(Exception e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(EditProfileActivity.this,
                    "Not Signed In", Toast.LENGTH_SHORT).show();


        }
    }
}
