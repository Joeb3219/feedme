package me.josephboyle.feedme.tools;

import java.util.List;

import com.google.cloud.language.v1.Sentiment;

import me.josephboyle.feedme.eatstreet.EatStreetRestaurant;

public class SpeechTools {

	public enum SpeechSentiment{
		POSITIVE, NEGATIVE, NEUTRAL, MIXED
	}
	
	private static final double SENTIMENT_POSITIVE_SCORE = 0.4;
	private static final double SENTIMENT_NEGATIVE_SCORE = -0.4;
	private static final double SENTIMENT_MAGNITUDE_MIXED = 0.4;
	
	public double[] getCosineSimilarities(List<EatStreetRestaurant> restaurants, String query){
		double[] similarities = new double[restaurants.size()];
		int[][] termFrequencies = new int[restaurants.size()][];
		String[] queryArray = stringToArray(query);
		double[] numDocsWithWord = new double[query.length()];
		double[][] tdidf = new double[restaurants.size()][query.length()];
		
		// First we compute the TF
		for(int i = 0; i < restaurants.size(); i ++){
			EatStreetRestaurant restaurant = restaurants.get(i);
			termFrequencies[i] = getTermFrequency(stringToArray(restaurant.toString()), queryArray);
			// Here we increase each word by 1 if it occurred in the document.
			for(int j = 0; j < termFrequencies.length; j ++){
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
		
		// Now we must compute the
		
		return similarities;
	}
	
	public double euclidianDistance(double[] array){
		double result = 0.0;
		for(double d : array) result += (d * d);
		return Math.sqrt(result);
	}
	
	public int[] getTermFrequency(String[] document, String[] query){
		int[] freq = new int[query.length];
		
		for(int i = 0; i < query.length; i ++){
			for(int j = 0; j < document.length; j ++){
				if(document[j].equals(query[i])) freq[i] ++;
			}
		}
		
		return freq;
	}
	
	public String[] stringToArray(String s){
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

	private String stripDelimiters(String s, String[] delimiters){
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
