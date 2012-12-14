package Views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Controllers.Config_Controller;
import Controllers.Image_Controller;
import lib.RoundedBorder;

/**
 * <p>Contains the user interface which allows the user to edit program
 * variables such as the displaying of the welcome message, the daycare
 * name, etc.
 * 
 * @author Adam Childs
 */
public class Properties_View implements ActionListener
{
	Config_Controller config_file = new Config_Controller();
	JCheckBox welcomeMessage = null;
	JDialog dialog = new JDialog();
	JTextField textfield = null;
	Image_Controller images = new Image_Controller();

	public Properties_View()
	{
		
	}

	/**
	 * <p>Displays the Properties dialog onto the user's screen.
	 */
	public void show()
	{
		Container c = dialog.getContentPane();
		c.setLayout(new BorderLayout());
		
		c.add(northPanel(), BorderLayout.CENTER);
		c.add(southPanel(), BorderLayout.SOUTH);

		dialog.setIconImage(images.loadImage("../images/menubar/properties_icon.png").getImage());
		dialog.setSize(300, 175);
		dialog.setLocationRelativeTo(null);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.setTitle("Properties");
		dialog.setVisible(true);
	}

	private JPanel northPanel()
	{
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 10));

		JLabel label;
		label = new JLabel("Daycare Name:");
		textfield = new JTextField();
		textfield.setText(config_file.getDaycareName());
		textfield.setPreferredSize(new Dimension(150, 20));
		p.add(label);
		p.add(textfield);

		nextLine(p);

		welcomeMessage = new JCheckBox("Display Welcome Message at startup");
		p.add(welcomeMessage);

		return p;
	}

	private JPanel southPanel()
	{
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton button;

		button = new JButton("Save");
		button.setActionCommand("save");
		button.addActionListener(this);
		button.setBorder(new RoundedBorder(5));
		button.setPreferredSize(new Dimension(75, 30));
		p.add(button);

		button = new JButton("Cancel");
		button.setActionCommand("cancel");
		button.addActionListener(this);
		button.setBorder(new RoundedBorder(5));
		button.setPreferredSize(new Dimension(75, 30));
		p.add(button);

		return p;
	}

	/**
	 * Fills the rest of the line with a blank Box object.
	 * Mainly used for our FlowLayout to fill the rest of the line so
	 * we can wrap to the next line to add more components that
	 * should be grouped together.
	 * 
	 * @param c The Container to add the Box object to
	 */
	private void nextLine(Container c)
	{
		c.add(Box.createRigidArea(new Dimension(9999, -15)));
	}

	/*
	 * Save the users preferences/properties
	 */
	private void save()
	{
		if (!(config_file.getDaycareName().equals(textfield.getText())))
			config_file.setDaycareName(textfield.getText());
		if (welcomeMessage.isSelected())
			config_file.setWelcomeMessage(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("save"))
		{
			save();
			dialog.dispose();
			return;
		}
		if (e.getActionCommand().equals("cancel"))
		{
			dialog.dispose();
			return;
		}
	}
}