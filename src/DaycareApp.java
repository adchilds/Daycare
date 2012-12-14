import java.io.File;

import javax.swing.SwingUtilities;

import lib.Logger;
import Controllers.Account_Controller;
import Controllers.Config_Controller;
import Controllers.DMS_Controller;
import Controllers.File_System_Controller;
import Controllers.Install_Controller;
import Controllers.Product_Key_Controller;

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
						new Logger().write("Program installed.", "INFO");
						System.out.println("Program installed.");
					}

					// Prompt the user to enter a product key if they haven't already
					if (!new Config_Controller().productKeySet())
					{
						new Logger().write("Product Key not set, please supply the one distributed with this software.", "INFO");
						new Product_Key_Controller().showView();
					}

					// Prompt the user to create an account if they haven't already
					if (!new Config_Controller().accountSet())
					{
						new Logger().write("No account found, please create one.", "INFO");
						new Account_Controller().showView();
					}

					// Open the main view of the program
					controller = new DMS_Controller();

					// Prompt the user to login
//					new Login_Controller().showView();
				}
			}
		);
	}
}