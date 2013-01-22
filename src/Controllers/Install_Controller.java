package controllers;

import views.Install_View;
import models.Install_Model;

/**
 * <p>Facilitates communication between the Install_View and
 * Install_Model objects. Facilitates the entire installation
 * process in order to install Daycare Management System on the
 * user's computer.
 * 
 * @author Adam Childs
 */
public class Install_Controller
{

	public Install_Controller()
	{
		
	}

	public void showView()
	{
		new Install_View(this).show();
	}

	/**
	 * <p> Installs the needed program files to the supplied directory.
	 * 
	 * @param directory The directory to install the files to
	 */
	public void installFiles(String directory)
	{
		System.out.println( "Installing files..." );
		
		Install_Model model = new Install_Model(this);
		model.installFiles(directory);

		System.out.println( "Complete!" );
	}
}