package controllers;

import models.Language_Model;

public class Language_Controller
{
	Language_Model model;

	public Language_Controller()
	{
		model = new Language_Model();
	}

	public Language_Model getModel()
	{
		return model;
	}
}