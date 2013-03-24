package views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import lib.RoundedBorder;
import lib.SpringUtilities;
import models.Language_Model;
import controllers.Config_Controller;
import controllers.Image_Controller;
import controllers.Language_Controller;

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
	JCheckBox welcomeMessage = null, checkinView = null;
	JDialog dialog = new JDialog();
	JTextField textfield = null;
	Image_Controller images = new Image_Controller();
	Language_Model lang_model = new Language_Controller().getModel();

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

		dialog.setIconImage(images.loadImage("Images/menubar/properties_icon.png").getImage());
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.setTitle( lang_model.getValue(161) );
		dialog.setVisible(true);
	}

	private JPanel northPanel()
	{
		JPanel p = new JPanel(new SpringLayout());
		String[] labels = { lang_model.getValue(162) +":", lang_model.getValue(163) + ":", lang_model.getValue(164) + ":" };
		int numPairs = labels.length;
		JLabel label;

		label = new JLabel(labels[0], JLabel.TRAILING);
		p.add(label);
		textfield = new JTextField(15);
		textfield.setText(config_file.getDaycareName());
		label.setLabelFor(textfield);
		p.add(textfield);

		label = new JLabel(labels[1], JLabel.TRAILING);
		p.add(label);

		welcomeMessage = new JCheckBox("");

		Config_Controller config_file = new Config_Controller();
		if (config_file.getWelcomeMessage()) // if welcome message is to be shown
			welcomeMessage.setSelected(true);
		else
			welcomeMessage.setSelected(false);

		label.setLabelFor(welcomeMessage);
		p.add(welcomeMessage);

		label = new JLabel(labels[2], JLabel.TRAILING);
		p.add(label);

		checkinView = new JCheckBox("");

		if (config_file.getCheckInView()) // if checkin-in/out is to be shown
			checkinView.setSelected(true);
		else
			checkinView.setSelected(false);

		label.setLabelFor(checkinView);
		p.add(checkinView);

		// Lay out the panel
		SpringUtilities.makeCompactGrid(p,
		                                numPairs, 2, //rows, cols
		                                3, 0,        //initX, initY
		                                5, 5);       //xPad, yPad

		return p;
	}

	private JPanel southPanel()
	{
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton button;

		button = new JButton( lang_model.getValue(15) );
		button.setActionCommand("save");
		button.addActionListener(this);
		button.setBorder(new RoundedBorder(5));
		button.setPreferredSize(new Dimension(75, 30));
		p.add(button);

		button = new JButton( lang_model.getValue(11) );
		button.setActionCommand("cancel");
		button.addActionListener(this);
		button.setBorder(new RoundedBorder(5));
		button.setPreferredSize(new Dimension(75, 30));
		p.add(button);

		return p;
	}

	/*
	 * Save the users preferences/properties
	 */
	private void save()
	{
		if (!config_file.getDaycareName().equals(textfield.getText()))
			config_file.setDaycareName(textfield.getText());
		if (welcomeMessage.isSelected())
			config_file.setWelcomeMessage(true);
		else
			config_file.setWelcomeMessage(false);
		if (checkinView.isSelected())
			config_file.setCheckInView(true);
		else
			config_file.setCheckInView(false);
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