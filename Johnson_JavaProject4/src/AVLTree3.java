/*
 * Class that that implements AVLTree
 * Got some of the implementation ideas and code form Stack overflow and geeksforgeeks
 * 
 */

public class AVLTree3<E extends Comparable<E>> {

	/*************************************************************/
	// Inner node class
	private static class Node<E> {

		private E data;
		private Node<E> left;
		private Node<E> right;
		private int height = 1;
		private int bf;

		
		/*************************************************************/
		// Node constructor
		public Node(E item) {
			data = item;
			left = right = null;
		}

		
		/*************************************************************/
		// Tostring method of node
		public String toString() {
			return data.toString();
		}
	}

	
	/*************************************************************/
	// Beginning of the AVLTree
	private Node<E> root;

	
	/*************************************************************/
	// Empty tree constructor
	public AVLTree3() {
		root = null;
	}

	
	/*************************************************************/
	// Gets the root of the tree
	public Node<E> getRoot(){
		return root;
	}
	
	
	/*************************************************************/
	// Client method to find
	public E find(E item) {
		return find(root, item);
	}

	
	/*************************************************************/
	// Method that does all the work to find
	private E find(Node<E> current, E item) {
		if (current == null)
			return null;
		int result = current.data.compareTo(item);
		if (result == 0)
			return current.data;
		else if (result < 0)
			return find(current.right, item);
		else
			return find(current.left, item);
	}

	
	/*************************************************************/
	// Client method to find nodes
	public Node<E> findNode(E item) {
		return findNode(root, item);
	}
	
	
	/*************************************************************/
	// Method that does all the work to find node
	private Node<E> findNode(Node<E> current, E item) {
		if (current == null)
			return null;
		int result = current.data.compareTo(item);
		if (result == 0)
			return current;
		else if (result < 0)
			return findNode(current.right, item);
		else
			return findNode(current.left, item);
	}
	
	
	/*************************************************************/
	// Method that returns height of the tree
	public int height() {
		if (root == null)
			return 0;
		return root.height;
	}

	
	/*************************************************************/
	// Client add method
	public void add(E value) {
		if (value == null) { //if the value is null
			return; //do nothing and return
		}
		if (find(value) == null) { //if the value doesnt already exist 
			root = add(root, value); //call the method that does the work
		}
	}

	
	/*************************************************************/
	// Add method that does all the work
	private Node<E> add(Node<E> node, E value) {

		// Base case.
		if (node == null) {
			return new Node<E>(value);
		}
		// Compare current value to the value in the node.
		int cmp = value.compareTo(node.data);

		// Insert node in left subtree.
		if (cmp < 0) {
			node.left = add(node.left, value);
			// Insert node in right subtree.
		} else {
			node.right = add(node.right, value);
		}

		// Update balance factor and height values.
		update(node);

		// Re-balance tree.
		return balance(node);
	}

	
	/*************************************************************/
	// Updates the tress balance factor and height
	private void update(Node<E> node) {

		int leftNodeHeight = (node.left == null) ? -1 : node.left.height;
		int rightNodeHeight = (node.right == null) ? -1 : node.right.height;

		// Update this node's height.
		node.height = 1 + Math.max(leftNodeHeight, rightNodeHeight);

		// Update balance factor.
		node.bf = rightNodeHeight - leftNodeHeight;

	}

	
	/*************************************************************/
	// Method to balance the tree
	private Node<E> balance(Node<E> node) {

		// Left heavy subtree.
		if (node.bf == -2) {

			// Left-Left case.
			if (node.left.bf <= 0) {
				return leftLeftCase(node);

				// Left-Right case.
			} else {
				return leftRightCase(node);
			}

			// Right heavy subtree needs balancing.
		} else if (node.bf == +2) {

			// Right-Right case.
			if (node.right.bf >= 0) {
				return rightRightCase(node);

				// Right-Left case.
			} else {
				return rightLeftCase(node);
			}

		}

		// Node either has a balance factor of 0, +1 or -1 which is fine.
		return node;

	}

	
	private Node<E> leftLeftCase(Node<E> node) {
		return rightRotation(node);
	}

	
	private Node<E> leftRightCase(Node<E> node) {
		node.left = leftRotation(node.left);
		return leftLeftCase(node);
	}

	
	private Node<E> rightRightCase(Node<E> node) {
		return leftRotation(node);
	}

	
	private Node<E> rightLeftCase(Node<E> node) {
		node.right = rightRotation(node.right);
		return rightRightCase(node);
	}

	
	private Node<E> leftRotation(Node<E> node) {
		Node<E> newParent = node.right;
		node.right = newParent.left;
		newParent.left = node;
		update(node);
		update(newParent);
		return newParent;
	}

	
	private Node<E> rightRotation(Node<E> node) {
		Node<E> newParent = node.left;
		node.left = newParent.right;
		newParent.right = node;
		update(node);
		update(newParent);
		return newParent;
	}

	
	/*************************************************************/
	// Does all the work to print levelorder method
	public void levelorder(Node<E> root) {
		QueueCircularArray<Node<E>> level = new QueueCircularArray<>(); //making a queue
		level.offer(root); //offering the root
		//loop to go through the whole tree
		while (true) {
			int nodeCount = level.getsize(); //getting the node count to the size of the queue
			if (nodeCount == 0)
				break;

			//loop to print out the subtrees
			for (int i = nodeCount; i>0; i--) {
				Node<E> node = level.peek();
				System.out.print(node.data + " ");
				level.remove(); //removing node you just printed from the queue
				if (node.left != null) //if there is a left node 
					level.offer(node.left); //offer it to the queue
				if (node.right != null) //if there is a right node 
					level.offer(node.right); //offer it to the queue
			}
			System.out.println();
		}
	}

	
	public E min() {
		return min(root);
	}

	
	private E min(Node<E> current) {
		if (current.left == null) //if the next value to the left is null 
			return current.data; //return current
		else
			return min(current.left); //recursive call
	}

	
	/*************************************************************/
	// Client method to delete
	public void delete(E item) {
		root = delete(root, item);
	}

	
	/*************************************************************/
	// Does all the work to delete
	private Node<E> delete(Node<E> current, E item) {
		if (current != null) {
			int result = current.data.compareTo(item);
			if (result < 0)
				current.right = delete(current.right, item);
			else if (result > 0)
				current.left = delete(current.left, item);
			else { // find it
				if (current.left == null) // current has 1 child
					current = current.right;
				else if (current.right == null)
					current = current.left;
				else { // current has two children
					E replace = min(current.right);
					current.data = replace;
					current.right = delete(current.right, replace);
				}
			}
		}
		return current;
	}
}
