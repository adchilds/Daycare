package controllers;

import java.awt.Component;

import views.Child_View;


/**
 * <p>Facilitates communication between the Child_View class and the
 * main program. Allows the display of the Child_View at the appropriate
 * time.
 * 
 * @author Adam Childs
 */
public class Child_Controller
{
	Child_View view = null;

	public Child_Controller()
	{
		
	}

	/**
	 * <p>Shows the Child_Controller's view, which is the view that
	 * allows the user to add a new child to the database.
	 * 
	 * @return A java.awt.Component (JInternalFrame)
	 */
	public Component showView()
	{
		view = new Child_View(this);
		return view.show();
	}

	public String toString()
	{
		String s = "\tCHILD_CONTROLLER:";
		s += "\n\t\t";
		if (view != null)
		{
			s += "CHILD_VIEW:";
			s += view;
		}
		return s;
	}
}