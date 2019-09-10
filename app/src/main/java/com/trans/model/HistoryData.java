package com.trans.model;

import java.io.Serializable;

public class HistoryData implements Serializable {
    String tktNumber, fare, from, to, numOfSeat, departDate, departTime, returnDate, returnTime;
    boolean twoWay;

    public HistoryData(String tktNumber, String fare, String from, String to, String numOfSeat, String departDate, String departTime) {
        this.tktNumber = tktNumber;
        this.fare = fare;
        this.from = from;
        this.to = to;
        this.numOfSeat = numOfSeat;
        this.departDate = departDate;
        this.departTime = departTime;
    }

     public HistoryData(String tktNumber, String fare, String from, String to, String numOfSeat, String departDate, String departTime,boolean twoWay, String returnDate, String returnTime) {
        this.tktNumber = tktNumber;
        this.fare = fare;
        this.from = from;
        this.to = to;
        this.numOfSeat = numOfSeat;
        this.departDate = departDate;
        this.departTime = departTime;
        this.twoWay = twoWay;
        this.returnDate = returnDate;
        this.returnTime = returnTime;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public boolean isTwoWay() {
        return twoWay;
    }

    public void setTwoWay(boolean twoWay) {
        this.twoWay = twoWay;
    }

    public String getTktNumber() {
        return tktNumber;
    }

    public void setTktNumber(String tktNumber) {
        this.tktNumber = tktNumber;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getNumOfSeat() {
        return numOfSeat;
    }

    public void setNumOfSeat(String numOfSeat) {
        this.numOfSeat = numOfSeat;
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }
}
