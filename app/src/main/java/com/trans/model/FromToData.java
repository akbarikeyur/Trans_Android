package com.trans.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class FromToData{

	@SerializedName("code")
	private int code;

	@SerializedName("data")
	private List<FromToItem> data=new ArrayList<>();

	@SerializedName("size")
	private int size;

	@SerializedName("limit")
	private int limit;

	@SerializedName("hasMore")
	private boolean hasMore;

	@SerializedName("format")
	private String format;

	@SerializedName("page")
	private int page;

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

	public void setData(List<FromToItem> data){
		this.data = data;
	}

	public List<FromToItem> getData(){
		return data;
	}

	public void setSize(int size){
		this.size = size;
	}

	public int getSize(){
		return size;
	}

	public void setLimit(int limit){
		this.limit = limit;
	}

	public int getLimit(){
		return limit;
	}

	public void setHasMore(boolean hasMore){
		this.hasMore = hasMore;
	}

	public boolean isHasMore(){
		return hasMore;
	}

	public void setFormat(String format){
		this.format = format;
	}

	public String getFormat(){
		return format;
	}

	public void setPage(int page){
		this.page = page;
	}

	public int getPage(){
		return page;
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
			"FromToData{" + 
			"code = '" + code + '\'' + 
			",data = '" + data + '\'' + 
			",size = '" + size + '\'' + 
			",limit = '" + limit + '\'' + 
			",hasMore = '" + hasMore + '\'' + 
			",format = '" + format + '\'' + 
			",page = '" + page + '\'' + 
			",message = '" + message + '\'' + 
			",timestamp = '" + timestamp + '\'' + 
			"}";
		}
}