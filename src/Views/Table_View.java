package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import lib.OSProperties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import controllers.File_System_Controller;
import controllers.Image_Controller;


/**
 * <p>Controls the display of the children and employee tables.
 * 
 * @author Adam Childs
 */
public class Table_View implements ActionListener, InternalFrameListener
{
	DefaultTableModel model;
	Image_Controller images = new Image_Controller();
	JDesktopPane desktop;
	JTable table;
	OSProperties osp = new OSProperties();
	String[] childColumnNames = { "Last Name", "First Name",
			"MI", "Photo", "Birthdate", "Sex", "Street Address",
			"City", "ST", "Zipcode", "Doctor Name", "Doctor Phone",
			"Medical Concerns/Allergies", "Special Family Situations", "Quoted Rate"
	}; // 15
	String[] employeeColumnNames = { "Last Name", "First Name", "MI", "Birthdate", "Sex",
			"Social Security #", "Street Address", "City", "ST", "Zipcode", "Drivers License #",
			"State Issued", "Doctor Name", "Doctor Phone", "Emergency Contact One", "Relationship",
			"Contact #", "Secondary Contact #", "Emergency Contact Two", "Relationship", "Contact #",
			"Secondary Contact #", "W4 Allowances", "Hire Date", "Salary", "Photo"
	}; // 26

	public Table_View(JDesktopPane d)
	{
		desktop = d;
	}

	/*
	 * Displays a dialog asking the user which table they would like to view
	 */
	public void showMenu()
	{
		Object[] options = {"Children", "Employees"};
		int n = JOptionPane.showOptionDialog(desktop.getParent(),
				"Which database would you like to view?",
				"Choose Database",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				null);
		if (n == JOptionPane.CLOSED_OPTION)
			return;

		showTable((byte)n);
	}

	/**
	 * <p>Shows the correct database table, specified by the parameter (0 or 1).
	 * 
	 * @param database a byte value indicating which database table to show
	 */
	public void showTable(byte database)
	{
		if (database == (byte)0)
			desktop.add(childFrame());
		else
			desktop.add(employeeFrame());
	}

	public JInternalFrame childFrame()
	{
		JInternalFrame f = new JInternalFrame("Child Database", 
				false, // Resizable
				true, // Closable
				true, // Maximizable
				true); // Iconifiable
		f.setLayout(new BorderLayout());

		JScrollPane sp = new JScrollPane(childTable());
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setBorder(BorderFactory.createTitledBorder("Children"));

		f.add(buttonPanel(true), BorderLayout.NORTH);
		f.add(sp);

//		f.setLocation(loc);
		f.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		f.setFrameIcon(images.loadImage("../images/menubar/database_icon.png"));
		f.addInternalFrameListener(this);

		if (osp.isLowResolution())
			f.setSize(550, 282);
		else if (osp.isMidResolution())
			f.setSize(550, 282);
		else
			f.setSize(850, 400); // Width, Height

		f.setVisible(true);

		try {
			f.setSelected(true);
		} catch (PropertyVetoException e1) {
			e1.printStackTrace();
		}

		return f;
	}

