package com.trans.model;

import com.trans.global.Utility;

import java.io.Serializable;

public class BusData implements Serializable {
    String cmpnyName, cmpnyImage, busDesc, avilableSeats, busFare, from, fromTime, to, toTime, duration;

    public BusData(String cmpnyName, String cmpnyImage, String busDesc, String avilableSeats, String busFare, String from, String fromTime, String to, String toTime, String duration) {
        this.cmpnyName = cmpnyName;
        this.cmpnyImage = cmpnyImage;
        this.busDesc = busDesc;
        this.avilableSeats = avilableSeats;
        this.busFare = busFare;
        this.from = from;
        this.fromTime = fromTime;
        this.to = to;
        this.toTime = toTime;
        this.duration = duration;
    }

    public String getCmpnyName() {
        return cmpnyName;
    }

    public void setCmpnyName(String cmpnyName) {
        this.cmpnyName = cmpnyName;
    }

    public String getCmpnyImage() {
        return cmpnyImage;
    }

    public void setCmpnyImage(String cmpnyImage) {
        this.cmpnyImage = cmpnyImage;
    }

    public String getBusDesc() {
        return busDesc;
    }

    public void setBusDesc(String busDesc) {
        this.busDesc = busDesc;
    }

    public String getAvilableSeats() {
        return avilableSeats;
    }

    public void setAvilableSeats(String avilableSeats) {
        this.avilableSeats = avilableSeats;
    }

    public String getBusFare() {
        return busFare;
    }

    public void setBusFare(String busFare) {
        this.busFare = busFare;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


}
