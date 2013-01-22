package models;

import java.io.UnsupportedEncodingException;

import lib.Logger;

/**
 * Contains String encryption algorithms.
 * 
 * @author Adam Childs
 */
public class Encryption_Model
{
	
	public Encryption_Model()
	{
		
	}

	/**
	 * <p>Generates a 128-bit (16 byte) MD5 hash for the specified String.
	 * 
	 * @param md5 the String to encrypt
	 * @return the encrypted String
	 */
	public String MD5(String md5)
	{
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes("UTF-8"));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i)
			{
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			new Logger().write("NoSuchAlgorithmException thrown.", "ERROR");
		} catch (UnsupportedEncodingException e) {
			new Logger().write("UTF-8 encoding not supported on this platform.", "ERROR");
		}
		return null;
	}
}