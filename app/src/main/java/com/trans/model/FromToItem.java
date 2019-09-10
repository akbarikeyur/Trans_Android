package com.trans.model;

import com.google.gson.annotations.SerializedName;


 public class FromToItem {

    @SerializedName("city")
    private String city;

    @SerializedName("_id")
    private String id;

    @SerializedName("state")
    private String state;

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return
                "FromToItem{" +
                        "city = '" + city + '\'' +
                        ",_id = '" + id + '\'' +
                        ",state = '" + state + '\'' +
                        "}";
    }
}