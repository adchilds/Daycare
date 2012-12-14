package Views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;

import lib.OSProperties;
import lib.RoundedBorder;
import lib.SpringUtilities;
import Controllers.Account_Controller;
import Controllers.Config_Controller;
import Controllers.Encryption_Controller;
import Controllers.File_System_Controller;
import Controllers.Image_Controller;
import Models.File_System_Model;

public class Account_View implements ActionListener
{
	Config_Controller config = new Config_Controller();
	JDialog dialog = new JDialog();
	Image_Controller images = new Image_Controller();
	JLabel feedbackLabel;
	JPanel southPanel;
	JPasswordField firstPass, secondPass;
	JTextField username;
	OSProperties osp = null;

	public Account_View()
	{
		
	}

	public void show()
	{
		osp = new OSProperties();

		Container container = dialog.getContentPane();
		container.add(dialogPanel());

		if (osp.isUnix())
			dialog.setSize(275, 230);
		else if (osp.isMac())
			dialog.setSize(275, 230); // TODO: Fix to correctly size on Mac OS
		else
			dialog.setSize(275, 230); // width, height

		dialog.setIconImage(images.loadImage("../images/program_icon_small.png").getImage());
		dialog.setLocationRelativeTo(null);
		dialog.setTitle( "Create New Account" );
		dialog.setModal(true);
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}

	private JPanel dialogPanel()
	{
		JButton button;
		JLabel label;
		JPanel northPanel, centerPanel;
		JPanel panel = new JPanel(new BorderLayout());

		// northPanel
		northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label = new JLabel( "Create New Account" );
		northPanel.add(label);

		// centerPanel
		centerPanel = new JPanel(new BorderLayout());
		centerPanel.setPreferredSize(new Dimension(250, 150));

		JPanel centerPanelc = new JPanel(new SpringLayout());
		JPanel centerPanels = new JPanel(new FlowLayout(FlowLayout.CENTER));

		/*
		 * Username field
		 */
		label = new JLabel( "Username:", JLabel.TRAILING );
		centerPanelc.add(label);
		username = new JTextField();
		username.setPreferredSize(new Dimension(190, 25));
		centerPanelc.add(username);

		label = new JLabel( "Password:", JLabel.TRAILING );
		centerPanelc.add(label);
		firstPass = new JPasswordField();
		firstPass.setPreferredSize(new Dimension(191, 25));
		centerPanelc.add(firstPass);
		
		label = new JLabel( "Re-type Password:", JLabel.TRAILING );
		centerPanelc.add(label);
		secondPass = new JPasswordField();
		secondPass.setPreferredSize(new Dimension(145, 25));
		centerPanelc.add(secondPass);

		SpringUtilities.makeCompactGrid(centerPanelc, 3, 2, //rows, cols
		        6, 6, //initX, initY
		        6, 6); //xPad, yPad

		feedbackLabel = new JLabel(" ");
		centerPanels.add(feedbackLabel);

		centerPanel.add(centerPanelc, BorderLayout.CENTER);
		centerPanel.add(centerPanels, BorderLayout.SOUTH);

		// southPanel
		southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		/*
		 * Create account Button
		 */
		southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		button = new JButton("Create");
		button.setBorder(new RoundedBorder(5));
		button.setPreferredSize(new Dimension(75, 30));
		button.setToolTipText("Create the new account");
		button.setFocusable(true);
		button.setActionCommand("create");
		button.addActionListener(this);
		southPanel.add(button);

		/*
		 * Cancel Button
		 */
		button = new JButton("Cancel");
		button.setBorder(new RoundedBorder(5));
		button.setPreferredSize(new Dimension(75, 30));
		button.setToolTipText("Cancels new account creation process");
		button.setFocusable(true);
		button.setActionCommand("cancel");
		button.addActionListener(this);
		southPanel.add(button);

		// Add all panels to the main panel
		panel.add(northPanel, BorderLayout.NORTH);
		panel.add(centerPanel, BorderLayout.CENTER);
		panel.add(southPanel, BorderLayout.SOUTH);
		return panel;
	}

	/*
	 * Checks if the supplied passwords in the two JPasswordFields
	 * match or not.
	 */
	private boolean passwordsMatch()
	{
		char[] pass1 = firstPass.getPassword();
		char[] pass2 = secondPass.getPassword();
		boolean match = false;

		if (pass1.length != pass2.length)
			return false;

		for (int i = 0; i < pass1.length; i++)
			if (pass1[i] == pass2[i])
				match = true;
			else
				return false;
		return match;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("create"))
		{
			File_System_Model fs = new File_System_Controller().getModel();
			String file = fs.getDefaultDirectory() + "Accounts" + osp.getSeparator() + username.getText() + ".xml";

			// Have they filled out all three fields?
			if (firstPass.getPassword().length == 0 || secondPass.getPassword().length == 0 || username.getText().length() == 0)
			{
				feedbackLabel.setText("You must fill all fields!");
				feedbackLabel.setForeground(Color.RED);
				return;
			}

			// Does the account name already exist?
			if (new File(file).exists())
			{
				feedbackLabel.setText("Username already registered.");
				feedbackLabel.setForeground(Color.RED);
				return;
			}

			// Do the passwords match?
			if (!passwordsMatch())
			{
				feedbackLabel.setText("Passwords do not match!");
				feedbackLabel.setForeground(Color.RED);
				return;
			} else {
				feedbackLabel.setText("Account successfully created.");
				feedbackLabel.setForeground(new Color(0, 139, 0));
			}

			// Is this the first account?
			if (!config.accountSet())
			{
				config.setAccountSet(true);
			}

			username.setEnabled(false);
			firstPass.setEnabled(false);
			secondPass.setEnabled(false);
			southPanel.removeAll();

			JButton button = new JButton("OK");
			button.setBorder(new RoundedBorder(5));
			button.setPreferredSize(new Dimension(75, 30));
			button.setActionCommand("ok");
			button.addActionListener(this);
			button.requestFocus();
			southPanel.add(button);
			southPanel.revalidate();
			southPanel.repaint();

			boolean finished = fs.createFile(file);
			if (finished)
				fs.populateFileFromFile(file, "../installation/account.txt");
			else
				System.out.println( "Something went wrong when adding the new account..." );

			Account_Controller acct = new Account_Controller(username.getText() + ".xml");
			// Set the username
			acct.setUsername(username.getText());
			// Set the password
			acct.setPassword(new Encryption_Controller().MD5(new String(firstPass.getPassword())));
			// Set last login to current time
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String d = dateFormat.format(date);
			acct.setLastLogin(d);
		} else if (e.getActionCommand().equals("cancel")) {
			dialog.dispose();
		} else if (e.getActionCommand().equals("ok")) {
			dialog.dispose();
		}
	}
}