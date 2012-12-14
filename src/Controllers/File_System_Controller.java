package Controllers;

import Models.File_System_Model;

public class File_System_Controller
{
	File_System_Model model;

	public File_System_Controller()
	{
		this.model = new File_System_Model();
	}

	/**
	 * <p>Gets the File_System_Model instance that this class is referring to.
	 * 
	 * @return a File_System_Model instance
	 */
	public File_System_Model getModel()
	{
		return model;
	}
}