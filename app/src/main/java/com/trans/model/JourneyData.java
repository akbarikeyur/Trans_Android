package com.trans.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class JourneyData{

	@SerializedName("code")
	private int code;

	@SerializedName("data")
	private List<JourneyItem> data;

	@SerializedName("format")
	private String format;

	@SerializedName("message")
	private String message;

	@SerializedName("timestamp")
	private String timestamp;

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setJournyItems(List<JourneyItem> data){
		this.data = data;
	}

	public List<JourneyItem> getJournyItems(){
		return data;
	}

	public void setFormat(String format){
		this.format = format;
	}

	public String getFormat(){
		return format;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setTimestamp(String timestamp){
		this.timestamp = timestamp;
	}

	public String getTimestamp(){
		return timestamp;
	}

	@Override
 	public String toString(){
		return 
			"JourneyData{" + 
			"code = '" + code + '\'' + 
			",data = '" + data + '\'' + 
			",format = '" + format + '\'' + 
			",message = '" + message + '\'' + 
			",timestamp = '" + timestamp + '\'' + 
			"}";
		}
}