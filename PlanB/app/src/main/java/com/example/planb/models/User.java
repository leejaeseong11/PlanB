package com.example.planb.models;

import android.graphics.drawable.Drawable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START rtdb_user_class]
@IgnoreExtraProperties
public class User {
    public String email;
    public String phone;
    public Character gender;     // M, F
    public String dob;      // YYYY-MM-DD
    public String introduce;
    public String picture;
    private String pk;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String phone, Character gender, String dob, String introduce, String picture) {
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.introduce = introduce;
        this.picture = picture;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> results = new HashMap<>();
        results.put("email", email);
        results.put("phone", phone);
        results.put("gender", gender.toString());
        results.put("date_of_birth", dob);
        results.put("introduce", introduce);
        results.put("picture", picture);
        return results;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                ", dob='" + dob + '\'' +
                ", introduce='" + introduce + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
// [END rtdb_user_class]