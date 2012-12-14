package Views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import lib.FixedSizeDocument;
import lib.OSProperties;
import lib.RoundedBorder;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import Controllers.Employee_Controller;
import Controllers.File_System_Controller;
import Controllers.Image_Controller;

/**
 * <p>Controls the Add New Employee view of the program. Shows the user a dialog
 * with a form so that they can add new employee's to the database.
 * 
 * @author Adam Childs
 */
public class Employee_View implements InternalFrameListener, ActionListener, KeyListener, FocusListener
{
	Employee_Controller controller = null;
	JDesktopPane desktop;
	Container c;
	Image_Controller images = new Image_Controller();
	ImageIcon image = images.loadImage("../images/no_photo_icon.png");
	JButton employeeImage, doneButton;
	JComboBox monthBox, dayBox, yearBox, rateBox;
	JComboBox[] stateBox = new JComboBox[4];
	ArrayList<JCheckBox> formBoxes, tempFormBoxes;
	JInternalFrame iframe = null;
	JPanel formPanel;
	JRadioButton sexMale, sexFemale;
	JScrollPane formScrollPane;
	JTextField[] employeeTextField = new JTextField[50];
	JTextField[] scheduleField = new JTextField[7];
	JTextArea[] employeeTextArea = new JTextArea[2];
	Object month, day, year, rate;
	Object[] state = new Object[2];
	int currentPanel = 1;
	ArrayList<String> content = new ArrayList<String>();
	boolean male, doneButtonEnabled = false;
	Point loc;
	File file;
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
	final OSProperties osp = new OSProperties();
	
	public Employee_View(Employee_Controller parent)
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
		iframe.setTitle("Add New Employee");

		iframe.add(firstScreen());

		iframe.pack();
//		iframe.setLocation(loc);
		iframe.setMaximizable(false);
		iframe.setClosable(true);
		iframe.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		iframe.addInternalFrameListener(this);
		iframe.setFrameIcon(images.loadImage("../images/employeeframe/employee_icon.png"));
		iframe.setVisible(true);

