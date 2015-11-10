package br.com.cdweb.persistence.util;

import com.google.gson.Gson;

public class UtilJson {
	private static Gson gson = new Gson(); 
	public static String toJson(Object object) {
		return gson.toJson(object);
	}
}
