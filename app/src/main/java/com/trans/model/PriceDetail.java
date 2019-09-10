package com.trans.model;

import com.google.gson.annotations.SerializedName;

public class PriceDetail{

	@SerializedName("serviceCharges")
	private String serviceCharges;

	@SerializedName("ticketPrice")
	private int ticketPrice;

	@SerializedName("totalPriceForAllSeats")
	private int totalPriceForAllSeats;

	@SerializedName("totalPriceForNormalSeats")
	private int totalPriceForNormalSeats;

	@SerializedName("totalCost")
	private String totalCost;

	@SerializedName("disabledTicketPrice")
	private int disabledTicketPrice;

	public void setServiceCharges(String serviceCharges){
		this.serviceCharges = serviceCharges;
	}

	public String getServiceCharges(){
		return serviceCharges;
	}

	public void setTicketPrice(int ticketPrice){
		this.ticketPrice = ticketPrice;
	}

	public int getTicketPrice(){
		return ticketPrice;
	}

	public void setTotalPriceForAllSeats(int totalPriceForAllSeats){
		this.totalPriceForAllSeats = totalPriceForAllSeats;
	}

	public int getTotalPriceForAllSeats(){
		return totalPriceForAllSeats;
	}

	public void setTotalPriceForNormalSeats(int totalPriceForNormalSeats){
		this.totalPriceForNormalSeats = totalPriceForNormalSeats;
	}

	public int getTotalPriceForNormalSeats(){
		return totalPriceForNormalSeats;
	}

	public void setTotalCost(String totalCost){
		this.totalCost = totalCost;
	}

	public String getTotalCost(){
		return totalCost;
	}

	public int getDisabledTicketPrice() {
		return disabledTicketPrice;
	}

	public void setDisabledTicketPrice(int disabledTicketPrice) {
		this.disabledTicketPrice = disabledTicketPrice;
	}

	@Override
 	public String toString(){
		return 
			"PriceDetail{" + 
			"serviceCharges = '" + serviceCharges + '\'' + 
			",ticketPrice = '" + ticketPrice + '\'' + 
			",totalPriceForAllSeats = '" + totalPriceForAllSeats + '\'' + 
			",totalPriceForNormalSeats = '" + totalPriceForNormalSeats + '\'' + 
			",totalCost = '" + totalCost + '\'' + 
			"}";
		}
}