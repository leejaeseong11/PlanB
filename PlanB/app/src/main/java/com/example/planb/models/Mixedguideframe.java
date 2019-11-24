package com.example.planb.models;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Mixedguideframe {
    
    private String email;
    private String photo;
    private String phone;
    private String desc;
    private String price;

    public Mixedguideframe() {
    }

    public Mixedguideframe(String email, String photo, String phone, String desc, String price) {
        this.photo = photo;
        this.phone = phone;
        this.desc = desc;
        this.price = price;
        this.email = email;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("photo", photo);
        result.put("phone", phone);
        result.put("desc", desc);
        result.put("price", price);
        result.put("email", email);
        return result;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoto() { return photo; }

    public void setPhoto(String photo) { this.photo = photo; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }
}