import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import database.datastrucutres.FoodPacketList;

/**
 * Home page/starting menu for users. From this panel users can either directly
 * search for a food item or view items categorically by selecting food group
 * buttons in the body.
 *
 */
public class HomePanel extends JPanel {
	
	private PanelSetter control;
	private Image logo;
	private Image[] bodyImages = new Image[13];
	private JButton[] btnsBody = new JButton[12];
	private FoodPacketList list;
	SearchBar searchBar;

	HomePanel(PanelSetter control) {
		
		// Logo that shows above the search bar
		logo = new ImageIcon("ICON.png").getImage().getScaledInstance(610, 158,
				Image.SCALE_SMOOTH);

		// Loads the images for home panel
		bodyImages[0] = new ImageIcon("Grain.png").getImage()
				.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
		bodyImages[1] = new ImageIcon("GrainLT.png").getImage()
				.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
		bodyImages[2] = new ImageIcon("Meat.png").getImage().getScaledInstance(
				200, 150, Image.SCALE_SMOOTH);
		bodyImages[3] = new ImageIcon("MeatLT.png").getImage()
				.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
		bodyImages[4] = new ImageIcon("vegFruit.png").getImage()
				.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
		bodyImages[5] = new ImageIcon("vegFruitLT.png").getImage()
				.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
		bodyImages[6] = new ImageIcon("Dairy.png").getImage()
				.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
		bodyImages[7] = new ImageIcon("DairyLT.png").getImage()
				.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
		bodyImages[8] = new ImageIcon("other.png").getImage()
				.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
		bodyImages[9] = new ImageIcon("otherLT.png").getImage()
				.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
		bodyImages[10] = new ImageIcon("Add.png").getImage().getScaledInstance(
				200, 150, Image.SCALE_DEFAULT);
		bodyImages[11] = new ImageIcon("AddLT.png").getImage()
				.getScaledInstance(200, 150, Image.SCALE_SMOOTH);

		// Sets the specifics of the home panel layout
		this.setLayout(new GridBagLayout());
		GridBagLayout gbl_home = new GridBagLayout();
		gbl_home.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 20, 451, 0, 0 };
		gbl_home.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_home.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };

		// Places the logo icon on the panel
		JLabel icon = new JLabel(new ImageIcon(logo));
		GridBagConstraints gbc_btnImageHere = new GridBagConstraints();
		gbc_btnImageHere.insets = new Insets(0, 40, 5, 5);
		gbc_btnImageHere.gridx = 7;
		gbc_btnImageHere.gridy = 0;
		this.add(icon, gbc_btnImageHere);

		// Sets up the textfield
		searchBar = new SearchBar(control);
		GridBagConstraints gbc_searchBar = new GridBagConstraints();
		gbc_searchBar.gridwidth = 10;
		gbc_searchBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_searchBar.insets = new Insets(0, 0, 5, 0);
		gbc_searchBar.gridx = 0;
		gbc_searchBar.gridy = 1;
		this.add(searchBar, gbc_searchBar);

		// Creates the panel which contains the buttons on the main page
		// Sets the layout and constraints for said panel
		JPanel homeOpt = new JPanel();
		GridBagConstraints gbc_homeOpt = new GridBagConstraints();
		gbc_homeOpt.gridheight = 5;
		gbc_homeOpt.gridwidth = 10;
		gbc_homeOpt.fill = GridBagConstraints.BOTH;
		gbc_homeOpt.gridx = 0;
		gbc_homeOpt.gridy = 3;
		this.add(homeOpt, gbc_homeOpt);
		homeOpt.setLayout(new GridLayout(2, 3, 0, 0));

		// Creates and displays the buttons on the main page
		for (int btn = 0; btn < bodyImages.length - 1; btn += 2) {

			// Creates and modifies the button (hides the background and border)
			JButton button = new JButton();
			button.setIcon(new ImageIcon(bodyImages[btn]));
			button.setBorderPainted(false);
			button.setContentAreaFilled(false);
			button.setOpaque(false);
			button.setFocusPainted(false);
			button.setRolloverEnabled(true);
			btnsBody[btn] = button;

			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent event) {

					// If the grain button is pressed switch the panel to the
					// food group panel for grains
					if (event.getSource() == btnsBody[0]) {
						control.showGrain();
					}

					// Applies the same to the meat and alternatives panel
					else if (event.getSource() == btnsBody[2]) {
						control.showMeat();
					}

					// Applies the same to the vegetables and fruits panel
					else if (event.getSource() == btnsBody[4]) {
						control.showVegFruit();
					}

					// Applies the same to the dairy panel
					else if (event.getSource() == btnsBody[6]) {
						control.showDairy();
					}

					// Applies the same to the other food groups panel (contains
					// any food groups that do not necessarily fit with the
					// basic grain, meat, dairy, and fruits and vegetables
					// groups)
					else if (event.getSource() == btnsBody[8]) {
						control.showOther();
					}

					// Applies the same to the add panel
					else if (event.getSource() == btnsBody[10]) {
						control.showAdd();
					}

				}

			});

			// Switches the image of the button to a highlighted version when
			// the mouse rolls over it
			button.setRolloverIcon(new ImageIcon(bodyImages[btn + 1]));

			// Adds the button to the panel
			homeOpt.add(button);
		}
	}

}
