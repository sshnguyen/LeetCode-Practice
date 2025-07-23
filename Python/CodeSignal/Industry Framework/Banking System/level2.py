class BankingSystem:
    def __init__(self):
        self.accounts = {}  # account_id -> balance
        self.outgoing_transactions = {}  # account_id -> total_outgoing
    
    def create_account(self, timestamp: int, account_id: str) -> bool:
        if account_id in self.accounts:
            return False
        
        self.accounts[account_id] = 0
        self.outgoing_transactions[account_id] = 0
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
        
        # Update outgoing transactions for source account
        self.outgoing_transactions[source_account_id] += amount
        
        return self.accounts[source_account_id]
    
    def top_spenders(self, timestamp: int, n: int) -> list[str]:
        # Get all accounts with their outgoing amounts
        spenders = []
        for account_id in self.accounts:
            outgoing = self.outgoing_transactions[account_id]
            spenders.append((account_id, outgoing))
        
        # Sort by outgoing amount descending, then by account_id ascending
        spenders.sort(key=lambda x: (-x[1], x[0]))
        
        # Take top n and format result
        result = []
        for i in range(min(n, len(spenders))):
            account_id, outgoing = spenders[i]
            result.append(f"{account_id}({outgoing})")
        
        return result

def main():
    banking = BankingSystem()
    
    # Test cases from level 2 example
    queries = [
        ("create_account", 1, "account3"),
        ("create_account", 2, "account2"),
        ("create_account", 3, "account1"),
        ("deposit", 4, "account3", 2000),
        ("deposit", 5, "account2", 3000),
        ("deposit", 6, "account3", 4000),
        ("top_spenders", 7, 3),
        ("transfer", 8, "account3", "account2", 500),
        ("transfer", 9, "account3", "account1", 1000),
        ("transfer", 10, "account1", "account2", 2500),
        ("top_spenders", 11, 3)
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
        elif operation == "top_spenders":
            result = banking.top_spenders(query[1], query[2])
        
        results.append(result)
        print(f"{query}: {result}")
    
    print(f"Final results: {results}")

if __name__ == "__main__":
    main()