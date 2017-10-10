package me.josephboyle.feedme.eatstreet;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class EatStreetMenu {

	@SerializedName("sections")
	public List<EatStreetSection> sections;
	
}
