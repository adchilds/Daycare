package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import lib.FixedSizeDocument;
import lib.Logger;
import lib.OSProperties;
import lib.RoundedBorder;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import controllers.Child_Controller;
import controllers.File_System_Controller;
import controllers.Guardian_Controller;
import controllers.Image_Controller;


/**
 * <p>Controls the Add New Child view of the program. Displays a form
 * to the user, allowing them to add a child to the database.
 * 
 * @author Adam Childs
 */
public class Child_View implements ActionListener, FocusListener, InternalFrameListener, KeyListener
{
	ArrayList<String> content = new ArrayList<String>();
	@SuppressWarnings("unchecked")
	ArrayList<String>[] parentInfo = (ArrayList<String>[])new ArrayList[3];
	boolean male, doneButtonEnabled = false;
	boolean[] checked = new boolean[7];
	Child_Controller controller = null;
	Container c = null;
	File file;
	Image_Controller images = new Image_Controller();
	ImageIcon image = images.loadImage("../images/no_photo_icon.png");
	int currentPanel = 1;
	JButton childImage = null, doneButton = null;
	JCheckBox[] childCheckBox = new JCheckBox[7];
	JComboBox monthBox, dayBox, yearBox, rateBox;
	JComboBox[] guardianComboBox = new JComboBox[3];
	JComboBox[] stateBox = new JComboBox[4];
	JInternalFrame iframe = null;
	JRadioButton sexMale, sexFemale;
	JTextArea[] childTextArea = new JTextArea[2];
	JTextField[] childTextField = new JTextField[12];
	JTextField[] parentNameTextField = null;
	JTextField[] scheduleField = new JTextField[7];
	Object month, day, year, state, quote = "Weekly";
	OSProperties osp = new OSProperties();
	Point loc;
	final String[] states = { "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
			"HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
			"MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
			"NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
			"SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"
	};
	final String[] rates = { "Hourly", "Daily", "Weekly", "Monthly", "Yearly" };
	final String[] months = { "January", "February", "March", "April", "May",
			"June", "July", "August", "September", "October",
			"November", "December"
	};

	public Child_View(Child_Controller parent)
	{
		controller = parent;
		iframe = new JInternalFrame();
	}

	public JInternalFrame show()
	{
		c = iframe.getContentPane();
		try {
			iframe.setSelected(true);
		} catch (PropertyVetoException e1) {
			e1.printStackTrace();
		}
		iframe.setTitle("Add New Child");

		iframe.add(firstScreen());

		iframe.pack();
//		iframe.setLocation(loc);
		iframe.setMaximizable(false);
		iframe.setClosable(true);
		iframe.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		iframe.addInternalFrameListener(this);
		iframe.setFrameIcon(new Image_Controller().loadImage("../images/childframe/child_icon.png"));
		iframe.setVisible(true);

		return iframe;
	}

	/**
	 * <p>Displays the first child information screen
	 */
	public JPanel firstScreen()
	{
		iframe.setTitle("Add New Child - Page 1/4");

		JPanel p = new JPanel(new BorderLayout());
			p.setBorder(BorderFactory.createTitledBorder("Child Information"));
		JPanel panel;
		JLabel label;

		int counter = 0;
		String[] days = new String[31];
		for (int m = 1; m < 32; m++)
		{
			days[counter] = Integer.toString(m);
			counter++;
		}
		String[] years = new String[41];
		counter = 0;
		for (int i = 1980; i < 2021; i++)
		{
			years[counter] = Integer.toString(i);
			counter++;
		}
		counter = 0;

		/*
		 * North Panel
		 */
		JPanel northPanel = new JPanel(new BorderLayout());

		JPanel imagePanel = new JPanel();
		childImage = new JButton();
		childImage.setPreferredSize(new Dimension(95, 110));
		childImage.setContentAreaFilled(false);
		childImage.setOpaque(false);
		childImage.setIcon(image);
		childImage.setForeground(Color.GRAY);
		childImage.setActionCommand( "childimage" );
		childImage.addActionListener(this);

		imagePanel.add(childImage);
		northPanel.add(imagePanel, BorderLayout.WEST);

		// North.Center Panel
		JPanel northCenterPanel = new JPanel(new GridLayout(0, 1));
		JPanel general = new JPanel(new BorderLayout());
		general.setBorder(BorderFactory.createTitledBorder("General"));

		// Panel One
		// Child Name
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		label = new JLabel( "First Name:" );
		panel.add(label);
		
		childTextField[0] = new JTextField(8);
		childTextField[0].addKeyListener(this);
		panel.add(childTextField[0]);

		label = new JLabel( "Middle Initial:" );
		panel.add(label);

		childTextField[1] = new JTextField(2);
		panel.add(childTextField[1]);

		label = new JLabel( "Last Name:" );
		panel.add(label);

		childTextField[2] = new JTextField(8);
		childTextField[2].addKeyListener(this);
		panel.add(childTextField[2]);

		general.add(panel, BorderLayout.NORTH);

		// Panel Two
		// Child Birth date
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		label = new JLabel( "Birthdate:" );
		panel.add(label);

		monthBox = new JComboBox(months);
		panel.add(monthBox);

		dayBox = new JComboBox(days);
		panel.add(dayBox);

		yearBox = new JComboBox(years);
		panel.add(yearBox);

		general.add(panel, BorderLayout.CENTER);

		// Panel Three
		// Sex
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		label = new JLabel(images.loadImage("../images/user_male_icon.png"));
		panel.add(label);

		sexMale = new JRadioButton("Male", true);
		sexMale.setActionCommand("sexMale");
		sexMale.addActionListener(this);
		panel.add(sexMale);

		label = new JLabel(images.loadImage("../images/user_female_icon.png"));
		panel.add(label);

		sexFemale = new JRadioButton("Female", false);
		sexFemale.setActionCommand("sexFemale");
		sexFemale.addActionListener(this);
		panel.add(sexFemale);

		general.add(panel, BorderLayout.SOUTH);
		northCenterPanel.add(general);
		northPanel.add(northCenterPanel, BorderLayout.CENTER);
		p.add(northPanel, BorderLayout.NORTH);

		/*
		 * Center panel
		 */	
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel address = new JPanel(new BorderLayout());
		address.setBorder(BorderFactory.createTitledBorder("Address"));

		/*
		 * Child Address
		 */
		// North panel
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		label = new JLabel( "Street:" );
		panel.add(label);

		childTextField[3] = new JTextField(21);
		panel.add(childTextField[3]);

		label = new JLabel( "City:" );
		panel.add(label);

		childTextField[4] = new JTextField(21);
		panel.add(childTextField[4]);

		address.add(panel, BorderLayout.NORTH);

		// South panel
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		label = new JLabel( "State:" );
		panel.add(label);

		stateBox = new JComboBox[4];
		stateBox[0] = new JComboBox(states);
		stateBox[0].setSelectedItem("ME");
		panel.add(stateBox[0]);

		label = new JLabel( "Zip Code:");
		panel.add(label);

		childTextField[5] = new JTextField(4);
		panel.add(childTextField[5]);

		address.add(panel, BorderLayout.CENTER);
		centerPanel.add(address, BorderLayout.NORTH);

		p.add(centerPanel, BorderLayout.CENTER);
		p.add(buttonPanel(true, false, doneButtonEnabled), BorderLayout.SOUTH);

		return p;
	}

