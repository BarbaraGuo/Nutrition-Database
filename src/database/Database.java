package database;

import java.io.File;
import java.io.IOException;

import database.datastrucutres.BinaryTree;
import database.datastrucutres.FoodPacket;
import database.datastrucutres.FoodPacketBinaryTree;
import database.datastrucutres.FoodPacketList;
import database.datastrucutres.IndependantSearchable;
import database.datastrucutres.LinkedList;
import database.datastrucutres.Nutrient;
import database.datastrucutres.NutrientList;
import database.datastrucutres.Searchable;

/**
 * Main object, creates and stores all USDA files' information
 *
 */
public class Database {

	/**
	 * Array of data used for the food description when adding foods to the
	 * Database (Order of the variables in the 'addFood()' method)
	 */
	public static final String[] ADD_FOOD = { "FdGrp_Cd", "Long_Desc", "ComName" };

	/**
	 * Array of data used for the food's nutrient information when adding foods
	 * to the Database (2-D nutrients array should have individual arrays of
	 * this format)<br>
	 * For example, use '{{ "Nutr_No", "Nutr_Val" }, { "Nutr_No", "Nutr_Val" },
	 * { "Nutr_No", "Nutr_Val" }}' for three nutrients associated with a
	 * specific food
	 */
	public static final String[] ADD_NUTRIENT = { "Nutr_No", "Nutr_Val" };

	/**
	 * Names of the files used in the database
	 */
	public static final String[] FILE_NAMES = { "FOOD_DES.txt", "NUT_DATA.txt", "WEIGHT.txt", "NUTR_DEF.txt",
			"FD_GROUP.txt", "LANGDESC.txt", "LANGUAL.txt", "FOOTNOTE.txt", };

	/**
	 * Total number of bytes in all of the files
	 */
	private long totalBytes;

	/**
	 * Amount of bytes currently loaded <br>
	 * Incremented as the files are being loaded through the 'parsedBytes()'
	 * method
	 */
	private long bytesLoaded;

	/**
	 * Boolean array representing all of the files and their loading status
	 */
	private boolean[] filesLoaded;

	/**
	 * All of the files used by the Database, loaded into as soon as a new
	 * Database object is created
	 */
	private File[] files;

	/**
	 * Represents the loaded state of the entire Database<br>
	 * False means at least one file is still to be loaded True means every
	 * required data-structure has been filled and is the Database is ready to
	 * be used
	 */
	private boolean loaded;

	/**
	 * Contains the information searchable by the user
	 */
	private FoodPacketBinaryTree main, foodGroups, nutrientDef;

	/**
	 * Contains langual information (used primarily for searching)
	 */
	private BinaryTree<Searchable> languals;

