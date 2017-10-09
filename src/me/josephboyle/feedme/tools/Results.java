package me.josephboyle.feedme.tools;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Results {

	@SerializedName("geometry")
	public Geometry geometry;

	@SerializedName("icon")
	public String icon;

	@SerializedName("id")
	public String id;

	@SerializedName("name")
	public String name;


	@SerializedName("rating")
	public Double rating;

	@SerializedName("reference")
	public String reference;

	@SerializedName("types")
	public List<String> types;

	@SerializedName("vicinity")
	public String vicinity;
	
	public String toString(){
		String s = "";
		s += "Name: " + name + "\r\n";
		s += "Rating: " + rating + "\r\n";
		s += "Vicinity: " + vicinity + "\r\n";
		if(geometry != null) s += geometry.toString();
		return s;
	}
	
}
