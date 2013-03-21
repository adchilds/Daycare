package controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import lib.Logger;
import lib.OSProperties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import views.Account_View;

/**
 * Facilitates communication between the Account_View class and the
 * main program. Houses internal methods related to querying the
 * user account files.
 * 
 * @author Adam Childs
 */
public class Account_Controller
{
	Account_View view = null;
	Document doc;
	Element root;
	OSProperties osp = new OSProperties();
	SAXBuilder builder;
	String accountFile;

	/**
	 * Default constructor, used for displaying the add new account dialog.
	 * 
	 * <p><b>NOTE</b>: If instantiating the class through this constructor, do not try
	 * to use any methods besides showView().
	 */
	public Account_Controller()
	{
		
	}

	/**
	 * Instantiates a new Account_Controller object with
	 * specified account filename.
	 * 
	 * @param filename The account filename to query
	 */
	public Account_Controller(String filename)
	{
		accountFile = "Accounts" + osp.getSeparator() + filename;

		try
		{
			builder = new SAXBuilder();
			doc = builder.build(new File(new File_System_Controller().getModel().findFile(accountFile)));
			root = doc.getRootElement();
		} catch (Exception e) {
			Logger.write("Account file could not be found.", Logger.Level.ERROR);
		}
	}

	/**
	 * Displays the Add New Account screen to the user
	 */
	public void showView()
	{
		view = new Account_View();
		view.show();
	}

	/**
	 * Saves the XML file.
	 */
	private void saveFile()
	{
		try
		{
			FileWriter writer = new FileWriter(new File_System_Controller().getModel().findFile(accountFile));
			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat(Format.getPrettyFormat());
			outputter.output(doc, writer); // Save the new document
		} catch (IOException e) {
			Logger.write("IOException while writing new XML file", Logger.Level.ERROR);
		}
	}

	public void setUsername(String n)
	{
		root.getChild("username").setText(n);
		Logger.write("Account username set to " + n, Logger.Level.SYSTEM);
		saveFile();
	}

	public String getUsername()
	{
		return root.getChildText("username");
	}

	public void setPassword(String p)
	{
		root.getChild("password").setText(p);
		Logger.write("Account password set to " + p, Logger.Level.SYSTEM);
		saveFile();
	}

	public String getPassword()
	{
		return root.getChildText("password");
	}

	public void setLastLogin(String l)
	{
		root.getChild("lastLogin").setText(l);
		Logger.write("Account lastlogin set to " + l, Logger.Level.SYSTEM);
		saveFile();
	}

	public String toString()
	{
		String s = "\tACCOUNT_CONTROLLER:";
		s += "\n\t\t";
		s += accountFile;
		
		return s;
	}
}