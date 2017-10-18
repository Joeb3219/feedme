package me.josephboyle.feedme.eatstreet;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class EatStreetMenu {

	@SerializedName("sections")
	public List<EatStreetSection> sections;

	public String toPrettyString(){
		String s = "";
		for(EatStreetSection section : sections){
			s += section.toPrettyString() + "\r\n";
		}
		return s;
	}
	
	public String toString(){
		String s = "";
		for(EatStreetSection section : sections){
			s += section.toString();
		}
		return s;
	}
	
}
