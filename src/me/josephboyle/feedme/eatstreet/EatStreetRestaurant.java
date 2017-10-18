package me.josephboyle.feedme.eatstreet;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class EatStreetRestaurant {

	// computes |ln(e+(2e*(w'-0.5)))|*similarity, where w' is the weight scaled to 0 through 1.
	public double computeSimarityWeight(double similarity){
		int maxWeight = 21;
		int minWeight = -20;
		if(weight <= minWeight) weight = minWeight + 1;
		if(weight >= maxWeight) weight = maxWeight - 1;
		double scaledWeight = (double) (weight - minWeight) / (maxWeight - minWeight);
		double logWeight = Math.log(Math.E + (2 * (scaledWeight - 0.5) * Math.E));
		logWeight = (logWeight + 2.1) / (4.1);
		return logWeight * similarity;
	}
	
	public String getDescription(){
		String desc = "";
		desc += name + " (" + address + ")" + "\r\n";
		if(foodTypes.size() > 0) desc += "Serving the following types of food: ";
		for(String s : foodTypes) desc += s + "; ";
		return desc;
	}
	
	public String getKeywordTriggers(String keywords){
		String triggers = "";
		
		return triggers;
	}
	
	@SerializedName("weight")
	public int weight = 1;
	
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
	
	@SerializedName("menu")
	public EatStreetMenu menu;
	
//	@SerializedName("hours")
	//public EatStreetHours hours;
	
	public String toString(){
		String result = "";
		result += name + " \r\n ";
		result += address + " \r\n ";
		result += menu.toString() + " \r\n ";
		if(pickup) result += "pickup" + " \r\n ";
		if(delivery) result += "delivery" + " \r\n ";
		
		for(String type : foodTypes) result += type + " \r\n ";
		
		return result;
	}
	
}
