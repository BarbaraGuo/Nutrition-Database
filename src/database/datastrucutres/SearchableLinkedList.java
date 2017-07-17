package database.datastrucutres;

/**
 * Linked list containing LinkedSearchable objects with specific methods for
 * easier retrieval of these objects using the header values
 *
 */
public class SearchableLinkedList extends LinkedList<Searchable> {

	/**
	 * Creates an empty LinkedList of type LinkedSearchable
	 */
	public SearchableLinkedList() {
		super();
	}

	/**
	 * Finds string value associated with provided header
	 * 
	 * @param header
	 *            Title of field associated with the desired value in string
	 *            form <br>
	 *            <b>MUST MATCH EXACTLY</b> For example: get("ndb_no") will not
	 *            return the value associated with the field "NDB_No"
	 * @return String value associated with the provided header or an EMPTY
	 *         STRING if not found
	 */
	public String get(String header) {
		ListNode<Searchable> temp = this.head;

		// Iteratively finds the node with the provided header value
		while (temp != null) {
			if (temp.getItem().getHeader().equals(header))
				return temp.getItem().getValue();
			temp = temp.getNext();
		}
		return "";
	}

	/**
	 * Replaces the value associated with a specific header
	 * 
	 * @param header
	 *            Title of field you want to associate with a new value <br>
	 *            <b>MUST MATCH EXACTLY</b> For example: set("ndb_no", newValue)
	 *            will not replace the value associated with the field "NDB_No"
	 * @param value
	 *            New value to associate with specified header
	 * @return True if the specified header was found in the list, false
	 *         otherwise
	 */
	boolean set(String header, String value) {
		ListNode<Searchable> temp = this.head;
		while (temp != null) {
			if (temp.getItem().getHeader().equals(header)) {
				temp.getItem().setValue(value);
				return true;
			}
			temp = temp.getNext();
		}

		// Only returns false is the header didn't exist in the list
		return false;
	}
}
