package me.josephboyle.feedme.bot;

import com.google.cloud.language.v1.LanguageServiceClient;

public class Bot {

	private LanguageServiceClient language;
	
	public Bot(LanguageServiceClient language){
		this.language = language;
	}
	
	public void processInputs(Packet packet){
		if(packet.sender == Packet.PacketType.START){
			speak("Hello, I'm Alan!");
			speak("Are you hungry?");
		}
	}
	
	public void speak(String s){
		System.out.println("[Alan]: " + s);
	}
	
}
