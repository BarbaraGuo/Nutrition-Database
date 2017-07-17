package database.datastrucutres;

/**
 * Data storage class which links a specified headerNo to a specified value, the
 * headerNo value being an integer reference to the headerNo in the
 * FoodPacketBinaryTree.HEADERS array to save space and prevent duplicate data
 *
 */
public class LinkedSearchable extends Searchable {

	/**
	 * The reference to the index of the array containing the headerNo (in the
	 * FoodPacketBinaryTree.HEADERS 2-D array)
	 */
	private int fileNo;

	/**
	 * The index of the headerNo in the array of headers for the specific
	 * fileNo, obtainable using the 'fileNo' variable for the first index. For
	 * example: 'FoodPacketBinaryTree.HEADERS[fileNo][headerNo]' is where the
	 * specified headerNo is stored
	 */
	private int headerNo;

	/**
	 * Creates a data storage object with a reference to the headerNo (to save
	 * memory) and a desired value to be linked to it
	 * 
	 * @param headerNo
	 *            Name of field
	 * @param value
	 *            Value to link the headerNo to
	 * @param fileNo
	 *            index of array where headerNo information is stored
	 *            (obtainable using fileNo name, for example: for fileNo
	 *            'FOOD_DES.txt', use variable FoodPacketBinaryTree.FOOD_DES as
	 *            the fileNo number)
	 */
	public LinkedSearchable(int headerNo, String value, int fileNo) {
		this.value = value;
		this.headerNo = headerNo;
		this.fileNo = fileNo;
	}

	/**
	 * Finds headerNo referenced by the contained headerNo number
	 * 
	 * @return headerNo referenced by the contained headerNo number, or a BLANK
	 *         STRING if not found
	 */
	public String getHeader() {
		try {
			return FoodPacketBinaryTree.HEADERS[this.fileNo][this.headerNo];
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Getter for the headerNo reference number
	 * 
	 * @return the index position in the specified headerNo array for where this
	 *         object's headerNo is stored
	 */
	public int getHeaderNo() {
		return this.headerNo;
	}

	/**
	 * Returns the 'String'.compareTo() value of the two (string) headers
	 */
	@Override
	public int compareTo(Searchable other) {
		if (this.getHeader() == null)
			return 1;
		else if (other.getHeader().equals(""))
			return -1;
		return this.getHeader().compareTo(other.getHeader());
	}
}
