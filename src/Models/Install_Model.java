package models;

import controllers.File_System_Controller;
import controllers.Install_Controller;

/**
 * <p>Install_Model houses methods related to the installation process
 * of Daycare Management System.
 * 
 * @author Adam Childs
 */
public class Install_Model
{
	Install_Controller controller = null;

	/**
	 * Instantiates a new Install_Model object.
	 * 
	 * @param parent The Install_Controller object that instantiates this object
	 */
	public Install_Model(Install_Controller parent)
	{
		controller = parent;
	}

	/**
	 * Installs the Daycare Management System files onto the user's system
	 * at the specified directory path.
	 * 
	 * @param directory The directory where the files should be installed
	 */
	public void installFiles(String directory)
	{
		File_System_Model fs = new File_System_Controller().getModel();
		boolean finished = false;

		finished = fs.createDirectory(directory); // create the initial directory
		finished = fs.createDirectory(directory + "Accounts");
		finished = fs.createDirectory(directory + "Children");
		finished = fs.createDirectory(directory + "Employees");
		finished = fs.createDirectory(directory + "logs");
		finished = fs.createFile(directory + "account.txt");
		finished = fs.createFile(directory + "config.xml");
		finished = fs.createFile(directory + "README.txt");
		finished = fs.createFile(directory + "changelog.txt");
		finished = fs.createFile(directory + "license.txt");
		finished = fs.createFile(directory + "forms.txt");
		finished = fs.createFile(directory + "logs/log.txt");

		if (finished)
		{
			fs.populateFileFromFile(directory + "account.txt", "../installation/account.txt");
			fs.populateFileFromFile(directory + "config.xml", "../installation/config.txt");
			fs.populateFileFromFile(directory + "README.txt", "../installation/README.txt");
			fs.populateFileFromFile(directory + "changelog.txt", "../installation/changelog.txt");
			fs.populateFileFromFile(directory + "license.txt", "../installation/license.txt");
			fs.populateFileFromFile(directory + "forms.txt", "../installation/forms.txt");
		} else {
			System.out.println( "Something went wrong installing, program already installed?" );
		}
	}
}