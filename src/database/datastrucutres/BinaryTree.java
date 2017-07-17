package database.datastrucutres;

/**
 * Balanced binary tree implementation; modest loading times, but O(log(n))
 * searches
 * 
 * @param <T>
 *            Object which extends Comparable class for Nodes to hold
 */
public class BinaryTree<T extends Comparable<T>> {

	/**
	 * The first node of the tree
	 */
	protected Node<T> head;

	/**
	 * Creates an empty, new binary tree of type T
	 */
	public BinaryTree() {
	}

	/**
	 * Adds the provided item to the BinaryTree
	 * 
	 * @param item
	 *            The item to add
	 */
	public void add(T item) {
		Node<T> itemNode = new Node<T>(item);
		if (head == null)
			this.head = itemNode;
		else
			this.internalAdd(itemNode, this.head);
	}

	/**
	 * RETURNS NULL IF LIST IS EMPTY Returns the item contained in the head of
	 * the tree
	 * 
	 * @return Item contained inside head of tree or NULL IF TREE IS EMPTY
	 */
	public T getHead() {
		if (this.head == null)
			return null;
		return this.head.getItem();
	}

	/**
	 * Gets the node in the tree containing item with the same primary key as
	 * the provided item
	 * 
	 * @param item
	 *            The item containing the primary key to search for
	 * @return The item from the tree containing the same primary key as the
	 *         specified item or NULL IF NOT FOUND
	 */
	public T get(T item) {
		Node<T> temp = this.getNode(item);
		if (temp == null)
			return null;
		return temp.getItem();
	}

	/**
	 * Print out the tree's contents using format: [branch] key[depth](height)
	 * Direction --> for branches and key[depth](height) for leaves
	 */
	public void print() {
		this.internalPrint(this.head);
	}

	/**
	 * Creates a linked list of type T with the items contained in each node
	 * 
	 * @return LinkedList of type T containing items from each node
	 */
	public LinkedList<T> toLinkedList() {
		LinkedList<T> list = new LinkedList<T>();
		this.internalToLinkedList(this.head, list);
		return list;
	}

	/**
	 * Checks whether the tree contains the specified item
	 * 
	 * @param item
	 *            The item to find
	 * @return Whether or not the tree contains the specified item
	 */
	public boolean contains(T item) {
		if (this.getNode(item) == null)
			return false;
		return true;
	}

	/**
	 * Find the max depth of the tree (how many nodes down the tree goes)
	 * 
	 * @return Max depth of the tree
	 */
	protected int maxDepth() {
		return this.internalMaxDepth(this.head);
	}

	/**
	 * Find the min depth of the tree (how many nodes deep is the most shallow
	 * note without both a left and right child)
	 * 
	 * @return the min depth of the tree
	 */
	protected int minDepth() {
		return this.internalMinDepth(this.head);
	}

	/**
	 * Recursively find the maximum depth of the tree
	 * 
	 * @param node
	 *            The node to find the deepest node from
	 * @return the maximum depth of the tree
	 */
	protected int internalMaxDepth(Node<T> node) {
		if (node == null)
			return -1;

		// If either of the node's branches don't exist, don't compare the
		// depths of the two branches
		if (node.isLeaf())
			return node.getDepth();
		if (node.getLeftChild() == null)
			return internalMaxDepth(node.getRightChild());
		if (node.getRightChild() == null)
			return internalMaxDepth(node.getLeftChild());

		// Return the maximum depth of either of the node's branches
		int depth1 = internalMaxDepth(node.getLeftChild());
		int depth2 = internalMaxDepth(node.getRightChild());
		return depth1 >= depth2 ? depth1 : depth2;

	}

	/**
	 * Recursively find the minimum depth of the tree
	 * 
	 * @param node
	 *            The node to find the most shallow
	 * @return the maximum depth of the tree
	 */
	protected int internalMinDepth(Node<T> node) {
		if (node.getLeftChild() == null || node.getRightChild() == null)
			return node.getDepth();
		return Math.min(internalMinDepth(node.getLeftChild()), internalMinDepth(node.getRightChild()));
	}

