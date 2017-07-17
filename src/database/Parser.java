package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import database.datastrucutres.BinaryTree;
import database.datastrucutres.FoodPacket;
import database.datastrucutres.FoodPacketBinaryTree;
import database.datastrucutres.IndependantSearchable;
import database.datastrucutres.Nutrient;
import database.datastrucutres.NutrientList;
import database.datastrucutres.Searchable;
import database.datastrucutres.SearchableLinkedList;

/**
 * Static class to parse files into appropriate data structure
 *
 */
class Parser {

	/**
	 * Boolean arrays with 'true' values representing fields that are numeric in
	 * the specified files
	 */
	public static final boolean[][] NUMERIC = new boolean[][] {
			{ false, false, false, false, false, false, false, false, true, false, true, true, true, true, },
			{ false, false, true, true, true, false, false, false, false, true, true, true, true, true, true, false,
					true, false } };

	/**
	 * Database object used for updating number of bytes parse
	 */
	private Database database;

	/**
	 * Created a new Parser object to load data
	 * 
	 * @param database
	 *            Database object which to update with loading status
	 */
	public Parser(Database database) {
		this.database = database;
	}

	/**
	 * General parsing method for majority of files.<br>
	 * Inputs data from specified text file into FoodPacketBinaryTree
	 * 
	 * @param file
	 *            The file to load data from
	 * @param type
	 *            The number associated with the file (obtainable from
	 *            FoodPacketBinaryTree class), ex.
	 *            'FoodPacketBinaryTree.FOOD_DES'
	 * @return The FoodPacketBinaryTree containing all information from file
	 * @throws Exception
	 *             If file is not found or any errors occur when loading data
	 *             into BinaryTree
	 */
	public FoodPacketBinaryTree parse(File file, int type) throws Exception {
		String[] headers = FoodPacketBinaryTree.HEADERS[type];
		file.length();
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = in.readLine();
		FoodPacketBinaryTree tree = new FoodPacketBinaryTree();
		// Add each line of data into BinaryTree as FoodPacket objects
		while (line != null) {
			String[] values = split(line, headers.length);
			tree.add(new FoodPacket(values, type));
			this.database.parsedBytes(line.getBytes().length);
			line = in.readLine();
		}
		in.close();
		return tree;
	}

