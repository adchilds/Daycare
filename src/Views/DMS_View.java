package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import models.Language_Model;
import controllers.About_Controller;
import controllers.Bug_Report_Controller;
import controllers.Child_Controller;
import controllers.Employee_Controller;
import controllers.File_System_Controller;
import controllers.Image_Controller;
import controllers.Language_Controller;
import controllers.Properties_Controller;
import controllers.Table_Controller;


/**
 * <p>Controls the main view of the Daycare Management System program.
 * 
 * @author Adam Childs
 */
public class DMS_View implements ActionListener
{
	Container contentPane = null;
	Image_Controller images = new Image_Controller();
	static JDesktopPane desktop = null;
	JFrame f = null;
	Language_Model lang_model = new Language_Controller().getModel();

	/**
	 * <p>Instantiates the DMS_View, which calls the createAndShowGUI(t, d)
	 * method to display the program to the user.
	 * 
	 * @param t The title of the program to be displayed in the frame
	 * @param d The dimensions of the frame/window (width, height)
	 * @param i The Image to be displayed in the user's "explorer" bar or "dock"
	 */
	public DMS_View(String t, Dimension d, ImageIcon i)
	{
		createAndShowGUI(t, d, i);
	}

	/**
	 * <p>Creates the main view of the Daycare Management System program
	 * and displays it to the user.
	 * 
	 * @param t The title of the program to be displayed in the frame
	 * @param d The dimensions of the frame/window (width, height)
	 * @param i The Image to be displayed in the user's "explorer" bar or "dock"
	 */
	private void createAndShowGUI(String t, Dimension d, ImageIcon i)
	{
		f = new JFrame();
		desktop = new JDesktopPane(); // Initialize a JDesktopPane where we will be adding the JInternalFrame's
		desktop.setBackground(new Color(200, 200, 200)); // Set the background color of the JDesktopPane
		contentPane = f.getContentPane();

		/*
		 * Add components to JFrame
		 */
		f.setJMenuBar(menuBar());
		contentPane.add(toolBar(), BorderLayout.NORTH);
		contentPane.add(desktop);

		f.setTitle(t);
		f.setSize(d);
		f.setMinimumSize(d);
		if (i != null)
			f.setIconImage(i.getImage()); // 64 x 64
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	/**
	 * <p>Constructs a JMenuBar that can be added to a JFrame
	 * 
	 * @return a JMenuBar
	 */
	private JMenuBar menuBar()
	{
		JMenuBar mb = new JMenuBar();
		
		JMenu fileMenu, editMenu, viewMenu, helpMenu, newEntry, viewDatabase;
		JMenuItem newChild, newEmployee, newAccount, openChild, viewFileChild, viewFileEmployee, saveChild, saveAs, printDocument, properties,
				viewDatabaseChild, viewDatabaseEmployee, viewFinances, viewCalendar, exit, cut, copy, paste, help, faq, report, about, checkupdate;
		
		// Initialize JMenu's
		fileMenu = new JMenu( lang_model.getValue(12) );
		editMenu = new JMenu( lang_model.getValue(23) );
		viewMenu = new JMenu( lang_model.getValue(27) );
		helpMenu = new JMenu( lang_model.getValue(33) );
		
		fileMenu.setRolloverEnabled(true);
		editMenu.setRolloverEnabled(true);
		helpMenu.setRolloverEnabled(true);

		// Initialize sub-JMenu's
		newEntry = new JMenu( lang_model.getValue(13) );
		newEntry.setIcon(images.loadImage("Images/menubar/application_icon.png") );
		openChild = new JMenu( lang_model.getValue(14) );
		openChild.setIcon(images.loadImage("Images/menubar/XML_icon_small.png") );
		viewDatabase = new JMenu( lang_model.getValue(29) );
		viewDatabase.setIcon(images.loadImage("Images/menubar/database_icon.png") );

		// Initialize JMenuItem's
		newChild = new JMenuItem( lang_model.getValue(20) );
		newEmployee = new JMenuItem( lang_model.getValue(21) );
		newAccount = new JMenuItem( lang_model.getValue(22) );
		saveChild = new JMenuItem( lang_model.getValue(15), images.loadImage("Images/menubar/save_icon.png") );
		saveAs = new JMenuItem( lang_model.getValue(16) );
		printDocument = new JMenuItem( lang_model.getValue(18), images.loadImage("Images/menubar/print_icon.png") );
		properties = new JMenuItem( lang_model.getValue(17), images.loadImage("Images/menubar/properties_icon.png") );
		viewCalendar = new JMenuItem( lang_model.getValue(28), images.loadImage("Images/menubar/calendar_icon.png") );
		viewDatabaseChild = new JMenuItem( lang_model.getValue(31) );
		viewDatabaseEmployee = new JMenuItem( lang_model.getValue(32) );
		viewFileChild = new JMenuItem( lang_model.getValue(20) );
		viewFileEmployee = new JMenuItem( lang_model.getValue(21) );

		saveChild.setEnabled(false);
		saveAs.setEnabled(false);
		printDocument.setEnabled(false);
		viewCalendar.setEnabled(false);

		viewFinances = new JMenuItem( lang_model.getValue(30), images.loadImage("Images/menubar/finances_icon_small.png") );

		exit = new JMenuItem( lang_model.getValue(19) );
		cut = new JMenuItem ( lang_model.getValue(24), images.loadImage("Images/menubar/cut_icon.png") );
		copy = new JMenuItem( lang_model.getValue(25), images.loadImage("Images/menubar/copy_icon.png") );
		paste = new JMenuItem( lang_model.getValue(26), images.loadImage("Images/menubar/paste_icon.png") );

		viewFinances.setEnabled(false);
		cut.setEnabled(false);
		copy.setEnabled(false);
		paste.setEnabled(false);

		help = new JMenuItem( lang_model.getValue(33), images.loadImage("Images/menubar/help_icon.png") );
		faq = new JMenuItem( lang_model.getValue(34) );
		report = new JMenuItem( lang_model.getValue(35) );
		about = new JMenuItem( lang_model.getValue(36), images.loadImage("Images/menubar/exclamation_icon.png") );
		checkupdate = new JMenuItem( lang_model.getValue(105) );

		faq.setEnabled(false);
		checkupdate.setEnabled(false);

		// Add JMenuItem's to the JMenu's
		fileMenu.add(newEntry);
			newEntry.add(newChild); // Sub-Menu of fileMenu
			newEntry.add(newEmployee); // Sub-Menu of fileMenu
			newEntry.add(newAccount); // Sub-Menu of fileMenu
		fileMenu.add(openChild);
			openChild.add(viewFileChild);
			openChild.add(viewFileEmployee);
		//fileMenu.add(view);
		fileMenu.addSeparator();
		fileMenu.add(saveChild);
		fileMenu.add(saveAs);
		fileMenu.addSeparator();
		fileMenu.add(printDocument);
		fileMenu.addSeparator();
		fileMenu.add(properties);
		fileMenu.addSeparator();
		fileMenu.add(exit);
		editMenu.add(cut);
		editMenu.add(copy);
		editMenu.add(paste);
		viewMenu.add(viewCalendar);
		viewMenu.add(viewDatabase);
			viewDatabase.add(viewDatabaseChild);
			viewDatabase.add(viewDatabaseEmployee);
		viewMenu.add(viewFinances);
		helpMenu.add(help);
		helpMenu.add(faq);
		helpMenu.add(report);
		helpMenu.add(about);
		helpMenu.add(checkupdate);
		
		// Add JMenu's to the MenuBar
		mb.add(fileMenu);
		mb.add(editMenu);
		mb.add(viewMenu);
		mb.add(helpMenu);

		// Mnemonics
		fileMenu.setMnemonic('F');
			newEntry.setMnemonic('N');
		editMenu.setMnemonic('E');
		viewMenu.setMnemonic('V');
		helpMenu.setMnemonic('H');
		
		// Accelerators
		newChild.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
		newEmployee.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
		saveChild.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		printDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		viewFinances.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
		viewCalendar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));

