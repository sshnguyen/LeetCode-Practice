import java.util.HashMap;
import java.util.Map;

public class BankingSystem {
    // Map to store account IDs and their balances
    private Map<String, Integer> accounts;

    // Constructor to initialize the accounts map
    public BankingSystem() {
        accounts = new HashMap<>();
    }

    // Method to create a new account
    public boolean createAccount(int timestamp, String accountId) {
        // Check if the account already exists
        if (accounts.containsKey(accountId)) {
            return false; // Account already exists
        }
        // Add the new account with a balance of 0
        accounts.put(accountId, 0);
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
        return sourceBalance; // Return the new balance of the source account
    }
}

// Main class for Level 1
public class Main {
    public static void main(String[] args) {
        // Create a new banking system
        BankingSystem bank = new BankingSystem();

        // Create accounts
        System.out.println(bank.createAccount(1, "account1")); // true (account created)
        System.out.println(bank.createAccount(2, "account2")); // true (account created)
        System.out.println(bank.createAccount(3, "account1")); // false (account already exists)

        // Deposit money
        System.out.println(bank.deposit(4, "account1", 1000)); // 1000 (new balance of account1)
        System.out.println(bank.deposit(5, "account2", 500)); // 500 (new balance of account2)

        // Transfer money
        System.out.println(bank.transfer(6, "account1", "account2", 300)); // 700 (new balance of account1)
        System.out.println(bank.transfer(7, "account1", "account3", 200)); // null (account3 does not exist)
    }
}