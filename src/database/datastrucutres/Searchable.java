package database.datastrucutres;

/**
 * Abstract object which provides a structure for linking a string header to a
 * string value, and comparing values for use in a sorted data structure (i.e a
 * binary tree or sorted list)
 *
 */
public abstract class Searchable implements Comparable<Searchable> {

	/**
	 * Value linked to the specified header
	 */
	protected String value;

	/**
	 * Getter for associated header value <br>
	 * Abstraction used as various Searchable objects could store their headers
	 * in different ways, i.e. as a reference to another object
	 * 
	 * @return Associated header value
	 */
	abstract public String getHeader();

	/**
	 * Compares two Searchable objects using their header values
	 */
	@Override
	abstract public int compareTo(Searchable other);

	/**
	 * Getter for the contained value
	 * 
	 * @return Contained value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Setter for the contained value
	 * 
	 * @param value
	 *            New value to be associated with current header
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Returns the header with the associated value using the format:
	 * "'Header' : 'Value'"
	 */
	@Override
	public String toString() {
		return this.getHeader() + " : " + this.value;
	}
}
