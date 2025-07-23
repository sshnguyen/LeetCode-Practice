import java.util.*;

class InMemoryDB {
    private Map<String, String> store;  // Global key-value store
    private Deque<Map<String, String>> transactionStack; // Stack for transactions
    //WE CAN USE A SINGLE MAP HERE TO STORE TRANSACTION AND A BOOLEAN FLAG OF TRANSACTION,
    //BUT ASK CLARIFYING QUESTION AND START WITH THIS STACK APPROACH BECAUSE OF EXTENSIBILITY.

    public InMemoryDB() {
        this.store = new HashMap<>();
        this.transactionStack = new ArrayDeque<>();
    }

    // Set key-value pair
    public void set(String key, String value) {
        if (!transactionStack.isEmpty()) {
            transactionStack.peek().put(key, value); // Modify in active transaction
        } else {
            store.put(key, value); // Modify global store if no transaction
        }
    }

    // Get value for a key
    public String get(String key) {
        if (!transactionStack.isEmpty() && transactionStack.peek().containsKey(key)) {
            return transactionStack.peek().get(key); // Get from active transaction
        }
        return store.getOrDefault(key, null); // Get from global store
    }

    // Delete key-value pair
    public void delete(String key) {
        if (!transactionStack.isEmpty()) {
            transactionStack.peek().put(key, null); // Mark as deleted in transaction
        } else {
            store.remove(key); // Remove from global store
        }
    }

    // Begin a transaction (copy parent state)
    public void begin() {
        if (transactionStack.isEmpty()) {
            transactionStack.push(new HashMap<>(store)); // Copy from global state
        } else {
            transactionStack.push(new HashMap<>(transactionStack.peek())); // Copy parent
        }
    }

    // Commit the active transaction
    public void commit() {
        if (transactionStack.isEmpty()) {
            throw new IllegalStateException("No active transaction");
        }

        Map<String, String> completedTransaction = transactionStack.pop();

        if (transactionStack.isEmpty()) { // parent transaction
            // Merge into global store
            for (Map.Entry<String, String> entry : completedTransaction.entrySet()) {
                if (entry.getValue() == null) {
                    store.remove(entry.getKey()); // Handle deletes
                } else {
                    store.put(entry.getKey(), entry.getValue());
                }
            }
        } else {
            // Merge into parent transaction
            transactionStack.peek().putAll(completedTransaction);
        }
    }

    // Rollback the active transaction
    public void rollback() {
        if (transactionStack.isEmpty()) {
            throw new IllegalStateException("No active transaction to rollback");
        }
        transactionStack.pop(); // Discard current transaction
    }
}



public class Solution {

   public static void main(String[] args) {
        InMemoryDB db = new InMemoryDB();

        // Part 1: Basic Key-Value Operations
        db.set("key0", "val0");
        System.out.println(db.get("key0")); // Output: val0
        db.delete("key0");
        System.out.println(db.get("key0")); // Output: null

        // Part 2: Transactions
        db.set("key0", "val0");
        db.begin();  // Start transaction
        System.out.println(db.get("key0")); // Output: val0 (inherited from global store)
        db.set("key1", "val1");
        System.out.println(db.get("key1")); // Output: val1 (inside transaction)
        db.commit(); // Commit transaction
        System.out.println(db.get("key1")); // Output: val1 (now in global store)

        // Transaction rollback test
        db.begin();
        db.set("key2", "val2");
        System.out.println(db.get("key2")); // Output: val2 (inside transaction)
        db.rollback();
        System.out.println(db.get("key2")); // Output: null (rolled back)

        // Test Delete Inside Transaction
        db.set("key3", "val3");
        db.begin();
        db.delete("key3");
        System.out.println(db.get("key3")); // Output: null (deleted inside transaction)
        db.rollback();
        System.out.println(db.get("key3")); // Output: val3 (rollback restores it)
    }
}
