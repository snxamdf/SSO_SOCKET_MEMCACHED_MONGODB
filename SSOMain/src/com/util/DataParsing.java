package com.util;

import net.sf.json.JSONObject;

public class DataParsing {
	public static JSONObject strToJson(String data) {
		JSONObject json = JSONObject.fromObject(data);
		return json;
	}

	public static void main(String[] args) {
		String urlString = "{\"username\":\"zjl\",\"password\":\"123\",\"bigText\":\"222\"}";
		JSONObject json = JSONObject.fromObject(urlString);
		System.out.println(json.get("username"));
	}
	
}
