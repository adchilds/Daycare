package views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import controllers.Config_Controller;
import controllers.Image_Controller;
import controllers.Language_Controller;

import lib.Logger;
import lib.OSProperties;
import lib.RoundedBorder;
import models.Language_Model;

/**
 * <p>Controls the about dialog view of the main program. Gives the user information
 * about the Daycare Management System program such as the author, current version,
 * links to the author's email, etc.
 * 
 * @author Adam Childs
 */
public class About_View implements ActionListener
{
	Config_Controller config = new Config_Controller();
	Image_Controller images = new Image_Controller();
	JDialog dialog = new JDialog();
	OSProperties osp = new OSProperties();
	Language_Model lang_model = new Language_Controller().getModel();

	public About_View()
	{
		
	}

	/**
	 * Displays the About_View dialog window.
	 */
	public void show()
	{
		osp = new OSProperties();
		Container container = dialog.getContentPane();
		container.add(dialogPanel());

		if (osp.isUnix())
			dialog.setSize(295, 175);
		else if (osp.isMac())
			dialog.setSize(280, 175);
		else
			dialog.setSize(220, 170); // width, height
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);
		dialog.setTitle("About D.M.S.");
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setIconImage(images.loadImage("../images/program_icon_small.png").getImage());
		dialog.setVisible(true);
	}

	/**
	 * Creates the About_View dialog window.
	 * 
	 * @return The JPanel containing all About_View window components
	 */
	private JPanel dialogPanel()
	{
		JLabel label;
		JButton button;
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JPanel textPanel = new JPanel(new FlowLayout());
	
		label = new JLabel(lang_model.getProgramVersionText() + config.getProgramVersion());
		textPanel.add(label);

		nextLine(textPanel, 0); // skip a line hack, better way to do this?

		label = new JLabel("Author: " + config.getAuthor());
		textPanel.add(label);

		nextLine(textPanel, 0); // skip a line hack, better way to do this?

		button = new JButton(images.loadImage("../images/website_icon.png"));
		button.setPreferredSize(new Dimension(25, 25));
		button.setBorder(new RoundedBorder(5));
		button.setToolTipText("Click to view the author's website");
		button.setFocusPainted(false);
		button.setActionCommand("website");
		button.addActionListener(this);
		textPanel.add(button, BorderLayout.CENTER);

		textPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		button = new JButton(images.loadImage("../images/facebook_icon.png"));
		button.setPreferredSize(new Dimension(25, 25));
		button.setBorder(new RoundedBorder(5));
		button.setToolTipText("Click to view the author's facebook profile");
		button.setFocusPainted(false);
		button.setActionCommand("facebook");
		button.addActionListener(this);
		textPanel.add(button, BorderLayout.CENTER);

		textPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		button = new JButton(images.loadImage("../images/email_icon.png"));
		button.setPreferredSize(new Dimension(25, 25));
		button.setBorder(new RoundedBorder(5));
		button.setToolTipText("Click to email the author");
		button.setFocusPainted(false);
		button.setActionCommand("email");
		button.addActionListener(this);
		textPanel.add(button, BorderLayout.CENTER);

		p.add(textPanel, BorderLayout.CENTER);

		/*
		 * OK Button
		 */
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		button = new JButton("OK");
		button.setBorder(new RoundedBorder(5));
		button.setPreferredSize(new Dimension(75, 30));
		button.setActionCommand("button");
		button.addActionListener(this);
		buttonPanel.add(button);
		p.add(buttonPanel, BorderLayout.SOUTH);
		
		return p;
	}

	/**
	 * Fills the rest of the line with a blank Box object.
	 * Mainly used for our FlowLayout to fill the rest of the line so
	 * we can wrap to the next line to add more components that
	 * should be grouped together.
	 * 
	 * @param c The Container to add the Box object to
	 * @param n The height of this component (-15 works well for empty lines)
	 */
	private void nextLine(Container c, int n)
	{
		c.add(Box.createRigidArea(new Dimension(9999, n)));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("button"))
		{
			dialog.dispose();
		} else if (e.getActionCommand().equals("facebook")) {
			if(!Desktop.isDesktopSupported())
			{
				new Logger().write("Desktop is not supported.", "ERROR");
	            return;
			}

			Desktop desktop = Desktop.getDesktop();

			if(!desktop.isSupported(Desktop.Action.BROWSE))
			{
				new Logger().write("Desktop doesn't support the browse action.", "ERROR");
				return;
			}

			try {
				URI uri = new URI(config.getAuthorFacebook());
				desktop.browse(uri);
			} catch ( Exception ex ) {
				System.err.println( ex.getMessage() );
			}
		} else if (e.getActionCommand().equals("email")) {
			if(!Desktop.isDesktopSupported())
			{
				new Logger().write("Desktop is not supported.", "ERROR");
	            return;
			}

			Desktop desktop = Desktop.getDesktop();

			if(!desktop.isSupported(Desktop.Action.MAIL))
			{
				new Logger().write("Desktop doesn't support the email action.", "ERROR");
				return;
			}

			try {
				URI uri = new URI("mailto:" + config.getAuthorEmail());
				desktop.mail(uri);
			} catch ( Exception ex ) {
				System.err.println( ex.getMessage() );
			}
		} else if (e.getActionCommand().equals("website")) {
			if(!Desktop.isDesktopSupported())
			{
				new Logger().write("Desktop is not supported.", "ERROR");
	            return;
			}

			Desktop desktop = Desktop.getDesktop();

			if(!desktop.isSupported(Desktop.Action.BROWSE))
			{
				new Logger().write("Desktop doesn't support the browse action.", "ERROR");
				return;
			}

			try {
				URI uri = new URI(config.getAuthorWebsite());
				desktop.browse(uri);
			} catch ( Exception ex ) {
				System.err.println( ex.getMessage() );
			}
		}
	}

}