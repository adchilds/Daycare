package Views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import lib.FixedSizeDocument;
import lib.Logger;
import lib.OSProperties;
import lib.RoundedBorder;
import lib.Validation;
import Controllers.Config_Controller;
import Controllers.Product_Key_Controller;

/**
 * <p>Controls the view of the product key screen.
 * 
 * @author Adam Childs
 */
public class Product_Key_View implements ActionListener, KeyListener
{
	JTextField[] textField = null;
	OSProperties osp = null;
	Product_Key_Controller controller = null;
	JDialog dialog = new JDialog();
	JLabel feedbackLabel = null;
	JPanel buttonPanel = null;

	public Product_Key_View(Product_Key_Controller parent)
	{
		controller = parent;
	}

	/**
	 * <p>Displays the dialog prompting the user to enter the product
	 * key for their copy of Daycare Management System.
	 */
	public void show()
	{
		osp = new OSProperties();
		Container container = dialog.getContentPane();
		container.add(dialogPanel());

		if (osp.isUnix())
			dialog.setSize(395, 125);
		else if (osp.isMac())
			dialog.setSize(380, 150); // TODO: Fix to correctly size on Mac OS
		else
			dialog.setSize(350, 150); // width, height
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);
		dialog.setTitle("Daycare Management System");
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		dialog.setVisible(true);
	}

	/**
	 * Displays the product key screen dialog
	 * 
	 * @return A JPanel component showing the dialog view
	 */
	private JPanel dialogPanel()
	{
		JButton button;
		JPanel p = new JPanel(new BorderLayout());
		JPanel labelPanel, productkeyPanel;
		JLabel label;
		textField = new JTextField[5];

		labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label = new JLabel();
		label.setText("Please enter the product key found on the CD:");
		labelPanel.add(label);

		productkeyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		textField[0] = new JTextField();
		if (osp.isMac())
			textField[0].setPreferredSize(new Dimension(50, 25));
		else
			textField[0].setPreferredSize(new Dimension(40, 25));
		textField[0].setDocument(new FixedSizeDocument(4));
		textField[0].setFocusable(true);
		textField[0].addKeyListener(this);
		productkeyPanel.add(textField[0]);

		label = new JLabel("-");
		productkeyPanel.add(label);

		textField[1] = new JTextField();
		if (osp.isMac())
			textField[1].setPreferredSize(new Dimension(50, 25));
		else
			textField[1].setPreferredSize(new Dimension(40, 25));
		textField[1].setDocument(new FixedSizeDocument(4));
		textField[1].setFocusable(true);
		textField[1].addKeyListener(this);
		productkeyPanel.add(textField[1]);

		label = new JLabel("-");
		productkeyPanel.add(label);

		textField[2] = new JTextField();
		if (osp.isMac())
			textField[2].setPreferredSize(new Dimension(50, 25));
		else
			textField[2].setPreferredSize(new Dimension(40, 25));
		textField[2].setDocument(new FixedSizeDocument(4));
		textField[2].setFocusable(true);
		textField[2].addKeyListener(this);
		productkeyPanel.add(textField[2]);

		label = new JLabel("-");
		productkeyPanel.add(label);

		textField[3] = new JTextField();
		if (osp.isMac())
			textField[3].setPreferredSize(new Dimension(50, 25));
		else
			textField[3].setPreferredSize(new Dimension(40, 25));
		textField[3].setDocument(new FixedSizeDocument(4));
		textField[3].setFocusable(true);
		textField[3].addKeyListener(this);
		productkeyPanel.add(textField[3]);

		label = new JLabel("-");
		productkeyPanel.add(label);

		textField[4] = new JTextField();
		if (osp.isMac())
			textField[4].setPreferredSize(new Dimension(50, 25));
		else
			textField[4].setPreferredSize(new Dimension(40, 25));
		textField[4].setDocument(new FixedSizeDocument(4));
		textField[4].setFocusable(true);
		textField[4].addKeyListener(this);
		productkeyPanel.add(textField[4]);

		productkeyPanel.add(Box.createRigidArea(new Dimension(9999, -5)));
		feedbackLabel = new JLabel();
		productkeyPanel.add(feedbackLabel);

		/*
		 * Validate Button
		 */
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		button = new JButton("Validate");
		button.setBorder(new RoundedBorder(5));
		button.setPreferredSize(new Dimension(75, 30));
		button.setActionCommand("validate");
		button.addActionListener(this);
		buttonPanel.add(button);

		/*
		 * Cancel Button
		 */
		button = new JButton("Cancel");
		button.setBorder(new RoundedBorder(5));
		button.setPreferredSize(new Dimension(75, 30));
		button.setActionCommand("cancel");
		button.addActionListener(this);
		buttonPanel.add(button);

		p.add(labelPanel, BorderLayout.NORTH);
		p.add(productkeyPanel, BorderLayout.CENTER);
		p.add(buttonPanel, BorderLayout.SOUTH);
		
		return p;
	}

	/**
	 * Checks the validity of a product key entered. The user is notified of
	 * the validity of the entered product key.
	 */
	public void checkValidity()
	{
		String[] s = new String[5];
		for (int i = 0; i < s.length; i++)
			s[i] = textField[i].getText();

		boolean valid = new Validation(s).validate();

		if (valid)
		{
			feedbackLabel.setText("Valid product key! Product registered.");
			feedbackLabel.setForeground(new Color(0, 139, 0));
			for (int i = 0; i < textField.length; i++)
				textField[i].setEnabled(false);

			buttonPanel.removeAll();

			JButton button = new JButton("OK");
			button.setBorder(new RoundedBorder(5));
			button.setPreferredSize(new Dimension(75, 30));
			button.setActionCommand("ok");
			button.addActionListener(this);
			buttonPanel.add(button);
			buttonPanel.revalidate();
			buttonPanel.repaint();

			new Config_Controller().setProductKeySet(true);
		} else {
			new Logger().write("Invalid product key: (" + s + ")", "ERROR");
			feedbackLabel.setText("Invalid product key!");
			feedbackLabel.setForeground(Color.RED);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("validate"))
		{
			checkValidity();
		}
		else if (e.getActionCommand().equals("cancel"))
		{
			System.exit(0);
		}
		else if (e.getActionCommand().equals("ok"))
		{
			dialog.dispose();
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			for (int i = 0; i < textField.length; i++)
			{
				boolean b = false;
				/*
				 * Switch focus to next textfield if not in the last textfield
				 */
				if (textField[i].hasFocus())
				{
					if (textField[i].getText().length() == 4 && i != 4)
						textField[i+1].requestFocusInWindow();
					else if (textField[4].hasFocus())
					{
						b = false;
						/*
						 * Make sure all textfields are filled in fully
						 */
						for (int m = 0; m < textField.length; m++)
							if (textField[i].getText().length() >= 4)
								b = true;
					}
				}
				if (b)
					checkValidity();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		/*
		 * Switch focus to next textfield if current is filled in with four values
		 */
		for (int i = 0; i < textField.length; i++)
			if (textField[i].hasFocus())
				if (textField[i].getText().length() == 4 && i != 4)
					textField[i+1].requestFocusInWindow();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}

}