package database.datastrucutres;

/**
 * Priority queue containing only FoodPacket items
 *
 */
public class FoodPacketList extends LinkedList<FoodPacket> {

	/**
	 * Flag to be used in the 'toStringArray()' method if user wants all the
	 * results
	 */
	public static final int ALL_RESULTS = -1;

	/**
	 * Creates a new LinkedList of type FoodPacket
	 */
	public FoodPacketList() {
		super();
	}

	/**
	 * Adds the specified FoodPacket object to the list in the appropriate
	 * position based on the priority, with the HIGHEST NUMBER OF MACTHES BEING
	 * FIRST
	 * 
	 * @param item
	 *            FoodPacket object to add
	 * @param matches
	 *            The number of matches the item had when searched for (used for
	 *            priority)
	 */
	public void add(FoodPacket item, int matches) {
		ListNode<FoodPacket> node = new ListNode<FoodPacket>(item, matches);

		// Ignore the ranking prioirty proccess if the list is empty
		if (this.isEmpty())
			this.add(node);
		else {
			ListNode<FoodPacket> searchNode = this.end;

			// Find the placement for the item based on the number of matches
			while (searchNode.getPriority() < matches) {
				searchNode = searchNode.getPrevious();
				if (searchNode == null)
					break;
			}
			// If the placement found is null, that means the priority of this
			// FoodPacket is greater than the head, so add this FoodPacket to
			// the front of the list
			if (searchNode == null) {
				this.head.setPrevious(node);
				node.setNext(this.head);
				this.head = node;
			} else {
				// Add the node into the appropriate position
				ListNode<FoodPacket> temp = searchNode.getNext();
				if (temp == null) {
					// If the position is at the end of the list, just insert
					// regularly
					searchNode.setNext(node);
					node.setPrevious(searchNode);
				} else {
					searchNode.setNext(node);
					node.setNext(temp);
					node.setPrevious(searchNode);
					temp.setPrevious(node);
				}
			}
			this.size++;
		}
	}

	/**
	 * Creates a new array of all of the objects
	 * 
	 * @return array of objects or NULL IF TMPTY
	 */
	public FoodPacket[] toArray() {
		if (this.isEmpty())
			return null;

		// Creates an array of type 'T' using casting technique
		FoodPacket[] items = new FoodPacket[this.size];

		// Add the items to the array iteratively
		ListNode<FoodPacket> temp = this.head;
		for (int i = 0; i < items.length; i++) {
			items[i] = temp.getItem();
			temp = temp.getNext();
		}
		return items;
	}

	/**
	 * ***PREFFERED METHOD due to increased speed of comparing integers vs.
	 * comparing strings and increased time of getting string value of key from
	 * associated values (as key in integer form is a separate 'free' call from
	 * the FoodPacket, whereas string must be retrieved from the list of food
	 * descriptions inside FoodPacket values)
	 * 
	 * @param key
	 *            The key (as an integer) that the desired FoodPacet is
	 *            associated with (this is the primary key from the
	 *            'FOOD_DES.txt' file)
	 * @return The FoodPacket object associated with the given key or NULL IF
	 *         NOT FOUND
	 */
	public FoodPacket get(int key) {
		ListNode<FoodPacket> temp = this.head;
		while (temp != null) {
			if (temp.getItem().getKey() == key)
				return temp.getItem();
			temp = temp.getNext();
		}
		return null;
	}

	/**
	 * 
	 * @param key
	 *            The key (as a string) that the desired FoodPacet is associated
	 *            with (this is the primary key from the 'FOOD_DES.txt' file)
	 * @return The FoodPacket object associated with the given key or NULL IF
	 *         NOT FOUND
	 */
	public FoodPacket get(String key) {
		return this.get(key, "NDB_No");
	}

	/**
	 * 
	 * @param key
	 *            The key (as a string) that the desired FoodPacet is associated
	 *            with (this is the primary key from the 'FOOD_DES.txt' file)
	 *   
	 * @return The FoodPacket object associated with the given key or NULL IF
	 *         NOT FOUND
	 */
	public FoodPacket get(String key, String field) {
		ListNode<FoodPacket> temp = this.head;
		while (temp != null) {
			if (temp.getItem().getValue(field).equals(key))
				return temp.getItem();
			temp = temp.getNext();
		}
		return null;
	}

	/**
	 * Searches list for all FoodPacket objects containing specified value in
	 * specified field
	 * 
	 * @param field
	 *            Field to search in
	 * @param query
	 *            Value to search for
	 * @return List all of all FoodPacket objects which contain the specified
	 *         values in the specified field
	 */
	public FoodPacketList search(String field, String query) {
		return this.search(field, new String[] { query });
	}

	/**
	 * Searches list for all FoodPacket objects containing specified value in
	 * specified field
	 * 
	 * @param field
	 *            Field to search in
	 * @param queries
	 *            Values to search for
	 * @return List all of all FoodPacket objects which contain the specified
	 *         values in the specified fields
	 */
	public FoodPacketList search(String field, String[] queries) {
		FoodPacketList list = new FoodPacketList();
		ListNode<FoodPacket> temp = this.head;
		for (String value : queries) {
			value = value.toLowerCase();
			while (temp != null) {
				if (temp.getItem().getValue(field).toLowerCase().contains(value))
					list.add(temp.getItem());
				temp = temp.getNext();
			}
		}
		return list;
	}

	/**
	 * Searches list for all FoodPacket objects containing specified integer
	 * value in specified field
	 * 
	 * @param field
	 *            Field to search in
	 * @param query
	 *            Integer value to search for
	 * @return List all of all FoodPacket objects which contain the specified
	 *         values in the specified field
	 * 
	 * @throws NumberFormatException
	 *             If the specified field does not contain integer values
	 */
	public FoodPacketList search(String field, int value) throws NumberFormatException {
		FoodPacketList list = new FoodPacketList();
		ListNode<FoodPacket> temp = this.head;
		while (temp != null) {
			if (Integer.parseInt(temp.getItem().getValue(field)) == value)
				list.add(temp.getItem());
			temp = temp.getNext();
		}
		return list;
	}

	/**
	 * Creates string array of data inside the specified field from all nodes in
	 * linked list
	 * 
	 * @param field
	 *            The field to get the data from
	 * @param maxResults
	 *            Maximum length of the string array <br>
	 *            Use FoodPacketList.ALL_RESULTS to get all the results
	 * @return String array of desired information, in same order as linked list
	 */
	public String[] toStringArray(String field, int maxResults) {
		int size = this.getSize();

		// Set the size of the array to the maximum number of results if the
		// specified number is greater than 0 abd
		if (maxResults >= 0)
			size = Math.min(maxResults, size);
		String[] array = new String[size];
		ListNode<FoodPacket> temp = this.head;
		int i = 0;
		while (i < size) {
			// Add the value inside the specified field into the array
			array[i] = temp.getItem().getValue(field);
			i++;
			temp = temp.getNext();
		}
		return array;
	}

}
