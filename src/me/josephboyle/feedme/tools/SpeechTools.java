package me.josephboyle.feedme.tools;

import java.util.List;

import com.google.cloud.language.v1.Sentiment;
import com.textrazor.annotations.NounPhrase;
import com.textrazor.annotations.Word;

import me.josephboyle.feedme.bot.Packet;
import me.josephboyle.feedme.eatstreet.EatStreetRestaurant;

public class SpeechTools {

	public enum SpeechSentiment{
		POSITIVE, NEGATIVE, NEUTRAL, MIXED
	}
	
	private static final double SENTIMENT_POSITIVE_SCORE = 0.4;
	private static final double SENTIMENT_NEGATIVE_SCORE = -0.4;
	private static final double SENTIMENT_MAGNITUDE_MIXED = 0.4;
	private static final double SORTABLE_SCORE_SIGNIFICANT = 0.001;
	
	public static boolean isAcceptanceString(String s){
		String[] acceptanceStrings = {
			"Yes", "Yes please", "Yes I'd like that", "Sounds good", "I pick that", "That's good",
			"I'll go there", "I like it here", "I like that", "that's good", "good", "okay", "ok", "k",
			"sure", "great"
		};
		for(String acceptanceString : acceptanceStrings){
			if(s.equalsIgnoreCase(acceptanceString)) return true;
		}
		return false;
	}
	
	public static boolean isRejectionString(String s){
		String[] rejectionStrings = {
				"No", "I hate it", "I don't like it there", "no thanks", "gross", "don't want", "bad",
				"awful", "negative", "bad idea", "please don't", "i'd rather not"
			};
			for(String rejection : rejectionStrings){
				if(s.equalsIgnoreCase(rejection)) return true;
			}
			return false;
	}
	
	public static boolean isAskingForMenuString(String s){
		String[] strings = {
				"menu", "show me the menu", "what do they have", "what's on their menu", "what can I get there", "what do they serve",
				"where is the menu", "where is their menu", "what are the options", "what can I eat there", "menu please", "give me the menu"
		};
		for(String string : strings){
			if(s.equalsIgnoreCase(string)) return true;
		}
		return false;
	}
	
	public static String getKeywords(Packet packet){
		String keywords = "";
		
		try{
			packet.analyzeText();
			for(NounPhrase noun : packet.analyzedText.getResponse().getNounPhrases()){
				if(!isPhraseKeyword(noun)) continue;
				for(Word w : noun.getWords()){
					keywords += w.getToken() + " ";
				}
			}
		}catch(Exception e){e.printStackTrace();}
		
		return keywords;
	}
	
	public static boolean isPhraseKeyword(NounPhrase phrase){
		if(phrase.getWords().size() == 1){
			String s = phrase.getWords().get(0).getToken();
			if(s.equalsIgnoreCase("I") || s.equalsIgnoreCase("or")) return false;
		}
		return true;
	}
	
	// Returns between 0 and resultsPerPage, given the page number.
	// If we have 12 objects and are on page 1, we get 8 results.
	// If we are have 12 objects and are on page 2, we get 4 results.
	public static Sortable[] paginate(Sortable[] objects, int page, int resultsPerPage){
		int startIndex = (page * resultsPerPage);
		int numResults = objects.length - startIndex;
		
		// If <= 0, then there aren't any results on this page so we can just stop now.
		if(numResults <= 0){
			return new Sortable[0];
		}

		// Now we restrict to our desired size.
		if(numResults > resultsPerPage) numResults = resultsPerPage;
		Sortable[] results = new Sortable[numResults];
		int j = 0;
		for(int i = startIndex; i < (startIndex + numResults); i ++){
			results[j ++] = objects[i];
		}
		return results;
	}
	
	public static Sortable[] removeInsignificantSortables(Sortable[] objects){
		int numSignificant = 0;
		for(int i = 0; i < objects.length; i ++){
			if(objects[i].score >= SORTABLE_SCORE_SIGNIFICANT) numSignificant ++;
		}
		Sortable[] significant = new Sortable[numSignificant];

		int j = 0;
		for(int i = 0; i < objects.length; i ++){
			if(objects[i].score >= SORTABLE_SCORE_SIGNIFICANT) significant[j ++] = objects[i];
		}
		
		return significant;
	}
	
	// For now, we implement a selection sort.
	// This is for a philosophy course.
	// What could possibly go wrong?
	public static Sortable[] sort(Sortable[] objects){
		for(int i = 0; i < objects.length; i ++){
			int biggest = i;
			for(int j = i; j < objects.length; j ++){
				if(objects[j].score > objects[biggest].score) biggest = j;
			}
			Sortable tmp = objects[i];
			objects[i] = objects[biggest];
			objects[biggest] = tmp;
			
		}
		return objects;
	}
	
