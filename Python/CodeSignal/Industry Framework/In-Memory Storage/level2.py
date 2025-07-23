class InMemoryDatabase:
    def __init__(self):
        self.records = {}  # key -> {field: value}
    
    def set(self, key: str, field: str, value: str) -> str:
        if key not in self.records:
            self.records[key] = {}
        
        self.records[key][field] = value
        return ""
    
    def get(self, key: str, field: str) -> str:
        if key not in self.records or field not in self.records[key]:
            return ""
        
        return self.records[key][field]
    
    def delete(self, key: str, field: str) -> str:
        if key not in self.records or field not in self.records[key]:
            return "false"
        
        del self.records[key][field]
        
        # Clean up empty records
        if not self.records[key]:
            del self.records[key]
        
        return "true"
    
    def scan(self, key: str) -> str:
        if key not in self.records:
            return ""
        
        # Get all fields sorted lexicographically
        fields = sorted(self.records[key].keys())
        
        # Format as field(value), field(value), ...
        result_parts = []
        for field in fields:
            value = self.records[key][field]
            result_parts.append(f"{field}({value})")
        
        return ", ".join(result_parts)
    
    def scan_by_prefix(self, key: str, prefix: str) -> str:
        if key not in self.records:
            return ""
        
        # Get fields that start with prefix, sorted lexicographically
        matching_fields = []
        for field in self.records[key]:
            if field.startswith(prefix):
                matching_fields.append(field)
        
        matching_fields.sort()
        
        # Format as field(value), field(value), ...
        result_parts = []
        for field in matching_fields:
            value = self.records[key][field]
            result_parts.append(f"{field}({value})")
        
        return ", ".join(result_parts)

def main():
    db = InMemoryDatabase()
    
    # Test cases from level 2 example
    queries = [
        ["SET", "A", "BC", "E"],
        ["SET", "A", "BD", "F"],
        ["SET", "A", "C", "G"],
        ["SCAN_BY_PREFIX", "A", "B"],
        ["SCAN", "A"],
        ["SCAN_BY_PREFIX", "B", "B"]
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
        
        results.append(result)
        print(f"{query}: {result}")
    
    print(f"Final results: {results}")
    # Expected: ["", "", "", "BC(E), BD(F)", "BC(E), BD(F), C(G)", ""]

if __name__ == "__main__":
    main()