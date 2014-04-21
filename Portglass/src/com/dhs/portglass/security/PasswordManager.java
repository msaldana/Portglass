package com.dhs.portglass.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Protects passwords by hashing them with the SHA-512
 * algorithm. A SALT variable is added to the password
 * in order to make the resulting hash harder to crack.
 * This SALT is generated using a secure number generator.
 * @author Manuel R Saldana
 *
 */
public class PasswordManager {

	
	/**
	 * Encrypts a password using the given salt. The salt and password
	 * are encrypted with SHA256 and appended bitwise. 
	 * @param password Password to be hashed
	 * @param salt SALT to append to password
	 * @return
	 */
	public static String encryptSHA256(String password, String salt)
	{
	
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt.getBytes());
			byte[] bytes = md.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return generatedPassword;
	}
	
	
	
	/**
	 * Encrypts a password using the given salt. The salt and password
	 * are encrypted with SHA512 and appended bitwise. 
	 * @param password Password to be hashed
	 * @param salt SALT to append to password
	 * @return
	 */
	public static String encryptSHA512(String password, String salt)
	{
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt.getBytes());
			byte[] bytes = md.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return generatedPassword;
	}

	/**
	 * Provides a Secure Random SALT to be used for encryption. Builds 
	 * upon the native SHA1 Pseudo Random Number Generator for SUN.
	 * @return Secure Random SALT String
	 * @throws NoSuchAlgorithmException
	 */
	public static String generateSalt() throws NoSuchAlgorithmException
	{
		//Use Native SHA1 Pseudo Random Number Generator for secure 
		//random generation
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt.toString();
	}
	
	

}