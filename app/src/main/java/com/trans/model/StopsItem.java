package com.trans.model;

import com.google.gson.annotations.SerializedName;

public class StopsItem{

	@SerializedName("stopSequence")
	private int stopSequence;

	@SerializedName("stopCity")
	private String stopCity;

	@SerializedName("stopId")
	private String stopId;

	@SerializedName("nameRef")
	private String nameRef;

	@SerializedName("stopArrivalTime")
	private StopArrivalTime stopArrivalTime;

	@SerializedName("stopDepartureTime")
	private StopDepartureTime stopDepartureTime;

	@SerializedName("stopState")
	private String stopState;

	public void setStopSequence(int stopSequence){
		this.stopSequence = stopSequence;
	}

	public int getStopSequence(){
		return stopSequence;
	}

	public void setStopCity(String stopCity){
		this.stopCity = stopCity;
	}

	public String getStopCity(){
		return stopCity;
	}

	public void setStopArrivalTime(StopArrivalTime stopArrivalTime){
		this.stopArrivalTime = stopArrivalTime;
	}

	public StopArrivalTime getStopArrivalTime(){
		return stopArrivalTime;
	}

	public void setStopId(String stopId){
		this.stopId = stopId;
	}

	public String getStopId(){
		return stopId;
	}

	public void setNameRef(String nameRef){
		this.nameRef = nameRef;
	}

	public String getNameRef(){
		return nameRef;
	}

	public void setStopDepartureTime(StopDepartureTime stopDepartureTime){
		this.stopDepartureTime = stopDepartureTime;
	}

	public StopDepartureTime getStopDepartureTime(){
		return stopDepartureTime;
	}

	public void setStopState(String stopState){
		this.stopState = stopState;
	}

	public String getStopState(){
		return stopState;
	}

	@Override
 	public String toString(){
		return 
			"StopsItem{" + 
			"stopSequence = '" + stopSequence + '\'' + 
			",stopCity = '" + stopCity + '\'' + 
			",stopArrivalTime = '" + stopArrivalTime + '\'' + 
			",stopId = '" + stopId + '\'' + 
			",nameRef = '" + nameRef + '\'' + 
			",stopDepartureTime = '" + stopDepartureTime + '\'' + 
			",stopState = '" + stopState + '\'' + 
			"}";
		}
}