	public static Sortable[] reorderRestaurants(List<EatStreetRestaurant> restaurants, double[] scores){
		Sortable[] sortableRestaurants = new Sortable[restaurants.size()];
		
		for(int i = 0; i < sortableRestaurants.length; i ++){
			sortableRestaurants[i] = new Sortable(restaurants.get(i), restaurants.get(i).computeSimarityWeight(scores[i]));
		}
		
		return sort(sortableRestaurants);
	}
	
	public static double[] getCosineSimilarities(List<EatStreetRestaurant> restaurants, String query){
		double[] similarities = new double[restaurants.size()];
		double[][] termFrequencies = new double[restaurants.size()][];
		String[] queryArray = stringToArray(query);
		double[] numDocsWithWord = new double[queryArray.length];
		double[][] tdidf = new double[restaurants.size()][queryArray.length];
		
		// First we compute the TF
		for(int i = 0; i < restaurants.size(); i ++){
			EatStreetRestaurant restaurant = restaurants.get(i);
			termFrequencies[i] = getTermFrequency(stringToArray(restaurant.toString()), queryArray);
			// Here we increase each word by 1 if it occurred in the document.
			for(int j = 0; j < termFrequencies[i].length; j ++){
				if(termFrequencies[i][j] > 0) numDocsWithWord[j] += 1; 
			}
		}
		
		// Now we evaluate the INF by normalizing each result.
		for(int i = 0; i < numDocsWithWord.length; i ++){
			numDocsWithWord[i] = Math.log(restaurants.size() / (0.5 + numDocsWithWord[i]));
		}
		
		// Now we evaluate the TD-INF by multiplying TD*INF for each restaurant
		for(int i = 0; i < restaurants.size(); i ++){
			for(int j = 0; j < numDocsWithWord.length; j ++){
				tdidf[i][j] = termFrequencies[i][j] * numDocsWithWord[j];
			}
		}
		
		// Now we compute the TDF of the query, to normalize it for the number of words used:
		double[] queryTdf = getTermFrequency(queryArray, queryArray);
		
		// Now for each document, we compute: dot(D,queryIdf)/(||D||, ||queryIdf||)
		for(int i = 0; i < similarities.length; i ++){
			similarities[i] = dotProduct(tdidf[i], queryTdf);
			double normalization = norm(tdidf[i]) * norm(queryTdf);
			if(normalization != 0) similarities[i] /= (normalization);
			else similarities[i] = 0;
		}
		
		return similarities;
	}
	
	public static double dotProduct(double[] a, double[] b){
		double result = 0;
		
		for(int i = 0; i < a.length; i ++){
			result += (a[i] * b[i]);
		}
		
		return result;
	}
	
	public static double norm(double[] array){
		double result = 0.0;
		for(double d : array) result += (d * d);
		return Math.sqrt(result);
	}
	
	public static double[] getTermFrequency(String[] document, String[] query){
		double[] freq = new double[query.length];
		
		for(int i = 0; i < query.length; i ++){
			for(int j = 0; j < document.length; j ++){
				if(document[j].equalsIgnoreCase(query[i])) freq[i] += (1.0 / document.length);
			}
		}
		
		return freq;
	}
	
	public static String[] stringToArray(String s){
		String[] result;
		String[] strippableWords = {"==", "==\r\n", "\r\n", ".", ",", "!", ";"};
		String[] exploded = s.split(" ");
		int i = 0;
		result = new String[exploded.length];
		
		for(String word : s.split(" ")){
			result[i ++] = stripDelimiters(word, strippableWords);
		}
	
		return result;
	}

	private static String stripDelimiters(String s, String[] delimiters){
		for(String del : delimiters){
			s = s.replace(del, "");
		}
		return s;
	}

	
	public static SpeechSentiment getSentimentValue(Sentiment sentiment){
		if(isPositive(sentiment)) return SpeechSentiment.POSITIVE;
		if(isNegative(sentiment)) return SpeechSentiment.NEGATIVE;
		if(sentiment.getMagnitude() > SENTIMENT_MAGNITUDE_MIXED) return SpeechSentiment.MIXED;
		return SpeechSentiment.NEUTRAL;
	}
	
	private static boolean isPositive(Sentiment sentiment){
		if(sentiment.getScore() > SENTIMENT_POSITIVE_SCORE &&
				sentiment.getMagnitude() > SENTIMENT_MAGNITUDE_MIXED) return true;
		return false;
	}
	
	private static boolean isNegative(Sentiment sentiment){
		if(sentiment.getScore() < SENTIMENT_NEGATIVE_SCORE &&
				sentiment.getMagnitude() > SENTIMENT_MAGNITUDE_MIXED) return true;
		return false;
	}
	
}
