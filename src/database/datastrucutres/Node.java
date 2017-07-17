package database.datastrucutres;

/**
 * Node for binary tree implementation
 *
 * @param <T>
 *            Type of the contained object
 */
public class Node<T> {

	/**
	 * Node in specified direction from current position in tree
	 */
	private Node<T> left, right, parent;

	/**
	 * Number of connections from head to node
	 */
	private int depth;

	/**
	 * Maximum number of connections to a leaf
	 */
	private int height;

	/**
	 * Object of type T held by node
	 */
	private T item;

	/**
	 * Creates a new node holding an object of type T
	 * 
	 * @param item
	 *            Object of type T to hold
	 */
	public Node(T item) {
		this.item = item;
		this.depth = 0;
		this.height = 0;
	}

	/**
	 * Returns the '.toString()' method's value of the contained object
	 */
	@Override
	public String toString() {
		return this.item.toString();
	}

	/**
	 * Getter for contained item
	 * 
	 * @return Contained item of type T
	 */
	public T getItem() {
		return this.item;
	}

	/**
	 * Getter for whether or not this node has any children
	 * 
	 * @return whether or not this node has any children
	 */
	protected boolean isLeaf() {
		return this.left == null && this.right == null;
	}

	/**
	 * **DOES NOT UPDATE HEIGHT OF NODE (for performance benefits)** (must
	 * manually update height using .updateHeight() or .fixHeight()) <br>
	 * <br>
	 * Setter for left child of node
	 * 
	 * @param left
	 *            Node to set as left child (of type T)
	 */
	protected void setLeft(Node<T> left) {
		if (left == null)
			this.left = null;
		else {
			// Update the properties of added node
			this.left = left;
			this.left.setParent(this);
			this.left.setDepth(this.depth + 1);
		}
	}

	/**
	 * **DOES NOT UPDATE HEIGHT OF NODE (for performance benefits)** (must
	 * manually update height using .updateHeight() or .fixHeight()) <br>
	 * <br>
	 * Setter for right child of node
	 * 
	 * @param right
	 *            Node to set as right child (of type T)
	 */
	protected void setRight(Node<T> right) {
		if (right == null)
			this.right = null;
		else {
			// Update the properties of added node
			this.right = right;
			this.right.setParent(this);
			this.right.setDepth(this.depth + 1);
		}
	}

	/**
	 * **DOES NOT UPDATE HEIGHT OF PARENT/NODE (for performance benefits)**
	 * (must manually update height using .updateHeight() or .fixHeight()) <br>
	 * <br>
	 * Setter for the parent of this node
	 * 
	 * @param parent
	 *            Preceding node (must also be of type T)
	 */
	protected void setParent(Node<T> parent) {
		this.parent = parent;
	}

	/**
	 * Setter for the depth of the node
	 * 
	 * @param depth
	 *            Number of connections from head to node
	 */
	protected void setDepth(int depth) {
		this.depth = depth;
		if (this.left != null)
			this.left.setDepth(this.depth + 1);
		if (this.right != null)
			this.right.setDepth(this.depth + 1);
	}

	/**
	 * Getter for this node's left child
	 * 
	 * @return Left child node (containing object of type T)
	 */
	protected Node<T> getLeftChild() {
		return this.left;
	}

	/**
	 * Getter for this node's right child
	 * 
	 * @return Right child node (containing object of type T)
	 */
	protected Node<T> getRightChild() {
		return this.right;
	}

	/**
	 * Getter for this node's parent
	 * 
	 * @return Preceding node (parent) containing object of type T
	 */
	protected Node<T> getParent() {
		return this.parent;
	}

	/**
	 * Getter for depth of node
	 * 
	 * @return Number of connections from head to node
	 */
	protected int getDepth() {
		return this.depth;
	}

	/**
	 * Getter for height of node
	 * 
	 * @return Maximum number of connections to a leaf
	 */
	protected int getHeight() {
		return this.height;
	}

	/**
	 * ** Preferred method if you know exactly which nodes' height property has
	 * changed ** (performance improvements due to not recursively updating
	 * height of parent)<br>
	 * ** ONLY FIXES HEIGHT OF THIS NODE, NOT PARENT NODES ** (Use
	 * '.updateHeight()' if that is needed)<br>
	 * <br>
	 * Re-evaluates height of node by checking for the longest path to a leaf
	 * <br>
	 * (i.e. If the right child is 3 connections away from a leaf, but the left
	 * child is only 2 connections away, the height of the node is going to be
	 * the longest path (3) + 1 (for the current node))
	 */
	protected void fixHeight() {
		// Set the height to one more than the maximum height of children nodes
		// Use -1 as the height of any branches that have no children
		this.height = Math.max(this.left != null ? this.left.getHeight() : -1,
				this.right != null ? this.right.getHeight() : -1) + 1;
	}

	/**
	 * ** Fixes and updates height of THIS NODE AND ALL ABOVE NODES (i.e.
	 * parent's parent as well, as the way until the head of the tree) ** <br>
	 * This <b>severely affects performance</b> if used ineffectively (i.e
	 * calling same method on a node and it's children's nodes)<br>
	 * <br>
	 * Re-evaluates height for this node (using the '.fixHeight()' method), then
	 * recursively calls 'updateHeight()' method on parent node
	 */
	protected void updateHeight() {
		this.fixHeight();

		// Update the height of parent if parent exists
		if (this.parent != null)
			this.parent.updateHeight();
	}

	/**
	 * Finds difference in height between the left and right children
	 * 
	 * @return Difference in height between the left and right children (
	 *         <b>returns negative values if left child has greater height than
	 *         right child</b> )
	 */
	protected int getHeightDifference() {
		// Deal with cases where left and/or right children don't exist
		if (this.right == null) {
			if (this.left == null)
				return 0;
			return (this.left.getHeight() + 1) * -1;
		}
		if (this.left == null)
			return this.right.getHeight() + 1;

		// If both children exists, return this difference in height
		return this.right.getHeight() - this.left.getHeight();
	}
}
