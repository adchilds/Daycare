package Views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lib.RoundedBorder;
import Controllers.Guardian_Controller;
import Controllers.Image_Controller;

/**
 * <p>Controls the view that allows user's to add guardian information for
 * the children of the database.
 * 
 * @author Adam Childs
 */
public class Guardian_View extends JDialog implements ActionListener
{
	static final long serialVersionUID = 1L;
	boolean saved = false;
	Guardian_Controller controller = null;
	JTextField[] textField = new JTextField[10];
	Image_Controller images = new Image_Controller();
	String relationship = "";
	ArrayList<String> info = new ArrayList<String>();

	/**
	 * <p>Instantiates the Guardian_View class.
	 * 
	 * @param cv The Child_View instance that this class spawns from.
	 */
	public Guardian_View(Guardian_Controller c)
	{
		controller = c;
	}

	/**
	 * <p>Displays the dialog allowing the user to edit guardian information.
	 * Displays information from the last edit, if the user has already added
	 * and edited this guardian.
	 * 
	 * @param r The relationship of the guardian to the child
	 * @param arr The data
	 */
	public void show(String r, ArrayList<String> arr)
	{
		relationship = r;

		initialize(r);

		if (arr != null)
			for (int i = 0; i < arr.size(); i++)
				textField[i].setText(arr.get(i));

		setVisible(true);
	}

	public void initialize(String r)
	{
		add(parentPanel());
		setTitle("Guardian Information - " + r);
		setIconImage(images.loadImage("../images/openxmlfile/parents_icon.png").getImage());
		setMinimumSize(new Dimension(400, 390)); // width, height
		setModal(true);
		setResizable(true);
		pack();
		setLocationRelativeTo(null);
	}

	private JPanel parentPanel()
	{
		JLabel label;
		JPanel p = new JPanel(new BorderLayout());
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel general = new JPanel(new BorderLayout(5, 0));
		general.setBorder(BorderFactory.createTitledBorder("General"));

		JPanel genWest = new JPanel(new GridLayout(0, 1, 0, 5));
		JPanel genEast = new JPanel(new GridLayout(0, 1, 0, 5));

		label = new JLabel( "First Name:", JLabel.RIGHT );
		textField[0] = new JTextField(10);
		genWest.add(label);
		genEast.add(textField[0]);

		label = new JLabel( "Last Name:", JLabel.RIGHT );
		textField[1] = new JTextField(10);
		genWest.add(label);
		genEast.add(textField[1]);

		label = new JLabel( "Relationship to Child:", JLabel.RIGHT );
		textField[2] = new JTextField(relationship, 10);
		textField[2].setEditable(false);
		genWest.add(label);
		genEast.add(textField[2]);

		label = new JLabel( "Employer:", JLabel.RIGHT );
		textField[2] = new JTextField(10);
		genWest.add(label);
		genEast.add(textField[2]);

		label = new JLabel( "Home Phone:", JLabel.RIGHT );
		textField[3] = new JTextField(10);
		genWest.add(label);
		genEast.add(textField[3]);

		label = new JLabel( "Cell Phone:", JLabel.RIGHT );
		textField[4] = new JTextField(10);
		genWest.add(label);
		genEast.add(textField[4]);

		label = new JLabel( "Work Phone:", JLabel.RIGHT );
		textField[5] = new JTextField(10);
		genWest.add(label);
		genEast.add(textField[5]);

		general.add(genWest, BorderLayout.WEST);
		general.add(genEast, BorderLayout.CENTER);

		JPanel address = new JPanel(new BorderLayout(5, 0));
		address.setBorder(BorderFactory.createTitledBorder("Address"));

		JPanel adrWest = new JPanel(new GridLayout(0, 1, 0, 5));
		JPanel adrEast = new JPanel(new GridLayout(0, 1, 0, 5));

		label = new JLabel( "Street:", JLabel.RIGHT );
		textField[6] = new JTextField(10);
		adrWest.add(label);
		adrEast.add(textField[6]);

		label = new JLabel( "City:", JLabel.RIGHT );
		textField[7] = new JTextField(10);
		adrWest.add(label);
		adrEast.add(textField[7]);

		label = new JLabel( "State:", JLabel.RIGHT );
		textField[8] = new JTextField(10);
		adrWest.add(label);
		adrEast.add(textField[8]);

		label = new JLabel( "Zipcode:", JLabel.RIGHT );
		textField[9] = new JTextField(10);
		adrWest.add(label);
		adrEast.add(textField[9]);

		address.add(adrWest, BorderLayout.WEST);
		address.add(adrEast, BorderLayout.CENTER);

		topPanel.add(general, BorderLayout.NORTH);
		topPanel.add(address, BorderLayout.SOUTH);
		p.add(topPanel, BorderLayout.CENTER);
		p.add(buttonPanel(), BorderLayout.SOUTH);

		return p;
	}

	private JPanel buttonPanel()
	{
		JPanel p = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		JButton button;

		button = new JButton("Save");
		button.setIcon(images.loadImage("../images/accept_icon.png"));
		button.setActionCommand("savebutton");
		button.setToolTipText("Save Changes");
		button.addActionListener(this);
		button.setPreferredSize(new Dimension(75, 25));
		button.setBorder(new RoundedBorder(5));
		p.add(button);

		button = new JButton("Cancel");
		button.setIcon(images.loadImage("../images/cross_icon.png"));
		button.setActionCommand("cancelbutton");
		button.setToolTipText("Cancel Changes");
		button.addActionListener(this);
		button.setPreferredSize(new Dimension(75, 25));
		button.setBorder(new RoundedBorder(5));
		p.add(button);

		return p;
	}

	/**
	 * Saves the entered information into an ArrayList<String>
	 * 
	 * @return an ArrayList<String> object containing the guardian information
	 */
	public ArrayList<String> saveInformation()
	{
		ArrayList<String> arr = new ArrayList<String>();

		for (int i = 0; i < textField.length; i++)
			arr.add(textField[i].getText());

		return arr;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("savebutton"))
		{
			controller.setGuardianInformation(saveInformation());
			dispose();
		} else if (e.getActionCommand().equals("cancelbutton")) {
			dispose();
		}
	}
}