	/*
	 * Displays the second child information screen
	 */
	public JPanel secondScreen()
	{
		iframe.setTitle("Add New Child - Page 2/4");

		JPanel p = new JPanel(new BorderLayout());
			p.setBorder(BorderFactory.createTitledBorder("Child Information"));
		JPanel panel, centerPanel;
		JLabel label;

		/*
		 * Medical information
		 */
		centerPanel = new JPanel(new BorderLayout());
		JPanel medical = new JPanel(new BorderLayout());
		medical.setBorder(BorderFactory.createTitledBorder("Medical"));

		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// Doctor information
		label = new JLabel( "Doctor Name:" );
		panel.add(label);

		childTextField[6] = new JTextField(17);
		panel.add(childTextField[6]);

		label = new JLabel( "Doctor Phone:" );
		panel.add(label);

		childTextField[7] = new JTextField(16);
		childTextField[7].setDocument(new FixedSizeDocument(14));
		childTextField[7].setText("012-345-6789");
		childTextField[7].setForeground(Color.LIGHT_GRAY);
		childTextField[7].addFocusListener(this);
		panel.add(childTextField[7]);

		medical.add(panel, BorderLayout.NORTH);

		JPanel cPanel = new JPanel(new BorderLayout());
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		label = new JLabel("Concerns/Allergies:");
		panel.add(label);
		cPanel.add(panel, BorderLayout.NORTH);

		// Medical Information JTextArea
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		childTextArea[0] = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(childTextArea[0]);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		if (osp.isMac())
			if (osp.isHighResolution())
				scrollPane.setPreferredSize(new Dimension(610, 75)); // width, height
			else
				scrollPane.setPreferredSize(new Dimension(573, 75));
		else
			scrollPane.setPreferredSize(new Dimension(510, 75));
		panel.add(scrollPane);

		cPanel.add(panel, BorderLayout.CENTER);
		medical.add(cPanel, BorderLayout.CENTER);

		JPanel southPanel = new JPanel(new BorderLayout());
		JPanel southNorthPanel = new JPanel();
		JPanel southSouthPanel = new JPanel();

		label = new JLabel( "Hospital Preference:" );
		if (osp.isMac())
			if (osp.isHighResolution())
				childTextField[9] = new JTextField(39);
			else
				childTextField[9] = new JTextField(36);
		else
			childTextField[9] = new JTextField(35);
		southNorthPanel.add(label);
		southNorthPanel.add(childTextField[9]);

		label = new JLabel( "Insurance Company:" );
		if (osp.isHighResolution())
			childTextField[10] = new JTextField(15);
		else
			childTextField[10] = new JTextField(13);
		southSouthPanel.add(label);
		southSouthPanel.add(childTextField[10]);

		label = new JLabel( "Policy Number:" );
		if (osp.isHighResolution())
			childTextField[11] = new JTextField(14);
		else
			childTextField[11] = new JTextField(13);
		southSouthPanel.add(label);
		southSouthPanel.add(childTextField[11]);

		southPanel.add(southNorthPanel, BorderLayout.NORTH);
		southPanel.add(southSouthPanel, BorderLayout.SOUTH);

		medical.add(southPanel, BorderLayout.SOUTH);
		centerPanel.add(medical, BorderLayout.CENTER);

		p.add(centerPanel, BorderLayout.CENTER);
		p.add(buttonPanel(true, true, doneButtonEnabled), BorderLayout.SOUTH);

		return p;
	}

