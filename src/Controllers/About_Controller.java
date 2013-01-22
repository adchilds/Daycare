package controllers;

import views.About_View;

/**
 * <p>Facilitates communication between the About_View class and the
 * main program. Allows the display of the About_View at the appropriate
 * time.
 * 
 * @author Adam Childs
 */
public class About_Controller
{

	public About_Controller()
	{
		
	}

	/**
	 * <p>Shows the About_Controller's view, which is the view that
	 * gives the user information about the Daycare Management
	 * System program, and the developers.
	 */
	public void showView()
	{
		new About_View().show();
	}
}