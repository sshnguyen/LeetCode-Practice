//Q1: Design a system to track recently viewed items (with int limit initialized with class) for customers, similar to an LRU cache. 

//1.Brute force:  Use ArrayList<int[]> cache to store the key value pairs.
// if over capacity, remove the first item in list, then add it
// if get, remove the item and re-add it to cache
//O(n) for all operations and O(n) space

//2. Double linked list and HashMap. Using HashMap<Integer, Node> cache
//to store the cache. Have variable to keep track of the head and tail of the
//linked list.
import java.util.HashMap;

public class Node {
    int key;      // Represents the item ID
    int value;    // Can represent additional info (e.g., timestamp); here we keep it simple.
    Node prev;
    Node next;

    public Node(int key, int value) {
        this.key = key;
        this.value = value;
        this.prev = null;
        this.next = null;
    }
    public Node() {
        this.prev = null;
        this.next = null;
    }
}

public class CustomerCaches {
    private int capacity;                    // Maximum capacity of the cache
    private HashMap<Integer, Node> cache;    // Map to store item ID to node mappings
    private Node head, tail;                 // Dummy head and tail nodes for the doubly linked list

    public CustomerCaches(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        
        // Create dummy head and tail nodes
        this.head = new Node(0, 0);
        this.tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }

    // Removes a node from the doubly linked list.
    private void removeNode(Node node) {
        Node previous = node.prev;
        Node nextNode = node.next;
        previous.next = nextNode;
        nextNode.prev = previous;
    }

    // Inserts a node at the end (most recent position) of the list.
    private void insertNode(Node node) {
        Node last = tail.prev;
        last.next = node;
        node.prev = last;
        node.next = tail;
        tail.prev = node;
    }

    // Returns the value associated with the given key (item ID). Moves the node to the end.
    public int get(int key) {
        if (!cache.containsKey(key)) {
            return -1;
        }
        Node node = cache.get(key);
        // Move the accessed node to the most recently used position.
        removeNode(node);
        insertNode(node);
        return node.value;
    }

    // Puts a key-value pair (item) in the cache.
    public void put(int key, int value) {
        if (cache.containsKey(key)) {
            // If the item already exists, update its value and move it to the end.
            Node node = cache.get(key);
            removeNode(node);
        }
        Node newNode = new Node(key, value);
        cache.put(key, newNode);
        insertNodeAtEnd(newNode);
        
        // If cache exceeds capacity, remove the least recently used item.
        if (cache.size() > capacity) {
            Node lruNode = head.next;  // The node right after head is the least recently used.
            removeNode(lruNode);
            cache.remove(lruNode.key);
        }
    }
}
