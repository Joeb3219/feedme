package me.josephboyle.feedme.eatstreet;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class EatStreetRestaurant {

	@SerializedName("apiKey")
	public String apiKey;
	
	@SerializedName("logoUrl")
	public String logoUrl;
	
	@SerializedName("name")
	public String name;
	
	@SerializedName("streetAddress")
	public String address;
	
	@SerializedName("city")
	public String city;
	
	@SerializedName("zip")
	public String zip;
	
	@SerializedName("deliveryMin")
	public double deliveryMin;
		
	@SerializedName("foodTypes")
	public List<String> foodTypes;

	@SerializedName("phone")
	public String phone;
	
	@SerializedName("latitude")
	public double latitude;	
	
	@SerializedName("longitude")
	public double longitude;	
	
	@SerializedName("minFreeDelivery")
	public int minFreeDelivery;	
	
	@SerializedName("taxRate")
	public double taxRate;	
	
	@SerializedName("acceptsCash")
	public boolean acceptsCash;
	
	@SerializedName("acceptsCard")
	public boolean acceptsCard;
	
	@SerializedName("offersPickup")
	public boolean pickup;
	
	@SerializedName("offersDelivery")
	public boolean delivery;
	
	@SerializedName("isTestRestaurant")
	public boolean testRestaurant;
	
	@SerializedName("minWaitTime")
	public int minWaitTime;
	
	@SerializedName("maxWaitTime")
	public int maxWaitTime;
	
	@SerializedName("open")
	public boolean open;
	
//	@SerializedName("hours")
	//public EatStreetHours hours;
	
	public String toString(){
		String result = "";
		result += name + "\r\n";
		result += address + " (" + latitude + "," + longitude + ")";
		return result;
	}
	
}
