import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import database.datastrucutres.FoodPacketList;

/**
 * General panel for all general food categories containing buttons that link to
 * the corresponding food groups.
 */
public class FoodGroupPanel extends JPanel {

	private PanelSetter control;
	private Image[] image;
	private Image[] options;
	private JButton[] optBtns;
	private JButton[] btns;
	private String[] imageKeys;

	/**
	 * Constructor for the food group panel. Loads up the images and buttons.
	 * The assigns functions to the buttons.
	 * 
	 * @param control
	 *            The panel setter (card layout) which in this case controls
	 *            which food group panel is currently being shown.
	 * @param images
	 *            An string array containing references to respective .png
	 *            images. This array is sent from the panel setter and is
	 *            specific to whichever food group is being shown to the user.
	 * @param current
	 *            Index to keep track of which food group is being shown
	 */
	FoodGroupPanel(PanelSetter control, String[] images, int current) {
		super();

		imageKeys = images;

		// Initiates the array, (first one), which contains the respective
		// images for each food group. The second array contains the images for
		// a set of permanent buttons that will enable users to switch between
		// different food groups (and their respective food group panels)
		image = new Image[images.length];
		options = new Image[6];

		// Loads permanent images for the panel into the image array (these
		// images will appear regardless of which food group the panel is
		// currently displaying)
		options[0] = new ImageIcon("ICON.png").getImage().getScaledInstance(400, 100, Image.SCALE_DEFAULT);
		options[1] = new ImageIcon("grainOption.png").getImage().getScaledInstance(150, 50, Image.SCALE_SMOOTH);
		options[2] = new ImageIcon("MeatOpt.png").getImage().getScaledInstance(150, 50, Image.SCALE_SMOOTH);
		options[3] = new ImageIcon("dairyOpt.png").getImage().getScaledInstance(150, 50, Image.SCALE_SMOOTH);
		options[4] = new ImageIcon("VegFruitOpt.png").getImage().getScaledInstance(150, 50, Image.SCALE_SMOOTH);
		options[5] = new ImageIcon("OtherOpt.png").getImage().getScaledInstance(150, 50, Image.SCALE_SMOOTH);

		// Initiates two button arrays. First one is respective to the buttons
		// that correspond with each individual food group. The second one is
		// respective to the permanent set of buttons that allows users to
		// switch between food group panels. (Like before)
		btns = new JButton[images.length / 2];
		optBtns = new JButton[6];

		// Loads the in the images into the array based on which food group is
		// being shown
		for (int i = 0; i < images.length; i++) {
			image[i] = new ImageIcon(images[i] + ".png").getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
		}

		// Loads the permanent buttons to switch between food groups
		for (int i = 0; i < options.length; i++) {

			// Buttons are set so that the border and default background is
			// hidden (for aesthetic purposes)
			JButton button = new JButton();
			button.setIcon(new ImageIcon(options[i]));
			button.setBorderPainted(false);
			button.setContentAreaFilled(false);
			button.setOpaque(false);
			button.setFocusPainted(false);
			optBtns[i] = button;

			// Depending on which button is selected the control (panel setter)
			// will be called to show the
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (event.getSource() == optBtns[0]) {
						control.showHome();
					} else if (event.getSource() == optBtns[1]) {
						control.showGrain();
					} else if (event.getSource() == optBtns[2]) {
						control.showMeat();
					} else if (event.getSource() == optBtns[3]) {
						control.showDairy();
					} else if (event.getSource() == optBtns[4]) {
						control.showVegFruit();
					} else if (event.getSource() == optBtns[5]) {
						control.showOther();
					}
				}
			});

		}
		this.control = control;

		// Sets the layout for the food group panels
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		this.setLayout(gridBagLayout);

		// Sets up the search bar and it's parameters
		SearchBar tfInput = new SearchBar(control);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.gridwidth = 3;
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 3;
		this.add(tfInput, gbc_textField_1);
		tfInput.setColumns(10);

		// Set constraints for the home button and adds it to the panel
		GridBagConstraints gbc_homeBtn = new GridBagConstraints();
		gbc_homeBtn.insets = new Insets(0, 0, 5, 5);
		gbc_homeBtn.gridx = 2;
		gbc_homeBtn.gridy = 1;
		this.add(optBtns[0], gbc_homeBtn);

		// Creates the panel with the permanent buttons used to switch between
		// different food group panels
		JPanel foodGroupSwitch = new JPanel();

		// Sets constraints and layout for the foodGroupSwitch panel
		GridBagConstraints gbc_foodGroupSwitch = new GridBagConstraints();
		gbc_foodGroupSwitch.gridwidth = 19;
		gbc_foodGroupSwitch.insets = new Insets(0, 0, 5, 5);
		gbc_foodGroupSwitch.fill = GridBagConstraints.BOTH;
		gbc_foodGroupSwitch.gridx = 0;
		gbc_foodGroupSwitch.gridy = 4;
		this.add(foodGroupSwitch, gbc_foodGroupSwitch);
		foodGroupSwitch.setLayout(new GridLayout(1, 5));

		// Adds the permanent buttons to the permanent button panel
		for (int btn = 1; btn < optBtns.length; btn++) {
			foodGroupSwitch.add(optBtns[btn]);
		}

		// Creates the panel which will hold the buttons corresponding to
		// whichever food group is being shown to the user
		JPanel foodBtns = new JPanel();
		foodBtns.setLayout(new GridLayout(1, btns.length));

		// Sets up a scrollpane for the foodBtns panel for panels with a lot of
		// buttons
		JScrollPane foodBtnsScroll = new JScrollPane();
		foodBtnsScroll.setBorder(BorderFactory.createEmptyBorder());
		foodBtnsScroll.setViewportView(foodBtns);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 21;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 6;
		this.add(foodBtnsScroll, gbc_scrollPane);

		// Adds the food group buttons to the panel
		for (int btn = 0; btn < btns.length; btn++) {
			JButton button = new JButton();
			button.setIcon(new ImageIcon(image[btn].getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
			button.setBorderPainted(false);
			button.setContentAreaFilled(false);
			button.setOpaque(false);
			button.setFocusPainted(false);
			button.setRolloverEnabled(true);
			button.setRolloverIcon(new ImageIcon(image[btn + (image.length / 2)]));
			btns[btn] = button;

			// create final variable to allow for access inside the Action
			// Listener class
			final int i = btn;

			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					FoodPacketList data = null;
					if (imageKeys[i].charAt(0) == '_')
						data = control.getDatabase().search(imageKeys[i].substring(1), "FdGrp_Cd");
					else
						data = control.getDatabase().search(imageKeys[i], "FdGrp_Cd");
					control.showResults(data);
				}
			});
			foodBtns.add(button);
		}

	}

}
