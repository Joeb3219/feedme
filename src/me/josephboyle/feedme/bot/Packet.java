package me.josephboyle.feedme.bot;

import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.annotations.AnalyzedText;

public class Packet {
	
	// Describes who sent the message: the bot or the user. 
	// START is used to describe when the system is initiating the conversation.
	public enum PacketType{USER, BOT, START};
	
	public PacketType sender;
	public String rawText;
	public Sentiment sentiment;
	private AnalyzeSyntaxResponse syntax;
	public AnalyzeEntitiesResponse entities;
	private Document document;
	private LanguageServiceClient language;
	private TextRazor textRazor;
	public AnalyzedText analyzedText;
	
	public Packet(LanguageServiceClient language, String input, PacketType sender){
		this.rawText = input;
		this.sentiment = null;
		this.sender = sender;
		this.language = language;
		this.textRazor = new TextRazor("f257e5fcc6992eb090d60a2f8bd267e4e32e70ae842ef8611e154fc4");
		textRazor.addExtractor("phrases");
	}
	
	private void createDocument(){
		document = Document.newBuilder().setContent(rawText).setType(Type.PLAIN_TEXT).build();
	}
	
	public void analyzeText() throws AnalysisException, NetworkException{
		analyzedText = textRazor.analyze(rawText);
	}
	
	public void processEntities(){
		if(document == null) createDocument();
		AnalyzeEntitiesRequest request = AnalyzeEntitiesRequest.newBuilder().setDocument(document).setEncodingType(EncodingType.UTF16).build();
		entities = language.analyzeEntities(request);
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
