package Controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import lib.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * <p>Interfaces with the config XML file
 * 
 * @author Adam Childs
 * @version 1.00
 */
public class Config_Controller
{
	private SAXBuilder builder;
	private Document doc;
	private Element root;
	private String configFile = "config.xml";

	public Config_Controller()
	{
		try
		{
			builder = new SAXBuilder();
			doc = builder.build(new File(new File_System_Controller().getModel().findFile(configFile)));
			root = doc.getRootElement();
		} catch (Exception e) {
			// Program must not have been installed. Install it!
			System.out.println( "SYSTEM: Config file could not be found at: " + new File_System_Controller().getModel().findFile(configFile) );
			System.out.println( "Program files have been deleted?" );
		}
	}

	/**
	 * <p>Saves the XML file.
	 */
	private void saveFile()
	{
		try
		{
			FileWriter writer = new FileWriter(new File_System_Controller().getModel().findFile(configFile));
			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat(Format.getPrettyFormat());
			outputter.output(doc, writer); // Save the new document
			writer.close();
		} catch (IOException e) {
			new Logger().write("IOException: Could not save XML config file.", "ERROR");
		}
	}

	/**
	 * Returns whether or not the user has created an initial account
	 */
	public boolean accountSet()
	{
		return Boolean.parseBoolean(root.getChild("settings").getChildText("accountSet"));
	}

	/**
	 * <p>Sets the accountSet tag's data, given the boolean value parameter
	 * 
	 * @param b Boolean value indicating if the user has created at least one account
	 */
	public void setAccountSet(boolean b)
	{
		String s = new Boolean(b).toString();
		root.getChild("settings").getChild("accountSet").setText(s);
		new Logger().write("accountSet set to: " + s, "SYSTEM");
		saveFile();
	}

	/**
	 * Returns the text to display in the welcome message.
	 */
	public String getWelcomeText()
	{
		return root.getChild("program").getChildText("welcomeText");
	}

	/**
	 * Returns whether or not the user has entered a valid product key
	 */
	public boolean productKeySet()
	{
		return Boolean.parseBoolean(root.getChild("settings").getChildText("productKeySet"));
	}

	/**
	 * Sets the productKeySet to true or false. Has the user entered
	 * a valid product key?
	 * 
	 * @param b The boolean value indicating whether or not the user has
	 * supplied a valid product key.
	 */
	public void setProductKeySet(boolean b)
	{
		String s = new Boolean(b).toString();
		root.getChild("settings").getChild("productKeySet").setText(s);
		new Logger().write("Valid Product Key set to: " + s, "SYSTEM");
		saveFile();
	}

	/**
	 * Returns whether or not the user has selected to hide
	 * the beginning welcome message.
	 */
	public boolean getWelcomeMessage()
	{
		return Boolean.parseBoolean(root.getChild("settings").getChildText("welcomeMessage"));
	}

	/**
	 * Sets the Welcome Message to true or false. Has the user
	 * selected to not display the message on program startup?
	 */
	public void setWelcomeMessage(boolean b)
	{
		String s = new Boolean(b).toString();
		root.getChild("settings").getChild("welcomeMessage").setText(s);
		new Logger().write("Display welcome message changed to: " + s, "SYSTEM");
		saveFile();
	}

	/**
	 * Returns the Daycare Name
	 */
	public String getDaycareName()
	{
		return root.getChild("settings").getChildText("daycareName");
	}

	/**
	 * Sets the Daycare Name within the XML file.
	 */
	public void setDaycareName(String s)
	{
		root.getChild("settings").getChild("daycareName").setText(s);
		new Logger().write("Daycare name changed to: " + s, "SYSTEM");
		saveFile();
	}

	/* Program Information */
	public String getAuthor()
	{
		return root.getChild("program").getChildText("author");
	}

	public String getAuthorEmail()
	{
		return root.getChild("program").getChildText("authoremail");
	}

	public String getAuthorFacebook()
	{
		return root.getChild("program").getChildText("authorfacebook");
	}

	public String getProgramName()
	{
		return root.getChild("program").getChildText("programName");
	}

	public String getProgramVersion()
	{
		return root.getChild("program").getChildText("version");
	}

	public String getProgramLastUpdate()
	{
		return root.getChild("program").getChildText("lastupdate");
	}

	public String getAuthorWebsite()
	{
		return root.getChild("program").getChildText("authorwebsite");
	}
}