	/*
	 * Displays the third child information screen
	 */
	private JPanel thirdScreen()
	{
		iframe.setTitle("Add New Child - Page 3/4");

		JPanel p = new JPanel(new BorderLayout());
			p.setBorder(BorderFactory.createTitledBorder("Child Information"));

		// Special information
		JPanel special = new JPanel(new FlowLayout(FlowLayout.LEFT));
		special.setBorder(BorderFactory.createTitledBorder("Special Notes"));
		childTextArea[1] = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(childTextArea[1]);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		if (osp.isMac())
		{
			if (osp.isHighResolution())
				scrollPane.setPreferredSize(new Dimension(610, 175));
		} else {		
			scrollPane.setPreferredSize(new Dimension(510, 175));
		}
		special.add(scrollPane);

		p.add(special, BorderLayout.NORTH);
		p.add(buttonPanel(true, true, doneButtonEnabled), BorderLayout.SOUTH);

		return p;
	}

	/*
	 * Displays the fourth child information screen
	 */
	private JPanel fourthScreen()
	{
		iframe.setTitle("Add New Child - Page 4/4");

		JPanel p = new JPanel(new BorderLayout());
			p.setBorder(BorderFactory.createTitledBorder("Child Information"));
		JLabel label;

		/*
		 * West panel
		 */
		// Schedule of Care
		JPanel westPanel = new JPanel(new GridLayout(0, 2));
		westPanel.setBorder(BorderFactory.createTitledBorder("Schedule"));

		// Row #1
		westPanel.add(new JLabel("Days:"));
		westPanel.add(new JLabel("Hours:"));

		// Monday
		childCheckBox[0] = new JCheckBox("Monday");
		childCheckBox[0].setForeground(Color.GRAY);
		childCheckBox[0].setActionCommand("3");
		childCheckBox[0].addActionListener(this);
		westPanel.add(childCheckBox[0]);

		scheduleField[0] = new JTextField();
		scheduleField[0].setEnabled(false);
		westPanel.add(scheduleField[0]);

		// Tuesday
		childCheckBox[1] = new JCheckBox("Tuesday");
		childCheckBox[1].setForeground(Color.GRAY);
		childCheckBox[1].setActionCommand("4");
		childCheckBox[1].addActionListener(this);
		westPanel.add(childCheckBox[1]);

		scheduleField[1] = new JTextField();
		scheduleField[1].setEnabled(false);
		westPanel.add(scheduleField[1]);

		// Wednesday
		childCheckBox[2] = new JCheckBox("Wednesday");
		childCheckBox[2].setForeground(Color.GRAY);
		childCheckBox[2].setActionCommand("5");
		childCheckBox[2].addActionListener(this);
		westPanel.add(childCheckBox[2]);

		scheduleField[2] = new JTextField();
		scheduleField[2].setEnabled(false);
		westPanel.add(scheduleField[2]);

		// Thursday
		childCheckBox[3] = new JCheckBox("Thursday");
		childCheckBox[3].setForeground(Color.GRAY);
		childCheckBox[3].setActionCommand("6");
		childCheckBox[3].addActionListener(this);
		westPanel.add(childCheckBox[3]);

		scheduleField[3] = new JTextField();
		scheduleField[3].setEnabled(false);
		westPanel.add(scheduleField[3]);

		// Friday
		childCheckBox[4] = new JCheckBox("Friday");
		childCheckBox[4].setForeground(Color.GRAY);
		childCheckBox[4].setActionCommand("7");
		childCheckBox[4].addActionListener(this);
		westPanel.add(childCheckBox[4]);

		scheduleField[4] = new JTextField();
		scheduleField[4].setEnabled(false);
		westPanel.add(scheduleField[4]);

		// Saturday
		childCheckBox[5] = new JCheckBox("Saturday");
		childCheckBox[5].setForeground(Color.GRAY);
		childCheckBox[5].setActionCommand("8");
		childCheckBox[5].addActionListener(this);
		westPanel.add(childCheckBox[5]);

		scheduleField[5] = new JTextField();
		scheduleField[5].setEnabled(false);
		westPanel.add(scheduleField[5]);

		// Sunday
		childCheckBox[6] = new JCheckBox("Sunday");
		childCheckBox[6].setForeground(Color.GRAY);
		childCheckBox[6].setActionCommand("9");
		childCheckBox[6].addActionListener(this);
		westPanel.add(childCheckBox[6]);

		scheduleField[6] = new JTextField();
		scheduleField[6].setEnabled(false);
		westPanel.add(scheduleField[6]);

		/*
		 * Center Panel
		 */
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel ratePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			ratePanel.setBorder(BorderFactory.createTitledBorder("Quoted Rate"));

		label = new JLabel(images.loadImage("../images/money_dollar_icon.png"));
		ratePanel.add(label);

		childTextField[8] = new JTextField(6);
		childTextField[8].setText("00000.00");
		childTextField[8].setForeground(Color.LIGHT_GRAY);
		childTextField[8].addFocusListener(this);
		ratePanel.add(childTextField[8]);

		rateBox = new JComboBox(rates);
		rateBox.setSelectedItem("Weekly");
		ratePanel.add(rateBox);

		JPanel parentPanel = new JPanel(new BorderLayout(5, 0));
		parentPanel.setBorder(BorderFactory.createTitledBorder("Guardian Information"));
		JPanel pWestPanel = new JPanel(new BorderLayout());
		JPanel pCenterPanel = new JPanel(new BorderLayout());
		JPanel pEastPanel = new JPanel(new BorderLayout());
		String[] pOptions = { "Mother", "Father", "Other", "None" };
		Object[] lo = { BorderLayout.NORTH, BorderLayout.CENTER, BorderLayout.SOUTH };

		// West North panel
		JPanel pWNorthPanel = new JPanel(new BorderLayout(0, 5));
		for (int i = 0; i < 3; i++)
		{
			guardianComboBox[i] = new JComboBox(pOptions);
			guardianComboBox[i].setSelectedItem(pOptions[i]);
			pWNorthPanel.add(guardianComboBox[i], lo[i]);
		}
		pWestPanel.add(pWNorthPanel, BorderLayout.NORTH);

		// Center North panel
		JPanel pCNorthPanel = new JPanel(new BorderLayout(0, 5));
		parentNameTextField = new JTextField[3];
		for (int i = 0; i < 3; i++)
		{
			parentNameTextField[i] = new JTextField();
			parentNameTextField[i].setPreferredSize(new Dimension(100, 25));
			parentNameTextField[i].setEnabled(false);
			pCNorthPanel.add(parentNameTextField[i], lo[i]);
		}
		pCenterPanel.add(pCNorthPanel, BorderLayout.NORTH);

		// East North panel
		JPanel pENorthPanel = new JPanel(new BorderLayout(0, 5));
		JButton button;
		for (int i = 0; i < 3; i++)
		{
			button = new JButton();
			button.setIcon(images.loadImage("../images/edit_icon.png"));
			button.setToolTipText("Edit Information");
			button.setPreferredSize(new Dimension(25, 25));
			button.setBorder(new RoundedBorder(5));
			button.setMargin(new Insets(0, 0, 0, 5));
			button.setActionCommand("editparent" + i);
			button.addActionListener(this);
			pENorthPanel.add(button, lo[i]);
		}
		pEastPanel.add(pENorthPanel, BorderLayout.NORTH);

		parentPanel.add(pWestPanel, BorderLayout.WEST);
		parentPanel.add(pCenterPanel, BorderLayout.CENTER);
		parentPanel.add(pEastPanel, BorderLayout.EAST);

		centerPanel.add(ratePanel, BorderLayout.NORTH);
		centerPanel.add(parentPanel, BorderLayout.CENTER);

		p.add(westPanel, BorderLayout.WEST);
		p.add(centerPanel, BorderLayout.CENTER);
		p.add(buttonPanel(false, true, doneButtonEnabled), BorderLayout.SOUTH);

		return p;
	}

