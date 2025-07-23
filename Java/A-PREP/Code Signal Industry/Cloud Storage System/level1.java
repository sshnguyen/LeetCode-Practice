import java.util.HashMap;
import java.util.Map;

public class CloudStorageSystem {
    // Map to store file names and their sizes
    private Map<String, Integer> files;

    // Constructor to initialize the files map
    public CloudStorageSystem() {
        files = new HashMap<>();
    }

    // Method to add a new file
    public boolean addFile(String name, int size) {
        // Check if the file already exists
        if (files.containsKey(name)) {
            return false; // File already exists
        }
        // Add the file to the storage
        files.put(name, size);
        return true; // File added successfully
    }

    // Method to get the size of a file
    public String getFileSize(String name) {
        // Check if the file exists
        if (files.containsKey(name)) {
            return String.valueOf(files.get(name)); // Return the file size as a string
        }
        return ""; // File does not exist
    }

    // Method to delete a file
    public String deleteFile(String name) {
        // Check if the file exists
        if (files.containsKey(name)) {
            int size = files.get(name); // Get the file size
            files.remove(name); // Delete the file
            return String.valueOf(size); // Return the deleted file size as a string
        }
        return ""; // File does not exist
    }

    // Main method to test the functionality
    public static void main(String[] args) {
        // Create a new cloud storage system
        CloudStorageSystem storage = new CloudStorageSystem();

        // Test case 1: Add a new file
        System.out.println(storage.addFile("/dir1/dir2/file.txt", 10)); // true

        // Test case 2: Try to add the same file again
        System.out.println(storage.addFile("/dir1/dir2/file.txt", 5)); // false

        // Test case 3: Get the size of an existing file
        System.out.println(storage.getFileSize("/dir1/dir2/file.txt")); // "10"

        // Test case 4: Delete a non-existing file
        System.out.println(storage.deleteFile("/non-existing.file")); // ""

        // Test case 5: Delete an existing file
        System.out.println(storage.deleteFile("/dir1/dir2/file.txt")); // "10"

        // Test case 6: Get the size of a deleted file
        System.out.println(storage.getFileSize("/dir1/dir2/file.txt")); // ""
    }
}