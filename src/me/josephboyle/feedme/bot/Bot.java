package me.josephboyle.feedme.bot;

import java.util.List;

import com.google.cloud.language.v1.LanguageServiceClient;

import me.josephboyle.feedme.eatstreet.EatStreetLoader;
import me.josephboyle.feedme.eatstreet.EatStreetRestaurant;
import me.josephboyle.feedme.tools.Sortable;
import me.josephboyle.feedme.tools.SpeechTools;

public class Bot {

	public static final boolean DEBUG = false;
	
	public enum BotState{
		INITIAL, SUGGESTED
	}
	
	private LanguageServiceClient language;
	private List<EatStreetRestaurant> restaurants;
	private BotState state = BotState.INITIAL;
	private double[] restaurantSimilaritiesToPreviousQuery;
	
	
	public Bot(LanguageServiceClient language, List<EatStreetRestaurant> restaurants){
		this.language = language;
		this.restaurants = restaurants;
	}
	
	private void debug(String s){
		if(DEBUG) System.out.println("[dbg]: " + s);
	}
	
	public void processInputs(Packet packet){
		debug("Given a packet: " + packet.rawText);
		debug("Current state: " + state);
		
		if(state == BotState.INITIAL){
			if(packet.sender == Packet.PacketType.START){
				speak("Hello, I'm Peet Za!");
				speak("Are you hungry?");
				return;
			}
		}
	
		if(state == BotState.SUGGESTED){
			boolean isAcceptance = SpeechTools.isAcceptanceString(packet.rawText);
			boolean isRejection = SpeechTools.isRejectionString(packet.rawText);
			boolean isAskingForMenu = SpeechTools.isAskingForMenuString(packet.rawText);

			if(isAcceptance){
				debug("User has accepted our suggestion so we can now die.");
				EatStreetRestaurant r = getNextSuggestion();
				speak("You should go to " + r.name);
				speak("You can find this place at: " + r.address);
				speak("Here is the phone number: " + r.phone);
				speak("Have a very nice day :)");
				System.exit(0);
			}
			
			if(isRejection){
				debug("The user has rejected our suggestion, so we now reweight and try again.");
				// Now we negatively weight this suggestion. 
				EatStreetLoader.weightRestaurant(getNextSuggestion(), -1);
				suggestRestaurant(getNextSuggestion());
				return;
			}
			
			if(isAskingForMenu){
				debug("The user asked for the menu.");
				EatStreetRestaurant r = getNextSuggestion();
				speak("Showing the menu for " + r.name);
				speak(r.menu.toPrettyString());
				speak("...");
				speak("Do you want to eat here?");
				return;
			}
			
			debug("The user is giving us input to further refine");
			// If we neither accepted nor reject or asking for menu, then the user is trying to further refine the search.
			getKeywordsAndRefine(packet);
			suggestRestaurant(getNextSuggestion());
			
			return;
		}
		
		debug("The user is giving us input for the first time");
		// If we reached here, then the user is trying to tell us stuff and we need to refine searches from it.
		getKeywordsAndRefine(packet);
		suggestRestaurant(getNextSuggestion());
		state = BotState.SUGGESTED;
		
	}
	
	private void suggestRestaurant(EatStreetRestaurant r){
		speak("Here's my suggestion:");
		speak(r.name);
	}
	
	private void getKeywordsAndRefine(Packet packet){
		String keywords = SpeechTools.getKeywords(packet);
		
		debug("Keywords: " + keywords);
		
		restaurantSimilaritiesToPreviousQuery = SpeechTools.getCosineSimilarities(restaurants, keywords);
	}
	
	private EatStreetRestaurant getNextSuggestion(){
		Sortable[] results = SpeechTools.removeInsignificantSortables(SpeechTools.reorderRestaurants(restaurants, restaurantSimilaritiesToPreviousQuery));
		Sortable[] orderedResults = SpeechTools.paginate(results, 0, 1);
		if(orderedResults.length == 0) return null;
		return (EatStreetRestaurant) orderedResults[0].object;
	}
		
	public void speak(String s){
		System.out.println("[Peet Za]: " + s);
	}
	
}
