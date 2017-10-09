package me.josephboyle.feedme.tools;

import com.google.gson.annotations.SerializedName;

public class Geometry {

	@SerializedName("location")
	public Location location;
	
	public String toString(){
		String s = location.toString();
		return s;
	}
	
}
