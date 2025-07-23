import java.util.*;
import java.util.stream.Collectors;

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

    // Method to get the top n largest files with a specific prefix
    public String getLargestFiles(String prefix, int n) {
    List<Map.Entry<String, Integer>> filteredFiles = new ArrayList<>();
    
    // Filter files that start with the given prefix
    for (Map.Entry<String, Integer> entry : files.entrySet()) {
        if (entry.getKey().startsWith(prefix)) {
            filteredFiles.add(entry);
        }
    }
    
    // Sort the filtered files by size in descending order, then by name in lexicographical order
    filteredFiles.sort((a, b) -> {
        int cmp = b.getValue().compareTo(a.getValue()); // Sort by size (descending)
        if (cmp == 0) {
            return a.getKey().compareTo(b.getKey()); // Sort by name (ascending) in case of a tie
        }
        return cmp;
    });;
    
    // Build the result string
    StringBuilder result = new StringBuilder();
    int count = 0;
    for (Map.Entry<String, Integer> entry : filteredFiles) {
        if (count >= n) {
            break;
        }
        if (count > 0) {
            result.append(", ");
        }
        result.append(entry.getKey()).append("(").append(entry.getValue()).append(")");
        count++;
    }
    
    return result.toString();
}


    // Main method to test the functionality
    public static void main(String[] args) {
        // Create a new cloud storage system
        CloudStorageSystem storage = new CloudStorageSystem();

        // Test case 1: Add a new file
        System.out.println(storage.addFile("/dir/file1.txt", 5)); // true

        // Test case 2: Add another file
        System.out.println(storage.addFile("/dir/file2", 20)); // true

        // Test case 3: Add a file in a deeper directory
        System.out.println(storage.addFile("/dir/deeper/files.mov", 9)); // true

        // Test case 4: Get the top 2 largest files with prefix "/dir"
        System.out.println(storage.getLargestFiles("/dir", 2)); // "/dir/file2(20), /dir/deeper/files.mov(9)"

        // Test case 5: Get the top 3 largest files with prefix "/dir/file1"
        System.out.println(storage.getLargestFiles("/dir/file1", 3)); // "/dir/file2(20), /dir/file1.txt(5)"

        // Test case 6: Get the top 1 largest file with prefix "/another_dir"
        System.out.println(storage.getLargestFiles("/another_dir", 1)); // ""

        // Test case 7: Add a large file
        System.out.println(storage.addFile("/big_file.mp4", 20)); // true

        // Test case 8: Get the top 2 largest files with prefix "/"
        System.out.println(storage.getLargestFiles("/", 2)); // "/big_file.mp4(20), /dir/file2(20)"
    }
}