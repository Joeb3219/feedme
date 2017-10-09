package me.josephboyle.feedme;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.gson.Gson;

import me.josephboyle.feedme.bot.Bot;
import me.josephboyle.feedme.bot.Packet;
import me.josephboyle.feedme.tools.GoogleMapper;
import me.josephboyle.feedme.tools.Results;
import me.josephboyle.feedme.tools.WebRequest;

public class FeedMe {

	public static void main(String... args) throws Exception {
	    
		LanguageServiceClient language = LanguageServiceClient.create();
		
		Bot bot = new Bot(language);
		
		
		Packet packet = new Packet(language, "", Packet.PacketType.START);
		bot.processInputs(packet);
		
		try{
			InputStream inStream = WebRequest.getPlacesRequest("nearbysearch", "type=restaurant&location=" + WebRequest.latitude + "," + WebRequest.longitude + "&radius=20000");
			InputStreamReader inReader = new InputStreamReader(inStream);
			GoogleMapper results = new Gson().fromJson( inReader , GoogleMapper.class);
			System.out.println("Status: " + results.status);
			for(Results result : results.results){
				System.out.println(result.toString());
				System.out.println("====================================");
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
		if(1 == 1) return;
		
		Scanner scanner = new Scanner(System.in);
		String line;
		
		while( scanner.hasNextLine() ){
			line = scanner.nextLine();
			packet = new Packet(language, line, Packet.PacketType.USER);
			bot.processInputs(packet);
		}
		
	}
	
}
