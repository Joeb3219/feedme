package me.josephboyle.feedme.bot;

import java.util.List;

import com.google.cloud.language.v1.LanguageServiceClient;

import me.josephboyle.feedme.eatstreet.EatStreetRestaurant;
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

		
		//packet.processSentiment();
		//speak("You said: " + packet.rawText);
		//speak("Score: " + packet.sentiment.getScore() + "; Magnitude: " + packet.sentiment.getMagnitude());
		//speak("Your sentiment: " + SpeechTools.getSentimentValue(packet.sentiment));
	}
		
	public void speak(String s){
		System.out.println("[Alan]: " + s);
	}
	
}
