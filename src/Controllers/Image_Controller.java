package Controllers;

import javax.swing.ImageIcon;

import lib.Logger;

/**
 * <p>This class contains helper functions for images used throughout the program.
 * 
 * @author Adam Childs
 * @version 1.00
 */
public class Image_Controller
{
	
	public Image_Controller()
	{
		// Don't need
	}

	/**
	 * <p>Loads the specified image, given the location i
	 * @param i the location of the image
	 * @return a new ImageIcon
	 */
	public ImageIcon loadImage(String i)
	{
		try {
			return new ImageIcon(getClass().getResource(i));
		} catch (NullPointerException e) {
			new Logger().write("NullPointerException: Image \"" + i + "\" could not be found.", "ERROR");
		}
		return null;
	}
}