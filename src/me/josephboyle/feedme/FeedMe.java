package me.josephboyle.feedme;

import java.util.Scanner;

import com.google.cloud.language.v1.LanguageServiceClient;

import me.josephboyle.feedme.bot.Bot;
import me.josephboyle.feedme.bot.Packet;

public class FeedMe {

	public static void main(String... args) throws Exception {
	    
		LanguageServiceClient language = LanguageServiceClient.create();
		
		Bot bot = new Bot(language);
		
		
		Packet packet = new Packet(language, "", Packet.PacketType.START);
		bot.processInputs(packet);
		
		Scanner scanner = new Scanner(System.in);
		String line;
		
		while( scanner.hasNextLine() ){
			line = scanner.next();
			packet = new Packet(language, line, Packet.PacketType.USER);
			bot.processInputs(packet);
		}
		
	}
	
}
