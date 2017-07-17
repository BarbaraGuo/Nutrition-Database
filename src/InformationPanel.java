import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import database.Database;
import database.datastrucutres.FoodPacket;
import database.datastrucutres.ListNode;
import database.datastrucutres.Nutrient;
import database.datastrucutres.NutrientList;

/**
 * Contains the basic information for a selected food item. Common name, long
 * description, manufacturer name , nutrient information and languals will be
 * displayed if available.
 *
 */
public class InformationPanel extends JPanel {

	private JPanel nutrientInfo;
	private FoodPacket foodPacket;
	private PanelSetter control;
	private Database database;
	private JTextPane commonName, longDescription, manufactuer, langual;
	private JTable nutrientInformation;
	private Image logo = new ImageIcon("HEAD_ICON.png").getImage()
			.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
	private String[] columnNames = {"Nutrient Name", "Quantity"};

	/**
	 * Constructor
	 * 
	 * @param control
	 *            Reference to the panel setter which allows the panel to be
	 *            switched to another
	 */
	InformationPanel(PanelSetter control) {

		this.control = control;
		this.database = control.getDatabase();

		// Initiates the JTextAreas
		commonName = new JTextPane();
		longDescription = new JTextPane();
		manufactuer = new JTextPane();
		langual = new JTextPane();
		nutrientInformation = new JTable();

		// Allows word wrap and disables editing for each text area
		commonName.setEditable(false);
		longDescription.setEditable(false);
		manufactuer.setEditable(false);
		langual.setEditable(false);

		// Sets specifics for the layout
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 68, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		// Button that takes users back to the home panel
		JButton homeBtn = new JButton(new ImageIcon(logo));
		homeBtn.setBorderPainted(false);
		homeBtn.setContentAreaFilled(false);
		homeBtn.setOpaque(false);
		homeBtn.setFocusPainted(false);

		// Returns the user to the home panel when pressed
		homeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				control.showHome();
			}

		});

		// Adds the home button to the specified coordinates
		GridBagConstraints gbc_homeBtn = new GridBagConstraints();
		gbc_homeBtn.insets = new Insets(0, 0, 5, 5);
		gbc_homeBtn.gridx = 0;
		gbc_homeBtn.gridy = 0;
		add(homeBtn, gbc_homeBtn);

		// Adds the search bar and sets the specific constraints for it
		SearchBar searchBar = new SearchBar(control);
		GridBagConstraints gbc_searchBar = new GridBagConstraints();
		gbc_searchBar.insets = new Insets(0, 0, 5, 0);
		gbc_searchBar.anchor = GridBagConstraints.WEST;
		gbc_searchBar.gridx = 2;
		gbc_searchBar.gridy = 0;
		add(searchBar, gbc_searchBar);
		searchBar.setColumns(35);

		// Sets up the tabbed pane which stores the individual information per
		// food item
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridwidth = 2;
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 4;
		add(tabbedPane, gbc_tabbedPane);

		// Panel of tabbed pane which hold the general information for each food
		// item
		JPanel general = new JPanel(new GridLayout(3, 2, 0, 5));
		tabbedPane.addTab("General", general);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		// Create labels to correspond with each text area
		// Sets the font and size of each text area and adds the text areas to
		// the general panel
		JLabel name = new JLabel("Common Name: ");
		name.setVerticalAlignment(SwingConstants.NORTH);
		JLabel desc = new JLabel("Description: ");
		desc.setVerticalAlignment(SwingConstants.NORTH);
		JLabel manu = new JLabel("Manufactuer Name: ");
		manu.setVerticalAlignment(SwingConstants.NORTH);
		commonName.setFont(new Font("SansSerif", Font.PLAIN, 10));
		commonName.setBackground(new Color(238, 238, 238));
		longDescription.setFont(new Font("SansSerif", Font.PLAIN, 10));
		longDescription.setBackground(new Color(238, 238, 238));
		manufactuer.setFont(new Font("SansSerif", Font.PLAIN, 10));
		manufactuer.setBackground(new Color(238, 238, 238));


		// Adds both the labels and the text areas to the general panel
		general.add(name);
		general.add(commonName);
		general.add(desc);
		general.add(longDescription);
		general.add(manu);
		general.add(manufactuer);

		// Panel which contains the nutrient information for the selected food
		// item (added to a scroll pane so that all information may be viewed)
		nutrientInfo = new JPanel();
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(nutrientInfo);
		tabbedPane.addTab("Nurtrient Information", scroll);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		nutrientInfo.add(nutrientInformation);

		// Panel that contains additional information regarding the food item
		// (if available)
		JPanel Other = new JPanel();
		tabbedPane.addTab("Other", Other);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
		JLabel lang = new JLabel("Langual:");
		lang.setVerticalAlignment(SwingConstants.NORTH);
		langual.setFont(new Font("SansSerif", Font.PLAIN, 10));
		langual.setBackground(new Color(238, 238, 238));
		if (langual.getText().equals("")) {
			langual.setText("No other information available");
			lang.setText("");
		}
		Other.add(lang);
		Other.add(langual);

		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	/**
	 * Retrieves information from the database and displays the information onto
	 * the respective panels
	 * 
	 * @param foodPacket
	 *            Package containing all of the information pertaining the
	 *            selected search item
	 */
	protected void displayResults(FoodPacket foodPacket) {

		this.foodPacket = foodPacket;

		// Retrieves general information (long description, common name,
		// manufacturer name). Since common name and manufacturer name are not
		// always available, check to see if they are. Otherwise display an
		// unavailable message.
		this.longDescription.setText(foodPacket.getValue("Long_Desc"));
		this.commonName.setText(foodPacket.getValue("ComName"));
		if (foodPacket.getValue("ComName").equals("")) {
			this.commonName.setText("Common name unavailable.");
		}
		this.manufactuer.setText(foodPacket.getValue("ManufacName"));
		if (foodPacket.getValue("ManufacName").equals("")) {
			this.manufactuer.setText("Manufactuer name unavailable.");
		}

		// Lists and arrays necessary to build and fill in the JTable of
		// nutrient information
		NutrientList nutrients = foodPacket.getNutrients();
		ListNode<Nutrient> head = nutrients.getHead();
		String[][] nutriInfo = new String[nutrients.getSize()][2];

		// Goes through all available nutrient information and adds the data to
		// the corresponding column arrays in the nutrient information table
		int i = 0;
		while (head != null) {
			Nutrient temp = head.getItem();
			nutriInfo[i][0] = this.database.getNutrientData(temp.getKey())
					.getValue("NutrDesc");
			nutriInfo[i][1] = temp.getValue("Nutr_Val");
			head = head.getNext();
			i++;
		}

		// Prevents users from editing cells in the table
		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Sets the specifics for the table (column headers)
		nutrientInformation.setModel(model);
		model.addColumn("Nutrient Name");
		model.addColumn("Quantity");
		TableColumn column = nutrientInformation.getColumnModel().getColumn(0);
		column.setPreferredWidth(750); //third column is bigger
		

		// Goes through the previously constructed column arrays and add the
		// information to the corresponding rows
		for (int q = 0; q < nutriInfo.length; q++) {
			model.addRow(nutriInfo[q]);
		}

		// Adds the table to the nutrient information panel
		nutrientInfo.add(nutrientInformation);
	}
}
