/**
 * Class to represent a node in a tree
 * @param <E> Data type to be held in the node.
 * @author Jordan - fjb19170
 */
public class Node<E> {

    private E element;
    private Node<E> parent;
    private Node<E> left;
    private Node<E> right;

    public Node(E e, Node<E> parent, Node<E> left, Node<E> right) {
        element = e;
        this.parent = parent;
        this.left = left;
        this.right = right;
    }

    // Getters
    public E getElement() { return element; }
    public Node<E> getParent() { return parent; }
    public Node<E> getLeft() {
        return left;
    }
    public Node<E> getRight() {
        return right;
    }

    // is the node a leaf?
    public boolean isLeaf() {
        return left == null && right == null;
    }

    // Setters
    public void setLeft(Node<E> left) {
        this.left = left;
    }

    public void setRight(Node<E> right) {
        this.right = right;
    }
}
