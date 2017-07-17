package database.datastrucutres;

import java.util.Arrays;

/**
 * Binary Tree containing only FoodPacket objects and FoodPacket-related methods
 * 
 *
 */
public class FoodPacketBinaryTree extends BinaryTree<FoodPacket> {

	/**
	 * The specified field headers from 'USDA National Nutrient Database for
	 * Standard Reference, Release 27'. Get array of headers for any file using
	 * the available public static variables, for example, for the file
	 * 'FOOD_DES.txt' use
	 * 'FoodPacketBinaryTree.HEADERS[FoodPacketBinaryTree.FOOD_DES]'
	 */
	public static final String[][] HEADERS = {
			{ "NDB_No", "FdGrp_Cd", "Long_Desc", "Short_Desc", "ComName", "ManufacName", "Survey", "Ref_desc", "Refuse",
					"SciName", "N_Factor", "Pro_Factor", "Fat_Factor", "CHO_Factor" },
			{ "NDB_No", "Nutr_No", "Nutr_Val", "Num_Data_Pts", "Std_Error", "Src_Cd", "Derriv_Cd", "Ref_NDB_No",
					"Add_Nutr_Mark", "Num_Studies", "Min", "Max", "DF", "Low_EB", "Up_EB", "Stat_cmt", "AddMod_Date",
					"CC" },
			{ "NDB_No", "Seq", "Amount", "Msre_Desc", "Gm_Wgt", "Num_Data_Pts", "Std_Dev" },
			{ "Nutr_No", "Units", "Tagname", "NutrDesc", "Num_Dec", "SR_Order" }, { "FdGrp_Cd", "FdGrp_Desc" },
			{ "Factor_Code", "Description" }, { "NDB_No", "Factor_Code" },
			{ "NDB_No", "Footnt_No", "Footnt_Typ", "Nutr_No", "Footnt_Txt" } };
	
	/**
	 * Specifies fields used in database program to reduce RAM usage
	 */
	public static final boolean[][] FIELDS_TO_LOAD = {
			{ true, true, true, true, true, true, false, false, false, true, false, false, false, false },
			{ true, true, true, false, false, false, false, false, false, false, false, false, false, false, false,
					false, false, false },
			{ true, true, true, true, true, false, false }, { true, true, true, true, false, false }, { true, true },
			{ true, true }, { true, true }, { true, true, false, false, true } };

	/**
	 * Value representing specific files for accessing the HEADERS array
	 * associated with the desired file
	 */
	public static final int FOOD_DES = 0, NUT_DATA = 1, WEIGHT = 2, NUTR_DEF = 3, FD_GROUP = 4, LANGDESC = 5,
			LANGUAL = 6, FOOTNOTE = 7;

	/**
	 * Maximum number of words searched by from a user's query
	 */
	private static final int QUERY_CAP = 5;

	/**
	 * Weight in searching algorithm of matches in food descriptions: If the
	 * query is contained in the food description fields, it is more likely to
	 * be what the user is searching for than if it is contained in the langual
	 * descriptions,a s the food descriptions are more specific, so add 5 for
	 * each match as opposed to only 1 point for each langual description match
	 */
	private static final int DESCRIPTIONS_WEIGHT = 5, LANGUAL_WEIGHT = 1;

	public FoodPacketBinaryTree() {
		super();
	}

	/**
	 * Finds the FoodPacket object associated with the specified key
	 * 
	 * @param key
	 *            The primary key associated key the desired FoodPacket
	 * @return The FoodPacket object associated with the specified key or NULL
	 *         IF NOT FOUND
	 */
	public FoodPacket get(int key) {
		Node<FoodPacket> temp = this.head;
		while (true) {
			if (temp == null)
				return null;
			int dif = temp.getItem().compareTo(key);
			if (dif == 0)
				return temp.getItem();
			if (temp.isLeaf())
				return null;
			if (dif > 0)
				temp = temp.getLeftChild();
			else
				temp = temp.getRightChild();
		}
	}

	/**
	 * Search the tree using desired queries in the specified fields
	 * 
	 * @param queries
	 *            The values to search for
	 * @param headers
	 *            The fields to search in
	 * @param useLanguals
	 *            Whether or not to search in associated langual files
	 * @return LinkedList of FoodPacket objects satisfying the criteria
	 */
	public FoodPacketList search(String[] queries, String[] headers, boolean useLanguals) {
		FoodPacketList list = new FoodPacketList();

		// Only look at first x words from the user's query, x being value
		// inside 'QUERY_CAP'
		if (queries.length > QUERY_CAP)
			queries = Arrays.copyOfRange(queries, 0, 5);

		// Call the recursive search method with the starting node (the head)
		// and specified parameters
		this.internalSearch(this.head, queries, headers, list, useLanguals);
		return list;
	}

	/**
	 * Returns a FoodPacketList instead of a linked list of type FoodPacket for
	 * added capabilities (i.e searching by primary key)
	 */
	@Override
	public FoodPacketList toLinkedList() {
		FoodPacketList list = new FoodPacketList();
		this.internalToLinkedList(this.head, list);
		return list;
	}

	/**
	 * Finds largest primary key in tree
	 * @return Largest primary key in tree
	 */
	public int getLargestKey() {
		Node<FoodPacket> temp = this.head;
		while (temp.getLeftChild() != null) {
			temp = temp.getLeftChild();
		}
		return temp.getItem().getKey();
	}

	/**
	 * Recursively find and add FoodPacket objects satisfying the criteria to a
	 * provided priority queue, feeding in the number of matches to use in the
	 * priority queue
	 * 
	 * @param node
	 *            node which contains FoodPacket object to search
	 * @param queries
	 *            Values to search for
	 * @param headers
	 *            Fields to search in
	 * @param list
	 *            Priority queue to add the results to
	 * @param useLanguals
	 *            Whether or not to search in associated langual files
	 */
	private void internalSearch(Node<FoodPacket> node, String[] queries, String[] headers, FoodPacketList list,
			boolean useLanguals) {
		if (node != null) {
			// Search the FoodPacket object with each search term individually
			// and add up the number of matches
			FoodPacket food = node.getItem();
			int matches = 0;
			for (String query : queries) {
				for (String header : headers) {
					query = query.toLowerCase();

					// If the FoodPacket object contains the query, increase the
					// number of matches
					if (food.getValue(header).toLowerCase().contains(query)) {

						matches += DESCRIPTIONS_WEIGHT;
					}
				}
				// Only search langual information if specified
				if (useLanguals)
					matches += langualSearch(node, query);
			}
			// Add item to result priority queue if at least one match was found
			if (matches > 0)
				list.add(food, matches);

			// Recursively search both children for matches
			internalSearch(node.getLeftChild(), queries, headers, list, useLanguals);
			internalSearch(node.getRightChild(), queries, headers, list, useLanguals);
		}
	}

	/**
	 * Specific method for searching through associated langual information
	 * 
	 * @param node
	 *            Node to search in
	 * @param query
	 *            Value to search for
	 * @return Number of matches found
	 */
	private int langualSearch(Node<FoodPacket> node, String query) {

		// Get all associated langual information and loop through them
		String[] langualData = node.getItem().getLanguals();
		int matches = 0;
		if (!(langualData == null || langualData.length <= 0)) {
			for (int i = 0; i < langualData.length; i++) {
				// Increase the number of matches by the langual weight every
				// time a langual contains the specified query
				if (langualData[i].toLowerCase().contains(query))
					matches += LANGUAL_WEIGHT;
			}
		}
		return matches;
	}
}