package me.josephboyle.feedme;

import java.util.List;
import java.util.Scanner;

import com.google.cloud.language.v1.LanguageServiceClient;

import me.josephboyle.feedme.bot.Bot;
import me.josephboyle.feedme.bot.Packet;
import me.josephboyle.feedme.eatstreet.EatStreetLoader;
import me.josephboyle.feedme.eatstreet.EatStreetRestaurant;

public class FeedMe {

	public static void main(String... args) throws Exception {
	    
		LanguageServiceClient language = LanguageServiceClient.create();
		
		Bot bot = new Bot(language);
		
		
		Packet packet = new Packet(language, "", Packet.PacketType.START);
		bot.processInputs(packet);
		
		try{
			List<EatStreetRestaurant> restaurants = EatStreetLoader.loadRestaurantsCached();
			System.out.println("Total number requests: " + restaurants.size());
			System.out.println("================================================");
			for(EatStreetRestaurant restaurant : restaurants){
				System.out.println(restaurant.toString());
				System.out.println("================================================");
			}
			System.out.println("Total number requests: " + restaurants.size());
			EatStreetLoader.saveRestaurants(restaurants);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(1 == 1) return;
		
		Scanner scanner = new Scanner(System.in);
		String line;
		
		while( scanner.hasNextLine() ){
			line = scanner.nextLine();
			packet = new Packet(language, line, Packet.PacketType.USER);
			bot.processInputs(packet);
		}
		
	}
	
}
