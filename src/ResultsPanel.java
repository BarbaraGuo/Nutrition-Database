import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import database.datastrucutres.FoodPacket;
import database.datastrucutres.FoodPacketList;
import database.datastrucutres.ListNode;

/**
 * Panel which displays the search results based on user inquiry or through the
 * food group filters
 *
 */
public class ResultsPanel extends JPanel implements ListSelectionListener {

	private ButtonGroup group = new ButtonGroup();
	private FoodPacket[] array;

	private Image home;
	private GridBagLayout gridBagLayout;
	private JButton homeButton;
	private PanelSetter control;
	private JList results;
	private DefaultListModel<FoodPacket> listModel;
	private SearchBar searchBar;
	private FoodPacketList data;

	/**
	 * Constructor for the results panel. Loads the
	 * 
	 * @param control
	 * @param data
	 */
	ResultsPanel(PanelSetter control, FoodPacketList data) {

		super(new GridBagLayout());

		home = new ImageIcon("HEAD_ICON.png").getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);

		// Sets up the gridbaglayout specifics for the panel
		gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 113 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, 1.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		this.setLayout(gridBagLayout);

		// Sets up and specifies the specifics of the JList which will hold the
		// results of the user search
		listModel = new DefaultListModel<FoodPacket>();
		results = new JList<FoodPacket>(listModel);
		results.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		results.addListSelectionListener(this);
		results.setVisibleRowCount(25);

		// Sets up a scroll pane that will hold the results JList
		JScrollPane resultScroll = new JScrollPane(results);
		resultScroll.setBackground(new Color(238, 238, 238));
		resultScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		resultScroll.setBorder(BorderFactory.createEmptyBorder());

		// Adds the scroll pane to the panel under specified grid bag
		// constraints
		GridBagConstraints gbc_scroll = new GridBagConstraints();
		gbc_scroll.gridwidth = 21;
		gbc_scroll.insets = new Insets(0, 0, 20, 100);
		gbc_scroll.fill = GridBagConstraints.BOTH;
		gbc_scroll.gridx = 1;
		gbc_scroll.gridy = 3;
		this.add(resultScroll, gbc_scroll);

		this.control = control;
		this.data = data;
		this.searchBar = new SearchBar(control, this);
		panel();

	}

	/**
	 * Main panel which contains the search results
	 */
	public void panel() {
		homeButton = new JButton();
		homeButton.setIcon(new ImageIcon(home));
		homeButton.setBorderPainted(false);
		homeButton.setContentAreaFilled(false);
		homeButton.setOpaque(false);
		homeButton.setFocusPainted(false);
		homeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				control.homePanel.searchBar.resetText();
				control.showHome();
			}

		});

		GridBagConstraints gbc_homeButton = new GridBagConstraints();
		gbc_homeButton.insets = new Insets(0, 100, 0, 0);
		gbc_homeButton.gridx = 0;
		gbc_homeButton.gridy = 1;
		this.add(homeButton, gbc_homeButton);

		searchBar = new SearchBar(control, this);
		GridBagConstraints gbc_search = new GridBagConstraints();
		gbc_search.gridwidth = 14;
		gbc_search.insets = new Insets(0, 0, 5, 150);
		gbc_search.fill = GridBagConstraints.HORIZONTAL;
		gbc_search.gridx = 1;
		gbc_search.gridy = 1;
		this.add(searchBar, gbc_search);
		searchBar.setColumns(10);

	}

	/**
	 * Sets the text of the search bar
	 * 
	 * @param query
	 *            The user entered query (search)
	 */
	public void setText(String query) {
		this.searchBar.setText(query);
		this.searchBar.setCaretPosition(query.length());
	}

	/**
	 * Sets immediate focus to the search bar (for user convenience)
	 */
	public void internallyRequestFocus() {
		this.requestFocusInWindow();
		this.searchBar.requestFocusInWindow();
	}

	/**
	 * Accesses the database and loads the results based off of user query
	 * 
	 * @param list
	 *            List of food packets (all the results and their respective
	 *            information)
	 */
	public void loadResults(FoodPacketList list) {
		refresh();
		array = list.toArray();
		if (array != null) {
			int length = 0;
			if (array.length > 25)
				length = 25;
			else
				length = array.length;

			// Adds each individual food packet and its information to the JList
			// of results
			for (int i = 0; i < length; i++) {
				FoodPacket foodPacket = array[i];
				listModel.addElement(foodPacket);
			}
		}
	}

	/**
	 * Accesses the database and loads the results based off of user query
	 * 
	 * @param query
	 *            user entered query
	 * @param isNew
	 *            If the search bar is new, determines what to set the text of
	 *            the search bar to
	 */
	public synchronized void loadResults(String query, boolean isNew) {
		refresh();
		if (isNew) {
			this.setText(query);
		}
		FoodPacketList search = control.getDatabase().search(query);
		this.array = search.toArray();
		if (this.array != null) {
			int length = 0;
			
			if (this.array.length > 25)
				length = 24;
			else
				length = this.array.length;

			// Adds each individual food packet and its information to the JList
			// of results
			for (int i = 0; i < length; i++) {
				FoodPacket foodPacket = this.array[i];
				this.listModel.addElement(foodPacket);
			}
		}

	}

	/**
	 * Refreshes the JList when another entry is entered or the current entry
	 * changes
	 */
	public void refresh() {
		listModel.clear();
	}

	/**
	 * Called when a food item is selected in the JList. Redirects the user to
	 * the corresponding page holding information on the selected item.
	 */
	@Override
	public void valueChanged(ListSelectionEvent event) {
		int temp = this.results.getSelectedIndex();
		if (temp != -1) {
			this.control.showInfo(this.array[this.results.getSelectedIndex()]);
		}
	}

}
