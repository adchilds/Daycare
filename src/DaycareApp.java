import java.io.File;

import javax.swing.SwingUtilities;

import lib.Logger;
import controllers.Account_Controller;
import controllers.Check_In_Controller;
import controllers.Config_Controller;
import controllers.DMS_Controller;
import controllers.File_System_Controller;
import controllers.Install_Controller;
import controllers.Login_Controller;
import controllers.Product_Key_Controller;

/**
 * <p>The DaycareApp class instantiates the entire program on its own thread,
 * after making crucial checks such as if the program is installed, if an
 * account has been created, etc.
 * 
 * @author Adam Childs
 * @version 1.00
 */
class DaycareApp
{
	static DMS_Controller controller = null;

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{
					// Install the program if it hasn't been
					if (!new File(new File_System_Controller().getModel().findFile("config.xml")).exists())
					{
						new Install_Controller().showView();
						Logger.write("Program installed.", Logger.Level.INFO);
					}

					// Prompt the user to enter a product key if they haven't already
					if (!new Config_Controller().productKeySet())
					{
						Logger.write("Product Key not set, please supply the one distributed with this software.", Logger.Level.INFO);
						new Product_Key_Controller().showView();
					}

					// Prompt the user to create an account if they haven't already
					if (!new Config_Controller().accountSet())
					{
						Logger.write("No account found, please create one.", Logger.Level.INFO);
						new Account_Controller().showView();
					}

					// Open the check-in/out view
					if (new Config_Controller().getCheckInView())
						new Check_In_Controller().showView();

					// Open the main view of the program
					controller = new DMS_Controller();

					// Prompt the user to login
					new Login_Controller().showView();
				}
			}
		);
	}
}