	/**
	 * Creates the button panel, used by every panel.
	 */
	private JPanel buttonPanel(boolean next, boolean back, boolean done)
	{
		JPanel p = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		JButton button;

		button = new JButton("Back");
		button.setIcon(images.loadImage("../images/arrow_left_icon.png"));
		button.setActionCommand("backbutton");
		button.setToolTipText("Back");
		button.addActionListener(this);
		button.setPreferredSize(new Dimension(75, 25));
		button.setBorder(new RoundedBorder(5));
		button.setEnabled(back);
		p.add(button);

		button = new JButton("Next");
		button.setIcon(images.loadImage("../images/arrow_right_icon.png"));
		button.setActionCommand("nextbutton");
		button.setToolTipText("Next");
		button.addActionListener(this);
		button.setPreferredSize(new Dimension(75, 25));
		button.setBorder(new RoundedBorder(5));
		button.setEnabled(next);
		p.add(button);

		doneButton = new JButton("Done");
		doneButton.setIcon(images.loadImage("../images/accept_icon.png"));
		doneButton.setActionCommand("donebutton");
		if (doneButtonEnabled)
			doneButton.setToolTipText("Save Child");
		else
			doneButton.setToolTipText("You must provide the child's first and last name");
		doneButton.addActionListener(this);
		doneButton.setPreferredSize(new Dimension(75, 25));
		doneButton.setBorder(new RoundedBorder(5));
		doneButton.setEnabled(done);
		p.add(doneButton);

		button = new JButton("Cancel");
		button.setIcon(images.loadImage("../images/cross_icon.png"));
		button.setToolTipText("Cancel");
		button.setActionCommand("cancelbutton");
		button.addActionListener(this);
		button.setPreferredSize(new Dimension(75, 25));
		button.setBorder(new RoundedBorder(5));
		p.add(button);

		return p;
	}

	/*
	 * Pulls information from all the fields on the childInformation() JInternalFrame
	 * and stores that information so it can be added to an XML document for storage
	 * or reloaded when the user selectes the back button from the parent information page.
	 */
	private void storeContent()
	{
		switch (currentPanel)
		{
			case 1: // first panel
				if (content.isEmpty())
					content.add(childTextField[0].getText());
				else
					content.set(0, childTextField[0].getText());
				for (int i = 1; i < 6; i++)
				{
					if (content.size() <= i)
						content.add(childTextField[i].getText());
					else
						content.set(i, childTextField[i].getText());
				}
				month = monthBox.getSelectedItem();
				day = dayBox.getSelectedItem();
				year = yearBox.getSelectedItem();
				state = stateBox[0].getSelectedItem();
				if (sexMale.isSelected()) // Determines whether the child has been selected as male or female
					male = true;
				else
					male = false;
				break;
			case 2: // second panel
				if (content.size() <= 6)
					content.add(childTextField[6].getText());
				else
					content.set(6, childTextField[6].getText());
					
				if (content.size() <= 7)
					content.add(childTextField[7].getText());
				else
					content.set(7, childTextField[7].getText());
				if (content.size() <= 8)
					content.add(childTextArea[0].getText());
				else
					content.set(8, childTextArea[0].getText());
				if (content.size() <= 11)
					for (int i = 9; i <= 11; i++)
						content.add(childTextField[i].getText());
				else
					for (int i = 9; i <= 11; i++)
						content.set(i, childTextField[i].getText());
					
				break;
			case 3: // third panel
				if (content.size() <= 12)
					content.add(childTextArea[1].getText());
				else
					content.set(12, childTextArea[1].getText());
				break;
			case 4: // fourth panel
				quote = rateBox.getSelectedItem();
				for (int i = 0; i < scheduleField.length; i++) // Store the hours the children will be in daycare
				{
					if (childCheckBox[i].isSelected())
						checked[i] = true;
					else
						checked[i] = false;
					if (content.size() <= i + 13)
						content.add(scheduleField[i].getText());
					else
						content.set(i + 13, scheduleField[i].getText());
				}

				if (content.size() <= 20)
					content.add(childTextField[8].getText());
				else
					content.set(20, childTextField[8].getText());

				// Parents
				for (int i = 0; i < 3; i++)
					if (content.size() <= i + 21)
						content.add((String)guardianComboBox[i].getSelectedItem());
					else
						content.set(i + 21, (String)guardianComboBox[i].getSelectedItem());
				break;
	
			default:
				break;
		}
	}

