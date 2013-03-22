package models;

import lib.Logger;
import lib.OSProperties;
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
	OSProperties osp = new OSProperties();

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
		finished = fs.createDirectory(directory + "logs");
		finished = fs.createFile(directory + "logs/log.txt");
		Logger.write("Created log.txt", Logger.Level.INFO);
		finished = fs.createDirectory(directory + "Accounts");
		Logger.write("Created directory: Accounts", Logger.Level.INFO);
		finished = fs.createDirectory(directory + "Children");
		Logger.write("Created directory: Children", Logger.Level.INFO);
		finished = fs.createDirectory(directory + "Employees");
		Logger.write("Created directory: Employees", Logger.Level.INFO);
		finished = fs.createFile(directory + "account.txt");
		Logger.write("Created file: account.txt", Logger.Level.INFO);
		finished = fs.createFile(directory + "config.xml");
		Logger.write("Created file: config.xml", Logger.Level.INFO);
		finished = fs.createFile(directory + "README.txt");
		Logger.write("Created file: README.txt", Logger.Level.INFO);
		finished = fs.createFile(directory + "changelog.txt");
		Logger.write("Created file: changelog.txt", Logger.Level.INFO);
		finished = fs.createFile(directory + "license.txt");
		Logger.write("Created file: license.txt", Logger.Level.INFO);
		finished = fs.createFile(directory + "forms.txt");
		Logger.write("Created file: forms.txt", Logger.Level.INFO);
//		finished = fs.copyFileToDirectory("src" + osp.getSeparator() + "installation" + osp.getSeparator() + "language.xls", directory + "language.xls");
//		Logger.write("Created file: language.xls", Logger.Level.INFO);

		if (finished)
		{
			fs.populateFileFromFile(directory + "account.txt", "installation/account.txt");
			Logger.write("Populated file: account.txt", Logger.Level.INFO);
			fs.populateFileFromFile(directory + "config.xml", "installation/config.txt");
			Logger.write("Populated file: config.xml", Logger.Level.INFO);
			fs.populateFileFromFile(directory + "README.txt", "installation/README.txt");
			Logger.write("Populated file: README.txt", Logger.Level.INFO);
			fs.populateFileFromFile(directory + "changelog.txt", "installation/changelog.txt");
			Logger.write("Populated file: changelog.txt", Logger.Level.INFO);
			fs.populateFileFromFile(directory + "license.txt", "installation/license.txt");
			Logger.write("Populated file: license.txt", Logger.Level.INFO);
			fs.populateFileFromFile(directory + "forms.txt", "installation/forms.txt");
			Logger.write("Populated file: forms.txt", Logger.Level.INFO);
		} else {
			System.out.println( "Something went wrong installing, program already installed?" );
		}
	}
}