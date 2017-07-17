package database.datastrucutres;

/**
 * *** NOT RECOMENDED FOR MASS STORAGE ***: due to object STORING HEADER VALUE
 * (duplicate values), unlike the 'LinkedSearchable' class.<br>
 * Data storage class which links a specified header to a specified value,
 * useful for storing temporary or small values where public static array of the
 * headers aren't available, or for SEARCHING for LinkedSearchable objects, as
 * only the headers are compared in the compareTo method, this means a
 * LinkedSearchable object with a reference to header x will equal a
 * IndependantSearchable object with x as the header
 *
 */
public class IndependantSearchable extends Searchable {

	/**
	 * The header value to store
	 */
	private String header;

	/**
	 * Creates a new Searchable object with no value and specified header value
	 * 
	 * @param header
	 *            The header value to store
	 */
	public IndependantSearchable(String header) {
		this.header = header;
	}

	/**
	 * Creates a new LinkedSearchable object with the specified value and 0 for
	 * the file number, but overwrites the header reference with the specified
	 * header value
	 * 
	 * @param header
	 * @param value
	 * @param fileNo
	 */
	public IndependantSearchable(String header, String value) {
		this.value = value;
		this.header = header;
	}

	/**
	 * Returns the header value instead of getting the header from the reference
	 */
	@Override
	public String getHeader() {
		return this.header;
	}

	/**
	 * Returns the 'String'.compareTo() value of the two (string) headers
	 */
	@Override
	public int compareTo(Searchable other) {
		if (this.header == null)
			return 1;
		else if (other.getHeader() == null)
			return -1;
		return this.header.compareTo(other.getHeader());
	}

}
