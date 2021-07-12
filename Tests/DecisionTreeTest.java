import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for decision tree.
 * @author Jordan Thomson - fjb19170
 */
public class DecisionTreeTest {
    private DecisionTree<String> tree = new DecisionTree<>();

    @Test
    public void testTree() {
        assertEquals(0,tree.getSize());
        assertTrue(tree.isEmpty());
        tree.addRoot("Does it eat meat?");
        Node<String> n = tree.addRight(tree.root(),"Does it have stripes?");
        tree.addRight(n,"Its a Tiger");
        tree.addLeft(n,"It's a Leopard");
        n = tree.addLeft(tree.root(),"Does it have stripes?");
        tree.addRight(n,"Its a Zebra");
        tree.addLeft(n,"Its a Horse");
        assertEquals(7,tree.getSize());
        assertFalse(tree.isEmpty());
        assertEquals(tree.parent(n),tree.root());
        assertEquals("Its a Tiger", tree.root().getRight().getRight().getElement());
        assertTrue(tree.root().getLeft().getRight().isLeaf());
        assertFalse(tree.root().getRight().isLeaf());
    }
}