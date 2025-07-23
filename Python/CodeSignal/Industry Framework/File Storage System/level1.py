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

def main():
    storage = CloudStorage()
    
    # Test cases from the example
    queries = [
        ["ADD_FILE", "/dir1/dir2/file.txt", "10"],
        ["ADD_FILE", "/dir1/dir2/file.txt", "5"],
        ["GET_FILE_SIZE", "/dir1/dir2/file.txt"],
        ["DELETE_FILE", "/non-existing.file"],
        ["DELETE_FILE", "/dir1/dir2/file.txt"],
        ["GET_FILE_SIZE", "/non-existing.file"]
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
    
    print(results)
    # Expected output: ["true", "false", "10", "", "10", ""]

if __name__ == "__main__":
    main()