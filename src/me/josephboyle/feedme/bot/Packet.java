package me.josephboyle.feedme.bot;

import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

public class Packet {
	
	// Describes who sent the message: the bot or the user. 
	// START is used to describe when the system is initiating the conversation.
	public enum PacketType{USER, BOT, START};
	
	public PacketType sender;
	private String rawText;
	private Sentiment sentiment;
	private AnalyzeSyntaxResponse syntax;
	private Document document;
	private LanguageServiceClient language;
	
	public Packet(LanguageServiceClient language, String input, PacketType sender){
		this.rawText = input;
		this.sentiment = null;
		this.sender = sender;
		this.language = language;
	}
	
	private void createDocument(){
		document = Document.newBuilder().setContent(rawText).setType(Type.PLAIN_TEXT).build();
	}
	
	public void processSentiment(){
		if(document == null) createDocument();
		sentiment = language.analyzeSentiment(document).getDocumentSentiment();
	}

	public void processSyntax(){
		if(document == null) createDocument();
		syntax = language.analyzeSyntax(document, EncodingType.NONE);
	}
	
}