	/**
	 * ***ONLY loads data for 'NUT_DATA.txt'*** Specific loading method for
	 * loading nutrient information into already existing BinaryTree
	 * 
	 * @param file
	 *            File to read nutrient information from
	 * 
	 * @param main
	 *            The BinaryTree to load information into
	 * @throws Exception
	 *             If file is not found or any errors occur when loading data
	 *             into BinaryTree
	 */
	public void parseNutrients(File file, FoodPacketBinaryTree main) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = in.readLine();
		while (line != null) {
			String[] values = split(line, FoodPacketBinaryTree.HEADERS[FoodPacketBinaryTree.NUT_DATA].length);

			// Create list of all nutrients for each food item using specified
			// primary key
			int key = Integer.parseInt(values[0]);
			NutrientList nutrients = new NutrientList();

			// Continues adding items to list as long as the primary key (key
			// which identifies which food the nutrient is in) is the same
			while (line != null && Integer.parseInt(values[0]) == key) {

				// Strip the 'NDB_No' from the Nutrient object to eliminate
				// duplicate data
				String[] tempData = Arrays.copyOfRange(values, 1, values.length);

				nutrients.add(new Nutrient(tempData));
				this.database.parsedBytes(line.getBytes().length);
				line = in.readLine();
				values = split(line, FoodPacketBinaryTree.HEADERS[FoodPacketBinaryTree.NUT_DATA].length);
			}
			// When key changes, add list of nutrients to specified food
			// object
			main.get(key).addNutrients(nutrients);
		}
		in.close();
	}

	/**
	 * ***ONLY loads data for 'FOOTNOTE.txt'*** Specific loading method for
	 * loading footnote information into already existing BinaryTree
	 * 
	 * @param file
	 *            File to read footnote information from
	 * 
	 * @param main
	 *            The BinaryTree to load information into
	 * @throws Exception
	 *             If file is not found or any errors occur when loading data
	 *             into BinaryTree
	 */
	public void parseFootNotes(File file, FoodPacketBinaryTree main) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = in.readLine();
		while (line != null) {
			String[] values = split(line, FoodPacketBinaryTree.HEADERS[FoodPacketBinaryTree.FOOTNOTE].length);

			// Use primary key to locate food object and add the footnote
			// description to it
			int key = Integer.parseInt(values[0]);
			main.get(key).addFootNote(values[4]);
			this.database.parsedBytes(line.getBytes().length);
			line = in.readLine();
		}
		in.close();
	}

	/**
	 * ***ONLY loads data for 'LANGUAL.txt' and 'LANGDESC.txt'*** Specific
	 * loading method for loading langual references into already existing
	 * BinaryTree and langual descriptions into new BinaryTree
	 * 
	 * @param langual
	 *            File to read langual references from
	 * @param langDesc
	 *            File to read langual descriptions from
	 * @param main
	 *            The BinaryTree to load information into
	 * @return BinaryTree containing langual descriptions (which could be
	 *         referenced using Langual key, ex. 'A0107')
	 * @throws Exception
	 *             If file is not found or any errors occur when loading data
	 *             into BinaryTree
	 */
	public BinaryTree<Searchable> parseLanguals(File langual, File langDesc, FoodPacketBinaryTree main)
			throws Exception {

		// Load the primary keys (ex. A2001) into the FoodPacket objects in
		// provided BinaryTree
		BufferedReader in = new BufferedReader(new FileReader(langual));
		String line = in.readLine();
		while (line != null) {
			String[] values = split(line, FoodPacketBinaryTree.HEADERS[FoodPacketBinaryTree.LANGUAL].length);
			int key = Integer.parseInt(values[0]);
			main.get(key).addLanguals(values[1]);
			this.database.parsedBytes(line.getBytes().length);
			line = in.readLine();
		}
		in.close();

		// Create a new BinaryTree to link the primary keys from the langual
		// descriptions file to the langual descriptions (allows user to get
		// langual descriptions by providing primary key)
		BinaryTree<Searchable> langualDescriptions = new BinaryTree<Searchable>();
		in = new BufferedReader(new FileReader(langDesc));
		line = in.readLine();
		while (line != null) {
			String[] values = split(line, FoodPacketBinaryTree.HEADERS[FoodPacketBinaryTree.LANGDESC].length);
			langualDescriptions.add(new IndependantSearchable(values[0], values[1]));
			this.database.parsedBytes(line.getBytes().length);
			line = in.readLine();
		}
		return langualDescriptions;
	}

	/**
	 * ***ONLY loads data for 'WEIGHT.txt'*** Specific loading method for weight
	 * information into already existing BinaryTree of FoodPacket objects
	 * 
	 * @param file
	 *            File to read data from
	 * @param main
	 *            FoodPacketBinaryTree to add weight information to
	 * @throws Exception
	 *             If file is not found, or any error occurs when loading data
	 *             into BinaryTree
	 */
	public void parseWeightData(File file, FoodPacketBinaryTree main) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = in.readLine();
		while (line != null) {
			String[] values = split(line, FoodPacketBinaryTree.HEADERS[FoodPacketBinaryTree.WEIGHT].length);

			// Set the key equal to the primary key of the 'WEIGHT.txt' file:
			int key = Integer.parseInt(values[0]);
			SearchableLinkedList weightData = new SearchableLinkedList();

			// Add weight data associated with the same food item to the same
			// LinkedList prior to adding list to FoodPacket in order to reduce
			// number of BinaryTree accesses
			while (line != null && Integer.parseInt(values[0]) == key) {
				weightData.add(new IndependantSearchable(values[3],
						Math.round((Double.parseDouble(values[4]) / Double.parseDouble(values[2])) * 10) / 10.0 + ""));
				line = in.readLine();
				values = split(line, FoodPacketBinaryTree.HEADERS[FoodPacketBinaryTree.WEIGHT].length);
			}
			main.get(key).addWeightData(weightData);
		}
	}

	/**
	 * 
	 * @param data
	 *            String array of data to write to file
	 * @param fileName
	 *            File to write data to
	 * @param fileNo
	 *            Index from FoodPacketBinaryTree class associated with file,
	 *            for example, use 'FoodPacketBinaryTree.FOOD_DES' for
	 *            'FOOD_DES.txt'
	 * @throws IOException
	 *             If the named file exists but is a directory rather than a
	 *             regular file, does not exist but cannot be created, or cannot
	 *             be opened for any other reason (from FileWriter)
	 */
	public static void addToFile(String[] data, String fileName, int fileNo) throws IOException {
		// Adds data to the file instead of overwriting it
		BufferedWriter out = new BufferedWriter(new FileWriter(new File(fileName), true));

		// Calls the join method to convert the string array into a string using
		// the boolean array to format the fields as numeric or text
		out.write(join(data, NUMERIC[fileNo]));

		// Add a new line to the end of the file to maintain a consistent format
		// with the provided USDA files
		out.newLine();
		out.close();
	}

	/**
	 * Splits a line of text into a String array using '^' as the delimiter Also
	 * removes all trailing and leading '^' characters from the strings
	 * 
	 * @param line
	 *            The line to break up
	 * @param items
	 *            The number of items in the String (used as the length of the
	 *            String array)
	 * @return String array of length 'items' containing fragments from provided
	 *         line
	 */
	private static String[] split(String line, int items) {
		if (line == null)
			return null;
		String[] data = new String[items];
		int index = 0, length = line.length(), lastCarrot = 0;

		// Loop through each line and add break information into array using i
		// as the start of the string and j as the position of the found
		// delimiter
		for (int i = 0; i < length; i++) {
			boolean done = false;
			for (int j = i; !done && j < length; j++) {

				// Remove the '~' characters from the string if they exist
				if (line.charAt(j) == '^') {
					if (line.charAt(i) == '~')
						data[index] = line.substring(i + 1, j - 1);
					else {
						data[index] = line.substring(i, j);
					}
					i = j;
					done = true;
					if (data[index] == null)
						data[index] = "";
					index++;
					lastCarrot = j;
				}
			}
		}

		// Deals with last case if there is no trailing '^' character
		if (length - 1 != lastCarrot) {
			if (line.charAt(lastCarrot + 1) == '~')
				data[index] = line.substring(lastCarrot + 2, line.length() - 1);
			else
				data[index] = line.substring(lastCarrot + 1);
		}
		if (data[data.length - 1] == null)
			data[data.length - 1] = "";
		return data;
	}

	/**
	 * Joins a provided string array into a single string using the provided
	 * boolean array to specify is a field is numeric or text <br>
	 * <br>
	 * Formatting used: Each field is separated by a '^' character but only text
	 * fields are surrounded by '~' characters
	 * 
	 * @param data
	 *            String array to join into one String
	 * @param numeric
	 *            Boolean array using 'true' values to represent fields which
	 *            should by numeric, and in turn not surrounded by '~'
	 *            characters
	 * @return New string which all data from the array formatted using above
	 *         specifications
	 */
	public static String join(String[] data, boolean[] numeric) {
		String output = "";
		for (int i = 0; i < data.length; i++) {
			// Add the '~' characters only if the field is not numeric
			if (!numeric[i]) {
				output += "~" + data[i] + "~^";
			} else {
				output += data[i] + "^";
			}
		}
		// Removes trailing '^' character if it exists to remain consistent with
		// USDA formatting
		if (output.charAt(output.length() - 1) == '^')
			return output.substring(0, output.length() - 1);
		return output;
	}
}
