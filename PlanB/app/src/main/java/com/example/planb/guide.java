package com.example.planb;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class guide{
    public String email;
    public String area;
    public String date;
    public String desc;
    public String price;

    public guide(){}

    public guide(String email, String area, String date, String desc, String price) {
        this.area = area;
        this.date = date;
        this.desc = desc;
        this.price = price;
        this.email = email;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("area", area);
        result.put("date", date);
        result.put("desc", desc);
        result.put("price", price);
        result.put("email", email);
        return result;
    }

}
