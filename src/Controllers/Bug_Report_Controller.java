package controllers;

import java.io.File;

import lib.Logger;
import models.Bug_Report_Model;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import views.Bug_Report_View;

/**
 * Handles emailing within the Daycare Management System.
 * 
 * @author Adam Childs
 * @since 1.09
 */
public class Bug_Report_Controller
{
	private Bug_Report_Model model = null;
	private Bug_Report_View view = null;
	private String configFile = "config.xml";

	public Bug_Report_Controller()
	{
		this.model = new Bug_Report_Model(getDaycareEmail());
		this.view = new Bug_Report_View(this.model);
	}

	public void showView()
	{
		this.view.show();
	}

	/**
	 * <p>Get's the current email address used by the Daycare Management System
	 * administrators for things like Bug reporting.
	 */
	private String getDaycareEmail()
	{
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try
		{
			doc = builder.build(new File(new File_System_Controller().getModel().findFile(configFile)));
		} catch (Exception e) {
			System.err.println(e);
			Logger.write("Could not load config file in Bug_Report_Controller class.", Logger.Level.ERROR);
		}
		Element root = doc.getRootElement();
		
		return root.getChild("program").getChildText("programEmail");
	}
}