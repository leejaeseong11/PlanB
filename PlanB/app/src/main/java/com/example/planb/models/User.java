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

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String phone, Character gender, String dob, String introduce) {
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.introduce = introduce;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> results = new HashMap<>();
        results.put("email", email);
        results.put("phone", phone);
        results.put("gender", gender.toString());
        results.put("date_of_birth", dob);
        results.put("introduce", introduce);

        return results;
    }
}
// [END rtdb_user_class]