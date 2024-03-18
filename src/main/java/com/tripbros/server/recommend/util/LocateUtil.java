package com.tripbros.server.recommend.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.tripbros.server.enumerate.City;
import com.tripbros.server.enumerate.Country;

@Component
public class LocateUtil {
	public static String getLocateImage(String apiKey,Country country, City city) {
		String url = "https://pixabay.com/api/";
		String searchKeyword = country.toString().concat(" ").concat(city.toString());

		WebClient webClient = WebClient.create(url);
		String response = webClient.get()
			.uri(uriBuilder -> uriBuilder
				.queryParam("q", searchKeyword)
				.queryParam("lang","ko")
				.queryParam("key", apiKey)
				.build())
			.retrieve()
			.bodyToMono(String.class)
			.block();

		return parseResponseToImageUrl(response);
	}

	private static String parseResponseToImageUrl(String response) {
		JSONParser parser = new JSONParser();
		JSONObject object;
		try {
			object = (JSONObject)parser.parse(response);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		JSONArray hits = (JSONArray)object.get("hits");
		JSONObject hitBody = (JSONObject)hits.get(0);
		return (String)hitBody.get("largeImageURL");
	}
}
