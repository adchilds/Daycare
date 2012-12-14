package Controllers;

import Views.Properties_View;

/**
 * <p>Controls communication between the user and the properties dialog.
 * 
 * @author Adam Childs
 */
public class Properties_Controller
{
	
	public Properties_Controller()
	{
		
	}

	/**
	 * <p>Displays the Properties dialog onto the user's screen.
	 */
	public void showView()
	{
		new Properties_View().show();
	}
}