package controllers;

import models.Encryption_Model;

/**
 * Controls communication between the main program and the String encryption
 * algorithms of the Encryption_Model class.
 * 
 * @author Adam Childs
 */
public class Encryption_Controller
{
	Encryption_Model model = null;

	public Encryption_Controller()
	{
		model = new Encryption_Model();
	}

	/**
	 * <p>Calls the MD5 encryption algorithm from the Encryption_Model class.
	 * 
	 * @param md5 the String to encrypt
	 * @return the encrypted String
	 */
	public String MD5(String md5)
	{
		return model.MD5(md5);
	}
}