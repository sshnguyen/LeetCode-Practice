import java.util.*;

public class CloudStorageSystem {
    // Map to store file names and their sizes
    private Map<String, Integer> files;
    // Map to store file ownership (file name -> user ID)
    private Map<String, String> fileOwners;
    // Map to store users and their storage capacities
    private Map<String, Integer> users;
    // Map to store user backups (user ID -> backup of files)
    private Map<String, Map<String, Integer>> backups;

    // Constructor to initialize the maps
    public CloudStorageSystem() {
        files = new HashMap<>();
        fileOwners = new HashMap<>();
        users = new HashMap<>();
        backups = new HashMap<>();
        // Add the admin user with unlimited capacity
        users.put("admin", Integer.MAX_VALUE);
    }

    // Method to add a new user
    public boolean addUser(String userId, int capacity) {
        // Check if the user already exists
        if (users.containsKey(userId)) {
            return false; // User already exists
        }
        // Add the user with the given capacity
        users.put(userId, capacity);
        return true; // User added successfully
    }

    // Method to add a new file by a specific user
    public String addFileByUser(String userId, String name, int size) {
        // Check if the user exists
        if (!users.containsKey(userId)) {
            return ""; // User does not exist
        }
        // Check if the file already exists
        if (files.containsKey(name)) {
            return ""; // File already exists
        }
        // Check if the user has enough remaining capacity
        int remainingCapacity = users.get(userId);
        if (remainingCapacity < size) {
            return ""; // Not enough capacity
        }
        // Add the file and update the user's remaining capacity
        files.put(name, size);
        fileOwners.put(name, userId);
        users.put(userId, remainingCapacity - size);
        return String.valueOf(remainingCapacity - size); // Return the remaining capacity
    }

    // Method to add a new file (admin operation)
    public boolean addFile(String name, int size) {
        // Check if the file already exists
        if (files.containsKey(name)) {
            return false; // File already exists
        }
        // Add the file (admin has unlimited capacity)
        files.put(name, size);
        fileOwners.put(name, "admin");
        return true; // File added successfully
    }

    // Method to merge two user accounts
    public String mergeUsers(String userId1, String userId2) {
        // Check if both users exist and are different
        if (!users.containsKey(userId1) || !users.containsKey(userId2) || userId1.equals(userId2)) {
            return ""; // Invalid merge
        }
        // Transfer ownership of all files from userId2 to userId1
        for (Map.Entry<String, String> entry : fileOwners.entrySet()) {
            if (entry.getValue().equals(userId2)) {
                fileOwners.put(entry.getKey(), userId1);
            }
        }
        // Add userId2's remaining capacity to userId1
        int remainingCapacity1 = users.get(userId1);
        int remainingCapacity2 = users.get(userId2);
        users.put(userId1, remainingCapacity1 + remainingCapacity2);
        // Remove userId2 from the system
        users.remove(userId2);
        return String.valueOf(remainingCapacity1 + remainingCapacity2); // Return the new remaining capacity
    }

    // Method to back up a user's files
    public String backupUser(String userId) {
        if (!users.containsKey(userId)) {
            return ""; // User does not exist
        }
        // Create a backup of the user's files
        Map<String, Integer> userFiles = new HashMap<>();
        for (Map.Entry<String, String> entry : fileOwners.entrySet()) {
            if (entry.getValue().equals(userId)) {
                userFiles.put(entry.getKey(), files.get(entry.getKey()));
            }
        }
        backups.put(userId, userFiles);
        return String.valueOf(userFiles.size()); // Return the number of backed-up files
    }

    // Method to restore a user's files from the latest backup
    public String restoreUser(String userId) {
        if (!users.containsKey(userId)) {
            return ""; // User does not exist
        }
        if (!backups.containsKey(userId)) {
            // If no backup exists, delete all of the user's files
            for (Map.Entry<String, String> entry : new HashMap<>(fileOwners).entrySet()) {
                if (entry.getValue().equals(userId)) {
                    files.remove(entry.getKey());
                    fileOwners.remove(entry.getKey());
                }
            }
            return "0"; // No files restored
        }
        // Restore the user's files from the backup
        Map<String, Integer> backupFiles = backups.get(userId);
        int restoredCount = 0;
        for (Map.Entry<String, Integer> entry : backupFiles.entrySet()) {
            String fileName = entry.getKey();
            int fileSize = entry.getValue();
            if (!files.containsKey(fileName)) {
                files.put(fileName, fileSize);
                fileOwners.put(fileName, userId);
                restoredCount++;
            }
        }
        return String.valueOf(restoredCount); // Return the number of files restored
    }

    // Main method to test the functionality
    public static void main(String[] args) {
        // Create a new cloud storage system
        CloudStorageSystem storage = new CloudStorageSystem();

        // Test case 1: Add a new user
        System.out.println(storage.addUser("user1", 200)); // true

        // Test case 2: Try to add the same user again
        System.out.println(storage.addUser("user1", 300)); // false

        // Test case 3: Add a file by user1
        System.out.println(storage.addFileByUser("user1", "/dir/file.mad", 50)); // "150"

        // Test case 4: Add another file by user1
        System.out.println(storage.addFileByUser("user1", "/file.hig", 140)); // "10"

        // Test case 5: Try to add a file that exceeds user1's capacity
        System.out.println(storage.addFileByUser("user1", "/dir/file.email", 20)); // ""

        // Test case 6: Add a file by admin (unlimited capacity)
        System.out.println(storage.addFile("/dir/saint_file", 300)); // true

        // Test case 7: Add a new user
        System.out.println(storage.addUser("user2", 110)); // true

        // Test case 8: Try to add a file that already exists
        System.out.println(storage.addFileByUser("user2", "/dir/file.mad", 45)); // ""

        // Test case 9: Add a file by user2
        System.out.println(storage.addFileByUser("user2", "/new_file", 50)); // "60"

        // Test case 10: Merge user1 and user2
        System.out.println(storage.mergeUsers("user1", "user2")); // "70"

        // Test case 11: Backup user1's files
        System.out.println(storage.backupUser("user1")); // "2"

        // Test case 12: Delete a file and restore user1's files
        storage.files.remove("/dir/file.mad");
        storage.fileOwners.remove("/dir/file.mad");
        System.out.println(storage.restoreUser("user1")); // "1"
    }
}