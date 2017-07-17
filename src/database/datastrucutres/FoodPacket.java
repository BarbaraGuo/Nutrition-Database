package database.datastrucutres;

/**
 * Data packet containing one food item and all associated data references (can
 * be reused for other applications)
 *
 */
public class FoodPacket implements Comparable<FoodPacket> {

	/**
	 * Data from specified file
	 */
	private SearchableLinkedList values;

	/**
	 * The primary key from the specified file (ex. 2001)
	 */
	private int key;

	/**
	 * The file of which the data is being retrieved from
	 */
	private int fileNo;

	/**
	 * The list of all nutrient information (if applicable)
	 */
	private NutrientList nutrientList;

	/**
	 * List containing related footnote information (if applicable)
	 */
	private LinkedList<String> footNotes;

	/**
	 * List containing related langual information (if applicable)
	 */
	private LinkedList<String> languals;

	/**
	 * Linked list containing related weight information
	 */
	private SearchableLinkedList weightData;

	/**
	 * Creates a food packet object which specified data and headers
	 * 
	 * @param data
	 *            The string array of data which is to be added as a LinkedList
	 *            into the FoodPacket
	 * @param fileNo
	 *            The integer value of the file obtainable from the
	 *            FoodPacketBinaryTree class as a public static integer value
	 *            using the name of the file (for example: for 'FOOD_DES.txt'
	 *            use 'FoodPacketBinaryTree.FOOD_DES' as the file number)
	 */
	public FoodPacket(String[] data, int fileNo) {
		this.key = Integer.parseInt(data[0]);
		this.footNotes = new LinkedList<String>();
		this.languals = new LinkedList<String>();
		this.fileNo = fileNo;
		this.addData(data);
	}

	/**
	 * Adds the provided data (in the form of the a string array) into this
	 * object as a LinkedList
	 * 
	 * @param data
	 *            The string array of data being added into the Linked List
	 */
	private void addData(String[] data) {
		this.values = new SearchableLinkedList();
		for (int i = 0; i < data.length; i++) {
			this.values.add(new LinkedSearchable(i, data[i], this.fileNo));
		}
	}

	/**
	 * Compares the keys of the FoodPacket objects, returning a negative value
	 * if value of this object's key is greater than value of other object's key
	 */
	@Override
	public int compareTo(FoodPacket other) {
		return other.getKey() - this.key;
	}

	/**
	 * Compares the value of this object's key with the provided key
	 * 
	 * @param key
	 * @return the difference between keys' values, negative if value of this
	 *         object's key is greater than value of provided key
	 */
	public int compareTo(int key) {
		return key - this.key;
	}

	/**
	 * Getter for key (primary key in provided data) Key can be obtained in
	 * string value by searching for first header in the specified file, for
	 * example: if the file is 'FOOD_DES.txt', use
	 * 'getValue(FoodPacketBinaryTree.HEADERS[FoodPacketBinaryTree.FOOD_DES][0])
	 * ' to get the header in string format)
	 * 
	 * @return the primary key as an integer
	 */
	public int getKey() {
		return this.key;
	}

	/**
	 * Finds the value associated with the specified header
	 * 
	 * @param header
	 *            The header associated with the value wanted
	 * @return The value associated with the specified header
	 */
	public String getValue(String header) {
		return this.values.get(header);
	}

	/**
	 * Associates a specified (already existing) header with a specified string
	 * value
	 * 
	 * @param header
	 *            The already existing header value (as a string)
	 * @param value
	 *            The new value to associate the header with
	 * @return Whether or not the header existed beforehand (if false, the value
	 *         was NOT ADDED to the object)
	 */
	public boolean setValue(String header, String value) {
		return this.values.set(header, value);
	}

	/**
	 * FOR DEBUGGING PURPOSES: returns the primary key instead of the object id
	 */
	@Override
	public String toString() {
		return this.key + " "  + this.getValue("Long_Desc") + " Hi";
	}

	/**
	 * Adds the linked list of associated Nutrient objects to the FoodPacket
	 * 
	 * @param nutrients
	 *            The linked list of Nutrient objects
	 */
	public void addNutrients(NutrientList nutrients) {
		this.nutrientList = nutrients;
	}

	/**
	 * Getter for list of associated Nutrient objects
	 * 
	 * @return a LinkedList of Nutrient objects
	 */
	public NutrientList getNutrients() {
		return this.nutrientList;
	}

	/**
	 * Adds a specific footnote to the LinkedList of footnotes
	 * 
	 * @param note
	 *            The footnote to add
	 */
	public void addFootNote(String note) {
		this.footNotes.add(note);
	}

	/**
	 * Getter for associated footnote information
	 * 
	 * @return Array of all associated footnote information
	 */
	public String[] getFootNotes() {
		return this.footNotes.toArray();
	}

	/**
	 * Adds a specific langual to the LinkedList of langual information
	 * 
	 * @param data
	 *            The langual to add
	 */
	public void addLanguals(String data) {
		this.languals.add(data);
	}

	/**
	 * Getter for associated langual information
	 * 
	 * @return Array of all associated langual information
	 */
	public String[] getLanguals() {
		return this.languals.toArray();
	}

	/**
	 * Associates the specified linked list to this FoodPacket object
	 * 
	 * @param weightData
	 */
	public void addWeightData(SearchableLinkedList weightData) {
		this.weightData = weightData;
	}

	/**
	 * Getter for the associated linked list of weight-related information
	 * 
	 * @return Associated linked list of weight information or NULL if no
	 *         associated weight information exists
	 */
	public SearchableLinkedList getWeightData() {
		return this.weightData;
	}
}
