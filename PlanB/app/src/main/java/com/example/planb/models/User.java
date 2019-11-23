package com.example.planb.models;

import android.graphics.drawable.Drawable;

import com.google.firebase.database.IgnoreExtraProperties;

// [START rtdb_user_class]
@IgnoreExtraProperties
public class User {
    public String email;
    public String phone;
    public char gender;     // M, W
    public String dob;      // YYYY-MM-DD
    public String introduce;
    public Drawable picture;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String phone, char gender, String dob, String introduce) {
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.introduce = introduce;
    }

}
// [END rtdb_user_class]