		// Action Listening
		newChild.setActionCommand("newchild");
		newChild.addActionListener(this);
		newEmployee.setActionCommand("newemployee");
		newEmployee.addActionListener(this);
		newAccount.setActionCommand("newaccount");
		newAccount.addActionListener(this);
		openChild.setActionCommand("openfile");
		openChild.addActionListener(this);
		viewFileChild.setActionCommand("viewfilechild");
		viewFileChild.addActionListener(this);
		viewFileEmployee.setActionCommand("viewfileemployee");
		viewFileEmployee.addActionListener(this);
		viewDatabaseChild.setActionCommand("viewchilddatabase");
		viewDatabaseChild.addActionListener(this);
		viewDatabaseEmployee.setActionCommand("viewemployeedatabase");
		viewDatabaseEmployee.addActionListener(this);
		viewFinances.setActionCommand("viewfinances");
		viewFinances.addActionListener(this);
		viewCalendar.setActionCommand("viewcalendar");
		viewCalendar.addActionListener(this);
		printDocument.setActionCommand("printdocument");
		printDocument.addActionListener(this);
		properties.setActionCommand("properties");
		properties.addActionListener(this);
		exit.setActionCommand("exit");
		exit.addActionListener(this);
		help.setActionCommand("help");
		help.addActionListener(this);
		faq.setActionCommand("faq");
		faq.addActionListener(this);
		report.setActionCommand("reportbug");
		report.addActionListener(this);
		about.setActionCommand("about");
		about.addActionListener(this);
		checkupdate.setActionCommand("checkupdate");
		checkupdate.addActionListener(this);

