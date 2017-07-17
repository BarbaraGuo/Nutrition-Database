package database.datastrucutres;

/**
 * Data storage class to store nutrient information for individual foods in most
 * efficient way
 *
 */
public class Nutrient implements Comparable<Nutrient> {

	private SearchableLinkedList values = new SearchableLinkedList();

	/**
	 * Primary key from the 'NUTR_DEF.txt' file
	 */
	private int key;

	/**
	 * Creates a new Nutrient object
	 * 
	 * @param nutrients
	 */
	public Nutrient(String[] nutrients) {
		this.key = Integer.parseInt(nutrients[0]);
		this.addData(nutrients);
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
			// Use 'i + 1' for links to 'FoodPacketBinaryTree' because the first
			// field 'NDB_NO' has been omitted from the passed in array
			// parameter to save memory space
			if (FoodPacketBinaryTree.FIELDS_TO_LOAD[FoodPacketBinaryTree.NUT_DATA][i + 1])
				this.values.add(new LinkedSearchable(i + 1, data[i], FoodPacketBinaryTree.NUT_DATA));
		}
	}

	/**
	 * Returns the string value associated with the specified header
	 * 
	 * @param header
	 *            Obtainable from
	 *            'FoodPacketBinaryTree.HEADERS[FoodPacketBinaryTree.NUT_DATA]',
	 *            which contains all possible header options
	 * @return String value associated with the specified header or a BLANK
	 *         STRING if not found
	 */
	public String getValue(String header) {
		return this.values.get(header);
	}

	/**
	 * Getter for the primary key for the Nutrient object
	 * 
	 * @return the primary key from the 'NUTR_DEF.txt' file
	 */
	public int getKey() {
		return this.key;
	}

	/**
	 * Compares the primary keys of the Nutrient objetcs, returning a negative
	 * number if this object's key is greater than the object which was passed a
	 * a parameter's key
	 */
	@Override
	public int compareTo(Nutrient other) {
		return other.getKey() - this.key;
	}

	/**
	 * Returns the associated primary key
	 */
	@Override
	public String toString() {
		return this.key + "";
	}
}
