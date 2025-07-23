class CloudStorage:
    def __init__(self):
        self.store = {} # name -> (size, owner)
        self.users = {} # userid -> capacity_limit
        self.user_usage = {} # userid -> current_usage
    
    def add_file(self, name, size):
        if name in self.store:
            return False
        
        # Admin has unlimited capacity, all files are owned by admin
        self.store[name] = (size, "admin")
        return True
    
    def get_file_size(self, name):
        if name not in self.store:
            return ""
        
        return self.store[name][0]
    
    def delete_file(self, name):
        if name not in self.store:
            return ""
        
        size, owner = self.store[name]
        del self.store[name]
        
        # Update user usage if not admin
        if owner != "admin" and owner in self.user_usage:
            self.user_usage[owner] -= int(size)
        
        return size
    
    def get_n_largest(self, prefix, n):
        matching_files = []
        for name, (size, owner) in self.store.items():
            if name.startswith(prefix):
                matching_files.append((name, int(size)))
        
        if not matching_files:
            return ""
        
        matching_files.sort(key=lambda x: (-x[1], x[0]))
        result_files = matching_files[:n]
        result_parts = [f"{name}({size})" for name, size in result_files]
        return ", ".join(result_parts)
    
    def add_user(self, userid, capacity):
        if userid in self.users:
            return False
        
        self.users[userid] = capacity
        self.user_usage[userid] = 0
        return True
    
    def add_file_by(self, userid, name, size):
        # Check if file already exists or user doesn't exist
        if name in self.store or userid not in self.users:
            return ""
        
        # Check if user has enough capacity
        if self.user_usage[userid] + size > self.users[userid]:
            return ""
        
        # Add file and update usage
        self.store[name] = (str(size), userid)
        self.user_usage[userid] += size
        
        # Return remaining capacity
        return str(self.users[userid] - self.user_usage[userid])
    
    def merge_user(self, userid1, userid2):
        # Check if both users exist and are different
        if userid1 not in self.users or userid2 not in self.users or userid1 == userid2:
            return ""
        
        # Transfer all files from userid2 to userid1
        for name, (size, owner) in list(self.store.items()):
            if owner == userid2:
                self.store[name] = (size, userid1)
        
        # Merge capacities and usage
        self.users[userid1] += self.users[userid2]
        self.user_usage[userid1] += self.user_usage[userid2]
        
        # Remove userid2
        del self.users[userid2]
        del self.user_usage[userid2]
        
        # Return remaining capacity of userid1
        return str(self.users[userid1] - self.user_usage[userid1])

def main():
    storage = CloudStorage()
    
    # Test cases from level 3 example
    queries = [
        ["ADD_USER", "user1", "200"],
        ["ADD_USER", "user1", "100"],
        ["ADD_FILE_BY", "user1", "/dir/file.med", "50"],
        ["ADD_FILE_BY", "user1", "/file.big", "140"],
        ["ADD_FILE_BY", "user1", "/dir/file.small", "20"],
        ["ADD_FILE", "/admin_file", "300"],
        ["ADD_USER", "user2", "110"],
        ["ADD_FILE_BY", "user2", "/dir/file.med", "45"],
        ["ADD_FILE_BY", "user2", "/new_file", "90"],
        ["MERGE_USER", "user1", "user2"]
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
        elif operation == "ADD_USER":
            result = storage.add_user(query[1], int(query[2]))
            results.append(str(result).lower())
        elif operation == "ADD_FILE_BY":
            result = storage.add_file_by(query[1], query[2], int(query[3]))
            results.append(result)
        elif operation == "MERGE_USER":
            result = storage.merge_user(query[1], query[2])
            results.append(result)
    
    print(results)
    # Expected output: ["true", "false", "150", "10", "", "true", "true", "", "60", "70"]

if __name__ == "__main__":
    main()