	/**
	 * Initializes and loads data into all data structures
	 */
	public Database() {
		this.bytesLoaded = 0;

		// Set the files to be loaded equal to the number of files
		this.filesLoaded = new boolean[FILE_NAMES.length];

		// Load specified files into data structure
		// Create the Parser object to do file input and data parsing
		Parser parser = new Parser(this);

		// Loads the files in a new thread to allow for the Database object to
		// be finished initializing and respond to loading status requests
		new Thread() {
			public void run() {

				// Load all the files and add the number of bytes in each file
				// to the 'totalBytes' variable
				Database.this.files = new File[FILE_NAMES.length];
				for (int i = 0; i < FILE_NAMES.length; i++) {
					Database.this.files[i] = new File(FILE_NAMES[i]);
					Database.this.totalBytes += Database.this.files[i].length();
				}

				// Load food description information
				try {
					Database.this.main = parser.parse(Database.this.files[FoodPacketBinaryTree.FOOD_DES],
							FoodPacketBinaryTree.FOOD_DES);
				} catch (Exception e) {
					System.err.println("Error loading 'FOOD_DES.txt'");
					e.printStackTrace();
				}
				// Set the loading status of 'FOOD_DES.txt'to true
				Database.this.setFileLoaded(FoodPacketBinaryTree.FOOD_DES);

				// Loading individual nutrient information for each food item
				try {
					parser.parseNutrients(Database.this.files[FoodPacketBinaryTree.NUT_DATA], Database.this.main);
				} catch (Exception e) {
					System.err.println("Error loading 'NUT_DATA.txt'");
					e.printStackTrace();
				}
				// Set the loading status of 'NUT_DATA.txt'to true
				Database.this.setFileLoaded(FoodPacketBinaryTree.NUT_DATA);

				// Load food weight information
				try {
					parser.parseWeightData(Database.this.files[FoodPacketBinaryTree.WEIGHT], Database.this.main);
				} catch (Exception e) {
					System.err.println("Error loading 'WEIGHT.txt'");
					e.printStackTrace();
				}
				// Set the loading status of 'WEIGHT.txt'to true
				Database.this.setFileLoaded(FoodPacketBinaryTree.WEIGHT);

				// Load nutrient descriptions
				try {
					Database.this.nutrientDef = parser.parse(Database.this.files[FoodPacketBinaryTree.NUTR_DEF],
							FoodPacketBinaryTree.NUTR_DEF);
				} catch (Exception e) {
					System.err.println("Error loading 'NUTR_DEF.txt'");
					e.printStackTrace();
				}
				// Set the loading status of 'NUTR_DEF.txt'to true
				Database.this.setFileLoaded(FoodPacketBinaryTree.NUTR_DEF);

				// Load food group descriptions
				try {
					Database.this.foodGroups = parser.parse(Database.this.files[FoodPacketBinaryTree.FD_GROUP],
							FoodPacketBinaryTree.FD_GROUP);
				} catch (Exception e) {
					System.err.println("Error loading 'FD_GROUP.txt'");
					e.printStackTrace();
				}
				// Set the loading status of 'FD_GROUP.txt'to true
				Database.this.setFileLoaded(FoodPacketBinaryTree.FD_GROUP);

				// Load langual information
				try {
					Database.this.languals = parser.parseLanguals(Database.this.files[FoodPacketBinaryTree.LANGUAL],
							Database.this.files[FoodPacketBinaryTree.LANGDESC], Database.this.main);
				} catch (Exception e) {
					System.err.println("Error loading 'LANGUAL.txt'");
					e.printStackTrace();
				}
				// Set the loading status of 'LANGUAL.txt' and 'LANGDESC.txt' to
				// true (Set together as these files load almost instantly and
				// are loaded in the same method)
				Database.this.setFileLoaded(FoodPacketBinaryTree.LANGDESC);
				Database.this.setFileLoaded(FoodPacketBinaryTree.LANGUAL);

				// Load footnote information
				try {
					parser.parseFootNotes(Database.this.files[FoodPacketBinaryTree.FOOTNOTE], Database.this.main);
				} catch (Exception e) {
					System.err.println("Error loading 'FOOTNOTE.txt'");
					e.printStackTrace();
				}
				// Set the loading status of 'FOOTNOTE.txt'to true
				Database.this.setFileLoaded(FoodPacketBinaryTree.FOOTNOTE);
			}
		}.start();
	}

	/**
	 * Finds all items in tree which contain specified query in the long
	 * description, short description, common name, primary key, or langual
	 * information
	 * 
	 * @param query
	 *            The string to search for
	 * @return LinkedList of FoodPacket objects for all matches, EMPTY linked
	 *         list (.size() == 0) if not results found, or NULL if Database is
	 *         not loaded
	 */
	public FoodPacketList search(String query) {
		query = query.trim();
		if (query.equals(""))
			return new FoodPacketList();
		if (this.isLoaded()) {
			return this.search(query.replaceAll("^[,\\s]+", "").split("[,\\s]+"),
					new String[] { "Long_Desc", "Short_Desc", "ComName", "NDB_No" }, true);
		}
		return null;
	}

