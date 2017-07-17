import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import database.Database;
import database.datastrucutres.FoodPacket;
import database.datastrucutres.FoodPacketList;

/**
 * Panel which allows users to add additional food items to the database.
 */
public class AddPanel extends JPanel {

	private FoodPacketList nutrients;
	private PanelSetter control;
	private Database database;
	private JComboBox<String> foodGroupSelect;
	public static final String[] FOOD_GROUPS = { "Dairy and Eggs", "Spices and Herbs", "Baby Foods", "Fats and Oils",
			"Poultry", "Soups, Sauces and Gravies", "Sausages and Luncheon Meats", "Breakfast Cereals",
			"Fruits and Fruit Juices", "Vegetables and Vegetable Products", "Nut and Seed Products", "Beef Products",
			"Beverages", "Finfish and Shellfish Products", "Legumes and Legume Products",
			"Lamb, Veal, and Game Products", "Baked Products", "Sweets", "Cereal Grains and Pasta", "Fast Foods",
			"Meals, Entrees, and Side Dishes", "Snacks", "American Indian/Alaska Native Foods", "Restaurant Foods" };
	
	public static final String[] FOOD_GROUP_CODES = { "0100", "0200", "0300", "0400", "0500", "0600", "0700", "0800",
			"0900", "1000", "1100", "1200", "1300", "1400", "1500", "1600", "1700", "1800", "1900", "2000", "2100",
			"2200", "2500", "3500", "3600" };

	AddPanel(PanelSetter control) {

		super();
		this.control = control;
	}

	public void addPanel() {
		this.setLayout(new BorderLayout());

		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.setBackground(Color.CYAN);
		JPanel topContainer = new JPanel(new GridLayout(0, 1));

		JLabel lblNewLabel = new JLabel("ADD ANOTHER FOOD ITEM");
		lblNewLabel.setPreferredSize(new Dimension(200, 50));
		topContainer.add(lblNewLabel);

		JScrollPane addMenu = new JScrollPane();
		addMenu.setBorder(BorderFactory.createEmptyBorder());
		addMenu.setViewportView(panel);
		this.add(addMenu, BorderLayout.CENTER);
		panel.add(topContainer);

		// GridBagLayout gbl_panel = new GridBagLayout();
		// gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		// gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0};
		// gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0,
		// Double.MIN_VALUE};
		// panel.setLayout(gbl_panel);

		String[] hints = { "Enter Common Name", "Enter Long Description", "Enter Manufactuer Name",
				"Enter Scientific Name" };
		JTextField[] texts = new JTextField[hints.length];
		for (int textField = 0; textField < hints.length; textField++) {
			JTextField text = new JTextField(20);
			text.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			text.setPreferredSize(new Dimension(50, 30));
			text.setForeground(Color.LIGHT_GRAY);
			text.setFont(new Font("SansSerif", Font.PLAIN, 20));
			text.setText(hints[textField]);

			final int i = textField;
			text.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent event) {

					if (text.getText().equals(hints[i])) {
						text.setText("");
						text.setForeground(Color.BLACK);
					}
				}

				@Override
				public void focusLost(FocusEvent event) {
					if (text.getText().equals("")) {
						text.setText(hints[i]);
						text.setForeground(Color.LIGHT_GRAY);
					}

				}
			});
			texts[textField] = text;
			text.setPreferredSize(new Dimension(200, 50));
			topContainer.add(text);
		}

		this.foodGroupSelect = new JComboBox<String>(FOOD_GROUPS);
		topContainer.add(this.foodGroupSelect);

		JPanel nutrientInformation = new JPanel(new GridLayout(0, 2));

		// Initial value, min value, max value, increment
		FoodPacket[] temp = this.nutrients.toArray();
		JSpinner[] spinners = new JSpinner[temp.length];
		JLabel[] labels = new JLabel[temp.length];

		for (int label = 0; label < temp.length; label++) {
			JLabel nutrient = new JLabel(temp[label].getValue("NutrDesc"));
			nutrientInformation.add(nutrient);
			labels[label] = nutrient;

			SpinnerNumberModel quantityModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 0.5);
			JSpinner quantity = new JSpinner(quantityModel);
			spinners[label] = quantity;
			nutrientInformation.add(quantity);
		}

		panel.add(nutrientInformation);
		JButton add = new JButton("Add");
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String[][] nutrientInfo = new String[spinners.length][2];
				for (int i = 0; i < spinners.length; i++) {
					nutrientInfo[i][1] = spinners[i].getValue() + "";
					nutrientInfo[i][0] = nutrients.get(labels[i].getText(), "NutrDesc").getValue("Nutr_No");
				}

				AddPanel.this.database.addFood(FOOD_GROUP_CODES[AddPanel.this.foodGroupSelect.getSelectedIndex()],
						texts[1].getText(), texts[0].getText(), texts[2].getText(), texts[3].getText(), nutrientInfo);
				AddPanel.this.control.showHome();
			}

		});
		add.setPreferredSize(new Dimension(200, 50));
		panel.add(add);
	}

	FoodPacketList getList() {
		return this.nutrients;
	}

	public void loaded() {
		this.database = control.getDatabase();
		this.nutrients = this.database.getAllNutrients();
		this.addPanel();
	}
}
