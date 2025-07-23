import java.util.*;

// Represents a file entry
class FileEntry {
    String name;
    long size;
    String extension;

    public FileEntry(String name, long size, String extension) {
        this.name = name;
        this.size = size;
        this.extension = extension;
    }
}

// Filter interface
interface IDirectoryFilter {
    boolean matches(FileEntry file);
}

// Size filter
class SizeFilter implements IDirectoryFilter {
    private long min, max;
    public SizeFilter(long min, long max) { this.min = min; this.max = max; }
    public boolean matches(FileEntry file) { return file.size >= min && file.size <= max; }
}

// Type filter
class TypeFilter implements IDirectoryFilter {
    private Set<String> types;
    public TypeFilter(String... extensions) { this.types = new HashSet<>(Arrays.asList(extensions)); }
    public boolean matches(FileEntry file) { return types.contains(file.extension); }
}

// Directory searcher that applies filters
class DirectorySearcher {
    private List<FileEntry> files;

    public DirectorySearcher(List<FileEntry> files) { this.files = files; }

    public List<FileEntry> search(List<IDirectoryFilter> filters) {
        List<FileEntry> result = new ArrayList<>();
        for (FileEntry file : files) {
            if (filters.stream().allMatch(f -> f.matches(file))) result.add(file);
        }
        return result;
    }
}

// Example usage
public class Main {
    public static void main(String[] args) {
        List<FileEntry> files = Arrays.asList(
            new FileEntry("doc1.txt", 5000, "txt"),
            new FileEntry("image.jpg", 100000, "jpg"),
            new FileEntry("code.java", 3000, "java")
        );

        DirectorySearcher searcher = new DirectorySearcher(files);
        List<IDirectoryFilter> filters = Arrays.asList(new SizeFilter(1000, 50000), new TypeFilter("txt", "java"));
        List<FileEntry> results = searcher.search(filters);

        System.out.println("Matching Files: " + results.size());
    }
}
