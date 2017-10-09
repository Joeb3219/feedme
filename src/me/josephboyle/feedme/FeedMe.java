package me.josephboyle.feedme;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.gson.Gson;

import me.josephboyle.feedme.bot.Bot;
import me.josephboyle.feedme.bot.Packet;
import me.josephboyle.feedme.tools.GoogleMapper;
import me.josephboyle.feedme.tools.Results;
import me.josephboyle.feedme.tools.WebRequest;

public class FeedMe {

	public static void main(String... args) throws Exception {
	    
		LanguageServiceClient language = LanguageServiceClient.create();
		
		Bot bot = new Bot(language);
		
		
		Packet packet = new Packet(language, "", Packet.PacketType.START);
		bot.processInputs(packet);

		ArrayList<Results> allResults = generateAllRestaurantsNearby(null);
		
//		Results[] allResults = new Results[60];
		
		for(Results result : allResults){
			if(result == null) continue;
			System.out.println(result.toString());
			System.out.println("====================================");
		}
		
		System.out.println("Found a total of " + allResults.size() + " results");
		
		if(1 == 1) return;
		
		Scanner scanner = new Scanner(System.in);
		String line;
		
		while( scanner.hasNextLine() ){
			line = scanner.nextLine();
			packet = new Packet(language, line, Packet.PacketType.USER);
			bot.processInputs(packet);
		}
		
	}
	
	public static ArrayList<Results> generateAllRestaurantsNearby(String nextPage) throws IOException, InterruptedException{
		ArrayList<Results> allResults = new ArrayList<Results>();
		InputStream inStream;
		if(nextPage == null) inStream = WebRequest.getPlacesRequest("nearbysearch", "type=restaurant&location=" + WebRequest.latitude + "," + WebRequest.longitude + "&radius=" + WebRequest.searchRadius);
		else inStream = WebRequest.getPlacesRequest("nearbysearch", "pagetoken=" + nextPage);
		InputStreamReader inReader = new InputStreamReader(inStream);
		GoogleMapper results = new Gson().fromJson( inReader , GoogleMapper.class);
		System.out.println("Status: " + results.status);
		for(Results result : results.results){
			if(!resultInList(allResults, result)) allResults.add(result);
		}
		if(results.next_page_token != null && !results.next_page_token.isEmpty()){
			TimeUnit.SECONDS.sleep(2);
			ArrayList<Results> returnedResults = generateAllRestaurantsNearby(results.next_page_token);
			for(Results r : returnedResults){
				if(!resultInList(allResults, r)) allResults.add(r);
			}
		}
		return allResults;
	}
	
	public static boolean resultInList(ArrayList<Results> results, Results result){
		for(Results r : results){
			if(r.id.equals(result.id)) return true;
		}
		return false;
	}
	
}
