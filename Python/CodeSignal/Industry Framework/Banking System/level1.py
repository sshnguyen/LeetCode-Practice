class BankingSystem:
    def __init__(self):
        self.accounts = {}  # account_id -> balance
    
    def create_account(self, timestamp: int, account_id: str) -> bool:
        if account_id in self.accounts:
            return False
        
        self.accounts[account_id] = 0
        return True
    
    def deposit(self, timestamp: int, account_id: str, amount: int) -> int | None:
        if account_id not in self.accounts:
            return None
        
        self.accounts[account_id] += amount
        return self.accounts[account_id]
    
    def transfer(self, timestamp: int, source_account_id: str, target_account_id: str, amount: int) -> int | None:
        # Check if both accounts exist
        if source_account_id not in self.accounts or target_account_id not in self.accounts:
            return None
        
        # Check if accounts are the same
        if source_account_id == target_account_id:
            return None
        
        # Check if source has sufficient funds
        if self.accounts[source_account_id] < amount:
            return None
        
        # Perform transfer
        self.accounts[source_account_id] -= amount
        self.accounts[target_account_id] += amount
        
        return self.accounts[source_account_id]

def main():
    banking = BankingSystem()
    
    # Test cases from level 1 example
    queries = [
        ("create_account", 1, "account1"),
        ("create_account", 2, "account1"),
        ("create_account", 3, "account2"),
        ("deposit", 4, "non-existing", 2700),
        ("deposit", 5, "account1", 2700),
        ("transfer", 6, "account1", "account2", 2701),
        ("transfer", 7, "account1", "account2", 200)
    ]
    
    results = []
    for query in queries:
        operation = query[0]
        if operation == "create_account":
            result = banking.create_account(query[1], query[2])
        elif operation == "deposit":
            result = banking.deposit(query[1], query[2], query[3])
        elif operation == "transfer":
            result = banking.transfer(query[1], query[2], query[3], query[4])
        
        results.append(result)
        print(f"{query}: {result}")
    
    print(f"Final results: {results}")
    # Expected: [True, False, True, None, 2700, None, 2500]

if __name__ == "__main__":
    main()