	/*
	 * Called when the user clicks the back button on the parentInformation() interface. Reloads
	 * the information (which was stored using the storeChildContent() method) into the specified fields.
	 */
	private void reloadContent()
	{
		switch(currentPanel)
		{
		case 1: // first panel
			if (image != null)
				childImage.setIcon(image);
			else
				childImage.setIcon(images.loadImage("../images/no_photo_icon.png"));

			for (int i = 0; i < 6; i++)
				childTextField[i].setText(content.get(i));
			monthBox.setSelectedItem(month);
			dayBox.setSelectedItem(day);
			yearBox.setSelectedItem(year);
			stateBox[0].setSelectedItem(state);
			if (male)
				sexMale.setSelected(true);
			else
				sexFemale.setSelected(true);
			break;
		case 2:// second panel
			if (content.size() < 7)
				break;
			childTextField[6].setText(content.get(6));
			childTextField[7].setText(content.get(7));
			if (!content.get(7).equals("012-345-6789"))
				childTextField[7].setForeground(Color.BLACK);
			childTextArea[0].setText(content.get(8));
			childTextField[9].setText(content.get(9));
			childTextField[10].setText(content.get(10));
			childTextField[11].setText(content.get(11));
			break;
		case 3: // third panel
			if (content.size() < 13)
				break;
			childTextArea[1].setText(content.get(12));
			break;
		case 4: // fourth panel
			if (content.size() < 14)
				break;
			for (int i = 0; i < scheduleField.length; i++) // Store the hours the children will be in daycare
			{
				scheduleField[i].setText(content.get(i + 13));
				if (checked[i])
				{
					scheduleField[i].setEnabled(true);
					childCheckBox[i].setSelected(true);
					childCheckBox[i].setForeground(Color.BLACK);
				}
			}
			childTextField[8].setText(content.get(20));
			if (!content.get(20).equals("00000.00"))
				childTextField[8].setForeground(Color.BLACK);
			rateBox.setSelectedItem(quote);

			// Parents
			for (int i = 0; i < 3; i++)
			{
				if (parentInfo[i] == null || parentInfo[i].isEmpty())
					continue;
				parentNameTextField[i].setText(parentInfo[i].get(0) + " " + parentInfo[i].get(1));
			}
			for (int i = 0; i < 3; i++)
				guardianComboBox[i].setSelectedItem(content.get(i + 21));
			break;
		}

		if (male)
			sexMale.setSelected(true);
		else
			sexMale.setSelected(false);
	}

