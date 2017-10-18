package me.josephboyle.feedme.bot;

import java.util.List;

import com.google.cloud.language.v1.LanguageServiceClient;

import me.josephboyle.feedme.eatstreet.EatStreetRestaurant;
import me.josephboyle.feedme.tools.Sortable;
import me.josephboyle.feedme.tools.SpeechTools;

public class Bot {

	private LanguageServiceClient language;
	private List<EatStreetRestaurant> restaurants;
	private List<String> foodTypes;
	
	public Bot(LanguageServiceClient language, List<EatStreetRestaurant> restaurants, List<String> foodTypes){
		this.language = language;
		this.restaurants = restaurants;
		this.foodTypes = foodTypes;
	}
	
	public void processInputs(Packet packet){
		if(packet.sender == Packet.PacketType.START){
			speak("Hello, I'm Alan!");
			speak("Are you hungry?");
			return;
		}

		String keywords = SpeechTools.getKeywords(packet);
		
		System.out.println("Keywords: " + keywords);
		
		double[] similarities = SpeechTools.getCosineSimilarities(restaurants, keywords);
		Sortable[] results = SpeechTools.removeInsignificantSortables(SpeechTools.reorderRestaurants(restaurants, similarities));
		Sortable[] orderedResults = SpeechTools.paginate(results, 0, 8);
		for(int i = 0; i < orderedResults.length; i ++){
			System.out.println(((EatStreetRestaurant)orderedResults[i].object).name + ": " + orderedResults[i].score);
		}
		
	}
		
	public void speak(String s){
		System.out.println("[Alan]: " + s);
	}
	
}
