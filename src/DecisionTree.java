/**
 * Class for a decision tree
 * @param <E> the data type to be held as nodes in the tree.
 * @author Jordan - fjb19170
 */
public class DecisionTree<E> {

    protected Node<E> root = null;
    private int size = 0;

    public DecisionTree() { }

    public int getSize() { return size; }

    public Node<E> root() {
        return root;
    }

    public Node<E> parent(Node<E> n) {
        return n.getParent();
    }

    public Node<E> left(Node<E> n) { return n.getLeft(); }
    public Node<E> right(Node<E> n) { return n.getRight(); }
    public boolean isEmpty() {
        return size == 0;
    }

    public Node<E> addRoot(E e) {
        if (!isEmpty()) throw new IllegalStateException("Tree is not empty");
        root = createNode(e,null,null,null);
        size = 1;
        return root;
    }

    public Node<E> addLeft(Node<E> parent, E e) {
        if (parent.getLeft() != null) {
            throw new IllegalArgumentException("parent already has a left child");
        }
        Node<E> child = createNode(e,parent,null,null);
        parent.setLeft(child);
        size++;
        return child;
    }

    public Node<E> addRight(Node<E> parent, E e) {
        if (parent.getRight() != null) {
            throw new IllegalArgumentException("parent already has a right child");
        }
        Node<E> child = createNode(e,parent,null,null);
        parent.setRight(child);
        size++;
        return child;
    }

    /** Method to create nodes for the tree */
    protected Node<E> createNode(E e, Node<E> parent,
                                 Node<E> left, Node<E> right) {
        return new Node<E>(e, parent, left, right);
    }


}
