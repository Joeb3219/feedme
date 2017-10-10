package me.josephboyle.feedme.eatstreet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import me.josephboyle.feedme.tools.WebRequest;

public class EatStreetLoader {

	public static final String RESTAURANTS_FILE = "restaurants.json";
	
	public static List<String> getFoodCategories(List<EatStreetRestaurant> restaurants){
		List<String> foodTypes = new ArrayList<String>();
		
		for(EatStreetRestaurant restaurant : restaurants){
			for(String type : restaurant.foodTypes){
				if(!foodTypes.contains(type)) foodTypes.add(type);
			}
		}
		
		return foodTypes;
	}
	
	public static List<EatStreetRestaurant> loadRestaurantsCached() throws FileNotFoundException{
		List<EatStreetRestaurant> restaurants = new Gson().fromJson(new FileReader(RESTAURANTS_FILE), new TypeToken<List<EatStreetRestaurant>>(){}.getType());
		return restaurants;
	}
	
	public static List<EatStreetRestaurant> loadRestaurantsLive() throws IOException, InterruptedException{
		InputStream inStream = WebRequest.getEatStreetRequest("search", "method=both&latitude=" + WebRequest.latitude + "&longitude=" + WebRequest.longitude);
		InputStreamReader inReader = new InputStreamReader(inStream);
		EatStreetSearch results = new Gson().fromJson( inReader , EatStreetSearch.class);
		
		for(EatStreetRestaurant r : results.restaurants){
			r.menu = loadMenu(r.apiKey);
			TimeUnit.MILLISECONDS.sleep(150);
		}
		
		return results.restaurants;
	}
	
	public static EatStreetMenu loadMenu(String apiKey) throws IOException{
		EatStreetMenu menu = new EatStreetMenu();
		InputStream inStream = WebRequest.getEatStreetRequest(apiKey + "/menu", "includeCustomizations=false");
		InputStreamReader inReader = new InputStreamReader(inStream);
		List<EatStreetSection> result = new Gson().fromJson( inReader , new TypeToken<List<EatStreetSection>>(){}.getType());
		menu.sections = result;
		return menu;
	}
	
	public static void saveRestaurants(List<EatStreetRestaurant> restaurants) throws FileNotFoundException{
		String json = new Gson().toJson(restaurants);
		try(PrintWriter out = new PrintWriter(RESTAURANTS_FILE)){
		    out.println(json);
		}
	}
	
}

