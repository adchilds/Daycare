package models;

import java.io.File;

import lib.Logger;
import lib.OSProperties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import controllers.File_System_Controller;


public class Language_Model
{
	private SAXBuilder builder;
	private Document doc;
	private Element root;
	private String languagesFile = "language.xml";
	private String language = ""; // User's default system language

	public Language_Model()
	{
		builder = new SAXBuilder();
		try
		{
			doc = builder.build(new File(new File_System_Controller().getModel().findFile(languagesFile)));
		} catch (Exception e) {
			try {
				doc = builder.build(new File("src/installation/language.xml"));
			} catch (Exception ex) {
				System.out.println("Something went wrong!");
				System.err.println(ex);
				System.exit(1);
			}
		}
		root = doc.getRootElement();

		language = new OSProperties().getLanguage();

		// If the program doesn't support the user's default language, set language to English
		if (root.getChild("available_languages").getChild(language) == null)
		{
			language = "en";
			new Logger().write("Language not supported! Defaulting to English.", "INFO");
		}
	}

	/**
	 * @return "Daycare Management System"
	 */
	public String getProgramNameText()
	{
		return root.getChild("dms_text").getChildText(language);
	}

	/**
	 * @return "Program Version: "
	 */
	public String getProgramVersionText()
	{
		return root.getChild("program_version_text").getChildText(language);
	}

	/**
	 * <p>Gets the specified text for the install menu in the appropriate language.
	 * @param choice
	 * @return
	 */
	public String getInstallText(String choice)
	{
		return root.getChild("installation").getChild(choice).getChildText(language);
	}

	/**
	 * <p>Gets the specified text for the menubar in the appropriate language.
	 * @param choice
	 * @return
	 */
	public String getMenuBarText(String choice)
	{
		return root.getChild("menubar").getChild(choice).getChildText(language);
	}

	/**
	 * <p>Gets the specified text for the menubar in the appropriate language.
	 * @param choice The menubar to get the text for
	 * @param m true for "menuitem" or false for "menu"
	 * @return
	 */
	public String getMenuBarText(String choice, boolean b, String m2)
	{
		if (b)
			return root.getChild("menubar").getChild(choice).getChild("menuitem").getChild(m2).getChildText(language);
		else
			return root.getChild("menubar").getChild(choice).getChild("menu").getChildText(language);

	}
}