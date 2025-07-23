class InMemoryDatabase:
    def __init__(self):
        self.records = {}  # key -> {field: (value, expiry_time or None)}
    
    def _cleanup_expired(self, timestamp: int):
        """Remove expired fields"""
        keys_to_remove = []
        for key in self.records:
            fields_to_remove = []
            for field, (value, expiry) in self.records[key].items():
                if expiry is not None and timestamp >= expiry:
                    fields_to_remove.append(field)
            
            for field in fields_to_remove:
                del self.records[key][field]
            
            if not self.records[key]:
                keys_to_remove.append(key)
        
        for key in keys_to_remove:
            del self.records[key]
    
    # Level 1 operations (backward compatibility)
    def set(self, key: str, field: str, value: str) -> str:
        if key not in self.records:
            self.records[key] = {}
        
        self.records[key][field] = (value, None)  # No expiry
        return ""
    
    def get(self, key: str, field: str) -> str:
        if key not in self.records or field not in self.records[key]:
            return ""
        
        value, expiry = self.records[key][field]
        return value
    
    def delete(self, key: str, field: str) -> str:
        if key not in self.records or field not in self.records[key]:
            return "false"
        
        del self.records[key][field]
        
        if not self.records[key]:
            del self.records[key]
        
        return "true"
    
    def scan(self, key: str) -> str:
        if key not in self.records:
            return ""
        
        fields = sorted(self.records[key].keys())
        result_parts = []
        for field in fields:
            value, expiry = self.records[key][field]
            result_parts.append(f"{field}({value})")
        
        return ", ".join(result_parts)
    
    def scan_by_prefix(self, key: str, prefix: str) -> str:
        if key not in self.records:
            return ""
        
        matching_fields = []
        for field in self.records[key]:
            if field.startswith(prefix):
                matching_fields.append(field)
        
        matching_fields.sort()
        result_parts = []
        for field in matching_fields:
            value, expiry = self.records[key][field]
            result_parts.append(f"{field}({value})")
        
        return ", ".join(result_parts)
    
    # Level 3 operations with timestamp
    def set_at(self, key: str, field: str, value: str, timestamp: int) -> str:
        self._cleanup_expired(timestamp)
        
        if key not in self.records:
            self.records[key] = {}
        
        self.records[key][field] = (value, None)  # No expiry
        return ""
    
    def set_at_with_ttl(self, key: str, field: str, value: str, timestamp: int, ttl: int) -> str:
        self._cleanup_expired(timestamp)
        
        if key not in self.records:
            self.records[key] = {}
        
        expiry_time = timestamp + ttl
        self.records[key][field] = (value, expiry_time)
        return ""
    
    def get_at(self, key: str, field: str, timestamp: int) -> str:
        self._cleanup_expired(timestamp)
        
        if key not in self.records or field not in self.records[key]:
            return ""
        
        value, expiry = self.records[key][field]
        if expiry is not None and timestamp >= expiry:
            return ""
        
        return value
    
    def delete_at(self, key: str, field: str, timestamp: int) -> str:
        self._cleanup_expired(timestamp)
        
        if key not in self.records or field not in self.records[key]:
            return "false"
        
        value, expiry = self.records[key][field]
        if expiry is not None and timestamp >= expiry:
            return "false"
        
        del self.records[key][field]
        
        if not self.records[key]:
            del self.records[key]
        
        return "true"
    
    def scan_at(self, key: str, timestamp: int) -> str:
        self._cleanup_expired(timestamp)
        
        if key not in self.records:
            return ""
        
        valid_fields = []
        for field, (value, expiry) in self.records[key].items():
            if expiry is None or timestamp < expiry:
                valid_fields.append(field)
        
        valid_fields.sort()
        result_parts = []
        for field in valid_fields:
            value, expiry = self.records[key][field]
            result_parts.append(f"{field}({value})")
        
        return ", ".join(result_parts)
    
    def scan_by_prefix_at(self, key: str, prefix: str, timestamp: int) -> str:
        self._cleanup_expired(timestamp)
        
        if key not in self.records:
            return ""
        
        matching_fields = []
        for field, (value, expiry) in self.records[key].items():
            if field.startswith(prefix) and (expiry is None or timestamp < expiry):
                matching_fields.append(field)
        
        matching_fields.sort()
        result_parts = []
        for field in matching_fields:
            value, expiry = self.records[key][field]
            result_parts.append(f"{field}({value})")
        
        return ", ".join(result_parts)

def main():
    db = InMemoryDatabase()
    
    # Test cases from level 3 example
    queries = [
        ["SET_AT_WITH_TTL", "A", "BC", "E", "1", "9"],
        ["SET_AT_WITH_TTL", "A", "BC", "E", "5", "10"],
        ["SET_AT", "A", "BD", "F", "5"],
        ["SCAN_BY_PREFIX_AT", "A", "B", "14"],
        ["SCAN_BY_PREFIX_AT", "A", "B", "15"]
    ]
    
    results = []
    for query in queries:
        operation = query[0]
        if operation == "SET":
            result = db.set(query[1], query[2], query[3])
        elif operation == "GET":
            result = db.get(query[1], query[2])
        elif operation == "DELETE":
            result = db.delete(query[1], query[2])
        elif operation == "SCAN":
            result = db.scan(query[1])
        elif operation == "SCAN_BY_PREFIX":
            result = db.scan_by_prefix(query[1], query[2])
        elif operation == "SET_AT":
            result = db.set_at(query[1], query[2], query[3], int(query[4]))
        elif operation == "SET_AT_WITH_TTL":
            result = db.set_at_with_ttl(query[1], query[2], query[3], int(query[4]), int(query[5]))
        elif operation == "GET_AT":
            result = db.get_at(query[1], query[2], int(query[3]))
        elif operation == "DELETE_AT":
            result = db.delete_at(query[1], query[2], int(query[3]))
        elif operation == "SCAN_AT":
            result = db.scan_at(query[1], int(query[2]))
        elif operation == "SCAN_BY_PREFIX_AT":
            result = db.scan_by_prefix_at(query[1], query[2], int(query[3]))
        
        results.append(result)
        print(f"{query}: {result}")
    
    print(f"Final results: {results}")
    # Expected: ["", "", "", "BC(E), BD(F)", "BD(F)"]

if __name__ == "__main__":
    main()