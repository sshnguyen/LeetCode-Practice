class BankingSystem:
    def __init__(self):
        self.accounts = {}  # account_id -> balance
        self.outgoing_transactions = {}  # account_id -> total_outgoing
        self.payment_counter = 0  # Global counter for payment IDs
        self.payments = {}  # payment_id -> (account_id, amount, timestamp, cashback_amount)
        self.pending_cashbacks = []  # List of (timestamp, account_id, cashback_amount)
    
    def _process_cashbacks(self, current_timestamp: int):
        """Process all pending cashbacks that are due"""
        i = 0
        while i < len(self.pending_cashbacks):
            cashback_time, account_id, cashback_amount = self.pending_cashbacks[i]
            if cashback_time <= current_timestamp:
                # Process cashback if account still exists
                if account_id in self.accounts:
                    self.accounts[account_id] += cashback_amount
                # Remove processed cashback
                self.pending_cashbacks.pop(i)
            else:
                i += 1
    
    def create_account(self, timestamp: int, account_id: str) -> bool:
        self._process_cashbacks(timestamp)
        
        if account_id in self.accounts:
            return False
        
        self.accounts[account_id] = 0
        self.outgoing_transactions[account_id] = 0
        return True
    
    def deposit(self, timestamp: int, account_id: str, amount: int) -> int | None:
        self._process_cashbacks(timestamp)
        
        if account_id not in self.accounts:
            return None
        
        self.accounts[account_id] += amount
        return self.accounts[account_id]
    
    def transfer(self, timestamp: int, source_account_id: str, target_account_id: str, amount: int) -> int | None:
        self._process_cashbacks(timestamp)
        
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
    
    def pay(self, timestamp: int, account_id: str, amount: int) -> str | None:
        self._process_cashbacks(timestamp)
        
        if account_id not in self.accounts:
            return None
        
        if self.accounts[account_id] < amount:
            return None
        
        # Perform payment
        self.accounts[account_id] -= amount
        self.outgoing_transactions[account_id] += amount
        
        # Generate payment ID
        self.payment_counter += 1
        payment_id = f"payment{self.payment_counter}"
        
        # Calculate cashback (2% rounded down)
        cashback_amount = amount * 2 // 100
        
        # Store payment info
        self.payments[payment_id] = (account_id, amount, timestamp, cashback_amount)
        
        # Schedule cashback for 24 hours later
        cashback_time = timestamp + 86400000  # 24 hours in milliseconds
        self.pending_cashbacks.append((cashback_time, account_id, cashback_amount))
        
        return payment_id
    
    def get_payment_status(self, timestamp: int, account_id: str, payment: str) -> str | None:
        self._process_cashbacks(timestamp)
        
        if account_id not in self.accounts:
            return None
        
        if payment not in self.payments:
            return None
        
        payment_account, amount, payment_timestamp, cashback_amount = self.payments[payment]
        
        if payment_account != account_id:
            return None
        
        # Check if cashback has been processed (24 hours passed)
        cashback_time = payment_timestamp + 86400000
        if timestamp >= cashback_time:
            return "CASHBACK_RECEIVED"
        else:
            return "IN_PROGRESS"
    
    def top_spenders(self, timestamp: int, n: int) -> list[str]:
        self._process_cashbacks(timestamp)
        
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
    MILLISECONDS_IN_1_DAY = 86400000
    
    # Test cases from level 3 example
    queries = [
        ("create_account", 1, "account1"),
        ("create_account", 2, "account2"),
        ("deposit", 3, "account1", 2000),
        ("pay", 4, "account1", 1000),
        ("pay", 100, "account1", 1000),
        ("get_payment_status", 101, "non-existing", "payment1"),
        ("get_payment_status", 102, "account2", "payment1"),
        ("get_payment_status", 103, "account1", "payment1"),
        ("top_spenders", 104, 2),
        ("deposit", 3 + MILLISECONDS_IN_1_DAY, "account1", 100),
        ("get_payment_status", 4 + MILLISECONDS_IN_1_DAY, "account1", "payment1"),
        ("deposit", 5 + MILLISECONDS_IN_1_DAY, "account1", 100),
        ("deposit", 99 + MILLISECONDS_IN_1_DAY, "account1", 100),
        ("deposit", 100 + MILLISECONDS_IN_1_DAY, "account1", 100)
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
        elif operation == "pay":
            result = banking.pay(query[1], query[2], query[3])
        elif operation == "get_payment_status":
            result = banking.get_payment_status(query[1], query[2], query[3])
        elif operation == "top_spenders":
            result = banking.top_spenders(query[1], query[2])
        
        results.append(result)
        print(f"{query}: {result}")
    
    print(f"Final results: {results}")

if __name__ == "__main__":
    main()