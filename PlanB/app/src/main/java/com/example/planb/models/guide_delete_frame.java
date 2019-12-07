package com.example.planb.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class guide_delete_frame {
    private String area;
    private String date;
    private String price;
    private String desc;

    public guide_delete_frame() {
    }

    public guide_delete_frame(String area, String date, String price, String desc) {
        this.area = area;
        this.date = date;
        this.desc = desc;
        this.price = price;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("area", area);
        result.put("date", date);
        result.put("desc", desc);
        result.put("price", price);
        return result;
    }
    public String getArea() { return area; }

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
}