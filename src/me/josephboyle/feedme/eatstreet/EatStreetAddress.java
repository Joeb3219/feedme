package me.josephboyle.feedme.eatstreet;

import com.google.gson.annotations.SerializedName;

public class EatStreetAddress {

	@SerializedName("apiKey")
	public String apiKey;
	
	@SerializedName("streetAddress")
	public String streetAddress;
	
	@SerializedName("latitude")
	public String latitude;
	
	@SerializedName("longitude")
	public String longitude;
	
	@SerializedName("city")
	public String city;
	
	@SerializedName("state")
	public String state;
	
	@SerializedName("zip")
	public String zip;
	
	@SerializedName("aptNumber")
	public String aptNumber;
	
}
