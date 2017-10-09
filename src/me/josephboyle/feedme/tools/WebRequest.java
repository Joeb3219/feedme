package me.josephboyle.feedme.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebRequest {

	public static final String latitude = "40.5477";
	public static final String longitude = "-74.4633";
	
	public static String requestToString(InputStream stream) throws IOException{
		String s = "", line = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		while( (line = reader.readLine()) != null ){
			s += line;
		}
		return s;
	}
	
	public static InputStream getPlacesRequest(String bucket, String parameters) throws IOException{
	//	HttpClient client = HttpClients.createDefault();
		String url  = "https://maps.googleapis.com/maps/api/place/" + bucket + "/json";
		if(parameters != null) parameters += "&key=AIzaSyCkcn-dySbSoQnDXF7NWF2wk9EnCXsilL4";
		url += "?" + parameters;
		System.out.println(url);
		URL requestUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		
		DataOutputStream sendRequest = new DataOutputStream(connection.getOutputStream());
		sendRequest.close();
		
		InputStream result = connection.getInputStream();
		return result;
	}
	
}
