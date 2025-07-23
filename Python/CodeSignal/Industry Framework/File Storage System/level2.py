class CloudStorage:
    def __init__(self):
        self.store = {} #name -> size
    
    def add_file(self, name, size):
        if name in self.store:
            return False
        
        self.store[name] = size

        return True
    
    def get_file_size(self, name):
        if name not in self.store:
            return ""
        
        return self.store[name]
    
    def delete_file(self, name):
        if name not in self.store:
            return ""
        
        size = self.store[name]
        del self.store[name]

        return size
    
    def get_n_largest(self, prefix, n):
        # Filter files that start with the given prefix
        matching_files = []
        for name, size in self.store.items():
            if name.startswith(prefix):
                matching_files.append((name, int(size)))
        
        # If no files match, return empty string
        if not matching_files:
            return ""
        
        # Sort by size descending, then by name lexicographically
        matching_files.sort(key=lambda x: (-x[1], x[0]))
        
        # Take the first n files
        result_files = matching_files[:n]
        
        # Format the result as required
        result_parts = [f"{name}({size})" for name, size in result_files]
        return ", ".join(result_parts)

def main():
    storage = CloudStorage()
    
    # Test cases from level 2 example
    queries = [
        ["ADD_FILE", "/dir/file1.txt", "5"],
        ["ADD_FILE", "/dir/file2", "20"],
        ["ADD_FILE", "/dir/deeper/file3.mov", "9"],
        ["GET_N_LARGEST", "/dir", "2"],
        ["GET_N_LARGEST", "/dir/file", "1"],
        ["GET_N_LARGEST", "/another_dir", "file.txt"],
        ["ADD_FILE", "/big_file.mp4", "20"],
        ["GET_N_LARGEST", "/", "2"]
    ]
    
    results = []
    for query in queries:
        operation = query[0]
        if operation == "ADD_FILE":
            result = storage.add_file(query[1], query[2])
            results.append(str(result).lower())
        elif operation == "GET_FILE_SIZE":
            result = storage.get_file_size(query[1])
            results.append(result)
        elif operation == "DELETE_FILE":
            result = storage.delete_file(query[1])
            results.append(result)
        elif operation == "GET_N_LARGEST":
            result = storage.get_n_largest(query[1], int(query[2]))
            results.append(result)
    
    print(results)
    # Expected output: ["true", "true", "true", "/dir/file2(20), /dir/deeper/file3.mov(9)", "/dir/file1.txt(5)", "", "true", "/big_file.mp4(20), /dir/file2(20)"]

if __name__ == "__main__":
    main()