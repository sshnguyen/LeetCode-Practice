class CloudStorage:
    def __init__(self):
        self.store = {} # name -> (size, owner)
        self.users = {} # userid -> capacity_limit
        self.user_usage = {} # userid -> current_usage
        self.user_backups = {} # userid -> {filename: size}
    
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
        self.user_backups[userid] = {}
        return True
    
    def add_file_by(self, userid, name, size):
        if name in self.store or userid not in self.users:
            return ""
        
        if self.user_usage[userid] + size > self.users[userid]:
            return ""
        
        self.store[name] = (str(size), userid)
        self.user_usage[userid] += size
        
        return str(self.users[userid] - self.user_usage[userid])
    
    def merge_user(self, userid1, userid2):
        if userid1 not in self.users or userid2 not in self.users or userid1 == userid2:
            return ""
        
        # Transfer all files from userid2 to userid1
        for name, (size, owner) in list(self.store.items()):
            if owner == userid2:
                self.store[name] = (size, userid1)
        
        # Update capacities and usage
        self.users[userid1] += self.users[userid2]
        self.user_usage[userid1] += self.user_usage[userid2]
        
        # Clean up userid2
        del self.users[userid2]
        del self.user_usage[userid2]
        if userid2 in self.user_backups:
            del self.user_backups[userid2]
        
        return str(self.users[userid1] - self.user_usage[userid1])
    
    def backup_user(self, userid):
        if userid not in self.users:
            return ""
        
        # Create backup of all files owned by this user
        backup = {}
        count = 0
        for name, (size, owner) in self.store.items():
            if owner == userid:
                backup[name] = size
                count += 1
        
        # Store the backup (overwrites previous backup)
        self.user_backups[userid] = backup
        
        return str(count)
    
    def restore_user(self, userid):
        if userid not in self.users or userid not in self.user_backups:
            return ""
        
        backup = self.user_backups[userid]
        if not backup:
            return "0"
        
        # First, delete all current files owned by this user
        files_to_delete = []
        for name, (size, owner) in self.store.items():
            if owner == userid:
                files_to_delete.append(name)
        
        for name in files_to_delete:
            del self.store[name]
        
        # Reset user usage
        self.user_usage[userid] = 0
        
        # Restore files from backup
        restored_count = 0
        for name, size in backup.items():
            # Only restore if file doesn't exist (another user might have created it)
            if name not in self.store:
                self.store[name] = (size, userid)
                self.user_usage[userid] += int(size)
                restored_count += 1
        
        return str(restored_count)

def main():
    storage = CloudStorage()
    
    # Test cases from level 4 example
    queries = [
        ["ADD_USER", "user", "100"],
        ["ADD_FILE_BY", "user", "/dir/file1", "50"],
        ["ADD_FILE_BY", "user", "/home.txt", "30"],
        ["BACKUP_USER", "user"],
        ["ADD_FILE_BY", "user", "/file3.mp4", "60"],
        ["DELETE_FILE", "/file3.mp4"],
        ["BACKUP_USER", "user"],
        ["DELETE_FILE", "/file3.mp4"],
        ["DELETE_FILE", "/file4.txt"],
        ["ADD_FILE", "/file3.mp4", "140"],
        ["ADD_FILE_BY", "user", "/dir/file5.new", "20"],
        ["RESTORE_USER", "user"]
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
        elif operation == "BACKUP_USER":
            result = storage.backup_user(query[1])
            results.append(result)
        elif operation == "RESTORE_USER":
            result = storage.restore_user(query[1])
            results.append(result)
    
    print(results)
    # Expected output: ["true", "50", "20", "0", "40", "30", "2", "60", "10", "true", "80", "1"]

if __name__ == "__main__":
    main()