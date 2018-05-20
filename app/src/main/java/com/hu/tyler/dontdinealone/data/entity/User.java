package com.hu.tyler.dontdinealone.data.entity;

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

    public String getDisplayNameKey() {
        return "displayName";
    }

    public String getGenderKey() {
        return "gender";
    }

    public String getAnimalKey() {
        return "animal";
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGender() {
        return gender;
    }

    public String getAnimal() {
        return animal;
    }


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }
}
