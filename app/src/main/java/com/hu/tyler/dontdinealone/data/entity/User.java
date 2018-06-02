package com.hu.tyler.dontdinealone.data.entity;

import android.view.View;

import com.google.firebase.firestore.DocumentReference;
import com.hu.tyler.dontdinealone.R;
import com.hu.tyler.dontdinealone.data.model.Collections;
import com.hu.tyler.dontdinealone.data.model.Documents;
import com.hu.tyler.dontdinealone.data.model.MatchPreferences;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;

public class User {
    String displayName;
    String gender;
    String animal;
    String avatarViewName;


    public User() {
        setToDefault();
    }

    public void setToDefault() {
        displayName = "Guest"; // TODO: Set to auth name
        gender = "";
        animal = "";
        avatarViewName = null;
    }

    public void set(User user) {
        displayName = user.displayName;
        gender = user.gender;
        animal = user.animal;
        avatarViewName = user.avatarViewName;
    }

    public String displayNameKey() {
        return "displayName";
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String genderKey() {
        return "gender";
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String animalKey() {
        return "animal";
    }
    public String getAnimal() {
        return animal;
    }
    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String avatarViewNameKey() {
        return "avatarViewName";
    }
    public String getAvatarViewName() {
        return avatarViewName;
    }
    public void setAvatarViewName(String avatar) {
        this.avatarViewName = avatar;
    }

}