	/*
	 * Saves all entered child and parent information into an XML file named:
	 * lastname_firstname.xml
	 * where the last and first name is of the child.
	 */
	private void saveNewChild()
	{
		Document doc = new Document();
		Element root = new Element("child");
		doc.setRootElement(root);

		// Save date file was created:
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy 'at' HH:mm aaa");
		root.addContent(new Element( "dateCreated" ).setText(formatter.format(currentDate.getTime())));

		// If the user clicks done before every page has been visited
		// a null pointer exception will be thrown, so let's instantiate
		// the fields and put an empty String in each
		for (int i = 0; i < childTextField.length; i++)
			if (childTextField[i] == null)
				childTextField[i] = new JTextField("");
		for (int i = 0; i < childTextArea.length; i++)
			if (childTextArea[i] == null)
				childTextArea[i] = new JTextArea("");
		if (rateBox == null)
		{
			rateBox = new JComboBox();
			rateBox.setSelectedItem("0.00");
		}
		for (int i = 0; i < 3; i++)
			if (guardianComboBox[i] == null)
			{
				guardianComboBox[i] = new JComboBox();
				guardianComboBox[i].setSelectedItem("None");
			}
		for (int i = 0; i < 3; i++)
		{
			if (parentInfo[i] == null)
				parentInfo[i] = new ArrayList<String>();

			if (parentInfo[i].isEmpty())
				for (int m = 0; m < 10; m++) // Edit this if more parent information is ever added/needed
					parentInfo[i].add("");
		}

		// Save child's name:
		root.addContent(new Element( "childname" ));
		root.getChild("childname").addContent(new Element( "first" ).setText(childTextField[0].getText()));
		root.getChild("childname").addContent(new Element( "mi" ).setText(childTextField[1].getText()));
		root.getChild("childname").addContent(new Element( "last" ).setText(childTextField[2].getText()));

		// Save child's local image location:
		if (file != null)
			root.addContent(new Element( "photo" ).setText(file.getPath()));
		else
			root.addContent(new Element( "photo" ));

		// Save child's birthdate:
		String birthday = (String)month + " " + (String)day + ", " + (String)year;
		root.addContent(new Element( "birthdate" ).setText(birthday));

		// Save child's sex:
		if (male)
			root.addContent(new Element( "sex" ).setText( "Male" ));
		else
			root.addContent(new Element( "sex" ).setText( "Female" ));

		// Save child's address:
		root.addContent(new Element( "childaddress" ));
		root.getChild("childaddress").addContent(new Element( "street" ).setText(childTextField[3].getText()));
		root.getChild("childaddress").addContent(new Element( "city" ).setText(childTextField[4].getText()));
		root.getChild("childaddress").addContent(new Element( "state" ).setText((String)stateBox[0].getSelectedItem()));
		root.getChild("childaddress").addContent(new Element ( "zipcode" ).setText(childTextField[5].getText()));

		// Save child's medical information:
		root.addContent(new Element( "medical" ));
		root.getChild("medical").addContent(new Element("doctorName").setText(childTextField[6].getText()));
		if (!childTextField[7].getText().matches("012-345-6789"))
			root.getChild("medical").addContent(new Element("doctorPhone").setText(childTextField[7].getText()));
		else
			root.getChild("medical").addContent(new Element("doctorPhone"));
		root.getChild("medical").addContent(new Element("concernsOrAllergies").setText(childTextArea[0].getText()));
		root.getChild("medical").addContent(new Element("hospitalPreference").setText(childTextField[9].getText()));
		root.getChild("medical").addContent(new Element("insurance"));
		root.getChild("medical").getChild("insurance").addContent(new Element("company").setText(childTextField[10].getText()));
		root.getChild("medical").getChild("insurance").addContent(new Element("policyNumber").setText(childTextField[11].getText()));

		// Save special information:
		root.addContent(new Element( "special" ));
		root.getChild("special").addContent(new Element("familyArrangement").setText(childTextArea[1].getText()));

		// Save proposed schedule of care:
		root.addContent(new Element( "schedule" ));
		String[] daysOfWeek = { "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday" };
		for (int i = 0; i < 7; i++)
		{
			if (checked[i])
				root.getChild("schedule").addContent(new Element( daysOfWeek[i] ).setText(scheduleField[i].getText()));
			else
				root.getChild("schedule").addContent(new Element( daysOfWeek[i] ));
		}

		// Save proposed quote:
		root.addContent(new Element( "quote" ));
		if (childTextField[8].getText().matches("00000.00"))
			root.getChild("quote").addContent(new Element( "rate" ).setText("00.00"));
		else
			root.getChild("quote").addContent(new Element( "rate" ).setText(childTextField[8].getText()));
		root.getChild("quote").addContent(new Element( "payCycle" ).setText((String)rateBox.getSelectedItem()));

		// Guardians
		// root
		root.addContent(new Element( "guardianInformation" ));

		// GUARDIAN ONE
		// root --> guardianInformation
		root.getChild("guardianInformation").addContent(new Element( "firstguardian" ));
		root.getChild("guardianInformation").getChild("firstguardian").addContent(new Element("relation").setText((String)guardianComboBox[0].getSelectedItem()));
		// root --> guardianInformation --> firstguardian
		root.getChild("guardianInformation").getChild("firstguardian").addContent(new Element( "name" ));
		// root --> guardianInformation --> firstguardian --> name
		root.getChild("guardianInformation").getChild("firstguardian").getChild("name").addContent(new Element( "first" ).setText(parentInfo[0].get(0)));
		root.getChild("guardianInformation").getChild("firstguardian").getChild("name").addContent(new Element( "last" ).setText(parentInfo[0].get(1)));
		// root --> guardianInformation --> firstguardian
		root.getChild("guardianInformation").getChild("firstguardian").addContent(new Element( "address" ));
		// root --> guardianInformation --> firstguardian --> address
		root.getChild("guardianInformation").getChild("firstguardian").getChild("address").addContent(new Element( "street" ).setText(parentInfo[0].get(6)));
		root.getChild("guardianInformation").getChild("firstguardian").getChild("address").addContent(new Element( "city" ).setText(parentInfo[0].get(7)));
		root.getChild("guardianInformation").getChild("firstguardian").getChild("address").addContent(new Element( "state" ).setText(parentInfo[0].get(8)));
		root.getChild("guardianInformation").getChild("firstguardian").getChild("address").addContent(new Element( "zipcode" ).setText(parentInfo[0].get(9)));
		root.getChild("guardianInformation").getChild("firstguardian").addContent(new Element("employer").setText(parentInfo[0].get(2)));
		root.getChild("guardianInformation").getChild("firstguardian").addContent(new Element("phone"));
		root.getChild("guardianInformation").getChild("firstguardian").getChild("phone").addContent(new Element("home").setText(parentInfo[0].get(3)));
		root.getChild("guardianInformation").getChild("firstguardian").getChild("phone").addContent(new Element("work").setText(parentInfo[0].get(5)));
		root.getChild("guardianInformation").getChild("firstguardian").getChild("phone").addContent(new Element("cell").setText(parentInfo[0].get(4)));

		// GUARDIAN TWO
		root.getChild("guardianInformation").addContent(new Element("secondguardian"));
		root.getChild("guardianInformation").getChild("secondguardian").addContent(new Element("relation").setText((String)guardianComboBox[1].getSelectedItem()));
		// root --> guardianInformation --> secondguardian
		root.getChild("guardianInformation").getChild("secondguardian").addContent(new Element( "name" ));
		// root --> guardianInformation --> secondguardian --> name
		root.getChild("guardianInformation").getChild("secondguardian").getChild("name").addContent(new Element( "first" ).setText(parentInfo[1].get(0)));
		root.getChild("guardianInformation").getChild("secondguardian").getChild("name").addContent(new Element( "last" ).setText(parentInfo[1].get(1)));
		// root --> guardianInformation --> firstguardian
		root.getChild("guardianInformation").getChild("secondguardian").addContent(new Element( "address" ));
		// root --> guardianInformation --> firstguardian --> address
		root.getChild("guardianInformation").getChild("secondguardian").getChild("address").addContent(new Element( "street" ).setText(parentInfo[1].get(6)));
		root.getChild("guardianInformation").getChild("secondguardian").getChild("address").addContent(new Element( "city" ).setText(parentInfo[1].get(7)));
		root.getChild("guardianInformation").getChild("secondguardian").getChild("address").addContent(new Element( "state" ).setText(parentInfo[1].get(8)));
		root.getChild("guardianInformation").getChild("secondguardian").getChild("address").addContent(new Element( "zipcode" ).setText(parentInfo[1].get(9)));
		root.getChild("guardianInformation").getChild("secondguardian").addContent(new Element("employer").setText(parentInfo[1].get(2)));
		root.getChild("guardianInformation").getChild("secondguardian").addContent(new Element("phone"));
		root.getChild("guardianInformation").getChild("secondguardian").getChild("phone").addContent(new Element("home").setText(parentInfo[1].get(3)));
		root.getChild("guardianInformation").getChild("secondguardian").getChild("phone").addContent(new Element("work").setText(parentInfo[1].get(5)));
		root.getChild("guardianInformation").getChild("secondguardian").getChild("phone").addContent(new Element("cell").setText(parentInfo[1].get(4)));

		// GUARDIAN THREE
		root.getChild("guardianInformation").addContent(new Element("thirdguardian"));
		root.getChild("guardianInformation").getChild("thirdguardian").addContent(new Element("relation").setText((String)guardianComboBox[2].getSelectedItem()));
		// root --> guardianInformation --> secondguardian
		root.getChild("guardianInformation").getChild("thirdguardian").addContent(new Element( "name" ));
		// root --> guardianInformation --> secondguardian --> name
		root.getChild("guardianInformation").getChild("thirdguardian").getChild("name").addContent(new Element( "first" ).setText(parentInfo[2].get(0)));
		root.getChild("guardianInformation").getChild("thirdguardian").getChild("name").addContent(new Element( "last" ).setText(parentInfo[2].get(1)));
		// root --> guardianInformation --> firstguardian
		root.getChild("guardianInformation").getChild("thirdguardian").addContent(new Element( "address" ));
		// root --> guardianInformation --> firstguardian --> address
		root.getChild("guardianInformation").getChild("thirdguardian").getChild("address").addContent(new Element( "street" ).setText(parentInfo[2].get(6)));
		root.getChild("guardianInformation").getChild("thirdguardian").getChild("address").addContent(new Element( "city" ).setText(parentInfo[2].get(7)));
		root.getChild("guardianInformation").getChild("thirdguardian").getChild("address").addContent(new Element( "state" ).setText(parentInfo[2].get(8)));
		root.getChild("guardianInformation").getChild("thirdguardian").getChild("address").addContent(new Element( "zipcode" ).setText(parentInfo[2].get(9)));
		root.getChild("guardianInformation").getChild("thirdguardian").addContent(new Element("employer").setText(parentInfo[2].get(2)));
		root.getChild("guardianInformation").getChild("thirdguardian").addContent(new Element("phone"));
		root.getChild("guardianInformation").getChild("thirdguardian").getChild("phone").addContent(new Element("home").setText(parentInfo[2].get(3)));
		root.getChild("guardianInformation").getChild("thirdguardian").getChild("phone").addContent(new Element("work").setText(parentInfo[2].get(5)));
		root.getChild("guardianInformation").getChild("thirdguardian").getChild("phone").addContent(new Element("cell").setText(parentInfo[2].get(4)));

		String childLastName = root.getChild("childname").getChild("last").getText();
		String childFirstName = root.getChild("childname").getChild("first").getText();
		saveXMLFile( childLastName + "_" + childFirstName, doc );
	}

