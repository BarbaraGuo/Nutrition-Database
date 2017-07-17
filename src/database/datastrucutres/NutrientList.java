package database.datastrucutres;

/**
 * Linked list containing Nutrient objects with Nutrient-specific methods for
 * easier retrieval of these objects
 *
 */
public class NutrientList extends LinkedList<Nutrient> {

	/**
	 * Creates an empty LinkedList of type Nutrient
	 */
	public NutrientList() {
		super();
	}

	/**
	 * PREFEREED METHOD: Due to efficiency of comparing integers instead to
	 * strings <br>
	 * Getter for the Nutrient object associated with the provided primary key
	 * value (in integer form)
	 * 
	 * @param key
	 *            Primary key of the Nutrient object (first field in the
	 *            'NUTR_DEF.txt' file and second field in the 'NUT_DATA.txt'
	 *            file) in integer form
	 * @return Nutrient object associated with provided key
	 */
	public Nutrient get(int key) {
		ListNode<Nutrient> temp = this.head;
		while (temp != null) {
			if (temp.getItem().getKey() == key)
				return temp.getItem();
			temp = temp.getNext();
		}
		return null;
	}

	/**
	 * Getter for the Nutrient object associated with the provided primary key
	 * value (in string form)
	 * 
	 * @param key
	 *            Primary key of the Nutrient object (first field in the
	 *            'NUTR_DEF.txt' file and second field in the 'NUT_DATA.txt'
	 *            file) in string form.<br>
	 *            <b>MUST MATCH EXACTLY</b> for example: get("101") will not
	 *            return the Nutrient with key "0101"
	 * @return Nutrient object associated with provided key
	 */
	public Nutrient get(String key) {
		ListNode<Nutrient> temp = this.head;
		while (temp != null) {
			if (temp.getItem().getValue("Nutr_No").equals(key))
				return temp.getItem();
			temp = temp.getNext();
		}
		return null;
	}

	/**
	 * PREFEREED METHOD: Due to efficiency of comparing integers instead to
	 * strings <br>
	 * Finds whether or not a Nutrient object is associated with the provided
	 * key in the list
	 * 
	 * @param key
	 *            Primary key of 'NUTR_DEF.txt' file in integer form
	 * @return Whether or not a Nutrient object is associated with the provided
	 *         key in the list
	 */
	public boolean contains(int key) {
		return this.get(key) != null;
	}

	/**
	 * Finds whether or not a Nutrient object is associated with the provided
	 * key in the list
	 * 
	 * @param key
	 *            Primary key of 'NUTR_DEF.txt' file in string form <br>
	 *            <b>MUST MATCH EXACTLY</b> for example: get("101") will not
	 *            return the Nutrient with key "0101"
	 * @return Whether or not a Nutrient object is associated with the provided
	 *         key in the list
	 */
	public boolean contains(String key) {
		return this.get(key) != null;
	}
}