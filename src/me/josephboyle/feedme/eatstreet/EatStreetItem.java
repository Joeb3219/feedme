package me.josephboyle.feedme.eatstreet;

import com.google.gson.annotations.SerializedName;

public class EatStreetItem {

	@SerializedName("apiKey")
	public String apiKey;
	
	@SerializedName("name")
	public String name;
	
	@SerializedName("description")
	public String description;
	
	@SerializedName("basePrice")
	public double basePrice;
	
}
