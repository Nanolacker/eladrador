package com.eladrador.common.utils;

import java.io.File;

import com.google.gson.Gson;

public class JsonUtils {

	private static Gson gson = new Gson();

	private JsonUtils() {
		// not to be instantiated
	}

	public static <T> T fromJsonFile(String filePath, Class<T> classOfT) {
		File file = new File(filePath);
		String json = "";
		return gson.fromJson(json, classOfT);
	}

}