	/*
	 * Returns a JTable which displays information from all
	 * .xml files in the Children/ directory.
	 */
	private JTable childTable()
	{
		model = new DefaultTableModel(childData(), childColumnNames); // Rows, Columns
		table = new JTable(model) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
				Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
				if (rowIndex % 2 == 0 && !isCellSelected(rowIndex, vColIndex)) {
					c.setBackground(new Color(220, 220, 220));
				} else {
					// If not shaded, match the table's background
					c.setBackground(getBackground());
				}
				return c;
			}
		};
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn header;
		for (int i = 0; i < childColumnNames.length; i++)
		{
			header = table.getColumnModel().getColumn(i);
			header.setPreferredWidth(150);
		}

		/*
		 * The column index on the table that we want to change
		 */
		int[] column = { 2, 5, 8, 9, 14 };
		/*
		 * The new width that we want to set the column to be
		 */
		int[] cwidth = { 25, 60, 25, 60, 100 }; // width to set the column

		for(int i = 0; i < column.length; i++)
		{
			header = table.getColumnModel().getColumn(column[i]);
			header.setPreferredWidth(cwidth[i]);
		}

		table.setRowSelectionAllowed(true);
		return table;
	}

	/*
	 * Returns a multi-dimensional array holding all information from all files
	 * in the Children/ directory. This information will be dispalyed in a table
	 * when the users selects to View Database.
	 */
	private Object[][] childData()
	{
		SAXBuilder builder = new SAXBuilder();
		File[] file = new File_System_Controller().getModel().getAllFiles("Children"); // get all files in the "Children/" directory
		Object[][] data = new Object[file.length][childColumnNames.length]; // Rows, Columns ([file.length][15])
		for (int i = 0; i < file.length; i++)
		{
			try {
				Document doc = builder.build(file[i]);
				Element root = doc.getRootElement();
				data[i][0] = root.getChild("childname").getChild("last").getText();
				data[i][1] = root.getChild("childname").getChild("first").getText();
				data[i][2] = root.getChild("childname").getChild("mi").getText();
				data[i][3] = root.getChild("photo").getText();
				if (root.getChild("birthdate").getText().contains("null"))
					data[i][4] = "No entry";
				else
					data[i][4] = root.getChild("birthdate").getText();
				data[i][5] = root.getChild("sex").getText();
				data[i][6] = root.getChild("childaddress").getChild("street").getText();
				data[i][7] = root.getChild("childaddress").getChild("city").getText();
				data[i][8] = root.getChild("childaddress").getChild("state").getText();
				data[i][9] = root.getChild("childaddress").getChild("zipcode").getText();
				data[i][10] = root.getChild("medical").getChild("doctorName").getText();
				data[i][11] = root.getChild("medical").getChild("doctorPhone").getText();
				data[i][12] = root.getChild("medical").getChild("concernsOrAllergies").getText();
				data[i][13] = root.getChild("special").getChild("familyArrangement").getText();
				data[i][14] = root.getChild("quote").getChild("rate").getText() + " " +
							root.getChild("quote").getChild("payCycle").getText();
			} catch (IOException e) {
				System.out.println( "SYSTEM: File not found error." );
			} catch (JDOMException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	public JInternalFrame employeeFrame()
	{
		JInternalFrame f = new JInternalFrame();
		
		f = new JInternalFrame("Employee Database", 
				false, // Resizable
				true, // Closable
				true, // Maximizable
				true); // Iconifiable
		f.setLayout(new BorderLayout());

		JScrollPane sp = new JScrollPane(employeeTable());
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setBorder(BorderFactory.createTitledBorder("Employees"));

		f.add(buttonPanel(false), BorderLayout.NORTH);
		f.add(sp);

//		f.setLocation(loc);
		f.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		f.setFrameIcon(images.loadImage("../images/menubar/database_icon.png"));
		f.addInternalFrameListener(this);

		if (osp.isLowResolution())
			f.setSize(550, 282);
		else if (osp.isMidResolution())
			f.setSize(550, 282);
		else
			f.setSize(850, 500); // Width, Height
		f.setVisible(true);

		try {
			f.setSelected(true);
		} catch (PropertyVetoException e1) {
			e1.printStackTrace();
		}

		return f;
	}

	/*
	 * Returns a JTable which displays information from all
	 * .xml files in the Children/ directory.
	 */
	private JTable employeeTable()
	{
		model = new DefaultTableModel(employeeData(), employeeColumnNames); // Rows, Columns
		table = new JTable(model) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
				Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
				if (rowIndex % 2 == 0 && !isCellSelected(rowIndex, vColIndex)) {
					c.setBackground(new Color(220, 220, 220));
				} else {
					// If not shaded, match the table's background
					c.setBackground(getBackground());
				}
				return c;
			}
		};
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn header;
		for (int i = 0; i < employeeColumnNames.length; i++)
		{
			header = table.getColumnModel().getColumn(i);
			header.setPreferredWidth(150);
		}

		/*
		 * The column index on the table that we want to change
		 */
		int[] column = { 2, 4, 5, 8, 9, 11 };
		/*
		 * The new width that we want to set the column to be
		 */
		int[] cwidth = { 25, 60, 100, 50, 60, 65 }; // width to set the column

		for(int i = 0; i < column.length; i++)
		{
			header = table.getColumnModel().getColumn(column[i]);
			header.setPreferredWidth(cwidth[i]);
		}

		table.setRowSelectionAllowed(true);
		return table;
	}

	/*
	 * Returns a multi-dimensional array holding all information from all files
	 * in the Employees/ directory. This information will be dispalyed in a table
	 * when the users selects to View Database.
	 */
	private Object[][] employeeData()
	{
		SAXBuilder builder = new SAXBuilder();
		File[] file = new File_System_Controller().getModel().getAllFiles("Employees"); // get all files in the "Employees/" directory
		Object[][] data = new Object[file.length][employeeColumnNames.length]; // Rows, Columns ([file.length][15])

		for (int i = 0; i < file.length; i++)
		{
			try {
				Document doc = builder.build(file[i]);
				Element root = doc.getRootElement();
				data[i][0] = root.getChild("name").getChild("last").getText();
				data[i][1] = root.getChild("name").getChild("first").getText();
				data[i][2] = root.getChild("name").getChild("mi").getText();
				if (root.getChild("birthdate").getText().contains("null"))
					data[i][3] = "No entry";
				else
					data[i][3] = root.getChild("birthdate").getText();
				data[i][4] = root.getChild("sex").getText();
				data[i][5] = root.getChild("ssn").getText();
				data[i][6] = root.getChild("address").getChild("street").getText();
				data[i][7] = root.getChild("address").getChild("city").getText();
				data[i][8] = root.getChild("address").getChild("state").getText();
				data[i][9] = root.getChild("address").getChild("zipcode").getText();
				data[i][10] = root.getChild("driversLicense").getChild("number").getText();
				data[i][11] = root.getChild("driversLicense").getChild("stateIssued").getText();
				data[i][12] = root.getChild("medical").getChild("doctorName").getText();
				data[i][13] = root.getChild("medical").getChild("doctorPhone").getText();
				data[i][14] = root.getChild("emergencyContacts").getChild("one").getChild("name").getText();
				data[i][15] = root.getChild("emergencyContacts").getChild("one").getChild("relationship").getText();
				data[i][16] = root.getChild("emergencyContacts").getChild("one").getChild("contactNumber").getText();
				data[i][17] = root.getChild("emergencyContacts").getChild("one").getChild("secondaryNumber").getText();
				data[i][18] = root.getChild("emergencyContacts").getChild("two").getChild("name").getText();
				data[i][19] = root.getChild("emergencyContacts").getChild("two").getChild("relationship").getText();
				data[i][20] = root.getChild("emergencyContacts").getChild("two").getChild("contactNumber").getText();
				data[i][21] = root.getChild("emergencyContacts").getChild("two").getChild("secondaryNumber").getText();
				data[i][22] = root.getChild("w4allowance").getText();
				data[i][23] = root.getChild("hireDate").getText();
				data[i][24] = root.getChild("salary").getChild("rate").getText() + " " +
								root.getChild("salary").getChild("payCycle").getText();
				data[i][25] = root.getChild("photo").getText();
			} catch (IOException e) {
				System.out.println( "SYSTEM: File not found error." );
			} catch (JDOMException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	// TODO: This
/*
	private void refreshTable()
	{
		Point temp = oldLocation();
		newDatabaseFrame.dispose();
		if (showDatabase == 0)
			desktop.add(viewChildDatabase());
		else if (showDatabase == 1)
			desktop.add(viewEmployeeDatabase());
		newDatabaseFrame.setLocation(temp);
		try {
			newDatabaseFrame.setSelected(true);
		} catch (PropertyVetoException e1) {
			e1.printStackTrace();
		}
	}
*/
	/*
	 * Returns a JPanel that holds JButtons. These buttons
	 * are used for various tasks such as adding a new child, deleting
	 * a child, refreshing the database, etc.
	 */
	private JPanel buttonPanel(boolean b)
	{
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel trailPanel = new JPanel(new FlowLayout());

		if (b)
		{
			JButton addChildButton = new JButton(images.loadImage("../images/toolbar/add_child_icon.png"));
			addChildButton.setPreferredSize(new Dimension(30, 30));
			addChildButton.setFocusPainted(false);
			addChildButton.setToolTipText( "Add New Child" );
			addChildButton.setActionCommand("newchild");
			addChildButton.addActionListener(this);
			addChildButton.setEnabled(false);
			trailPanel.add(addChildButton);
		} else {
			JButton addEmployeeButton = new JButton(images.loadImage("../images/toolbar/add_employee_icon.png"));
			addEmployeeButton.setPreferredSize(new Dimension(30, 30));
			addEmployeeButton.setFocusPainted(false);
			addEmployeeButton.setToolTipText( "Add New Employee" );
			addEmployeeButton.setActionCommand("newemployee");
			addEmployeeButton.addActionListener(this);
			addEmployeeButton.setEnabled(false);
			trailPanel.add(addEmployeeButton);
		}

		// TODO: opening a file but currently selected row
		JButton openButton = new JButton(images.loadImage("../images/toolbar/XML_icon_large.png"));
		openButton.setPreferredSize(new Dimension(30, 30));
		openButton.setFocusPainted(false);
		openButton.setToolTipText( "Open Selected File" );
		openButton.setActionCommand("open");
		openButton.addActionListener(this);
		trailPanel.add(openButton);

		JButton refreshButton = new JButton(images.loadImage("../images/database/database_refresh_icon.png"));
		refreshButton.setPreferredSize(new Dimension(30, 30));
		refreshButton.setFocusPainted(false);
		refreshButton.setToolTipText( "Refresh Database" );
		refreshButton.setActionCommand("refresh");
		refreshButton.addActionListener(this);
		//refreshButton.setEnabled(false);
		trailPanel.add(refreshButton);

		JButton saveButton = new JButton(images.loadImage("../images/database/database_save_icon.png"));
		saveButton.setPreferredSize(new Dimension(30, 30));
		saveButton.setFocusPainted(false);
		saveButton.setToolTipText( "Save Changes" );
		saveButton.setActionCommand("save");
		saveButton.addActionListener(this);
		saveButton.setEnabled(false);
		trailPanel.add(saveButton);

		JButton deleteButton = new JButton(images.loadImage("../images/database/database_delete_icon.png"));
		deleteButton.setPreferredSize(new Dimension(30, 30));
		deleteButton.setFocusPainted(false);
		if (b)
			deleteButton.setToolTipText( "Delete Selected Child" );
		else
			deleteButton.setToolTipText( "Delete Selected Employee" );
		deleteButton.setActionCommand("delete");
		deleteButton.addActionListener(this);
		trailPanel.add(deleteButton);

		mainPanel.add(trailPanel, BorderLayout.WEST);

		return mainPanel;
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
		// TODO: Setting as selected isn't working for any internal frames
		try {
			e.getInternalFrame().setSelected(true);
		} catch (PropertyVetoException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		
	}
}