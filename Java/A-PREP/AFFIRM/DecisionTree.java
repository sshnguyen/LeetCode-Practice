
/*

We use a single DecisionTree class with an inner Node class to represent the tree.
Each Node can be either:
*A leaf node with a value like "Y" or "N".
*A decision node with:
A signal name (like "X1")
A constant (e.g., 3)
Left and right children
Go left if signal < constant
Go right if signal >= constant

Ex:
When we create the tree initially, add a leaf node with a value.
then we can replace that leaf node with a decision node, that will create
2 children leaf nodes.


*leaf node constructor take in a value as input("Y" or "N"). We can assign default value
and it can be set later using a setValue function.

*Decision node take in a signalName and a constant. 

*For evaluate function, we take in a map of signalName and signalValue.
and starting from root, if the node is a leaf, then we return that value, otherwise
the calculation is such

Node current = root;
        while (!current.isLeaf) {
            int signalValue = signals.getOrDefault(current.signalName, 0);
            current = (signalValue < current.constant) ? current.left : current.right;
        }
        return current.value;


*/

import java.util.HashMap;
import java.util.Map;

public class DecisionTree {

    // Node class to represent tree structure
    private static class Node {
        boolean isLeaf;
        String signalName;
        int constant;
        String value;
        Node left, right;

        // Constructor for leaf node
        Node(String value) {
            this.isLeaf = true;
            this.value = value;
        }

        // Constructor for non-leaf node
        Node(String signalName, int constant) {
            this.isLeaf = false;
            this.signalName = signalName;
            this.constant = constant;
        }
    }

    private final Node root;

    // Initialize tree with a leaf node with an initial value
    public DecisionTree(String initialValue) {
        root = new Node(initialValue);
    }

    // Add a split condition to a leaf node, and return the two child nodes
    public void addSplit(Node leaf, String signalName, int constant) {
        if (!leaf.isLeaf) throw new IllegalArgumentException("Node is not a leaf");

        leaf.isLeaf = false; // Mark the node as no longer a leaf
        leaf.signalName = signalName;
        leaf.constant = constant;

        // Create two new child nodes
        leaf.left = new Node("Y");  // Assign default values, these can be set later
        leaf.right = new Node("N");
    }

    // Set the value for a leaf node
    public void setLeafValue(Node leaf, String value) {
        if (leaf.isLeaf) {
            leaf.value = value;
        } else {
            throw new IllegalArgumentException("Node is not a leaf");
        }
    }

    // Evaluate the tree given a set of signals
    public String evaluate(Map<String, Integer> signals) {
        Node current = root;
        while (!current.isLeaf) {
            int signalValue = signals.getOrDefault(current.signalName, 0);
            current = (signalValue < current.constant) ? current.left : current.right;
        }
        return current.value;
    }

    // Helper method to access the root node (for testing)
    public Node getRoot() {
        return root;
    }
    
    public static void main(String[] args) {
        // Create a new decision tree with an initial leaf value
        DecisionTree tree = new DecisionTree("Y");

        // Split the root node (leaf) into a condition
        DecisionTree.Node root = tree.getRoot();
        tree.addSplit(root, "X1", 3);  // root splits based on X1 < 3

        // Further split the left node of the root
        tree.addSplit(root.left, "X2", 1);  // Left child splits based on X2 < 1

        // Further split the right node of the root (now we can safely split right node too)
        tree.addSplit(root.right, "X3", 5);  // Right child splits based on X3 < 5

        // Set the values for the leaf nodes
        tree.setLeafValue(root.left.left, "N");
        tree.setLeafValue(root.left.right, "Y");
        tree.setLeafValue(root.right.left, "N");
        tree.setLeafValue(root.right.right, "Y");

        // Create a set of test signals
        Map<String, Integer> signals = new HashMap<>();
        signals.put("X1", 2);  // X1 is less than 3
        signals.put("X2", 4);  // X2 is greater than 1
        signals.put("X3", 6);  // X3 is greater than 5

        // Evaluate the tree with the given signals
        String result = tree.evaluate(signals);
        System.out.println("Test Result: " + result);  // Expected: "Y"
    }
}
