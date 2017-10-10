package me.josephboyle.feedme.eatstreet;

import com.google.gson.annotations.SerializedName;

public class EatStreetHours {

	@SerializedName("Monday")
	public String monday;
	
	@SerializedName("Tuesday")
	public String tuesday;
	
	@SerializedName("Wednesday")
	public String wednesday;
	
	@SerializedName("Thursday")
	public String thursday;
	
	@SerializedName("Friday")
	public String friday;
	
	@SerializedName("Saturday")
	public String saturday;
	
	@SerializedName("Sunday")
	public String sunday;
	
	public String toString(){
		String result = "";
		
		result += "Hours: \r\n";
		result += "Monday: " + monday + "\r\n";
		result += "Tuesday: " + tuesday + "\r\n";
		result += "Wednesday: " + wednesday + "\r\n";
		result += "Thursday: " + thursday + "\r\n";
		result += "Friday: " + friday + "\r\n";
		result += "Saturday: " + saturday + "\r\n";
		result += "Sunday: " + sunday + "\r\n";
		
		return result;
	}
	
}
