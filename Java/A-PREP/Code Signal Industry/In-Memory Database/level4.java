import java.util.*;

public class InMemoryDatabase {
    // Map to store records (key -> {field -> {value, expirationTimestamp}})
    private Map<String, Map<String, FieldValue>> database;
    // TreeMap to store backups (timestamp -> backup state)
    private TreeMap<Long, Map<String, Map<String, FieldValue>>> backups;

    // Constructor to initialize the database
    public InMemoryDatabase() {
        database = new HashMap<>();
        backups = new TreeMap<>();
    }

    // Inner class to store value and expiration timestamp
    private static class FieldValue {
        String value;
        long expirationTimestamp; // -1 means no expiration

        FieldValue(String value, long expirationTimestamp) {
            this.value = value;
            this.expirationTimestamp = expirationTimestamp;
        }

        FieldValue copy() {
            return new FieldValue(this.value, this.expirationTimestamp);
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

    // Method to back up the database state at a specific timestamp
    public String backup(long timestamp) {
        // Create a deep copy of the database
        Map<String, Map<String, FieldValue>> backup = new HashMap<>();
        for (Map.Entry<String, Map<String, FieldValue>> recordEntry : database.entrySet()) {
            Map<String, FieldValue> recordCopy = new HashMap<>();
            for (Map.Entry<String, FieldValue> fieldEntry : recordEntry.getValue().entrySet()) {
                recordCopy.put(fieldEntry.getKey(), fieldEntry.getValue().copy());
            }
            backup.put(recordEntry.getKey(), recordCopy);
        }
        // Save the backup
        backups.put(timestamp, backup);
        // Count non-empty, non-expired records
        int count = 0;
        for (Map.Entry<String, Map<String, FieldValue>> recordEntry : database.entrySet()) {
            boolean hasValidFields = false;
            for (Map.Entry<String, FieldValue> fieldEntry : recordEntry.getValue().entrySet()) {
                if (fieldEntry.getValue().expirationTimestamp == -1 || timestamp < fieldEntry.getValue().expirationTimestamp) {
                    hasValidFields = true;
                    break;
                }
            }
            if (hasValidFields) {
                count++;
            }
        }
        return String.valueOf(count);
    }

    // Method to restore the database state from a backup
    public String restore(long timestamp, long timestampToRestore) {
        // Find the latest backup before or at timestampToRestore
        Long backupTimestamp = backups.floorKey(timestampToRestore);
        if (backupTimestamp == null) {
            return ""; // No backup found (should not happen as per the problem statement)
        }
        // Get the backup
        Map<String, Map<String, FieldValue>> backup = backups.get(backupTimestamp);
        // Clear the current database
        database.clear();
        // Restore the backup and recalculate TTLs
        for (Map.Entry<String, Map<String, FieldValue>> recordEntry : backup.entrySet()) {
            Map<String, FieldValue> record = new HashMap<>();
            for (Map.Entry<String, FieldValue> fieldEntry : recordEntry.getValue().entrySet()) {
                FieldValue fieldValue = fieldEntry.getValue();
                long remainingTTL = fieldValue.expirationTimestamp == -1 ? -1 : fieldValue.expirationTimestamp - backupTimestamp;
                long newExpirationTimestamp = remainingTTL == -1 ? -1 : timestamp + remainingTTL;
                record.put(fieldEntry.getKey(), new FieldValue(fieldValue.value, newExpirationTimestamp));
            }
            database.put(recordEntry.getKey(), record);
        }
        return "";
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
        System.out.println(db.setAtWithTTL("A", "B", "C", 1, 10)); // ""
        System.out.println(db.backup(3)); // "1"

        // Test case 2: SET_AT operation
        System.out.println(db.setAt("A", "D", "E", 4)); // ""
        System.out.println(db.backup(5)); // "1"

        // Test case 3: DELETE_AT operation
        System.out.println(db.deleteAt("A", "B", 8)); // "true"
        System.out.println(db.backup(9)); // "1"

        // Test case 4: RESTORE operation
        System.out.println(db.restore(10, 7)); // ""
        System.out.println(db.backup(11)); // "1"

        // Test case 5: SCAN_AT operation
        System.out.println(db.scanAt("A", 15)); // "B(C), D(E)"
        System.out.println(db.scanAt("A", 16)); // "D(E)"
    }
}