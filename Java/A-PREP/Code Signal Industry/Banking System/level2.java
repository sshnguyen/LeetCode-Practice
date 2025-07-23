import java.util.*;
import java.util.stream.Collectors;

public class BankingSystem {
    // Map to store account IDs and their balances
    private Map<String, Integer> accounts;
    // Map to track the total outgoing transactions for each account
    private Map<String, Integer> outgoingTransactions;

    // Constructor to initialize the maps
    public BankingSystem() {
        accounts = new HashMap<>();
        outgoingTransactions = new HashMap<>();
    }

    // Method to create a new account
    public boolean createAccount(int timestamp, String accountId) {
        // Check if the account already exists
        if (accounts.containsKey(accountId)) {
            return false; // Account already exists
        }
        // Initialize the account with a balance of 0 and outgoing transactions of 0
        accounts.put(accountId, 0);
        outgoingTransactions.put(accountId, 0);
        return true; // Account created successfully
    }

    // Method to deposit money into an account
    public Integer deposit(int timestamp, String accountId, int amount) {
        // Check if the account exists
        if (!accounts.containsKey(accountId)) {
            return null; // Account does not exist
        }
        // Update the account balance
        int balance = accounts.get(accountId);
        balance += amount;
        accounts.put(accountId, balance);
        return balance; // Return the new balance
    }

    // Method to transfer money between two accounts
    public Integer transfer(int timestamp, String sourceAccountId, String targetAccountId, int amount) {
        // Check if both accounts exist and are different
        if (!accounts.containsKey(sourceAccountId) || !accounts.containsKey(targetAccountId) || sourceAccountId.equals(targetAccountId)) {
            return null; // Invalid transfer
        }
        // Check if the source account has sufficient funds
        int sourceBalance = accounts.get(sourceAccountId);
        if (sourceBalance < amount) {
            return null; // Insufficient funds
        }
        // Perform the transfer
        int targetBalance = accounts.get(targetAccountId);
        sourceBalance -= amount;
        targetBalance += amount;
        accounts.put(sourceAccountId, sourceBalance);
        accounts.put(targetAccountId, targetBalance);
        // Update the outgoing transactions for the source account
        outgoingTransactions.put(sourceAccountId, outgoingTransactions.getOrDefault(sourceAccountId, 0) + amount);
        return sourceBalance; // Return the new balance of the source account
    }

    // Method to get the top N accounts with the highest outgoing transactions
    public List<String> topAccounts(int timestamp, int n) {
        // Step 1: Convert the map entries to a list
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(outgoingTransactions.entrySet());

        // Step 2: Sort the list of entries
        entries.sort((a, b) -> {
            // Compare the outgoing transactions in descending order
            int cmp = b.getValue().compareTo(a.getValue());
            if (cmp == 0) {
                // If outgoing transactions are equal, compare account IDs in ascending order
                return a.getKey().compareTo(b.getKey());
            }
            return cmp; // Return the comparison result
        });

        List<String> result = new ArrayList<>();

        // Step 3: Iterate through the sorted entries and add the top N accounts to the result list
        for (int i = 0; i < Math.min(n, entries.size()); i++) {
            // Get the current entry
            Map.Entry<String, Integer> entry = entries.get(i);

            // Format the entry as "<account_id>(<outgoing_transactions>)" and add it to the result list
            result.add(entry.getKey() + "(" + entry.getValue() + ")");
        }

        // Step 5: Return the result list
        return result;
    }
}

// Main class for Level 2
public class Main {
    public static void main(String[] args) {
        // Create a new banking system
        BankingSystem bank = new BankingSystem();

        // Create accounts
        bank.createAccount(1, "account1");
        bank.createAccount(2, "account2");
        bank.createAccount(3, "account3");

        // Deposit money
        bank.deposit(4, "account1", 1000);
        bank.deposit(5, "account2", 2000);
        bank.deposit(6, "account3", 3000);

        // Transfer money
        bank.transfer(7, "account1", "account2", 500);
        bank.transfer(8, "account2", "account3", 1000);
        bank.transfer(9, "account3", "account1", 1500);

        // Get top accounts by outgoing transactions
        System.out.println(bank.topAccounts(10, 3)); // [account3(1500), account2(1000), account1(500)]
    }
}