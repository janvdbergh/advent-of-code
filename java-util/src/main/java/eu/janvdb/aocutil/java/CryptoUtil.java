package eu.janvdb.aocutil.java;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtil {

	private final static MessageDigest md5;

	static {
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String md5ToHexString(String input) {
		return Hex.encodeHexString(md5.digest(input.getBytes()));
	}
}
