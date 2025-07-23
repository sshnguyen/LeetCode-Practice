import java.util.*;

public class InMemoryDatabase {
    // Map to store records (key -> {field -> {value, expirationTimestamp}})
    private Map<String, Map<String, FieldValue>> database;

    // Constructor to initialize the database
    public InMemoryDatabase() {
        database = new HashMap<>();
    }

    // Inner class to store value and expiration timestamp
    private static class FieldValue {
        String value;
        long expirationTimestamp; // -1 means no expiration

        FieldValue(String value, long expirationTimestamp) {
            this.value = value;
            this.expirationTimestamp = expirationTimestamp;
        }
    }

    // Method to insert or update a field-value pair with a timestamp
    public String setAt(String key, String field, String value, long timestamp) {
        database.putIfAbsent(key, new HashMap<>());
        database.get(key).put(field, new FieldValue(value, -1)); // No expiration
        return "";
    }

    // Method to insert or update a field-value pair with a timestamp and TTL
    public String setAtWithTTL(String key, String field, String value, long timestamp, long ttl) {
        database.putIfAbsent(key, new HashMap<>());
        database.get(key).put(field, new FieldValue(value, timestamp + ttl));
        return "";
    }

    // Method to delete a field at a specific timestamp
    public String deleteAt(String key, String field, long timestamp) {
        if (!database.containsKey(key)) {
            return "false";
        }
        Map<String, FieldValue> record = database.get(key);
        if (!record.containsKey(field)) {
            return "false";
        }
        record.remove(field);
        return "true";
    }

    // Method to get the value of a field at a specific timestamp
    public String getAt(String key, String field, long timestamp) {
        if (!database.containsKey(key)) {
            return "";
        }
        Map<String, FieldValue> record = database.get(key);
        if (!record.containsKey(field)) {
            return "";
        }
        FieldValue fieldValue = record.get(field);
        if (fieldValue.expirationTimestamp != -1 && timestamp >= fieldValue.expirationTimestamp) {
            return "";
        }
        return fieldValue.value;
    }

    // Method to scan all fields of a record at a specific timestamp
    public String scanAt(String key, long timestamp) {
        if (!database.containsKey(key)) {
            return "";
        }
        Map<String, FieldValue> record = database.get(key);
        // Filter out expired fields
        Map<String, String> validFields = new HashMap<>();
        for (Map.Entry<String, FieldValue> entry : record.entrySet()) {
            if (entry.getValue().expirationTimestamp == -1 || timestamp < entry.getValue().expirationTimestamp) {
                validFields.put(entry.getKey(), entry.getValue().value);
            }
        }
        // Sort the valid fields lexicographically
        TreeMap<String, String> sortedFields = new TreeMap<>(validFields);
        // Build the result string
        return buildResultString(sortedFields);
    }

    // Method to scan fields of a record that start with a prefix at a specific timestamp
    public String scanByPrefixAt(String key, String prefix, long timestamp) {
        if (!database.containsKey(key)) {
            return "";
        }
        Map<String, FieldValue> record = database.get(key);
        // Filter out expired fields and fields that don't start with the prefix
        Map<String, String> validFields = new HashMap<>();
        for (Map.Entry<String, FieldValue> entry : record.entrySet()) {
            if ((entry.getValue().expirationTimestamp == -1 || timestamp < entry.getValue().expirationTimestamp) &&
                entry.getKey().startsWith(prefix)) {
                validFields.put(entry.getKey(), entry.getValue().value);
            }
        }
        // Sort the valid fields lexicographically
        TreeMap<String, String> sortedFields = new TreeMap<>(validFields);
        // Build the result string
        return buildResultString(sortedFields);
    }

    // Helper method to build the result string
    private String buildResultString(Map<String, String> map) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (result.length() > 0) {
                result.append(", ");
            }
            result.append(entry.getKey()).append("(").append(entry.getValue()).append(")");
        }
        return result.toString();
    }

    // Main method to test the functionality
    public static void main(String[] args) {
        InMemoryDatabase db = new InMemoryDatabase();

        // Test case 1: SET_AT_WITH_TTL operation
        System.out.println(db.setAtWithTTL("A", "BC", "E", 1, 9)); // ""
        System.out.println(db.setAtWithTTL("A", "BC", "E", 5, 10)); // ""

        // Test case 2: SET_AT operation
        System.out.println(db.setAt("A", "BD", "F", 5)); // ""

        // Test case 3: SCAN_BY_PREFIX_AT operation
        System.out.println(db.scanByPrefixAt("A", "B", 14)); // "BC(E), BD(F)"
        System.out.println(db.scanByPrefixAt("A", "B", 15)); // "BD(F)"
    }
}