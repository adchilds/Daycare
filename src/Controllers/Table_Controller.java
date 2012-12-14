package Controllers;

import javax.swing.JDesktopPane;

import Views.Table_View;

/**
 * <p>Controls communication between the Table_View and Table_Model class,
 * which will be displaying the children or employees in the database.
 * 
 * @author Adam Childs
 */
public class Table_Controller
{
	JDesktopPane desktop; // The JDesktopPane that we'll be adding the table view to

	public Table_Controller(JDesktopPane d)
	{
		desktop = d;
	}

	/**
	 * <p>Displays a menu, prompting the user to select which database
	 * table that they would like to view (Children or Employees).
	 */
	public void showMenu()
	{
		new Table_View(desktop).showMenu();
	}

	/**
	 * <p>Shows either the child or employee database, depending on the
	 * value in the parameter.
	 * 
	 * @param database A byte value representing either children or employees
	 */
	public void showView(byte database)
	{
		new Table_View(desktop).showTable(database);
	}
}