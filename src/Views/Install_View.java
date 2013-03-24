package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import lib.OSProperties;
import lib.RoundedBorder;
import models.Language_Model;
import controllers.File_System_Controller;
import controllers.Image_Controller;
import controllers.Install_Controller;
import controllers.Language_Controller;

/**
 * <p>Controls the view of the installation screen.
 * 
 * @author Adam Childs
 */
public class Install_View implements ActionListener, KeyListener
{
	static final long serialVersionUID = 1L;
	Image_Controller images = new Image_Controller();
	Install_Controller controller = null;
	JDialog dialog = new JDialog();
	JPanel buttonPanel = null;
	Language_Model lang_model = new Language_Controller().getModel();
	OSProperties osp = new OSProperties();
	String version = "v1.10";

	/**
	 * Instantiates a new Install_View object, which can display the
	 * installation screen.
	 * 
	 * @param parent The Install_Controller instance that creates the Install_View
	 */
	public Install_View(Install_Controller parent)
	{
		controller = parent;
	}

	/**
	 * <p>Displays the dialog allowing the user to install the
	 * Daycare Management System program onto their system.
	 */
	public void show()
	{
		osp = new OSProperties();
		Container container = dialog.getContentPane();
		container.add(dialogPanel());

		if (osp.isUnix())
			dialog.setSize(590, 425);
		else if (osp.isMac())
			dialog.setSize(590, 420); // TODO: Fix to correctly size on Mac OS
		else if (osp.isWindows())
			dialog.setSize(590, 425);
		else
			dialog.setSize(590, 425); // width, height

		dialog.setContentPane(container);
		dialog.setIconImage(images.loadImage("Images/program_icon_small.png").getImage());
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);
		dialog.setTitle(lang_model.getValue(3) + version);
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		dialog.setVisible(true);
	}

	private JPanel dialogPanel()
	{
		JButton button;
		JLabel label;
		JPanel p = new JPanel(new BorderLayout());
		JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel centerPanel = new JPanel(new BorderLayout());
		JTextArea textArea;
		JTextPane textPane;

		/*
		 * Welcome label
		 */
		textPane = new JTextPane();
		textPane.setText(lang_model.getValue(4) + version + lang_model.getValue(5));
		textPane.setFont(new Font("Book Antiqua", Font.PLAIN, 24));
		textPane.setPreferredSize(new Dimension(300, 100)); // width height
		textPane.setEditable(false);
		textPane.setOpaque(false);
		textPane.setForeground(Color.BLACK);
		StyledDocument doc = textPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		labelPanel.add(textPane);

		/*
		 * JPanel holding a JTextArea holding general information
		 */
		JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		textArea = new JTextArea();
		textArea.setText(lang_model.getValue(6) + version + lang_model.getValue(7) + "\n\n" +
						 lang_model.getValue(8) + "\n\n" +
						 lang_model.getValue(9));
		textArea.setPreferredSize(new Dimension(300, 400)); // width height
		textArea.setEditable(false);
		textArea.setOpaque(false);
		textArea.setForeground(Color.BLACK);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textPanel.add(textArea);

		/*
		 * Install Button
		 */
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		button = new JButton(lang_model.getValue(10));
		button.setIcon(images.loadImage("Images/accept_icon.png"));
		button.setBorder(new RoundedBorder(5));
		//button.setPreferredSize(new Dimension(75, 30));
		button.setToolTipText("Install the Program");
		button.setFocusable(true);
		button.setActionCommand("install");
		button.addActionListener(this);
		button.addKeyListener(this);
		buttonPanel.add(button);

		/*
		 * Cancel Button
		 */
		button = new JButton(lang_model.getValue(11));
		button.setIcon(images.loadImage("Images/cross_icon.png"));
		button.setBorder(new RoundedBorder(5));
		//button.setPreferredSize(new Dimension(75, 30));
		button.setToolTipText("Exit the setup");
		button.setFocusable(true);
		button.setActionCommand("cancel");
		button.addActionListener(this);
		buttonPanel.add(button);

		/*
		 * Add components to the centerPanel
		 */
		centerPanel.add(labelPanel, BorderLayout.NORTH);
		centerPanel.add(textPanel, BorderLayout.CENTER);
		centerPanel.add(buttonPanel, BorderLayout.SOUTH);

		/*
		 * JPanel holding the image for the program
		 */
		label = new JLabel(images.loadImage("Images/install_splash.png"));
		label.setPreferredSize(new Dimension(250, 400));

		p.add(label, BorderLayout.WEST); // Image
		p.add(centerPanel, BorderLayout.CENTER); // Text and buttons

		return p;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("install"))
		{
			if (osp.isUnix())
				controller.installFiles(""); // TODO: UNIX DIRECTORY
			else if (osp.isMac())
				controller.installFiles(new File_System_Controller().getModel().getDefaultDirectory()); // MAC DIRECTORY
			else if (osp.isWindows())
				controller.installFiles(new File_System_Controller().getModel().getDefaultDirectory()); // Windows install directory
			else {
				System.out.println( "Operating System not recognized..." );
				System.out.println(osp.getOS());
			}
			dialog.dispose();
		}
		else if (e.getActionCommand().equals("cancel"))
		{
			System.exit(0);
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}
}