	/**
	 * Recursively adds a node to the tree, then calls the balancing methods to
	 * fix balancing problems from the root node
	 * 
	 * @param node
	 *            The node to add to the tree
	 * @param tempHead
	 *            The node that is currently being searched
	 * @return Wether or not the node was added (if the node has an item that
	 *         already exists in the tree, it WILL NOT BT ADDED!!!
	 */
	protected boolean internalAdd(Node<T> node, Node<T> tempHead) {
		// Get the value difference between the items in the current node and
		// node trying to be added using the mandatory implemented Comparable
		// interface
		int dif = node.getItem().compareTo(tempHead.getItem());

		// Look left if the difference is lower, look right if it is greater,
		// and return false if the item already exists in the tree
		boolean added = false;
		if (dif < 0) {
			if (tempHead.getLeftChild() == null) {
				tempHead.setLeft(node);
				node.updateHeight();
				added = true;
			} else
				added = this.internalAdd(node, tempHead.getLeftChild());
		} else if (dif > 0) {
			if (tempHead.getRightChild() == null) {
				tempHead.setRight(node);
				node.updateHeight();
				added = true;
			} else
				added = this.internalAdd(node, tempHead.getRightChild());
		}

		// Find balancing problems and fix them
		this.findProblems(tempHead);
		return added;
	}

	/**
	 * Finds the node containing the specified item
	 * 
	 * @param item
	 *            The item which the desired node contains
	 * @return If it exists, the node containing the specified item, NULL
	 *         otherwise
	 */
	protected Node<T> getNode(T item) {
		Node<T> temp = this.head;

		// Iteratively find the node using a binary search for better
		// performance / lower memory usage
		while (true) {
			if (temp == null)
				return null;
			int dif = temp.getItem().compareTo(item);
			if (dif == 0)
				return temp;
			if (temp.isLeaf())
				return null;
			if (dif > 0)
				temp = temp.getLeftChild();
			else
				temp = temp.getRightChild();
		}
	}

	/**
	 * FOR DEBUGGING PURPOSES: RECURSIVELY prints out the entire tree using the
	 * format: For branches: 'Primary Key(depth)[height] Direction->' For non
	 * branches: 'Primary Key(depth)[height]'
	 * 
	 * @param tempHead
	 *            The node to print from
	 */
	protected void internalPrint(Node<T> tempHead) {
		// Prints using the appropriate format based on children
		if (tempHead.getLeftChild() == null) {
			if (tempHead.getRightChild() == null) {
				System.out.println(tempHead + "(" + tempHead.getDepth() + ")[" + tempHead.getHeight() + "]");
			} else {
				System.out.println(tempHead + "(" + tempHead.getDepth() + ")[" + tempHead.getHeight() + "] R-> ");
				this.internalPrint(tempHead.getRightChild());
			}
		} else if (tempHead.getRightChild() == null) {
			System.out.println(tempHead + "(" + tempHead.getDepth() + ")[" + tempHead.getHeight() + "] L-> ");
			this.internalPrint(tempHead.getLeftChild());
		} else {
			System.out.println(tempHead + "[branch](" + tempHead.getDepth() + ")[" + tempHead.getHeight() + "] L-> ");
			this.internalPrint(tempHead.getLeftChild());
			System.out.println(tempHead + "[branch](" + tempHead.getDepth() + ")[" + tempHead.getHeight() + "] R-> ");
			this.internalPrint(tempHead.getRightChild());
		}
	}

	/**
	 * Sets the head of the tree and updates the height of the node
	 * 
	 * @param node
	 */
	protected void setHead(Node<T> node) {
		this.head = node;
		this.head.setDepth(0);
		this.head.setParent(null);
	}

	/**
	 * Find and fix balancing issues using by calling the balance method on
	 * unbalanced nodes
	 * 
	 * @param node
	 *            The node to find and fix balancing issues
	 */
	protected void findProblems(Node<T> node) {
		if (node == null)
			return;
		// Balances the node if the height of one branch is more than one node
		// greater than the other branch (i.e if the left child is 3 nodes high,
		// but the right child is only one node high, then there is a balancing
		// issue that must be fixed)
		if (Math.abs(node.getHeightDifference()) > 1)
			balance(node);
	}

