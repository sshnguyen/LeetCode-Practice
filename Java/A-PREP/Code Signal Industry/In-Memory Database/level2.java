import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class InMemoryDatabase {
    // Map to store records (key -> {field -> value})
    private Map<String, Map<String, String>> database;

    // Constructor to initialize the database
    public InMemoryDatabase() {
        database = new HashMap<>();
    }

    // Method to insert or update a field-value pair in a record
    public String set(String key, String field, String value) {
        // If the key doesn't exist, create a new record
        database.putIfAbsent(key, new HashMap<>());
        // Insert or update the field-value pair
        database.get(key).put(field, value);
        return ""; // Return an empty string as required
    }

    // Method to get the value of a field in a record
    public String get(String key, String field) {
        // Check if the key exists
        if (!database.containsKey(key)) {
            return ""; // Key doesn't exist
        }
        // Check if the field exists in the record
        Map<String, String> record = database.get(key);
        return record.getOrDefault(field, ""); // Return the value or an empty string
    }

    // Method to delete a field from a record
    public String delete(String key, String field) {
        // Check if the key exists
        if (!database.containsKey(key)) {
            return "false"; // Key doesn't exist
        }
        // Check if the field exists in the record
        Map<String, String> record = database.get(key);
        if (!record.containsKey(field)) {
            return "false"; // Field doesn't exist
        }
        // Remove the field
        record.remove(field);
        return "true"; // Field successfully deleted
    }

    // Method to scan all fields of a record
    public String scan(String key) {
        // Check if the key exists
        if (!database.containsKey(key)) {
            return ""; // Key doesn't exist
        }
        // Get the record and sort fields lexicographically
        Map<String, String> record = database.get(key);
        TreeMap<String, String> sortedRecord = new TreeMap<>(record);
        // Build the result string
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedRecord.entrySet()) {
            if (result.length() > 0) {
                result.append(", ");
            }
            result.append(entry.getKey()).append("(").append(entry.getValue()).append(")");
        }
        return result.toString();
    }

    // Method to scan fields of a record that start with a prefix
    public String scanByPrefix(String key, String prefix) {
        // Check if the key exists
        if (!database.containsKey(key)) {
            return ""; // Key doesn't exist
        }
        // Get the record and sort fields lexicographically
        Map<String, String> record = database.get(key);
        TreeMap<String, String> sortedRecord = new TreeMap<>(record);
        // Build the result string
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedRecord.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                if (result.length() > 0) {
                    result.append(", ");
                }
                result.append(entry.getKey()).append("(").append(entry.getValue()).append(")");
            }
        }
        return result.toString();
    }

    // Main method to test the functionality
    public static void main(String[] args) {
        InMemoryDatabase db = new InMemoryDatabase();

        // Test case 1: SET operation
        System.out.println(db.set("A", "BC", "E")); // ""
        System.out.println(db.database); // {A={BC=E}}

        // Test case 2: SET operation (update existing field)
        System.out.println(db.set("A", "BD", "F")); // ""
        System.out.println(db.database); // {A={BC=E, BD=F}}

        // Test case 3: SET operation (add another field)
        System.out.println(db.set("A", "C", "G")); // ""
        System.out.println(db.database); // {A={BC=E, BD=F, C=G}}

        // Test case 4: SCAN_BY_PREFIX operation
        System.out.println(db.scanByPrefix("A", "B")); // "BC(E), BD(F)"

        // Test case 5: SCAN operation
        System.out.println(db.scan("A")); // "BC(E), BD(F), C(G)"

        // Test case 6: SCAN_BY_PREFIX operation (non-existent prefix)
        System.out.println(db.scanByPrefix("B", "B")); // ""
    }
}