package me.josephboyle.feedme.eatstreet;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class EatStreetSection {

	@SerializedName("apiKey")
	public String apiKey;
	
	@SerializedName("items")
	public List<EatStreetItem> items;
	
	@SerializedName("name")
	public String name;
	
	public String toString(){
		String s = "";
		
		s += "== " + name + " ==\r\n";
		for(EatStreetItem i : items){
			s += i + " \r\n";
		}
		
		return s;
	}
	
}
