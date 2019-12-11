package io.github.ecsoya.fabric.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class FabricUtil {

	public static byte[] stringToHash(String str) {
		if (str == null || str.equals("")) {
			return null;
		}
		try {
			return Hex.decodeHex(str.toCharArray());
		} catch (DecoderException e) {
			return null;
		}
	}

	public static String hashToString(byte[] hash) {
		if (hash == null) {
			return null;
		}
		return Hex.encodeHexString(hash);
	}
}