	/**
	 * Finds all items in tree which contain specified query in the long
	 * description, short description, common name, primary key, or langual
	 * information
	 * 
	 * @param query
	 *            The string to search for
	 * @param header
	 *            The field to look in
	 * @return LinkedList of FoodPacket objects for all matches, EMPTY linked
	 *         list (.size() == 0) if not results found, or NULL if Database is
	 *         not loaded
	 */
	public FoodPacketList search(String query, String header) {
		query = query.trim();
		if (query.equals(""))
			return new FoodPacketList();
		if (this.isLoaded()) {
			return this.search(query.replaceAll("^[,\\s]+", "").split("[,\\s]+"), new String[] { header }, false);
		}
		return null;
	}

	/**
	 * Finds all items in tree which contain specified query in the specified
	 * fields
	 * 
	 * @param queries
	 *            The strings to search for
	 * @param header
	 *            The fields to look at in the database
	 * @param useLanguals
	 *            Whether or not to search using langual information
	 * @return LinkedList of FoodPacket objects for all matches, EMPTY linked
	 *         list (.size() == 0) if not results found, or NULL if Database is
	 *         not loaded
	 */
	public FoodPacketList search(String[] queries, String[] headers, boolean useLanguals) {
		if (this.isLoaded()) {
			return this.main.search(queries, headers, useLanguals);
		}
		return null;
	}

	/**
	 * PREFEREED METHOD: Due to efficiency of comparing integers instead to
	 * strings <br>
	 * Finds the description of the requested food group using key in intriguer
	 * format
	 * 
	 * @param no
	 *            Food group number
	 * @return The description of the requested food group, BLANK string ("") if
	 *         not found, or NULL if Database is not loaded
	 */
	public String getFoodGroup(int no) {
		if (this.isLoaded()) {
			return this.foodGroups.get(no).getValue("FdGrp_Desc");
		}
		return null;
	}

	/**
	 * Finds the description of the requested food group using key in string
	 * format
	 * 
	 * @param no
	 *            Food group number <br>
	 *            <b>MUST MATCH EXACTLY</b> for example: get("100") will not
	 *            return the food group description with key "0100"
	 * @return The description of the requested food group, BLANK string ("") if
	 *         not found, or NULL if Database is not loaded
	 */
	public String getFoodGroup(String no) {
		if (this.isLoaded()) {
			return this.foodGroups.get(Integer.parseInt(no)).getValue("FdGrp_Desc");
		}
		return null;
	}

	/**
	 * PREFEREED METHOD: Due to efficiency of comparing integers instead to
	 * strings Finds the information associated with the requested nutrient
	 * number
	 * 
	 * @param key
	 *            the nutrient key (primary key in 'NUTR_DEF' file), ex. '301'
	 * @return The information associated with the requested nutrient number or
	 *         NULL if not found or Database isn't loaded
	 */
	public FoodPacket getNutrientData(int key) {
		if (this.isLoaded()) {
			return this.nutrientDef.get(key);
		}
		return null;
	}

	/**
	 * Gets all nutrient information from the file 'NUTR_DEF.txt'
	 * 
	 * @return All nutrient descriptions or NULL if Database isn't loaded
	 */
	public FoodPacketList getAllNutrients() {
		if (this.isLoaded()) {
			return this.nutrientDef.toLinkedList();
		}
		return null;
	}

	/**
	 * Finds the information associated with the requested nutrient number
	 * 
	 * @param key
	 *            the nutrient key (primary key in 'NUTR_DEF' file), ex. "301"
	 *            in string form<br>
	 *            <b>MUST MATCH EXACTLY</b> for example: get("101") will not
	 *            return the info about the nutrient with key "0101"
	 * @return The information associated with the requested nutrient number or
	 *         NULL if not found or the Database isn't loaded
	 */
	public FoodPacket getNutrientData(String key) {
		if (this.isLoaded()) {
			return this.nutrientDef.get(Integer.parseInt(key));
		}
		return null;
	}

