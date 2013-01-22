package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import controllers.Account_Controller;
import controllers.Encryption_Controller;


/**
 * <p>Contains all the business logic of logging a user
 * into the system.
 * 
 * @author Adam Childs
 */
public class Login_Model
{
	
	public Login_Model()
	{
		
	}

	/**
	 * <p>Compares the username and password entered by the user with the correct
	 * username and password stored in the Accounts folder.
	 * 
	 * @param username A String representing the username of the account to validate
	 * @param password S String representing the password of the account to validate
	 * 
	 * @return true if the username and password match the ones stored for
	 * the particular account and false otherwise.
	 */
	public boolean validateLogin(String username, String password)
	{
		String file = username + ".xml";
		Account_Controller acct = new Account_Controller(file);

		String user = acct.getUsername();
		String pass = acct.getPassword();
		String encryptedPassword = new Encryption_Controller().MD5(password);

		// If username and password match one in the Accounts folder
		if (username.equalsIgnoreCase(user) && encryptedPassword.equals(pass))
		{
			// Set last login to current time
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String d = dateFormat.format(date);
			acct.setLastLogin(d);

			return true;
		}
		else
			return false;
	}
}