//Design Unix File Search API to search file with different arguments as "extension", "name", "size" ...

class FileEntry {
    String name;
    String extension;
    long size;

    // Constructor to initialize file attributes
    public FileEntry(String name, String extension, long size) {
        this.name = name;
        this.extension = extension;
        this.size = size;
    }

    // Override toString() for better output readability
    public String toString() {
        return name + "." + extension + " (" + size + " bytes)";
    }
}

class FileSearch {
    private List<FileEntry> files; // List to store file records

    // Constructor to initialize the file list
    public FileSearch() {
        this.files = new ArrayList<>();
    }

    // Method to add a file entry to the system
    public void addFile(String name, String extension, long size) {
        files.add(new FileEntry(name, extension, size));
    }

    // Search files by extension (e.g., "txt", "jpg")
    public List<FileEntry> searchByExtension(String ext) {
        List<FileEntry> result = new ArrayList<>();
        for (FileEntry file : files) {
            if (file.extension.equals(ext)) {
                result.add(file);
            }
        }
        return result;
    }

    // Search files by exact file name (without extension)
    public List<FileEntry> searchByName(String name) {
        List<FileEntry> result = new ArrayList<>();
        for (FileEntry file : files) {
            if (file.name.equals(name)) {
                result.add(file);
            }
        }
        return result;
    }

    // Search files by size range (e.g., between 500 bytes and 10,000 bytes)
    public List<FileEntry> searchBySize(long minSize, long maxSize) {
        List<FileEntry> result = new ArrayList<>();
        for (FileEntry file : files) {
            if (file.size >= minSize && file.size <= maxSize) {
                result.add(file);
            }
        }
        return result;
    }
}

public class Main {
    public static void main(String[] args) {
        FileSearch fileSearch = new FileSearch();

        // Adding some sample files
        fileSearch.addFile("document", "txt", 500);
        fileSearch.addFile("photo", "jpg", 2000);
        fileSearch.addFile("video", "mp4", 100000);
        fileSearch.addFile("report", "pdf", 3000);
        fileSearch.addFile("music", "mp3", 4000);

        // Performing various searches
        System.out.println("Search by extension 'txt': " + fileSearch.searchByExtension("txt"));
        System.out.println("Search by name 'photo': " + fileSearch.searchByName("photo"));
        System.out.println("Search by size (500 - 10,000 bytes): " + fileSearch.searchBySize(500, 10000));
    }
}
