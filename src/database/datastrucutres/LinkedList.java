package database.datastrucutres;

import java.lang.reflect.Array;

/**
 * Generic linked list to store data with fast load times and o(1)-O(n) read
 * times
 *
 * @param <T>
 *            Type of item for nodes to hold
 */
public class LinkedList<T> {
	/**
	 * Reference to first node in list
	 */
	protected ListNode<T> head;

	/**
	 * Reference to last node in list
	 */
	protected ListNode<T> end;

	/**
	 * Number of nodes in list
	 */
	protected int size;

	/**
	 * Created a new Linked List of size 0
	 */
	public LinkedList() {
		this.size = 0;
	}

	/**
	 * Getter for first node in list
	 * 
	 * @return the first node in the list
	 */
	public ListNode<T> getHead() {
		return this.head;
	}

	/**
	 * Getter for size of the list
	 * 
	 * @return Number of nodes in the list
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * Checks if the list is empty
	 * 
	 * @return Whether or not the list is empty
	 */
	public boolean isEmpty() {
		return this.head == null;
	}

	/**
	 * Adds a specified item to the list
	 * 
	 * @param item
	 *            The item to add to the list
	 */
	public void add(T item) {
		ListNode<T> add = new ListNode<T>(item);
		if (this.head == null)
			this.head = add;
		else {
			this.end.setNext(add);
		}
		this.end = add;
		this.size++;
	}

	/**
	 * Creates a new array of all of the objects
	 * 
	 * @return array of type T or NULL IF TMPTY
	 */
	public T[] toArray() {
		if (this.isEmpty())
			return null;

		// Generates an array of type 'T'
		@SuppressWarnings("unchecked")
		T[] items = internalToArray((Class<T>) this.getHead().getItem().getClass());

		// Add the items to the array iteratively
		ListNode<T> temp = this.head;
		for (int i = 0; i < items.length; i++) {
			items[i] = temp.getItem();
			temp = temp.getNext();
		}
		return items;
	}

	/**
	 * Creates an array of type T and of the same length as size of using a
	 * specified class
	 * 
	 * @param clazz
	 *            Class to use for 'Array.newInstance()' method
	 * @return Empty array of type T and
	 */
	@SuppressWarnings("unchecked")
	private T[] internalToArray(Class<T> clazz) {
		return (T[]) Array.newInstance(clazz, this.getSize());
	}

	/**
	 * Combines specified list with current list
	 * 
	 * @param list
	 *            linked list to combine with current list
	 */
	public void merge(LinkedList<T> list) {
		this.end.setNext(list.getHead());
		this.size += list.getSize();
	}

	/**
	 * FOR DEBUGGING PURPOSTS: iteratively prints out the values of all of the
	 * items in the list, from head onwards
	 */
	public void print() {
		ListNode<T> temp = this.head;
		while (temp != null) {
			System.out.println(temp.getItem());
			temp = temp.getNext();
		}

	}

	/**
	 * Adds a specified node to the end of the list
	 * 
	 * @param node
	 *            Node to add
	 */
	public void add(ListNode<T> node) {
		if (this.head == null)
			this.head = node;
		else {
			this.end.setNext(node);
		}
		this.end = node;
		this.size++;
	}
}
