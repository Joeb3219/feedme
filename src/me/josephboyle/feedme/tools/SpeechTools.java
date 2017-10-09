package me.josephboyle.feedme.tools;

import com.google.cloud.language.v1.Sentiment;

public class SpeechTools {

	public enum SpeechSentiment{
		POSITIVE, NEGATIVE, NEUTRAL, MIXED
	}
	
	private static final double SENTIMENT_POSITIVE_SCORE = 0.4;
	private static final double SENTIMENT_NEGATIVE_SCORE = -0.4;
	private static final double SENTIMENT_MAGNITUDE_MIXED = 0.4;
	
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
