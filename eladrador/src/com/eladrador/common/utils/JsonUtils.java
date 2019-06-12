package com.eladrador.common.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class JsonUtils {

	private static Gson gson = new Gson();

	private JsonUtils() {
		// not to be instantiated
	}

	public static <T> T fromJsonFile(String filePathName, Class<T> classOfT) {
		FileReader fileReader;
		try {
			fileReader = new FileReader(filePathName);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		JsonReader reader = new JsonReader(fileReader);
		String json = gson.fromJson(reader, classOfT);
		return gson.fromJson(json, classOfT);
	}

}
