package com.dhs.portglass.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Protects passwords by hashing them with the SHA-256
 * algorithm. A SALT variable is added to the password
 * in order to make the resulting hash harder to crack.
 * @author Manuel R Saldana
 *
 */
public class PasswordManager {
	
	public static final String SALT = "The big red car drives over" +
			"the yellow brick road.";
 
   
     /**
      * Encrypts password with SHA-256 using a predefined SALT.
      * @param input
      * @return
      */
    public static String encrypt(String input) {
        
        String sha = null;
         
        if(null == input) return null;
        input = input+SALT;
        try {
             
        //Create MessageDigest object for MD5
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
         
        //Update input string in message digest
        digest.update(input.getBytes(), 0, input.length());
 
        //Converts message digest value in base 16 (hex)
        sha = new BigInteger(1, digest.digest()).toString(16);
 
        } catch (NoSuchAlgorithmException e) {
 
            e.printStackTrace();
        }
        return sha;
    }
    
}