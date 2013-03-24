package controllers;

import models.Check_In_Model;
import views.Check_In_View;

/**
 * <p>Handles checking children and employees in/out of
 * the daycare facility each day. Logs the amount of time
 * weekly that each child or employee is present at the
 * daycare.
 * 
 * @author Adam Childs
 * @since 03/22/2013
 */
public class Check_In_Controller
{
	private Check_In_Model model;
	private Check_In_View view;

	public Check_In_Controller()
	{
		this.model = new Check_In_Model();
		this.view = new Check_In_View(this.model);
	}

	public void showView()
	{
		this.view.show();
	}

	public Check_In_Model getModel()
	{
		return this.model;
	}
}