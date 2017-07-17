package database.datastrucutres;

/**
 * 'Bare-bones' node for a basic linked list implementation (with potential to
 * be used in a double-linked list or a priority queue)
 *
 * @param <T>
 *            Type of item for node to hold
 */
public class ListNode<T> {

	/**
	 * The item to hold
	 */
	private T item;

	/**
	 * Reference to following node
	 */
	private ListNode<T> next;

	/**
	 * Reference to preceding node
	 */
	private ListNode<T> previous;

	/**
	 * Integer value of priority (useful if being used as priority queue)
	 */
	int priority;

	/**
	 * Creates a new node containing specified object
	 * 
	 * @param value
	 *            Object of type T for node to hold
	 */
	public ListNode(T value) {
		this.item = value;
	}

	/**
	 * Creates a new node with specified object and priority
	 * 
	 * @param value
	 *            Object of type T for node to hold
	 * @param priority
	 *            Priority value used for a priority queue
	 */
	public ListNode(T value, int priority) {
		this.item = value;
		this.priority = priority;
	}

	/**
	 * Getter for the following node
	 * 
	 * @return THe following node
	 */
	public ListNode<T> getNext() {
		return this.next;
	}

	/**
	 * Setter for the preceding node
	 * 
	 * @param node
	 *            Node to set as preceding
	 */
	public void setPrevious(ListNode<T> node) {
		this.previous = node;
	}

	/**
	 * Getter for the preceding node
	 * 
	 * @return The preceding node
	 */
	public ListNode<T> getPrevious() {
		return this.previous;
	}

	/**
	 * Getter for the contained item (of type T)
	 * 
	 * @return Contained item
	 */
	public T getItem() {
		return this.item;
	}

	/**
	 * Setter for the following node
	 * 
	 * @param node
	 *            Node to set as following
	 */
	public void setNext(ListNode<T> next) {
		this.next = next;
	}

	/**
	 * Returns the .toString() method's value for the contained object
	 */
	@Override
	public String toString() {
		return this.item.toString();
	}

	/**
	 * Getter for the priority value (ONLY IF APPLICABLE)
	 * 
	 * @return Priority value
	 */
	public int getPriority() {
		return this.priority;
	}
}
