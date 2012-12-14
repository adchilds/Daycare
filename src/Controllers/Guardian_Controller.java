package Controllers;

import java.util.ArrayList;

import Views.Child_View;
import Views.Guardian_View;

/**
 * <p>Facilitates communication between the Guardian_View class and the
 * main program. Allows the display of the Guardian_View at the appropriate
 * time.
 * 
 * @author Adam Childs
 */
public class Guardian_Controller
{
	Child_View child_view = null;
	int index = 0;

	/**
	 * <p>Instantiates the Guardian_Controller which controls communication
	 * between the Guardian_View and the Child_View classes.
	 * 
	 * @param cv The Child_View instance that created this object
	 * @param i The index of the guardian that the user is editing
	 */
	public Guardian_Controller(Child_View cv, int i)
	{
		child_view = cv;
		index = i;
	}

	/**
	 * <p>Shows the Guardian_Controller's view, which is the view that
	 * allows the user to add a new guardian, for a child, to the database.
	 * 
	 * @param r The relationship of the guardian to the child
	 * @param arr The data/information of the guardian
	 */
	public void showView(String r, ArrayList<String> arr)
	{
		new Guardian_View(this).show(r, arr);
	}

	/**
	 * <p>Sets the specified guardians information in the child view.
	 * 
	 * @param info The guardian's information
	 * @param index The guardian index in the view to set the name for (0-2)
	 */
	public void setGuardianInformation(ArrayList<String> info)
	{
		child_view.setGuardianInformation(info, index);
	}
}