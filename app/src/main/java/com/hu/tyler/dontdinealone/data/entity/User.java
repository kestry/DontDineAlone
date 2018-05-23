package com.hu.tyler.dontdinealone.data.entity;

import com.hu.tyler.dontdinealone.res.DatabaseKeys;

public class User {
    String displayName;
    String gender;
    String animal;

    public User() {
        setToDefault();
    }

    public void setToDefault() {
        displayName = "";
        gender = "";
        animal = "";
    }

    public void set(User user) {
        displayName = user.displayName;
        gender = user.gender;
        animal = user.animal;
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

}
