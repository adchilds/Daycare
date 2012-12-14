package Controllers;

import java.awt.Component;

import Models.Login_Model;
import Views.Login_View;

/**
 * <p>Controls logging a user into the system. Controls communication
 * between the login view and model where the user enters their username
 * and password and then has it validated to be true or false.
 * 
 * @author Adam Childs
 */
public class Login_Controller
{
	Login_View view = null;
	Login_Model model = null;

	public Login_Controller()
	{
		model = new Login_Model();
	}

	public Component showView()
	{
		view = new Login_View(this);
		return view.show();
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
		return model.validateLogin(username, password);
	}
}