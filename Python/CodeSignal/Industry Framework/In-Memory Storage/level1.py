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

def main():
    db = InMemoryDatabase()
    
    # Test cases from level 1 example
    queries = [
        ["SET", "A", "B", "E"],
        ["SET", "A", "C", "F"],
        ["GET", "A", "B"],
        ["GET", "A", "D"],
        ["DELETE", "A", "B"],
        ["DELETE", "A", "D"]
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
        
        results.append(result)
        print(f"{query}: {result}")
    
    print(f"Final results: {results}")
    # Expected: ["", "", "E", "", "true", "false"]

if __name__ == "__main__":
    main()