		return mb;
	}

	/**
	 * <p>Constructs a JToolBar that can be added to a JFrame
	 * 
	 * @return a JToolBar
	 */
	private JToolBar toolBar()
	{
		JToolBar toolbar = new JToolBar();
		JButton button;

		button = new JButton(images.loadImage("Images/toolbar/add_child_icon.png"));
		button.setToolTipText( lang_model.getValue(41) );
		button.setFocusPainted(false);
		button.setActionCommand("newchild");
		button.addActionListener(this);
		toolbar.add(button);

		button = new JButton(images.loadImage("Images/toolbar/add_employee_icon.png"));
		button.setToolTipText( lang_model.getValue(147) );
		button.setFocusPainted(false);
		button.setActionCommand("newemployee");
		button.addActionListener(this);
		toolbar.add(button);

		button = new JButton(images.loadImage("Images/toolbar/XML_icon_large.png"));
		button.setToolTipText( lang_model.getValue(148) );
		button.setFocusPainted(false);
		button.setActionCommand("openfile");
		button.addActionListener(this);
		toolbar.add(button);

		button = new JButton(images.loadImage("Images/toolbar/calendar_icon_large.png"));
		button.setToolTipText( lang_model.getValue(149) );
		button.setFocusPainted(false);
		button.setActionCommand("viewcalendar");
		button.addActionListener(this);
		button.setEnabled(false);
		toolbar.add(button);

		button = new JButton(images.loadImage("Images/toolbar/database_icon_large.png"));
		button.setToolTipText( lang_model.getValue(150) );
		button.setFocusPainted(false);
		button.setActionCommand("viewdatabase");
		button.addActionListener(this);
		toolbar.add(button);

		button = new JButton(images.loadImage("Images/toolbar/finances_icon_large.png"));
		button.setToolTipText( lang_model.getValue(151) );
		button.setFocusPainted(false);
		button.setActionCommand("viewfinances");
		button.addActionListener(this);
		button.setEnabled(false);
		toolbar.add(button);

		toolbar.setRollover(true);
		toolbar.setFloatable(false);
		toolbar.setSize(f.getWidth(), 15);

		return toolbar;
	}

	/**
	 * <p>Add text from a specified file to the specified JTextArea
	 * 
	 * @param t The JTextArea to add the content to
	 * @param f The text file name
	 */
	private void addContent(JTextArea t, String f)
	{
		String line;
		try {
			InputStream iStream = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(iStream);
			BufferedReader reader = new BufferedReader(isr);
			
			while ((line = reader.readLine()) != null)
			{
				t.append(line);
				t.append("\n");
			}
			iStream.close();
			isr.close();
			reader.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	/**
	 * <p>Allows other classes to get the frame in-order to use it
	 * as a parent component for displaying windows on top of it,
	 * for example.
	 * 
	 * @return The JFrame instance
	 */
	public JFrame getFrame()
	{
		return f;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("about"))
		{
			new About_Controller().showView();
		}
		else if (e.getActionCommand().equals("checkupdate"))
		{
			
		}
		else if (e.getActionCommand().equals("exit"))
		{
			System.exit(0);
		}
		else if (e.getActionCommand().equals("faq"))
		{
			
		}
		else if (e.getActionCommand().equals("help"))
		{
			// Open a general information window
			JTextArea textArea = new JTextArea();
			addContent(textArea, new File_System_Controller().getModel().findFile("README.txt"));
			textArea.setEditable(false);
			textArea.setCaretPosition(java.awt.Frame.NORMAL);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			JScrollPane sc = new JScrollPane(textArea);
			sc.setPreferredSize(new Dimension(540, 250)); // Width, Height
			sc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			String[] option = { lang_model.getValue(124) };
			JOptionPane.showOptionDialog(f, sc, lang_model.getValue(33), JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
		}
		else if (e.getActionCommand().equals("newchild"))
		{
			JInternalFrame i = (JInternalFrame)new Child_Controller().showView();
			desktop.add(i);
			try {
				i.setSelected(true);
			} catch (PropertyVetoException e1) {
				e1.printStackTrace();
			}
		}
		else if (e.getActionCommand().equals("newemployee"))
		{
			JInternalFrame i = (JInternalFrame)new Employee_Controller().showView();
			desktop.add(i);
			try {
				i.setSelected(true);
			} catch (PropertyVetoException e1) {
				e1.printStackTrace();
			}
		}
		else if (e.getActionCommand().equals("newaccount"))
		{
			new Account_View().show();
		}
		else if (e.getActionCommand().equals("properties"))
		{
			new Properties_Controller().showView();
		}
		else if (e.getActionCommand().equals("reportbug"))
		{
			new Bug_Report_Controller().showView();
		}
		else if (e.getActionCommand().equals("viewdatabase"))
		{
			new Table_Controller(desktop).showMenu();
		}
		else if (e.getActionCommand().equals("viewchilddatabase"))
		{
			new Table_Controller(desktop).showView((byte)0);
		}
		else if (e.getActionCommand().equals("viewemployeedatabase"))
		{
			new Table_Controller(desktop).showView((byte)1);
		}
	}

}