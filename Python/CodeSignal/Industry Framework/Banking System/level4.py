class BankingSystem:
    def __init__(self):
        self.accounts = {}  # account_id -> balance
        self.outgoing_transactions = {}  # account_id -> total_outgoing
        self.payment_counter = 0  # Global counter for payment IDs
        self.payments = {}  # payment_id -> (account_id, amount, timestamp, cashback_amount)
        self.pending_cashbacks = []  # List of (timestamp, account_id, cashback_amount)
        self.balance_history = {}  # account_id -> [(timestamp, balance)]
        self.merged_accounts = {}  # old_account_id -> new_account_id
    
    def _process_cashbacks(self, current_timestamp: int):
        """Process all pending cashbacks that are due"""
        i = 0
        while i < len(self.pending_cashbacks):
            cashback_time, account_id, cashback_amount = self.pending_cashbacks[i]
            if cashback_time <= current_timestamp:
                # Check if account was merged
                target_account = self._get_current_account(account_id)
                if target_account and target_account in self.accounts:
                    self.accounts[target_account] += cashback_amount
                    self._record_balance(target_account, cashback_time, self.accounts[target_account])
                # Remove processed cashback
                self.pending_cashbacks.pop(i)
            else:
                i += 1
    
    def _get_current_account(self, account_id: str) -> str | None:
        """Get the current account ID (following merge chain)"""
        current = account_id
        while current in self.merged_accounts:
            current = self.merged_accounts[current]
        return current if current in self.accounts else None
    
    def _record_balance(self, account_id: str, timestamp: int, balance: int):
        """Record balance at a specific timestamp"""
        if account_id not in self.balance_history:
            self.balance_history[account_id] = []
        self.balance_history[account_id].append((timestamp, balance))
    
    def create_account(self, timestamp: int, account_id: str) -> bool:
        self._process_cashbacks(timestamp)
        
        if account_id in self.accounts:
            return False
        
        self.accounts[account_id] = 0
        self.outgoing_transactions[account_id] = 0
        self._record_balance(account_id, timestamp, 0)
        return True
    
    def deposit(self, timestamp: int, account_id: str, amount: int) -> int | None:
        self._process_cashbacks(timestamp)
        
        if account_id not in self.accounts:
            return None
        
        self.accounts[account_id] += amount
        self._record_balance(account_id, timestamp, self.accounts[account_id])
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
        
        # Record balances
        self._record_balance(source_account_id, timestamp, self.accounts[source_account_id])
        self._record_balance(target_account_id, timestamp, self.accounts[target_account_id])
        
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
        
        self._record_balance(account_id, timestamp, self.accounts[account_id])
        return payment_id
    
    def get_payment_status(self, timestamp: int, account_id: str, payment: str) -> str | None:
        self._process_cashbacks(timestamp)
        
        # Check if account exists (could be merged)
        current_account = self._get_current_account(account_id)
        if not current_account:
            return None
        
        if payment not in self.payments:
            return None
        
        payment_account, amount, payment_timestamp, cashback_amount = self.payments[payment]
        
        # Check if payment belongs to this account (considering merges)
        payment_current_account = self._get_current_account(payment_account)
        if payment_current_account != current_account:
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
    
    def merge_accounts(self, timestamp: int, account_id_1: str, account_id_2: str) -> bool:
        self._process_cashbacks(timestamp)
        
        # Check if accounts are the same
        if account_id_1 == account_id_2:
            return False
        
        # Check if both accounts exist
        if account_id_1 not in self.accounts or account_id_2 not in self.accounts:
            return False
        
        # Merge balance
        self.accounts[account_id_1] += self.accounts[account_id_2]
        
        # Merge outgoing transactions
        self.outgoing_transactions[account_id_1] += self.outgoing_transactions[account_id_2]
        
        # Transfer balance history
        if account_id_2 in self.balance_history:
            if account_id_1 not in self.balance_history:
                self.balance_history[account_id_1] = []
            self.balance_history[account_id_1].extend(self.balance_history[account_id_2])
            self.balance_history[account_id_1].sort()  # Keep chronological order
        
        # Record the merge
        self.merged_accounts[account_id_2] = account_id_1
        
        # Update pending cashbacks to point to account_id_1
        for i in range(len(self.pending_cashbacks)):
            cashback_time, account_id, cashback_amount = self.pending_cashbacks[i]
            if account_id == account_id_2:
                self.pending_cashbacks[i] = (cashback_time, account_id_1, cashback_amount)
        
        # Remove account_id_2
        del self.accounts[account_id_2]
        del self.outgoing_transactions[account_id_2]
        if account_id_2 in self.balance_history:
            del self.balance_history[account_id_2]
        
        self._record_balance(account_id_1, timestamp, self.accounts[account_id_1])
        return True
    
    def get_balance(self, timestamp: int, account_id: str, time_at: int) -> int | None:
        self._process_cashbacks(timestamp)
        
        # Check if account exists at time_at (considering merges)
        target_account = account_id
        
        # If account was merged, check if it existed at time_at
        if account_id in self.merged_accounts:
            # Find when the merge happened by looking at balance history
            target_account = self._get_current_account(account_id)
            if not target_account:
                return None
        elif account_id not in self.accounts:
            return None
        
        # Get balance history for the target account
        history = self.balance_history.get(target_account, [])
        
        # Find the balance at time_at
        balance = None
        for hist_timestamp, hist_balance in history:
            if hist_timestamp <= time_at:
                balance = hist_balance
            else:
                break
        
        return balance

def main():
    banking = BankingSystem()
    MILLISECONDS_IN_1_DAY = 86400000
    
    # Test cases from level 4 example
    queries = [
        ("create_account", 1, "account1"),
        ("create_account", 2, "account2"),
        ("deposit", 3, "account1", 2000),
        ("deposit", 4, "account2", 2000),
        ("pay", 5, "account1", 500),
        ("transfer", 6, "account1", "account2", 500),
        ("merge_accounts", 7, "account1", "non-existing"),
        ("merge_accounts", 8, "account1", "account1"),
        ("merge_accounts", 9, "account1", "account2"),
        ("get_balance", 10, "account1", 10),
        ("get_balance", 11, "account2", 10),
        ("get_payment_status", 12, "account1", "payment1"),
        ("get_payment_status", 13, "account2", "payment1"),
        ("get_balance", 14, "account2", 1),
        ("get_balance", 15, "account2", 9),
        ("get_balance", 16, "account1", 11),
        ("deposit", 5 + MILLISECONDS_IN_1_DAY, "account1", 100)
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
        elif operation == "merge_accounts":
            result = banking.merge_accounts(query[1], query[2], query[3])
        elif operation == "get_balance":
            result = banking.get_balance(query[1], query[2], query[3])
        
        results.append(result)
        print(f"{query}: {result}")
    
    print(f"Final results: {results}")

if __name__ == "__main__":
    main()