	/**
	 * Fixes balancing issues of a node using the AVL self-balancing binary tree
	 * balancing principles using the four cases,(left-left, left-right,
	 * right-right, and right-left) where each case has a specific balancing
	 * technique to fix the issue
	 * 
	 * @param root
	 *            The node to balance
	 */
	protected void balance(Node<T> root) {
		int rootHeightBias = root.getHeightDifference();

		// Either left-left case or left-right case (as the first balancing
		// issue spawns from the left child)
		if (rootHeightBias < 0) {
			Node<T> temp = root.getLeftChild();
			int bias = temp.getHeightDifference();

			// Left-right case because the second balancing issue spawns from
			// the left child, fix by rotating the node left, moving the
			// balancing
			// issue to the right child, creating a left-left case, which is
			// easy to fix
			if (bias > 0)
				this.rotateLeft(temp);

			// Left-left case as the balancing problems stretch down the right
			// side, fix by rotating the sub-tree left from the root node,
			// creating a perfectly balanced sub-tree (even height on both
			// sides)
			this.rotateRight(root);
			root.fixHeight();

			// Update the heights of the nodes for further balancing later
			temp.updateHeight();
		}

		// Either right-right case or right-left case (as the first balancing
		// issue spawns from the right child)
		else if (rootHeightBias > 0) {
			Node<T> temp = root.getRightChild();
			int bias = temp.getHeightDifference();

			// Right-left case because the second balancing issue spawns from
			// the left child, fix by rotating the node left, moving the
			// balancing
			// issue to the right child, creating a right-right case, which is
			// easy to fix
			if (bias < 0)
				this.rotateRight(temp);

			// Right-right case as the balancing problems stretch down the right
			// side, fix by rotating the sub-tree left from the root node,
			// creating a perfectly balanced sub-tree (even height on both
			// sides)
			this.rotateLeft(root);
			root.fixHeight();

			// Update the heights of the nodes for further balancing later
			temp.updateHeight();
		}
	}

	/**
	 * Recursively updates the height of specified node and all children nodes
	 * 
	 * @param node
	 *            The node to update the heights from
	 */
	protected void updateHeights(Node<T> node) {
		// Call the 'updateHeight()' method on leaves (nodes with no children)
		// only as the 'updateHeight()' method updates the height of all of the
		// nodes above it recursively
		if (node.getLeftChild() == null) {
			if (node.getRightChild() == null)
				node.updateHeight();
			else
				this.updateHeights(node.getRightChild());
		} else if (node.getRightChild() == null)
			this.updateHeights(node.getLeftChild());
		else {
			this.updateHeights(node.getLeftChild());
			this.updateHeights(node.getRightChild());
		}
	}

	/**
	 * Performs a right rotation on the specified node
	 * 
	 * @param root
	 *            the node to perform the rotation on
	 */
	protected void rotateRight(Node<T> root) {
		Node<T> pivot = root.getLeftChild(), parent = root.getParent();

		// Deal with special case of root being the head of the tree
		if (this.head == root)
			this.setHead(pivot);

		// Rotate the root away from the pivot
		root.setLeft(pivot.getRightChild());
		pivot.setRight(root);

		// Change the child of the root's parent to the pivot node
		if (parent != null) {
			if (parent.getLeftChild() == root)
				parent.setLeft(pivot);
			else
				parent.setRight(pivot);
		}
	}

	/**
	 * Performs a left rotation on the specified node
	 * 
	 * @param root
	 *            the node to perform the rotation on
	 */
	protected void rotateLeft(Node<T> root) {
		Node<T> pivot = root.getRightChild(), parent = root.getParent();

		// Deal with special case of root being the head of the tree
		if (this.head == root)
			this.setHead(pivot);

		// Rotate the root away from the pivot
		root.setRight(pivot.getLeftChild());
		pivot.setLeft(root);

		// Change the child of the root's parent to the pivot node
		if (parent != null) {
			if (parent.getLeftChild() == root)
				parent.setLeft(pivot);
			else
				parent.setRight(pivot);
		}
	}

	/**
	 * Recursively adds all the items in the tree to the specified LinkedList
	 * 
	 * @param node
	 *            Current node which to add it's item to the list
	 * @param list
	 *            LinkedList of type T to add items to
	 */
	protected void internalToLinkedList(Node<T> node, LinkedList<T> list) {
		if (node == null)
			return;
		list.add(node.getItem());

		// Recursively call the method on all children of the node
		this.internalToLinkedList(node.getLeftChild(), list);
		this.internalToLinkedList(node.getRightChild(), list);
	}
}
