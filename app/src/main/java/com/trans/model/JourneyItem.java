package com.trans.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class JourneyItem implements Serializable {

    @SerializedName("fare")
    private int fare;

    @SerializedName("vehicleName")
    private String vehicleName;

    @SerializedName("vehicleDetails")
    private String vehicleDetails;

    @SerializedName("totalStops")
    private int totalStops;

    @SerializedName("availableSeats")
    private int availableSeats;

    @SerializedName("available")
    private boolean available;

    @SerializedName("seats")
    private int seats;

    @SerializedName("toStopId")
    private String toStopId;

    @SerializedName("routeId")
    private String routeId;

    @SerializedName("specialNeedSeatAvailable")
    private boolean specialNeedSeatAvailable;

    @SerializedName("fromStopId")
    private String fromStopId;

    @SerializedName("days")
    private List<Integer> days;

    @SerializedName("_id")
    private String id;

    @SerializedName("vehicleId")
    private String vehicleId;

    @SerializedName("stops")
    private List<StopsItem> stops;

    @SerializedName("timeDifference")
    private String timeDifference;

    @SerializedName("fromStopCity")
    private String fromStopCity;

    @SerializedName("fromStopState")
    private String fromStopState;

    @SerializedName("toStopCity")
    private String toStopCity;

    @SerializedName("toStopState")
    private String toStopState;

    @SerializedName("toStopArrivalTime")
    private StopArrivalTime toStopArrivalTime;

    @SerializedName("toStopDepartureTime")
    private StopDepartureTime toStopDepartureTime;

    @SerializedName("fromStopArrivalTime")
    private StopArrivalTime fromStopArrivalTime;

    @SerializedName("fromStopDepartureTime")
    private StopDepartureTime fromStopDepartureTime;

    @SerializedName("priceDetails")
    private PriceDetail priceDetails;

    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    public int getFare() {
        return fare;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleDetails(String vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public String getVehicleDetails() {
        return vehicleDetails;
    }

    public void setTotalStops(int totalStops) {
        this.totalStops = totalStops;
    }

    public int getTotalStops() {
        return totalStops;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getSeats() {
        return seats;
    }

    public void setToStopId(String toStopId) {
        this.toStopId = toStopId;
    }

    public String getToStopId() {
        return toStopId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setSpecialNeedSeatAvailable(boolean specialNeedSeatAvailable) {
        this.specialNeedSeatAvailable = specialNeedSeatAvailable;
    }

    public boolean isSpecialNeedSeatAvailable() {
        return specialNeedSeatAvailable;
    }

    public void setFromStopId(String fromStopId) {
        this.fromStopId = fromStopId;
    }

    public String getFromStopId() {
        return fromStopId;
    }

    public void setDays(List<Integer> days) {
        this.days = days;
    }

    public List<Integer> getDays() {
        return days;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setStops(List<StopsItem> stops) {
        this.stops = stops;
    }

    public List<StopsItem> getStops() {
        return stops;
    }

    public void setTimeDifference(String timeDifference) {
        this.timeDifference = timeDifference;
    }

    public String getTimeDifference() {
        return timeDifference;
    }

    public PriceDetail getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(PriceDetail priceDetails) {
        this.priceDetails = priceDetails;
    }

    @Override
    public String toString() {
        return
                "JourneyItem{" +
                        "fare = '" + fare + '\'' +
                        ",vehicleName = '" + vehicleName + '\'' +
                        ",vehicleDetails = '" + vehicleDetails + '\'' +
                        ",totalStops = '" + totalStops + '\'' +
                        ",availableSeats = '" + availableSeats + '\'' +
                        ",available = '" + available + '\'' +
                        ",seats = '" + seats + '\'' +
                        ",toStopId = '" + toStopId + '\'' +
                        ",routeId = '" + routeId + '\'' +
                        ",specialNeedSeatAvailable = '" + specialNeedSeatAvailable + '\'' +
                        ",fromStopId = '" + fromStopId + '\'' +
                        ",days = '" + days + '\'' +
                        ",_id = '" + id + '\'' +
                        ",vehicleId = '" + vehicleId + '\'' +
                        ",stops = '" + stops + '\'' +
                        ",timeDifference = '" + timeDifference + '\'' +
                        "}";
    }

    public String getFromStopCity() {
        return fromStopCity;
    }

    public void setFromStopCity(String fromStopCity) {
        this.fromStopCity = fromStopCity;
    }

    public String getFromStopState() {
        return fromStopState;
    }

    public void setFromStopState(String fromStopState) {
        this.fromStopState = fromStopState;
    }

    public String getToStopCity() {
        return toStopCity;
    }

    public void setToStopCity(String toStopCity) {
        this.toStopCity = toStopCity;
    }

    public String getToStopState() {
        return toStopState;
    }

    public void setToStopState(String toStopState) {
        this.toStopState = toStopState;
    }

    public StopArrivalTime getToStopArrivalTime() {
        return toStopArrivalTime;
    }

    public void setToStopArrivalTime(StopArrivalTime toStopArrivalTime) {
        this.toStopArrivalTime = toStopArrivalTime;
    }

    public StopDepartureTime getToStopDepartureTime() {
        return toStopDepartureTime;
    }

    public void setToStopDepartureTime(StopDepartureTime toStopDepartureTime) {
        this.toStopDepartureTime = toStopDepartureTime;
    }

    public StopArrivalTime getFromStopArrivalTime() {
        return fromStopArrivalTime;
    }

    public void setFromStopArrivalTime(StopArrivalTime fromStopArrivalTime) {
        this.fromStopArrivalTime = fromStopArrivalTime;
    }

    public StopDepartureTime getFromStopDepartureTime() {
        return fromStopDepartureTime;
    }

    public void setFromStopDepartureTime(StopDepartureTime fromStopDepartureTime) {
        this.fromStopDepartureTime = fromStopDepartureTime;
    }
}