	/*
	 * Save the child's XML file to the Children folder.
	 */
	private void saveXMLFile(String childname, Document doc)
	{
		try
		{
			FileWriter writer = new FileWriter(new File_System_Controller().getModel().getDirectoryPath("Children") + childname + ".xml");
			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat(Format.getPrettyFormat());
			outputter.output(doc, writer); // Save the new document
			System.out.println( "SYSTEM: Saved new child XML file:\n" + childname + ".xml" );
		} catch (IOException e) {
			Logger.write("IOException: Could not save new child XML file.", Logger.Level.ERROR);
		}
	}

	/**
	 * Sets the specified guardians information in the child view.
	 * 
	 * @param info The guardian's information
	 * @param index The guardian index in the view to set the name for (0-2)
	 */
	public void setGuardianInformation(ArrayList<String> info, int index)
	{
		parentInfo[index] = info;
		parentNameTextField[index].setText(parentInfo[index].get(0) + " " + parentInfo[index].get(1));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String[] scheduleArr = { "3", "4", "5", "6", "7", "8", "9" };

		if (e.getActionCommand().equals("nextbutton"))
		{
			storeContent();
			JPanel panel = null;
			if (currentPanel == 1)
			{
				panel = secondScreen();
				iframe.setTitle("Add New Child - Page 2/4"); 
				currentPanel++;
			}
			else if (currentPanel == 2)
			{
				panel = thirdScreen();
				iframe.setTitle("Add New Child - Page 3/4");
				currentPanel++;
			}
			else if (currentPanel == 3)
			{
				panel = fourthScreen();
				iframe.setTitle("Add New Child - Page 4/4");
				currentPanel++;
			}
			c.removeAll();
			c.repaint();
			c.add(panel);
			panel.revalidate();
			reloadContent();
		}
		else if (e.getActionCommand().equals("backbutton"))
		{
			storeContent();
			JPanel panel = null;
			if (currentPanel == 2)
			{
				panel = firstScreen();
				currentPanel--;
			}
			else if (currentPanel == 3)
			{
				panel = secondScreen();
				currentPanel--;
			}
			else if (currentPanel == 4)
			{
				panel = thirdScreen();
				currentPanel--;
			}
			c.removeAll();
			c.repaint();
			c.add(panel);
			panel.revalidate();
			reloadContent();
		}
		else if (e.getActionCommand().equals("cancelbutton"))
		{
			iframe.dispose();
		}
		else if (e.getActionCommand().equals("donebutton"))
		{
			if (childTextField[0].getText().equals("") || childTextField[2].getText().equals(""))
			{
				JOptionPane.showMessageDialog(null, "You must specify the child's first and last name.", "Error!", JOptionPane.ERROR_MESSAGE );
				return;
			}
			// Save child and parent information to an XML file
			saveNewChild();
			iframe.dispose();
		}
		else if (e.getActionCommand().equals("sexMale"))
		{			
			sexFemale.setSelected(false);
			male = true;
			return;
		}
		else if (e.getActionCommand().equals("sexFemale"))
		{
			sexMale.setSelected(false);
			male = false;
			return;
		}
		else if (e.getActionCommand().equals("childimage"))
		{
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(new File_System_Controller().getModel().getDefaultPictureDirectory()));
			int returnVal = fc.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				image = new ImageIcon(file.getPath());
				Image img = image.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH); // Resize the image to fit the childImage button
				image = new ImageIcon(img);
				childImage.setIcon(image);
				System.out.println( "SYSTEM: Image selected from path: " + file.getPath() );
			} else {
				// Have this here just in case
				childImage.setIcon(image);
			}
		}
		for (int i = 0; i < 3; i++)
		{
			if (e.getActionCommand().equals("editparent" + i))
			{
				if ((String)guardianComboBox[i].getSelectedItem() == "None")
					return;

				/*
				 * Initialize the dialog box with no textfields filled or 
				 * fill the textfields that have already been supplied information
				 * from previous edits.
				 */
				new Guardian_Controller(this, i).showView((String)guardianComboBox[i].getSelectedItem(), parentInfo[i]);
			}
		}
		for (int s = 0; s < scheduleArr.length; s++) // Check all checkboxs
		{
			if (e.getActionCommand().equals(scheduleArr[s]))
			{
				int i = Integer.parseInt(e.getActionCommand());
				
				if (!scheduleField[i - 3].isEnabled())
				{
					scheduleField[i - 3].setEnabled(true);
					childCheckBox[i - 3].setForeground(Color.BLACK);
					checked[i - 3] = true;
				}
				else
				{
					scheduleField[i - 3].setEnabled(false);
					childCheckBox[i - 3].setForeground(Color.GRAY);
					checked[i - 3] = false;
				}
				return;
			}
		}
	}

	@Override
	public void internalFrameActivated(InternalFrameEvent e)
	{
		
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e)
	{
		
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e)
	{
		
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e)
	{
		
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent e)
	{
		
	}

	@Override
	public void internalFrameIconified(InternalFrameEvent e)
	{
		
	}

	@Override
	public void internalFrameOpened(InternalFrameEvent e)
	{
		
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (childTextField[0].getText().length() > 0 && childTextField[2].getText().length() > 0)
		{
			doneButton.setEnabled(true);
			doneButtonEnabled = true;
			doneButton.setToolTipText("Save Child");
		} else {
			doneButton.setEnabled(false);
			doneButtonEnabled = false;
			doneButton.setToolTipText("You must provide the child's first and last name");
		}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}

	@Override
	public void focusGained(FocusEvent e)
	{
		if (e.getSource().equals(childTextField[7]) && childTextField[7].getText().matches("012-345-6789"))
		{
			childTextField[7].setText("");
			childTextField[7].setForeground(Color.BLACK);
			return;
		}
		if (e.getSource().equals(childTextField[8]) && childTextField[8].getText().matches("00000.00"))
		{
			childTextField[8].setText("");
			childTextField[8].setForeground(Color.BLACK);
			return;
		}
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		if (e.getSource().equals(childTextField[7]) && childTextField[7].getText().matches(""))
		{
			childTextField[7].setText("012-345-6789");
			childTextField[7].setForeground(Color.LIGHT_GRAY);
			return;
		}
		if (e.getSource().equals(childTextField[8]) && childTextField[8].getText().matches(""))
		{
			childTextField[8].setText("00000.00");
			childTextField[8].setForeground(Color.LIGHT_GRAY);
			return;
		}
	}
}