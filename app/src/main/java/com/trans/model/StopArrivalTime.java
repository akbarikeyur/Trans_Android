package com.trans.model;


import com.google.gson.annotations.SerializedName;


public class StopArrivalTime{

	@SerializedName("hour")
	private int hour;

	@SerializedName("minute")
	private int minute;

	public void setHour(int hour){
		this.hour = hour;
	}

	public int getHour(){
		return hour;
	}

	public void setMinute(int minute){
		this.minute = minute;
	}

	public int getMinute(){
		return minute;
	}

	@Override
 	public String toString(){
		return 
			"StopArrivalTime{" + 
			"hour = '" + hour + '\'' + 
			",minute = '" + minute + '\'' + 
			"}";
		}
}