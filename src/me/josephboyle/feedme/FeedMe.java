package me.josephboyle.feedme;

import java.util.List;
import java.util.Scanner;

import com.google.cloud.language.v1.LanguageServiceClient;

import me.josephboyle.feedme.bot.Bot;
import me.josephboyle.feedme.bot.Packet;
import me.josephboyle.feedme.eatstreet.EatStreetLoader;
import me.josephboyle.feedme.eatstreet.EatStreetRestaurant;

public class FeedMe {

	public static final boolean loadRestaurantsLive = false;
	public static final boolean showDebugPrints = false;
	
	public static void main(String... args) throws Exception {
		LanguageServiceClient language = LanguageServiceClient.create();
		
		Bot bot;
		Packet packet;
		
		List<EatStreetRestaurant> restaurants = null;
		
		try{
			if(!FeedMe.loadRestaurantsLive) restaurants = EatStreetLoader.loadRestaurantsCached();
			if(FeedMe.loadRestaurantsLive || restaurants == null || restaurants.isEmpty()){
				System.out.println("Loading restaurants live!");
				restaurants = EatStreetLoader.loadRestaurantsLive();
				EatStreetLoader.saveRestaurants(restaurants);
			}
			if(showDebugPrints){
				System.out.println("Total number requests: " + restaurants.size());
				System.out.println("================================================");
				for(EatStreetRestaurant restaurant : restaurants){
					System.out.println(restaurant.toString());
					System.out.println("================================================");
				}
				System.out.println("Total number requests: " + restaurants.size());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(restaurants == null){
			System.out.println("An error occurred in the creation of restaurants");
		}
		
		bot = new Bot(language, restaurants);
		
		packet = new Packet(language, "", Packet.PacketType.START);
		bot.processInputs(packet);
		
		Scanner scanner = new Scanner(System.in);
		String line;
		
		while( scanner.hasNextLine() ){
			line = scanner.nextLine();
			packet = new Packet(language, line, Packet.PacketType.USER);
			bot.processInputs(packet);
	//		EatStreetLoader.saveRestaurants(restaurants);
		}
		
	}
	
}
