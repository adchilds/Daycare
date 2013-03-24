package views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import lib.OSProperties;
import lib.RoundedBorder;
import lib.Timer;
import models.File_System_Model;
import models.Language_Model;
import controllers.Config_Controller;
import controllers.File_System_Controller;
import controllers.Image_Controller;
import controllers.Language_Controller;

/**
 * <p>Contains the view for checking children
 * and/or employees in/out of the daycare daily.
 * 
 * @author Adam Childs
 * @since 03/22/2013
 */
public class Check_In_View implements ActionListener, InternalFrameListener, KeyListener
{
	ArrayList<Object> tempcheckedout = new ArrayList<Object>();
	ArrayList<Object> tempcheckedin = new ArrayList<Object>();
	ArrayList<Timer> timers = new ArrayList<Timer>();
	DefaultListModel checkedout, checkedin;
	Image_Controller images = new Image_Controller();
	JDialog dialog = new JDialog();
	JList checkedoutlist, checkedinlist;
	JTextField checkedoutTextfield, checkedinTextfield;
	Language_Model lang_model = new Language_Controller().getModel();
	OSProperties osp = new OSProperties();

	public Check_In_View()
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

		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);
		dialog.setTitle( lang_model.getValue(140) );
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setIconImage(images.loadImage("Images/program_icon_small.png").getImage());
		dialog.setVisible(true);
	}

	private JPanel dialogPanel()
	{
		JPanel p = new JPanel(new BorderLayout());

		p.add(centerPanel(), BorderLayout.CENTER);
		p.add(southPanel(), BorderLayout.SOUTH);

		return p;
	}

	private JPanel centerPanel()
	{
		JPanel p = new JPanel();

		p.add(leftPanel());
		p.add(buttonPanel());
		p.add(rightPanel());
		
		return p;
	}

	private JPanel buttonPanel()
	{
		JPanel p = new JPanel(new BorderLayout(0, 5));
		JButton button;

		button = new JButton( lang_model.getValue(141) );
		button.setBorder(new RoundedBorder(5));
		button.setIcon(images.loadImage("Images/arrow_right_icon.png"));
		button.setActionCommand("checkin");
		button.addActionListener(this);
		button.setToolTipText( lang_model.getValue(141) );
		p.add(button, BorderLayout.NORTH);

		button = new JButton( lang_model.getValue(142) );
		button.setBorder(new RoundedBorder(5));
		button.setIcon(images.loadImage("Images/arrow_left_icon.png"));
		button.setActionCommand("checkout");
		button.addActionListener(this);
		button.setToolTipText( lang_model.getValue(142) );
		p.add(button, BorderLayout.SOUTH);

		return p;
	}

	private JPanel leftPanel()
	{
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createTitledBorder( lang_model.getValue(143) ));
		checkedout = new DefaultListModel();

		JPanel upper = new JPanel();
		JLabel label = new JLabel(lang_model.getValue(144) + ": ");
		checkedoutTextfield = new JTextField(7);
		checkedoutTextfield.addKeyListener(this);
		upper.add(label);
		upper.add(checkedoutTextfield);

		File_System_Model fs = new File_System_Controller().getModel();
		for (File f : fs.getAllFiles("Children"))
		{
			String fn = f.getName();
			if (fn.startsWith("."))
				continue;
			String n = fn.substring(0, fn.indexOf("."));
			String name = n.substring(n.indexOf("_") + 1) + " " + n.substring(0, n.indexOf("_"));
			checkedout.addElement(name);
		}

		checkedoutlist = new JList(checkedout);
		checkedoutlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		checkedoutlist.setLayoutOrientation(JList.VERTICAL);
		checkedoutlist.setSelectedIndex(0);
		checkedoutlist.setVisibleRowCount(-1);
		JScrollPane listScrollPane = new JScrollPane(checkedoutlist);
		listScrollPane.setPreferredSize(new Dimension(150, 350));
		
		p.add(upper, BorderLayout.NORTH);
		p.add(listScrollPane, BorderLayout.CENTER);
		return p;
	}

	private JPanel rightPanel()
	{
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createTitledBorder( lang_model.getValue(145) ));
		checkedin = new DefaultListModel();

		JPanel upper = new JPanel();
		JLabel label = new JLabel(lang_model.getValue(144) + ": ");
		checkedinTextfield = new JTextField(7);
		checkedinTextfield.addKeyListener(this);
		upper.add(label);
		upper.add(checkedinTextfield);

		checkedinlist = new JList(checkedin);
		checkedinlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		checkedinlist.setLayoutOrientation(JList.VERTICAL);
		checkedinlist.setSelectedIndex(0);
		checkedinlist.setVisibleRowCount(-1);
		JScrollPane listScrollPane = new JScrollPane(checkedinlist);
		listScrollPane.setPreferredSize(new Dimension(150, 350));

		p.add(upper, BorderLayout.NORTH);
		p.add(listScrollPane, BorderLayout.CENTER);
		return p;
	}

	private JPanel southPanel()
	{
		JPanel p = new JPanel();
		
		JCheckBox ch = new JCheckBox( lang_model.getValue(146) );
		ch.addActionListener(this);
		ch.setActionCommand("checkbox");
		p.add(ch);

		return p;
	}

	/**
	 * <p>Searches the checkedout lists to see if they contain the
	 * specified string. If so, they stay in the list. If
	 * not, they are removed from the list and not displayed
	 * on screen to the user.
	 * 
	 * @param listmodel The DefaultListModel to iterate over
	 * @param temp The temp ArrayList to add non-matching objects to
	 * @param textfield The JTextField instance being typed into
	 */
	private void search(DefaultListModel listmodel, ArrayList<Object> temp, JTextField textfield)
	{
		String s = textfield.getText();

		for (Object o : listmodel.toArray()) // All in listmodel
		{
			if (((String)o).toLowerCase().contains(s.toLowerCase())) // If a name contains the typed string
			{
				// Keep element in listmodel
				
			} else {
				// Remove element from listmodel
				if (!temp.contains(o))
					temp.add(o);
				listmodel.removeElement(o);
			}
		}
		for (Object o : temp.toArray()) // All in temp
		{
			if (((String)o).toLowerCase().contains(s.toLowerCase())) // If a name contains the typed string
			{
				// Remove element from temp
				if (temp.contains(o))
					temp.remove(o);
					
				if (!listmodel.contains(o))
					listmodel.addElement(o);
			} else {
				// Keep element in temp
					
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("checkbox"))
		{
			Config_Controller c = new Config_Controller();
			c.setCheckInView(!c.getCheckInView());
		}
		else if (e.getActionCommand().equals("checkin"))
		{
			String s = (String)checkedoutlist.getSelectedValue();

			if (s == null)
				return;

			// If the list size is <= 0, return, otherwise we'll get random null objects
			if (checkedout.getSize() <= 0)
				return;

			// Remove random null objects
			if (s.equals(""))
			{
				checkedout.removeElement(s);
				return;
			}

			checkedin.addElement(s);
			checkedout.removeElement(s);
			checkedoutlist.setSelectedIndex(0);

			// Start timer for individual
			timers.add(new Timer(s));
			
			checkedinlist.setSelectedIndex(0);
		}
		else if (e.getActionCommand().equals("checkout"))
		{
			String s = (String)checkedinlist.getSelectedValue();

			if (s == null)
				return;

			// If the list size is <= 0, return, otherwise we'll get random null objects
			if (checkedin.getSize() <= 0)
				return;

			// Remove random null objects
			if (s.equals(""))
			{
				checkedin.removeElement(s);
				return;
			}

			checkedout.addElement(s);
			checkedin.removeElement(s);
			checkedinlist.setSelectedIndex(0);

			// End timer for individual
			for (Object t : timers.toArray())
			{
				if (((Timer)t).getName().matches(s))
				{
					long seconds = (System.currentTimeMillis() - ((Timer)t).getStartTime()) / 1000;
					long minutes = seconds / 60;
					long hours = minutes / 60;
					System.out.println(((Timer)t).getName() + " [" + hours + "h:" + minutes + "m:" + (seconds%60) + "s]");
					timers.remove(t);
				}
			}
			checkedoutlist.setSelectedIndex(0);
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
		if (checkedoutTextfield.hasFocus())
			search(checkedout, tempcheckedout, checkedoutTextfield);
		else if (checkedinTextfield.hasFocus())
			search(checkedin, tempcheckedin, checkedinTextfield);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}

}