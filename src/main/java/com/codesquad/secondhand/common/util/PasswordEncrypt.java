package com.codesquad.secondhand.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordEncrypt {

	public static String getSalt() {
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			byte[] bytes = new byte[16];
			random.nextBytes(bytes);
			return String.valueOf(bytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getEncrypt(String password, String salt) {
		try {
			String passwordAndSalt = password + salt;
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(passwordAndSalt.getBytes());
			return String.format("%064x", new BigInteger(1, md.digest()));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
