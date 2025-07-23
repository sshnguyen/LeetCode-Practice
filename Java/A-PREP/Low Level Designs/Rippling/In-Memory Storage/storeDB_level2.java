import java.util.*;

class InMemoryDB {
    private Map<String, String> store;  // Global key-value store
    private Map<String, String> transaction; // Active transaction store (null if no transaction)

    public InMemoryDB() {
        this.store = new HashMap<>();
        this.transaction = null; // No transaction active initially
    }

    // Set key-value pair
    public void set(String key, String value) {
        if (transaction != null) {
            transaction.put(key, value); // Modify in active transaction
        } else {
            store.put(key, value); // Modify global store if no transaction
        }
    }

    // Get value for a key
    public String get(String key) {
        if (transaction != null && transaction.containsKey(key)) {
            return transaction.get(key); // Get from active transaction
        }
        return store.getOrDefault(key, null); // Get from global store
    }

    // Delete key-value pair
    public void delete(String key) {
        if (transaction != null) {
            transaction.put(key, null); // Mark as deleted in transaction
        } else {
            store.remove(key); // Remove from global store
        }
    }

    // Begin a transaction (create new transaction store)
    public void begin() {
        if (transaction != null) {
            throw new IllegalStateException("Transaction already in progress");
        }
        transaction = new HashMap<>(store); // Copy the global store state
    }

    // Commit the active transaction
    public void commit() {
        if (transaction == null) {
            throw new IllegalStateException("No active transaction");
        }

        // Merge transaction into global store
        for (Map.Entry<String, String> entry : transaction.entrySet()) {
                if (entry.getValue() == null) {
                    store.remove(entry.getKey()); // Handle deletes
                } else {
                    store.put(entry.getKey(), entry.getValue());
                }
            }
        transaction = null; // Clear the active transaction
    }

    // Rollback the active transaction
    public void rollback() {
        if (transaction == null) {
            throw new IllegalStateException("No active transaction to rollback");
        }
        transaction = null; // Discard current transaction
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
