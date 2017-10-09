package me.josephboyle.feedme.tools;

import com.google.gson.annotations.SerializedName;

public class Location {

	@SerializedName("lat")
	public Double lat;

	@SerializedName("lng")
	public Double lng;
	
	public String toString(){
		return "[" + lat + ", "  + lng + "]";
	}
	
}
