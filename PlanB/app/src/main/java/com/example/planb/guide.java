package com.example.planb;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class guide {
    private String email;
    private String photo;
    private String phone;
    private String area;
    private String date;
    private String desc;
    private String price;

    public guide() {
    }

    public guide(String email, String photo, String phone, String area, String date, String desc, String price) {
        this.area = area;
        this.photo = photo;
        this.phone = phone;
        this.date = date;
        this.desc = desc;
        this.price = price;
        this.email = email;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("area", area);
        result.put("photo", photo);
        result.put("phone", phone);
        result.put("date", date);
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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