package controllers;

import views.Product_Key_View;

/**
 * <p>Facilitates communication between the Product_Key_View and the
 * main program. Allows the user to validate their copy of this
 * program by entering the product key distributed with their copy
 * of Daycare Management System.
 * 
 * @author Adam Childs
 */
public class Product_Key_Controller
{
	
	public Product_Key_Controller()
	{
		
	}

	/**
	 * <p>Initiates a method that displays the dialog prompting the user to
	 * enter the product key for their copy of Daycare Management System.
	 */
	public void showView()
	{
		new Product_Key_View(this).show();
	}
}