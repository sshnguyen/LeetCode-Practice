import java.util.HashMap;
import java.util.Map;

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

    // Main method to test the functionality
    public static void main(String[] args) {
        InMemoryDatabase db = new InMemoryDatabase();

        // Test case 1: SET operation
        System.out.println(db.set("A", "B", "E")); // "" // {A={B=E}}

        // Test case 2: SET operation (update existing field)
        System.out.println(db.set("A", "C", "F")); // "" // {A={B=E, C=F}}

        // Test case 3: GET operation (existing field)
        System.out.println(db.get("A", "B")); // "E"

        // Test case 4: GET operation (non-existent field)
        System.out.println(db.get("A", "D")); // ""

        // Test case 5: DELETE operation (existing field)
        System.out.println(db.delete("A", "B")); // "true" {A={C=F}}

        // Test case 6: DELETE operation (non-existent field)
        System.out.println(db.delete("A", "D")); // "false {A={C=F}}
    }
}