		return iframe;
	}

	/**
	 * Displays the first employee information screen
	 */
	public JPanel firstScreen()
	{
		iframe.setTitle("Add New Employee - Page 1/5");

		JPanel p = new JPanel(new BorderLayout());
			p.setBorder(BorderFactory.createTitledBorder("Employee Information"));
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
		employeeImage = new JButton();
		employeeImage.setPreferredSize(new Dimension(95, 110));
		employeeImage.setContentAreaFilled(false);
		employeeImage.setOpaque(false);
		employeeImage.setIcon(image);
		employeeImage.setForeground(Color.GRAY);
		employeeImage.setActionCommand( "employeeimage" );
		employeeImage.addActionListener(this);

		imagePanel.add(employeeImage);
		northPanel.add(imagePanel, BorderLayout.WEST);

		// North.Center Panel
		JPanel northCenterPanel = new JPanel(new GridLayout(0, 1));
		JPanel general = new JPanel(new BorderLayout());
		general.setBorder(BorderFactory.createTitledBorder("General"));

		// Panel One
		// Employee Name
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		label = new JLabel( "First Name:" );
		panel.add(label);
		
		employeeTextField[0] = new JTextField(8);
		employeeTextField[0].addKeyListener(this);
		panel.add(employeeTextField[0]);

		label = new JLabel( "Middle Initial:" );
		panel.add(label);

		employeeTextField[1] = new JTextField(2);
		panel.add(employeeTextField[1]);

		label = new JLabel( "Last Name:" );
		panel.add(label);

		employeeTextField[2] = new JTextField(8);
		employeeTextField[2].addKeyListener(this);
		panel.add(employeeTextField[2]);

		general.add(panel, BorderLayout.NORTH);

		// Panel Two
		// Employee Birth date
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
		 * Employee Address
		 */
		// North panel
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		label = new JLabel( "Street:" );
		panel.add(label);

		employeeTextField[3] = new JTextField(21);
		panel.add(employeeTextField[3]);

		label = new JLabel( "City:" );
		panel.add(label);

		employeeTextField[4] = new JTextField(21);
		panel.add(employeeTextField[4]);

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

		employeeTextField[5] = new JTextField(4);
		panel.add(employeeTextField[5]);

		address.add(panel, BorderLayout.CENTER);
		centerPanel.add(address, BorderLayout.NORTH);

		p.add(centerPanel, BorderLayout.CENTER);
		p.add(buttonPanel(true, false, doneButtonEnabled), BorderLayout.SOUTH);

		return p;
	}

	/*
	 * Displays the second employee information screen
	 */
	public JPanel secondScreen()
	{
		iframe.setTitle("Add New Employee - Page 2/5");

		JPanel p = new JPanel(new BorderLayout());
			p.setBorder(BorderFactory.createTitledBorder("Employee Information"));
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

		employeeTextField[6] = new JTextField(17);
		panel.add(employeeTextField[6]);

		label = new JLabel( "Doctor Phone:" );
		panel.add(label);

		employeeTextField[7] = new JTextField(16);
		employeeTextField[7].setDocument(new FixedSizeDocument(14));
		employeeTextField[7].setText("012-345-6789");
		employeeTextField[7].setForeground(Color.LIGHT_GRAY);
		employeeTextField[7].addFocusListener(this);
		panel.add(employeeTextField[7]);

		medical.add(panel, BorderLayout.NORTH);

		JPanel cPanel = new JPanel(new BorderLayout());
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		label = new JLabel("Concerns/Allergies:");
		panel.add(label);
		cPanel.add(panel, BorderLayout.NORTH);

		// Medical Information JTextArea
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		employeeTextArea[0] = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(employeeTextArea[0]);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		if (osp.isMac())
			if (osp.isHighResolution())
				scrollPane.setPreferredSize(new Dimension(610, 75));
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
				employeeTextField[8] = new JTextField(39);
			else
				employeeTextField[8] = new JTextField(36);
		else
			employeeTextField[8] = new JTextField(35);
		southNorthPanel.add(label);
		southNorthPanel.add(employeeTextField[8]);

		label = new JLabel( "Insurance Company:" );
		if (osp.isHighResolution())
			employeeTextField[9] = new JTextField(15);
		else
			employeeTextField[9] = new JTextField(13);
		southSouthPanel.add(label);
		southSouthPanel.add(employeeTextField[9]);

		label = new JLabel( "Policy Number:" );
		if (osp.isHighResolution())
			employeeTextField[10] = new JTextField(14);
		else
			employeeTextField[10] = new JTextField(13);
		southSouthPanel.add(label);
		southSouthPanel.add(employeeTextField[10]);

		southPanel.add(southNorthPanel, BorderLayout.NORTH);
		southPanel.add(southSouthPanel, BorderLayout.SOUTH);

		medical.add(southPanel, BorderLayout.SOUTH);
		centerPanel.add(medical, BorderLayout.CENTER);

		p.add(centerPanel, BorderLayout.CENTER);
		p.add(buttonPanel(true, true, doneButtonEnabled), BorderLayout.SOUTH);

		return p;
	}

	/*
	 * Displays the third employee information screen
	 */
	private JPanel thirdScreen()
	{
		iframe.setTitle("Add New Employee - Page 3/5");

		JPanel p = new JPanel(new BorderLayout());
			p.setBorder(BorderFactory.createTitledBorder("Employee Information"));

		// Special information
		JPanel special = new JPanel(new FlowLayout(FlowLayout.LEFT));
		special.setBorder(BorderFactory.createTitledBorder("Special Notes"));
		employeeTextArea[1] = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(employeeTextArea[1]);
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
	 * Displays the fourth employee information screen
	 */
	private JPanel fourthScreen()
	{
		iframe.setTitle("Add New Employee - Page 4/5");

		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createTitledBorder("Employee Information"));
		JLabel label;

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createTitledBorder("Emergency Contacts"));

		// Panel One
		JPanel leftPanel = new JPanel(new GridLayout(0, 2, 5, 5));
		leftPanel.setBorder(BorderFactory.createTitledBorder("Primary"));
		
		label = new JLabel( "Name:", JLabel.RIGHT );
		leftPanel.add(label);

		employeeTextField[11] = new JTextField(8);
		leftPanel.add(employeeTextField[11]);

		label = new JLabel( "Relationship:", JLabel.RIGHT );
		leftPanel.add(label);

		employeeTextField[12] = new JTextField(8);
		leftPanel.add(employeeTextField[12]);

		label = new JLabel( "Contact #:", JLabel.RIGHT );
		leftPanel.add(label);

		employeeTextField[13] = new JTextField(8);
		leftPanel.add(employeeTextField[13]);

		label = new JLabel( "Secondary Contact #:", JLabel.RIGHT );
		leftPanel.add(label);

		employeeTextField[14] = new JTextField(8);
		leftPanel.add(employeeTextField[14]);

		label = new JLabel( "Current Address:", JLabel.RIGHT );
		leftPanel.add(label);

		employeeTextField[15] = new JTextField(8);
		leftPanel.add(employeeTextField[15]);

		// Panel Two
		JPanel rightPanel = new JPanel(new GridLayout(0, 2, 5, 5));
		rightPanel.setBorder(BorderFactory.createTitledBorder("Alternative"));
		
		label = new JLabel( "Name:", JLabel.RIGHT );
		rightPanel.add(label);

		employeeTextField[16] = new JTextField(8);
		rightPanel.add(employeeTextField[16]);

		label = new JLabel( "Relationship:", JLabel.RIGHT );
		rightPanel.add(label);

		employeeTextField[17] = new JTextField(8);
		rightPanel.add(employeeTextField[17]);

		label = new JLabel( "Contact #:", JLabel.RIGHT );
		rightPanel.add(label);

		employeeTextField[18] = new JTextField(8);
		rightPanel.add(employeeTextField[18]);

		label = new JLabel( "Secondary Contact #:", JLabel.RIGHT );
		rightPanel.add(label);

		employeeTextField[19] = new JTextField(8);
		rightPanel.add(employeeTextField[19]);

		label = new JLabel( "Current Address:", JLabel.RIGHT );
		rightPanel.add(label);

		employeeTextField[20] = new JTextField(8);
		rightPanel.add(employeeTextField[20]);

		centerPanel.add(leftPanel, BorderLayout.WEST);
		centerPanel.add(rightPanel, BorderLayout.EAST);

		p.add(centerPanel, BorderLayout.CENTER);
		p.add(buttonPanel(true, true, doneButtonEnabled), BorderLayout.SOUTH);

		return p;
	}

	private JPanel fifthScreen()
	{
		iframe.setTitle("Add New Employee - Page 5/5");

		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createTitledBorder("Employee Information"));
		JLabel label;
		JButton button;

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createTitledBorder("Other"));
		JPanel westPanel = new JPanel(new BorderLayout());
		westPanel.setBorder(BorderFactory.createTitledBorder("Forms/Certifications"));

		/*
		 * Forms and certifications checkbox scrollpane.
		 * Loads certifications from a textfile. Each line is a new form
		 * or certification that the user can check if the employee has
		 * completed the form or is certified.
		 */
		JScrollPane scrollPane = formDisplay();

		// Add/delete a form/certification
		JPanel westSouthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		employeeTextField[21] = new JTextField(10);
		employeeTextField[21].setText("New...");
		employeeTextField[21].setForeground(Color.LIGHT_GRAY);
		employeeTextField[21].addFocusListener(this);
		westSouthPanel.add(employeeTextField[21]);
		
		button = new JButton(images.loadImage("../images/employeeframe/plus_icon_small.png"));
		button.setBorder(new RoundedBorder(5));
		button.setPreferredSize(new Dimension(25, 25));
		button.setToolTipText("Add new form/certification");
		button.setActionCommand("addForm");
		button.addActionListener(this);
		westSouthPanel.add(button);
		
		button = new JButton(images.loadImage("../images/employeeframe/minus_icon_small.png"));
		button.setBorder(new RoundedBorder(5));
		button.setPreferredSize(new Dimension(25, 25));
		button.setToolTipText("Remove selected form(s)/certification(s)");
		button.setActionCommand("removeForm");
		button.addActionListener(this);
		westSouthPanel.add(button);
		
		westPanel.add(scrollPane, BorderLayout.CENTER);
		westPanel.add(westSouthPanel, BorderLayout.SOUTH);

		/*
		 * Center Panel
		 * 		North - contact information
		 * 		Center - General (Drivers license, social security #, etc)
		 * 		South - Salary, w4 Allowances, Hire date, etc.
		 */
		// NORTH
		JPanel cNorthPanel = new JPanel(new BorderLayout());
		JPanel cNorthCPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		label = new JLabel( "Home Phone:" );
		cNorthCPanel.add(label);
		if (osp.isMac())
		{
			if (osp.isHighResolution())
				employeeTextField[22] = new JTextField(8);
		} else {
			employeeTextField[22] = new JTextField(7);
		}
		employeeTextField[22].setText("012-345-6789");
		employeeTextField[22].setForeground(Color.LIGHT_GRAY);
		employeeTextField[22].addFocusListener(this);
		cNorthCPanel.add(employeeTextField[22]);

		label = new JLabel( "Cell Phone:" );
		cNorthCPanel.add(label);
		if (osp.isMac())
		{
			if (osp.isHighResolution())
				employeeTextField[23] = new JTextField(8);
		} else {
			employeeTextField[23] = new JTextField(7);
		}
		employeeTextField[23].setText("012-345-6789");;
		employeeTextField[23].setForeground(Color.LIGHT_GRAY);
		employeeTextField[23].addFocusListener(this);
		cNorthCPanel.add(employeeTextField[23]);

		JPanel cNorthWestPanel = new JPanel(new BorderLayout());
		cNorthWestPanel.add(cNorthCPanel, BorderLayout.CENTER);
		cNorthPanel.add(cNorthWestPanel, BorderLayout.WEST);
		centerPanel.add(cNorthPanel, BorderLayout.NORTH);

		// CENTER
		JPanel top = new JPanel(new BorderLayout());
		JPanel cCenterNPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		label = new JLabel( "Social Security Number:" );
		cCenterNPanel.add(label);

		if (osp.isMac())
		{
			if (osp.isHighResolution())
				employeeTextField[24] = new JTextField(5);
		} else {
			employeeTextField[24] = new JTextField(4);
		}
		employeeTextField[24].setDocument(new FixedSizeDocument(3));
		cCenterNPanel.add(employeeTextField[24]);

		label = new JLabel("-");
		cCenterNPanel.add(label);

		if (osp.isMac())
		{
			if (osp.isHighResolution())
				employeeTextField[25] = new JTextField(3);
		} else {
			employeeTextField[25] = new JTextField(3);
		}
		employeeTextField[25].setDocument(new FixedSizeDocument(2));
		cCenterNPanel.add(employeeTextField[25]);

		label = new JLabel("-");
		cCenterNPanel.add(label);

		if (osp.isMac())
		{
			if (osp.isHighResolution())
				employeeTextField[26] = new JTextField(5);
		} else {
			employeeTextField[26] = new JTextField(5);
		}
		employeeTextField[26].setDocument(new FixedSizeDocument(4));
		cCenterNPanel.add(employeeTextField[26]);

		JPanel cCenterSPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		label = new JLabel( "Drivers License #:" );
		cCenterSPanel.add(label);

		if (osp.isMac())
		{
			if (osp.isHighResolution())
				employeeTextField[27] = new JTextField(6);
		} else {
			employeeTextField[27] = new JTextField(7);
		}
		cCenterSPanel.add(employeeTextField[27]);

		label = new JLabel( "State Issued:" );
		cCenterSPanel.add(label);

		stateBox[1] = new JComboBox(states);
		stateBox[1].setSelectedItem("ME");
		cCenterSPanel.add(stateBox[1]);

		top.add(cCenterNPanel, BorderLayout.NORTH);
		top.add(cCenterSPanel, BorderLayout.CENTER);

		// SOUTH
		JPanel cSouthPanel = new JPanel(new BorderLayout());
		JPanel cSNorthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel cSSouthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		label = new JLabel( "Salary:" );
		cSNorthPanel.add(label);

		label = new JLabel(images.loadImage("../images/money_dollar_icon.png"));
		cSNorthPanel.add(label);

		if (osp.isMac())
		{
			if (osp.isHighResolution())
				employeeTextField[28] = new JTextField(7);
		} else {
			employeeTextField[28] = new JTextField(5);
		}
		employeeTextField[28].setText("00000.00");
		employeeTextField[28].setForeground(Color.LIGHT_GRAY);
		employeeTextField[28].addFocusListener(this);
		cSNorthPanel.add(employeeTextField[28]);

		rateBox = new JComboBox(rates);
		rateBox.setSelectedIndex(2);
		cSNorthPanel.add(rateBox);

		label = new JLabel( "Hire Date:" );
		cSSouthPanel.add(label);

		if (osp.isMac())
		{
			if (osp.isHighResolution())
				employeeTextField[29] = new JTextField(8);
		} else {
			employeeTextField[29] = new JTextField(6);
		}
		employeeTextField[29].setDocument(new FixedSizeDocument(10));
		employeeTextField[29].setText("mm/dd/yyyy");
		employeeTextField[29].setForeground(Color.LIGHT_GRAY);
		employeeTextField[29].addFocusListener(this);
		cSSouthPanel.add(employeeTextField[29]);
		label = new JLabel( "W4 Allowance:" );
		cSSouthPanel.add(label);

		if (osp.isMac())
		{
			if (osp.isHighResolution())
				employeeTextField[30] = new JTextField(6);
		} else {
			employeeTextField[30] = new JTextField(4);
		}
		employeeTextField[30].setText("000.00");
		employeeTextField[30].setForeground(Color.LIGHT_GRAY);
		employeeTextField[30].addFocusListener(this);
		cSSouthPanel.add(employeeTextField[30]);

		top.add(cSNorthPanel, BorderLayout.SOUTH);
		cSouthPanel.add(cSSouthPanel, BorderLayout.CENTER);

		centerPanel.add(top, BorderLayout.CENTER);
		centerPanel.add(cSouthPanel, BorderLayout.SOUTH);

		p.add(centerPanel, BorderLayout.CENTER);
		p.add(westPanel, BorderLayout.WEST);
		p.add(buttonPanel(false, true, doneButtonEnabled), BorderLayout.SOUTH);

		return p;
	}

	/*
	 * Displays the Form and Certification checkbox scrollpane. Reads in all the
	 * required forms and certifications from a text file located in the main
	 * program directory on the user's computer.
	 */
	private JScrollPane formDisplay()
	{
		formPanel = new JPanel(new GridLayout(0, 1));
		formBoxes = new ArrayList<JCheckBox>();
		
		try
		{
			FileInputStream fstream = new FileInputStream(new File_System_Controller().getModel().findFile("forms.txt"));
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String str;
			int i = 0;
			while ((str = br.readLine()) != null) // Read the file line by line
			{
				final JCheckBox checkbox = new JCheckBox(str);
				if (tempFormBoxes != null)
					if (tempFormBoxes.get(i).isSelected())
						checkbox.setSelected(true);
				formBoxes.add(checkbox);
				formPanel.add(checkbox);
				i++;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		formScrollPane = new JScrollPane(formPanel);
		formScrollPane.getVerticalScrollBar().setUnitIncrement(10);
		formScrollPane.setPreferredSize(new Dimension(150, 100));
		formScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		return formScrollPane;
	}

	/**
	 * Creates the button panel for every panel
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
			doneButton.setToolTipText("Save Employee");
		else
			doneButton.setToolTipText("You must provide the employee's first and last name");
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

	/**
	 * Pulls information from all the fields on the employeeInformation() JInternalFrame
	 * and stores that information so it can be added to an XML document for storage
	 * or reloaded when the user selected the back button from the parent information page.
	 */
	private void storeContent()
	{
		switch (currentPanel)
		{
			case 1: // first panel
				if (content.isEmpty())
					content.add(employeeTextField[0].getText());
				else
					content.set(0, employeeTextField[0].getText());
				for (int i = 1; i < 6; i++)
				{
					if (content.size() <= i)
						content.add(employeeTextField[i].getText());
					else
						content.set(i, employeeTextField[i].getText());
				}
				month = monthBox.getSelectedItem();
				day = dayBox.getSelectedItem();
				year = yearBox.getSelectedItem();
				state[0] = stateBox[0].getSelectedItem();
				if (sexMale.isSelected()) // Determines whether the employee has been selected as male or female
					male = true;
				else
					male = false;
				break;
			case 2: // second panel
				if (content.size() <= 6)
					content.add(employeeTextField[6].getText());
				else
					content.set(6, employeeTextField[6].getText());
					
				if (content.size() <= 7)
					content.add(employeeTextField[7].getText());
				else
					content.set(7, employeeTextField[7].getText());

				if (content.size() <= 8)
					content.add(employeeTextArea[0].getText());
				else
					content.set(8, employeeTextArea[0].getText());

				if (content.size() <= 9)
					content.add(employeeTextField[8].getText());
				else
					content.set(9, employeeTextField[8].getText());

				if (content.size() <= 10)
					content.add(employeeTextField[9].getText());
				else
					content.set(10, employeeTextField[9].getText());

				if (content.size() <= 11)
					content.add(employeeTextField[10].getText());
				else
					content.set(11, employeeTextField[10].getText());
				break;
			case 3: // third panel
				if (content.size() <= 12)
					content.add(employeeTextArea[1].getText());
				else
					content.set(12, employeeTextArea[1].getText());
				break;
			case 4: // fourth panel
				if (content.size() <= 13)
					for (int i = 11; i <= 20; i++)
						content.add(employeeTextField[i].getText());
				else
					for (int i = 11; i <= 20; i++)
						content.set(i + 2, employeeTextField[i].getText());
				break;
			case 5:
				if (content.size() <= 23)
					for (int i = 22; i <= 30; i++)
						content.add(employeeTextField[i].getText());
				else
					for (int i = 22; i <= 30; i++)
						content.set(i + 1, employeeTextField[i].getText());
				state[1] = stateBox[1].getSelectedItem();
				rate = rateBox.getSelectedItem();
				tempFormBoxes = formBoxes;
				break;
			default:
				break;
		}
	}

	/**
	 * Called when the user clicks the back button on the parentInformation() interface. Reloads
	 * the information (which was stored using the storeContent() method) into the specified fields.
	 */
	private void reloadContent()
	{
		switch(currentPanel)
		{
		case 1: // first panel
			if (image != null)
				employeeImage.setIcon(image);
			else
				employeeImage.setIcon(images.loadImage("images/no_photo_icon.png"));

			for (int i = 0; i < 6; i++)
				employeeTextField[i].setText(content.get(i));
			monthBox.setSelectedItem(month);
			dayBox.setSelectedItem(day);
			yearBox.setSelectedItem(year);
			stateBox[0].setSelectedItem(state[0]);
			if (male)
				sexMale.setSelected(true);
			else
				sexFemale.setSelected(true);
			break;
		case 2:// second panel
			if (content.size() < 7)
				break;
			employeeTextField[6].setText(content.get(6));
			employeeTextField[7].setText(content.get(7));
			if (!content.get(7).equals("012-345-6789"))
				employeeTextField[7].setForeground(Color.BLACK);
			employeeTextArea[0].setText(content.get(8));
			employeeTextField[8].setText(content.get(9));
			employeeTextField[9].setText(content.get(10));
			employeeTextField[10].setText(content.get(11));
			break;
		case 3: // third panel
			if (content.size() < 13)
				break;
			employeeTextArea[1].setText(content.get(12));
			break;
		case 4: // fourth panel
			if (content.size() < 14)
				break;
			for (int i = 11; i <= 20; i++)
				employeeTextField[i].setText(content.get(i + 2));
			break;
		case 5:
			if (content.size() < 24)
				break;
			stateBox[1].setSelectedItem(state[1]);
			rateBox.setSelectedItem(rate);
			for (int i = 22; i <= 30; i++)
			{
				employeeTextField[i].setText(content.get(i + 1));
				if (i == 22 && !content.get(23).equals("012-345-6789"))
					employeeTextField[22].setForeground(Color.BLACK);
				if (i == 23 && !content.get(24).equals("012-345-6789"))
					employeeTextField[23].setForeground(Color.BLACK);
				if (i == 28 && !content.get(29).equals("00000.00"))
					employeeTextField[28].setForeground(Color.BLACK);
				if (i == 29 && !content.get(30).equals("mm/dd/yyyy"))
					employeeTextField[29].setForeground(Color.BLACK);
				if (i == 30 && !content.get(31).equals("000.00"))
					employeeTextField[30].setForeground(Color.BLACK);
			}
		}

		if (male)
			sexMale.setSelected(true);
		else
			sexMale.setSelected(false);
	}

	/**
	 * Saves all entered employee information into an XML file named:
	 * lastname_firstname.xml
	 * where the last and first name is of the child.
	 */
	private void saveNewEmployee()
	{
		String name = null;
		Object month, day, year;
		Document doc = new Document();
		Element root = new Element( "employee" );
		doc.setRootElement(root);

		// Save date file was created:
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy 'at' HH:mm aaa");
		Element date_created = new Element( "dateCreated" );
		date_created.setText(formatter.format(currentDate.getTime()));
		root.addContent(date_created);

		// Save employee name:
		Element eName = new Element( "name" );
		root.addContent(eName);
		Element fName = new Element( "first" ).setText(employeeTextField[0].getText());
		Element mName = new Element( "mi" ).setText(employeeTextField[1].getText());
		Element lName = new Element( "last" ).setText(employeeTextField[2].getText());
		eName.addContent(fName);
		eName.addContent(mName);
		eName.addContent(lName);
		name = lName.getText() + "_" + fName.getText();

		// Save employee's local image location:
		Element employee_photo = new Element( "photo" );
		if (file != null)
			employee_photo.setText(file.getPath());
		root.addContent(employee_photo);

		// Save employee birthdate:
		month = monthBox.getSelectedItem();
		day = dayBox.getSelectedItem();
		year = yearBox.getSelectedItem();
		Element employee_birthdate = new Element( "birthdate" );
		employee_birthdate.setText((String)month + " " +
									(String)day + ", " +
									(String)year);
		root.addContent(employee_birthdate);

		// Save employee's sex:
		Element employee_sex = new Element( "sex" );
		if (male)
			employee_sex.setText("male");
		else
			employee_sex.setText("female");
		root.addContent(employee_sex);

		// Save employee's social security number:
		Element employee_ssn = new Element( "ssn" );
		if (employeeTextField[24] == null)
			employee_ssn.setText("");
		else
			employee_ssn.setText(employeeTextField[24].getText() + "-"
								+ employeeTextField[25].getText() + "-"
								+ employeeTextField[26].getText());
		root.addContent(employee_ssn);

		// Save employee's address:
		Element employee_address = new Element( "address" );
		root.addContent(employee_address);
		Element employee_street_address = new Element( "street" ).setText(employeeTextField[3].getText());
		Element employee_city = new Element( "city" ).setText(employeeTextField[4].getText());
		Element employee_state = new Element( "state" ).setText((String)stateBox[0].getSelectedItem());
		Element employee_zipcode = new Element( "zipcode" ).setText(employeeTextField[5].getText());
		employee_address.addContent(employee_street_address);
		employee_address.addContent(employee_city);
		employee_address.addContent(employee_state);
		employee_address.addContent(employee_zipcode);

		// Save employee's drivers license information:
		Element drivers_license = new Element( "driversLicense" );
		root.addContent(drivers_license);
		Element employee_license_number = new Element( "number" ).setText(employeeTextField[27].getText());
		drivers_license.addContent(employee_license_number);
		Element employee_state_issued = new Element( "stateIssued" ).setText((String)state[1]);
		drivers_license.addContent(employee_state_issued);

		// Save employee's medical information:
		Element employee_medical_info = new Element( "medical" );
		root.addContent(employee_medical_info);
		Element employee_doctor_name = new Element( "doctorName" ).setText(employeeTextField[6].getText());
		employee_medical_info.addContent(employee_doctor_name);
		Element employee_doctor_phone = new Element( "doctorPhone" );
		if (!employeeTextField[7].getText().matches("012-345-6789"))
			employee_doctor_phone.setText(employeeTextField[7].getText());
		root.getChild("medical").addContent(new Element("concernsOrAllergies").setText(employeeTextArea[0].getText()));
		root.getChild("medical").addContent(new Element("hospitalPreference").setText(employeeTextField[8].getText()));
		root.getChild("medical").addContent(new Element("insurance"));
		root.getChild("medical").getChild("insurance").addContent(new Element("company").setText(employeeTextField[9].getText()));
		root.getChild("medical").getChild("insurance").addContent(new Element("policyNumber").setText(employeeTextField[10].getText()));
		employee_medical_info.addContent(employee_doctor_phone);

		// Save special information:
		root.addContent(new Element( "special" ).setText(employeeTextArea[1].getText()));

		// Save employee's emergency contact information:
		Element employee_emergency_contacts = new Element( "emergencyContacts" );
		root.addContent(employee_emergency_contacts);
		// First contact
		Element emergency_contact_one = new Element( "one" );
		employee_emergency_contacts.addContent(emergency_contact_one);
		Element e_contact_one_name = new Element( "name" ).setText(employeeTextField[11].getText());
		emergency_contact_one.addContent(e_contact_one_name);
		Element e_contact_one_relation = new Element( "relationship" ).setText(employeeTextField[12].getText());
		emergency_contact_one.addContent(e_contact_one_relation);
		Element e_contact_one_number = new Element( "contactNumber" ).setText(employeeTextField[13].getText());
		emergency_contact_one.addContent(e_contact_one_number);
		Element e_contact_one_secnumber = new Element( "secondaryNumber" ).setText(employeeTextField[14].getText());
		emergency_contact_one.addContent(e_contact_one_secnumber);
		Element e_contact_one_address = new Element( "address" ).setText(employeeTextField[15].getText());
		emergency_contact_one.addContent(e_contact_one_address);
		// Second contact
		Element emergency_contact_two = new Element( "two" );
		employee_emergency_contacts.addContent(emergency_contact_two);
		Element e_contact_two_name = new Element( "name" ).setText(employeeTextField[16].getText());
		emergency_contact_two.addContent(e_contact_two_name);
		Element e_contact_two_relation = new Element( "relationship" ).setText(employeeTextField[17].getText());
		emergency_contact_two.addContent(e_contact_two_relation);
		Element e_contact_two_number = new Element( "contactNumber" ).setText(employeeTextField[18].getText());
		emergency_contact_two.addContent(e_contact_two_number);
		Element e_contact_two_secnumber = new Element( "secondaryNumber" ).setText(employeeTextField[19].getText());
		emergency_contact_two.addContent(e_contact_two_secnumber);
		Element e_contact_two_address = new Element( "address" ).setText(employeeTextField[20].getText());
		emergency_contact_two.addContent(e_contact_two_address);

		Element contact_info = new Element( "contact" );
		contact_info.addContent(new Element("home").setText(employeeTextField[22].getText()));
		contact_info.addContent(new Element("cell").setText(employeeTextField[23].getText()));
		root.addContent(contact_info);

		// Save employee's hire date:
		Element employee_hire_date = new Element( "hireDate" );
		if (!employeeTextField[29].getText().matches("mm/dd/yyyy"))
			employee_hire_date.setText(employeeTextField[29].getText());
		root.addContent(employee_hire_date);

		// Save employee's w4 allowances:
		Element employee_w4_allowance = new Element( "w4allowance" );
		if (!employeeTextField[30].getText().matches("000.00"))
			employee_w4_allowance.setText(employeeTextField[30].getText());
		root.addContent(employee_w4_allowance);

		// Save employee's salary:
		Element quoted_rate;
		Element employee_salary = new Element( "salary" );
		root.addContent(employee_salary);
		if (employeeTextField[28].getText().matches("00000.00"))
			quoted_rate = new Element( "rate" ).setText("00.00");
		else
			quoted_rate = new Element( "rate" ).setText(employeeTextField[28].getText());
		Element quoted_interval = new Element( "payCycle" ).setText((String)rateBox.getSelectedItem());
		employee_salary.addContent(quoted_rate);
		employee_salary.addContent(quoted_interval);

		Element form_element = new Element( "forms" );
		root.addContent(form_element);

		int m = 0;
		for (JCheckBox i : formBoxes)
		{
			String str = formBoxes.get(m).getText().replaceAll(" ", "_");

			form_element.addContent(new Element( str ).setText(Boolean.toString(i.isSelected())));
			m++;
		}

		saveXMLFile(name, doc);
	}

	/**
	 * Save the employee's XML file to the Employee folder.
	 */
	private void saveXMLFile(String employeename, Document doc)
	{
		try
		{
			FileWriter writer = new FileWriter(new File_System_Controller().getModel().getDirectoryPath("Employees") + employeename + ".xml");
			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat(Format.getPrettyFormat());
			outputter.output(doc, writer); // Save the new document
			System.out.println( "SYSTEM: Saved new employee XML file:\n" + employeename + ".xml" );
		} catch (IOException e) {
			System.out.println( "SYSTEM: IOException while writing new XML file." );
		}
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
				currentPanel++;
			}
			else if (currentPanel == 2)
			{
				panel = thirdScreen();
				currentPanel++;
			}
			else if (currentPanel == 3)
			{
				panel = fourthScreen();
				currentPanel++;
			}
			else if (currentPanel == 4)
			{
				panel = fifthScreen();
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
			else if (currentPanel == 5)
			{
				panel = fourthScreen();
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
			if (employeeTextField[0].getText().equals("") || employeeTextField[2].getText().equals(""))
			{
				JOptionPane.showMessageDialog(desktop, "You must specify the employee's first and last name.", "Error!", JOptionPane.ERROR_MESSAGE );
				return;
			}
			// Save child and parent information to an XML file
			saveNewEmployee();
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
		else if (e.getActionCommand().equals("employeeimage"))
		{
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(new File_System_Controller().getModel().getDefaultPictureDirectory()));
			int returnVal = fc.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				image = new ImageIcon(file.getPath());
				Image img = image.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH); // Resize the image to fit the childImage button
				image = new ImageIcon(img);
				employeeImage.setIcon(image);
				System.out.println( "SYSTEM: Image selected from path: " + file.getPath() );
			} else {
				// Have this here just in case
				employeeImage.setIcon(image);
			}
		}
		else if (e.getActionCommand().equals("addForm"))
		{
			String str = employeeTextField[21].getText();

			if (str.equals("New..."))
			{
				JOptionPane.showMessageDialog(desktop, "Please give the form/certification a name in the provided text field.", "Error!", JOptionPane.ERROR_MESSAGE );
				return;
			}

			if (new File_System_Controller().getModel().appendLine(str, "forms.txt"))
			{
				formPanel.add(new JCheckBox(str));
				formScrollPane.revalidate();
				employeeTextField[21].setText("New...");
				employeeTextField[21].setForeground(Color.LIGHT_GRAY);
				formBoxes.add(new JCheckBox(str));
			}
		}
		else if (e.getActionCommand().equals("removeForm"))
		{
			for (int i = formBoxes.size() - 1; i >= 0; i--)
			{
				if (formBoxes.get(i).isSelected())
				{
					System.out.println( "Removing: " + formBoxes.get(i).getText());
					new File_System_Controller().getModel().removeLineFromFile("forms.txt", formBoxes.get(i).getText());
					formPanel.remove(i);
					formPanel.repaint();
					formPanel.revalidate();
					formScrollPane.revalidate();
					formBoxes.remove(i);
					if (tempFormBoxes != null)
						tempFormBoxes.remove(i);
				}
			}
		}
		for (int s = 0; s < scheduleArr.length; s++) // Check all checkboxs
		{
			if (e.getActionCommand().equals(scheduleArr[s]))
			{
				int i = Integer.parseInt(e.getActionCommand());
				
				if (!scheduleField[i - 3].isEnabled())
					scheduleField[i - 3].setEnabled(true);
				else
					scheduleField[i - 3].setEnabled(false);
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
		if (employeeTextField[0].getText().length() > 0 && employeeTextField[2].getText().length() > 0)
		{
			doneButton.setEnabled(true);
			doneButtonEnabled = true;
			doneButton.setToolTipText("Save Employee");
		} else {
			doneButton.setEnabled(false);
			doneButtonEnabled = false;
			doneButton.setToolTipText("You must provide the employee's first and last name");
		}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}

	@Override
	public void focusGained(FocusEvent e)
	{
		if (e.getSource().equals(employeeTextField[7]) && employeeTextField[7].getText().matches("012-345-6789"))
		{
			employeeTextField[7].setText("");
			employeeTextField[7].setForeground(Color.BLACK);
		}
		else if (e.getSource().equals(employeeTextField[8]) && employeeTextField[8].getText().matches("00000.00"))
		{
			employeeTextField[8].setText("");
			employeeTextField[8].setForeground(Color.BLACK);
		}
		else if (e.getSource().equals(employeeTextField[21]) && employeeTextField[21].getText().matches("New..."))
		{
			employeeTextField[21].setText("");
			employeeTextField[21].setForeground(Color.BLACK);
		}
		else if (e.getSource().equals(employeeTextField[22]) && employeeTextField[22].getText().matches("012-345-6789"))
		{
			employeeTextField[22].setText("");
			employeeTextField[22].setForeground(Color.BLACK);
		}
		else if (e.getSource().equals(employeeTextField[23]) && employeeTextField[23].getText().matches("012-345-6789"))
		{
			employeeTextField[23].setText("");
			employeeTextField[23].setForeground(Color.BLACK);
		}
		else if (e.getSource().equals(employeeTextField[28]) && employeeTextField[28].getText().matches("00000.00"))
		{
			employeeTextField[28].setText("");
			employeeTextField[28].setForeground(Color.BLACK);
		}
		else if (e.getSource().equals(employeeTextField[29]) && employeeTextField[29].getText().matches("mm/dd/yyyy"))
		{
			employeeTextField[29].setText("");
			employeeTextField[29].setForeground(Color.BLACK);
		}
		else if (e.getSource().equals(employeeTextField[30]) && employeeTextField[30].getText().matches("000.00"))
		{
			employeeTextField[30].setText("");
			employeeTextField[30].setForeground(Color.BLACK);
		}
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		if (e.getSource().equals(employeeTextField[7]) && employeeTextField[7].getText().matches(""))
		{
			employeeTextField[7].setText("012-345-6789");
			employeeTextField[7].setForeground(Color.LIGHT_GRAY);
		}
		else if (e.getSource().equals(employeeTextField[8]) && employeeTextField[8].getText().matches(""))
		{
			employeeTextField[8].setText("00000.00");
			employeeTextField[8].setForeground(Color.LIGHT_GRAY);
		}
		else if (e.getSource().equals(employeeTextField[21]) && employeeTextField[21].getText().matches(""))
		{
			employeeTextField[21].setText("New...");
			employeeTextField[21].setForeground(Color.LIGHT_GRAY);
		}
		else if (e.getSource().equals(employeeTextField[22]) && employeeTextField[22].getText().matches(""))
		{
			employeeTextField[22].setText("012-345-6789");
			employeeTextField[22].setForeground(Color.LIGHT_GRAY);
		}
		else if (e.getSource().equals(employeeTextField[23]) && employeeTextField[23].getText().matches(""))
		{
			employeeTextField[23].setText("012-345-6789");
			employeeTextField[23].setForeground(Color.LIGHT_GRAY);
		}
		else if (e.getSource().equals(employeeTextField[28]) && employeeTextField[28].getText().matches(""))
		{
			employeeTextField[28].setText("00000.00");
			employeeTextField[28].setForeground(Color.LIGHT_GRAY);
		}
		else if (e.getSource().equals(employeeTextField[29]) && employeeTextField[29].getText().matches(""))
		{
			employeeTextField[29].setText("mm/dd/yyyy");
			employeeTextField[29].setForeground(Color.LIGHT_GRAY);
		}
		else if (e.getSource().equals(employeeTextField[30]) && employeeTextField[30].getText().matches(""))
		{
			employeeTextField[30].setText("000.00");
			employeeTextField[30].setForeground(Color.LIGHT_GRAY);
		}
	}
	
}