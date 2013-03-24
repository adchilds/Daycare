package models;

import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JTextField;

/**
 * <p>Contains all methods related to checking children
 * and/or employees in/out of the daycare daily.
 * 
 * @author Adam Childs
 * @since 03/22/2013
 */
public class Check_In_Model
{
	
	public Check_In_Model()
	{
		
	}

	/**
	 * <p>Sets the maximum time allowed that a user who has
	 * checked in may stay checked in. This method is available
	 * so that checked in users will automatically be checked out
	 * after the specified time frame so that daycare providers
	 * will not over-charge families or over-pay employees who have
	 * forgotten to check out for the day.
	 * 
	 * @param h Maximum hours allowed
	 * @param m Maximum minutes allowed
	 * @param s Maximum seconds allowed
	 */
	public void setMaxTimeAllowed(int h, int m, int s)
	{
		
	}

	/**
	 * <p>Searches the checkedout lists to see if they contain the
	 * specified string. If so, they stay in the list. If
	 * not, they are removed from the list and not displayed
	 * on screen to the user.
	 * 
	 * @param listmodel The DefaultListModel to iterate over
	 * @param temp The temp ArrayList to add non-matching objects to
	 * @param textfield The JTextField instance being typed into
	 */
	public void search(DefaultListModel listmodel, ArrayList<Object> temp, JTextField textfield)
	{
		String s = textfield.getText();

		for (Object o : listmodel.toArray()) // All in listmodel
		{
			if (((String)o).toLowerCase().contains(s.toLowerCase())) // If a name contains the typed string
			{
				// Keep element in listmodel
				
			} else {
				// Remove element from listmodel
				if (!temp.contains(o))
					temp.add(o);
				listmodel.removeElement(o);
			}
		}
		for (Object o : temp.toArray()) // All in temp
		{
			if (((String)o).toLowerCase().contains(s.toLowerCase())) // If a name contains the typed string
			{
				// Remove element from temp
				if (temp.contains(o))
					temp.remove(o);
					
				if (!listmodel.contains(o))
					listmodel.addElement(o);
			} else {
				// Keep element in temp
					
			}
		}
	}

}