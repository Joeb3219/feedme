package me.josephboyle.feedme.eatstreet;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class EatStreetSearch {

	@SerializedName("address")
	public EatStreetAddress address;
	
	@SerializedName("restaurants")
	public List<EatStreetRestaurant> restaurants;
	
}
