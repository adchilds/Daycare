package Controllers;

import java.awt.Component;

import Views.Employee_View;

/**
 * <p>Facilitates communication between the Employee_View class and the
 * main program. Allows the display of the Employee_View at the appropriate
 * time.
 * 
 * @author Adam Childs
 */
public class Employee_Controller
{
	
	Employee_View view = null;

	public Employee_Controller()
	{
		
	}

	/**
	 * <p>Shows the Employee_Controller's view, which is the view that
	 * allows the user to add a new employee to the database.
	 * 
	 * @return A java.awt.Component (JInternalFrame)
	 */
	public Component showView()
	{
		view = new Employee_View(this);
		return view.show();
	}
}