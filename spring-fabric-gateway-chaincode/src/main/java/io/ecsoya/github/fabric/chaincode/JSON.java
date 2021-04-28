package io.ecsoya.github.fabric.chaincode;

import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

public class JSON {

	private static final Gson gson = new Gson();

	public static String stringify(Object object) {
		if (object == null) {
			return "";
		}
		return gson.toJson(object);
	}

	public static byte[] payload(Object object) {
		return stringify(object).getBytes(StandardCharsets.UTF_8);
	}
}