	/**
	 * Find the langual information associated with the supplied keys
	 * 
	 * @param keys
	 *            String array of primary keys from 'LANGDESC' file, ex. 'A0107'
	 * @return LinkedList of same length as keys array (if keys are valid) of
	 *         langual descriptions, or NULL if Database is not loaded yet
	 */
	public LinkedList<String> getLanguals(String[] keys) {
		if (this.isLoaded()) {
			LinkedList<String> list = new LinkedList<String>();
			if (keys == null || this.languals == null)
				return list;
			for (String key : keys) {
				list.add(this.languals.get(new IndependantSearchable(key)).getValue());
			}
			return list;
		}
		return null;
	}

	/**
	 * ** MOST EFFICIENT way to retrieve info if you have primary key **<br>
	 * Useful for getting reference data based of reference key <br>
	 * Finds a FoodPacket based on supplied primary key
	 * 
	 * @param key
	 *            Primary key from 'FOOD_DES.txt' file associated with desired
	 *            food
	 * @return FoodPacket object associated with the provided key or NULL is not
	 *         found or the Database is not ready
	 */
	public FoodPacket getFood(int key) {
		if (this.isLoaded()) {
			return this.main.get(key);
		}
		return null;
	}

	/**
	 * ** <b>THIS IS A PERMANENT ACTION</b> ** <br>
	 * 
	 * Allows the user to add a desired food with specified nutrients into the
	 * data-structures and files <br>
	 * <br>
	 * 
	 * <b>Use the format specified in the public static 'Database.ADD_NUTRIENT'
	 * array </b> for the structure of the individual arrays for the nutrient
	 * information
	 * 
	 * @param foodGroupCode
	 *            Specific food group code the food item falls under (primary
	 *            key from 'FD_GROUP.txt')
	 * @param description
	 *            Description of food, in key words, separated by commas
	 * @param commonName
	 *            Common names of the food item separated by commas (i.e 'Seeds,
	 *            shells')
	 * @param manufacturerName
	 *            (If applicable) Name of manufacturer of food item
	 * @param nutrientInfo
	 *            2-dimensional string array containing n arrays of the
	 *            'Nutr_No' and 'Nutr_Val', n being the number of associated
	 *            nutrients. '<b>Nutr_No</b>' is the primary key for the
	 *            'NUTR_DEF.txt' file and '<b>Nutr_Val</b>' is the amount of the
	 *            nutrient in 100g of the food item
	 */
	public void addFood(String foodGroupCode, String description, String commonName, String manufacturerName,
			String scientificName, String[][] nutrientInfo) {
		if (this.isLoaded()) {
			// Capitalize first letter of description to stay consistent with
			// format
			if (!Character.isUpperCase(description.charAt(0))) {
				try {
					description = Character.toUpperCase(description.charAt(0)) + description.substring(1);
				} catch (Exception e) {
					// If this capitalization didn't work, then leave the
					// description as it is
				}
			}
			// Use the first 60 characters of the long description as the short
			// description (because it is mandatory in the file)
			String[] foodDescription = { (this.main.getLargestKey() + 1) + "", foodGroupCode, description,
					description.length() <= 60 ? description.toUpperCase() : description.substring(0, 60).toUpperCase(),
					commonName, manufacturerName, "", "", "", scientificName, "", "", "", "11/2015" };
			FoodPacket item = new FoodPacket(foodDescription, FoodPacketBinaryTree.FOOD_DES);
			this.main.add(item);

			// Add nutrient information to 'NUT_DATA.txt'
			NutrientList nutrients = new NutrientList();
			for (String[] values : nutrientInfo) {
				nutrients.add(new Nutrient(values));
				try {
					Parser.addToFile(new String[] { foodDescription[0], values[0], values[1], "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "" }, "NUT_DATA.txt", FoodPacketBinaryTree.NUT_DATA);
				} catch (IOException e) {
					System.err.println("Error adding nutrient data for food '" + description + "' to 'NUT_DATA.txt'");
					e.printStackTrace();
				}
			}
			item.addNutrients(nutrients);

			// Add food description information to 'FOOD_DES.txt'
			try {
				Parser.addToFile(foodDescription, "FOOD_DES.txt", FoodPacketBinaryTree.FOOD_DES);
			} catch (IOException e) {
				System.err.println("Error adding food '" + description + "' to 'FOOD_DES.txt'");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets file at the specified index as loaded Synchronized to prevent
	 * multiple threads from updating the 'filesLoaded' array at the same time
	 * and to lock the array from use by other methods
	 * 
	 * @param index
	 *            Index of file to set as loaded
	 */
	private synchronized void setFileLoaded(int index) {
		this.filesLoaded[index] = true;
		if (index == this.filesLoaded.length - 1)
			this.setLoaded();
	}

	/**
	 * Sets the loaded status of the database to true<br>
	 * Synchronized to prevent multiple threads from updating the 'loaded'
	 * variable at the same time and to lock the 'loaded' variable from use by
	 * other methods
	 */
	private synchronized void setLoaded() {
		this.loaded = true;
	}

	/**
	 * Get the current number of loaded bytes<br>
	 * Synchronized to prevent multiple threads from obtaining out-of-date
	 * information and retrieval of data while it's being changed by the
	 * 'parsedBytes()' methods
	 * 
	 * @return Current number of loaded bytes
	 */
	private synchronized long getLoadedBytes() {
		return this.bytesLoaded;
	}

	/**
	 * Adds specified number of bytes to the total bytes counts<br>
	 * Synchronized to prevent multiple threads from updating the 'bytesLoaded'
	 * variable at the same time and to lock the 'bytesLoaded' variable from use
	 * by other methods
	 * 
	 * @param bytes
	 *            The specified number of bytes processed from the file
	 */
	synchronized void parsedBytes(long bytes) {
		this.bytesLoaded += bytes;
	}

	/**
	 * Returns the loaded status of the database<br>
	 * Synchronized to prevent GUI threads from obtaining out-of-date
	 * information and retrieval of data while it's being changed by the
	 * 'setLoaded()' method
	 * 
	 * @return Whether or not all the data structures in the Database object are
	 *         fully loaded
	 */
	public synchronized boolean isLoaded() {
		return this.loaded;
	}

	/**
	 * Gets percentage of the data from the files have been loaded <br>
	 * Synchronized to prevent GUI threads from obtaining out-of-date
	 * information and retrieval of data while it's being changed by the
	 * 'parsedBytes()' method
	 * 
	 * @return Percentage of the data from the files have been loaded (rounded
	 *         down to the nearest integer)<br>
	 *         A integer between 0 and 100
	 */
	public synchronized int getPercentageLoaded() {
		return (int) (this.getLoadedBytes() * 100 / this.totalBytes);
	}

	/**
	 * Creates a loading message with the current file being loaded and the
	 * percentage of total data that has been loaded. <br>
	 * <br>
	 * Format: "Loading FILE_NAME.txt: x% completed!", x being the percentage of
	 * the data from the files that have been loaded (from
	 * 'getPercentageLoaded()' method)
	 * 
	 * @return The pre-formatted loading message with the file name and
	 *         percentage loaded
	 */
	public synchronized String getLoadingMessage() {
		// Loop through the filesLoaded boolean array to find the first file
		// that isn't loaded yet (as the files are loaded in the order of the
		// array) and return the message based on that file's name
		for (int i = 0; i < this.filesLoaded.length; i++) {
			if (!this.filesLoaded[i]) {
				return "Loading " + FILE_NAMES[i] + ": " + this.getPercentageLoaded() + "% completed!";
			}
		}
		return "File's all loaded!";
	}
}
