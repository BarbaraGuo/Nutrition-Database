import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import database.Database;
import database.datastrucutres.FoodPacket;
import database.datastrucutres.FoodPacketList;

/**
 * Acts as the control center for the panels. Uses a card layout to determine
 * which panels to display to the user. Also sends images to the food group
 * panels to display their respective button images.
 *
 */
public class PanelSetter extends JPanel {

	private Database database;
	public static final int SEARCH_DELAY = 500;
	protected static String[][] panelImages = {
			{ "1800", "2000", "0800", "18002", "20002", "08002" },
			{ "1300", "1700", "1000", "0500", "0700", "1600", "1200", "1500",
					"13002", "17002", "10002", "05002", "07002", "16002",
					"12002", "15002" },
			{ "0100", "_0100", "01002", "_01002"},
			{ "1100", "0900", "11002", "09002" },
			{ "0200", "0300", "0400", "0600", "1400", "1900", "2100", "3500",
					"3600", "02002", "03002", "04002", "06002", "14002",
					"19002", "21002", "35002", "36002" } };
	protected static final int GRAIN = 0, MEAT = 1, DAIRY = 2, VEGFRUIT = 3,
			OTHER = 4;
	protected boolean isHome;
	private BorderLayout borderlayout;
	private FoodGroupPanel grainPanel, meatPanel, dairyPanel, vegFruitPanel,
			otherPanel;
	protected HomePanel homePanel;
	private ResultsPanel resultsPanel;
	private AddPanel addPanel;
	private InformationPanel infoPanel;
	private JPanel loading;
	private FoodPacketList data;

	CardLayout clManager;

	/**
	 * Constructor for the Panel setter. Adds everything to the card layout.
	 * 
	 * @param borderlayout
	 *            Reference to the border layout instantiated in Nutrition Main.
	 *            Used to determine the distance from the header depending on
	 *            which panel the card layout (panel setter) is showing (design
	 *            use).
	 */
	PanelSetter(BorderLayout borderlayout) {

		super();

		// Initiates both the database and the card layout manager
		database = new Database();
		clManager = new CardLayout();

		this.setLayout(clManager);
		this.borderlayout = borderlayout;
		this.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		
		// Initiates all of the panels
		homePanel = new HomePanel(this);
		resultsPanel = new ResultsPanel(this, data);
		addPanel = new AddPanel(this);
		infoPanel = new InformationPanel(this);
		loading = new Loading(this);
		grainPanel = new FoodGroupPanel(this, panelImages[GRAIN], GRAIN);
		meatPanel = new FoodGroupPanel(this, panelImages[MEAT], MEAT);
		dairyPanel = new FoodGroupPanel(this, panelImages[DAIRY], DAIRY);
		vegFruitPanel = new FoodGroupPanel(this, panelImages[VEGFRUIT],
				VEGFRUIT);
		otherPanel = new FoodGroupPanel(this, panelImages[OTHER], OTHER);

		// Adds previously initiated panels to the cardlayout
		this.add(homePanel, "home");
		this.add(resultsPanel, "results");
		this.add(addPanel, "add");
		this.add(infoPanel, "info");
		this.add(meatPanel, "meat");
		this.add(dairyPanel, "dairy");
		this.add(grainPanel, "grain");
		this.add(vegFruitPanel, "vegFruit");
		this.add(otherPanel, "other");
		this.add(loading, "loading");

		// Alerts the panel setter when the database is done loading
		new Thread() {
			public void run() {
				while (!PanelSetter.this.database.isLoaded()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//PanelSetter.this.doneLoading();
			}
		}.start();

	}

	/**
	 * Method which allows all associated panels to gain access to the database
	 * 
	 * @return The database
	 */
	protected Database getDatabase() {
		return this.database;
	}

	/**
	 * Shows the results panel
	 * 
	 * @param query
	 *            User entered query, used later for searching the database
	 * @param isReset
	 *            Determines whether or not the search bar in the results panel
	 *            rests its text
	 */
	protected void showResults(String query, boolean isReset) {
		clManager.show(this, "results");
		resultsPanel.loadResults(query, isReset);
		isHome = false;
		this.resultsPanel.internallyRequestFocus();
	}

	/**
	 * Alternate method to show the results panel. Text in the search bar is
	 * reset by default.
	 * 
	 * @param query
	 *            User entered query, used later for searching the database
	 */
	protected void showResults(String query) {
		clManager.show(this, "results");
		resultsPanel.loadResults(query, true);
		isHome = false;
		this.resultsPanel.internallyRequestFocus();
	}

	/**
	 * Another method to show the results. This is exclusively for the food
	 * group panels that search via filters opposed to user entered queries.
	 * 
	 * @param list
	 *            List of all food items pertaining to a food group
	 */
	protected void showResults(FoodPacketList list) {
		clManager.show(this, "results");
		resultsPanel.loadResults(list);
		isHome = false;
		this.resultsPanel.internallyRequestFocus();
	}

	/**
	 * Shows the home panel
	 */
	protected void showHome() {
		clManager.show(this, "home");

		// Sets the gap between to header and the body of the home panel ( this
		// is purely for design purposes)
		isHome = true;
		borderlayout.setVgap(40);
	}

	/**
	 * In the food group panels, shows the grain panel
	 */
	protected void showGrain() {
		clManager.show(this, "grain");
		isHome = false;
	}

	/**
	 * In the food group panels, shows the meat panel
	 */
	protected void showMeat() {
		clManager.show(this, "meat");
		isHome = false;
	}

	/**
	 * In the food group panels, shows the dairy panel
	 */
	protected void showDairy() {
		clManager.show(this, "dairy");
		isHome = false;
	}

	/**
	 * In the food group panels, shows the vegetables and fruits panel
	 */
	protected void showVegFruit() {
		clManager.show(this, "vegFruit");
		isHome = false;
	}

	/**
	 * In the food group panels, shows the other food groups panel
	 */
	protected void showOther() {
		clManager.show(this, "other");
		isHome = false;
	}

	/**
	 * Displays panel showing the individual information for a selected food
	 * item
	 */
	protected void showInfo(FoodPacket foodPacket) {
		clManager.show(this, "info");
		infoPanel.displayResults(foodPacket);
		isHome = false;
	}

	/**
	 * Displays the add panel for the user
	 */
	protected void showAdd() {
		clManager.show(this, "add");
		isHome = false;
	}

	protected void doneLoading() {
		clManager.show(this, "home");
		addPanel.loaded();
	}

}
