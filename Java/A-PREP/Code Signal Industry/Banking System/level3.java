import java.util.*;

public class BankingSystem {
    // Map to store account IDs and their balances
    private Map<String, Integer> accounts;
    // Map to track the total outgoing transactions for each account
    private Map<String, Integer> outgoingTransactions;
    // Map to store scheduled payments for each account
    private Map<String, List<Payment>> scheduledPayments;
    // Map to track cashback refunds (key: timestamp, value: list of cashback transactions)
    private Map<Long, List<Cashback>> cashbackQueue;
    // Counter to generate unique payment IDs
    private int paymentCounter;

    // Constructor to initialize the maps
    public BankingSystem() {
        accounts = new HashMap<>();
        outgoingTransactions = new HashMap<>();
        scheduledPayments = new HashMap<>();
        cashbackQueue = new TreeMap<>(); // Use TreeMap to process cashback in chronological order
        paymentCounter = 0;
    }

    // Method to create a new account
    public boolean createAccount(int timestamp, String accountId) {
        // Check if the account already exists
        if (accounts.containsKey(accountId)) {
            return false; // Account already exists
        }
        // Initialize the account with a balance of 0, outgoing transactions of 0, and an empty list of scheduled payments
        accounts.put(accountId, 0);
        outgoingTransactions.put(accountId, 0);
        scheduledPayments.put(accountId, new ArrayList<>());
        return true; // Account created successfully
    }

    // Method to deposit money into an account
    public Integer deposit(int timestamp, String accountId, int amount) {
        // Process cashback before performing the deposit
        processCashback(timestamp);

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
        // Process cashback before performing the transfer
        processCashback(timestamp);

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
        // Process cashback before retrieving the top accounts
        processCashback(timestamp);

        // Step 1: Convert the map entries to a list
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(outgoingTransactions.entrySet());

        // Step 2: Sort the list using a custom comparator
        entries.sort((a, b) -> {
            int cmp = b.getValue().compareTo(a.getValue()); // Sort by outgoing transactions (descending)
            if (cmp == 0) {
                return a.getKey().compareTo(b.getKey()); // If equal, sort by account ID (ascending)
            }
            return cmp;
        });

        // Step 3: Limit the results to the top N accounts
        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(n, entries.size()); i++) {
            Map.Entry<String, Integer> entry = entries.get(i);
            result.add(entry.getKey() + "(" + entry.getValue() + ")"); // Format the result
        }

        return result;
    }

    // Method to schedule a payment with cashback
    public String pay(int timestamp, String accountId, int amount) {
        // Process cashback before performing the payment
        processCashback(timestamp);

        // Check if the account exists
        if (!accounts.containsKey(accountId)) {
            return null; // Account does not exist
        }
        // Check if the account has sufficient funds
        int balance = accounts.get(accountId);
        if (balance < amount) {
            return null; // Insufficient funds
        }
        // Withdraw the amount from the account
        balance -= amount;
        accounts.put(accountId, balance);
        // Update the outgoing transactions for the account
        outgoingTransactions.put(accountId, outgoingTransactions.getOrDefault(accountId, 0) + amount);

        // Generate a unique payment ID
        String paymentId = "payment" + paymentCounter++;
        // Calculate cashback (2% of the amount, rounded down)
        int cashbackAmount = (int) Math.floor(amount * 0.02);
        // Schedule the cashback to be refunded after 24 hours (24 * 60 * 60 * 1000 milliseconds)
        long cashbackTimestamp = timestamp + 24 * 60 * 60 * 1000;
        cashbackQueue.computeIfAbsent(cashbackTimestamp, k -> new ArrayList<>()).add(new Cashback(accountId, cashbackAmount));

        // Add the payment to the scheduled payments list for the account
        scheduledPayments.get(accountId).add(new Payment(paymentId, timestamp, amount, cashbackAmount));
        return paymentId; // Return the payment ID
    }

    // Method to check the status of a scheduled payment
    public String getPaymentStatus(int timestamp, String accountId, String paymentId) {
        // Process cashback before checking the payment status
        processCashback(timestamp);

        // Check if the account exists
        if (!scheduledPayments.containsKey(accountId)) {
            return null; // Account does not exist
        }
        // Iterate through the scheduled payments for the account
        for (Payment payment : scheduledPayments.get(accountId)) {
            // Check if the payment ID matches
            if (payment.getId().equals(paymentId)) {
                // Check if the cashback has been processed
                if (timestamp >= payment.getTimestamp() + 24 * 60 * 60 * 1000) {
                    return "CASHBACK_RECEIVED";
                } else {
                    return "IN_PROGRESS";
                }
            }
        }
        return null; // Payment not found
    }

    // Internal method to process cashback refunds at the current timestamp
    private void processCashback(int timestamp) {
        // Iterate through the cashback queue and process refunds
        Iterator<Map.Entry<Long, List<Cashback>>> iterator = cashbackQueue.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, List<Cashback>> entry = iterator.next();
            if (entry.getKey() <= timestamp) {
                // Process all cashback refunds for this timestamp
                for (Cashback cashback : entry.getValue()) {
                    int balance = accounts.get(cashback.getAccountId());
                    balance += cashback.getAmount();
                    accounts.put(cashback.getAccountId(), balance);
                }
                // Remove the processed cashback entries
                iterator.remove();
            } else {
                // Stop processing if the timestamp is in the future
                break;
            }
        }
    }

    // Inner class to represent a payment
    private static class Payment {
        private String id; // Unique ID for the payment
        private int timestamp; // Timestamp of the payment
        private int amount; // Amount of the payment
        private int cashbackAmount; // Cashback amount

        // Constructor to initialize a payment
        public Payment(String id, int timestamp, int amount, int cashbackAmount) {
            this.id = id;
            this.timestamp = timestamp;
            this.amount = amount;
            this.cashbackAmount = cashbackAmount;
        }

        // Method to get the payment ID
        public String getId() {
            return id;
        }

        // Method to get the payment timestamp
        public int getTimestamp() {
            return timestamp;
        }
    }

    // Inner class to represent a cashback transaction
    private static class Cashback {
        private String accountId; // Account ID for the cashback
        private int amount; // Cashback amount

        // Constructor to initialize a cashback transaction
        public Cashback(String accountId, int amount) {
            this.accountId = accountId;
            this.amount = amount;
        }

        // Method to get the account ID
        public String getAccountId() {
            return accountId;
        }

        // Method to get the cashback amount
        public int getAmount() {
            return amount;
        }
    }
}

// Main class to demonstrate the functionality
public class Main {
    public static void main(String[] args) {
        // Create a new banking system
        BankingSystem bank = new BankingSystem();

        // Create accounts
        System.out.println("Creating accounts:");
        System.out.println(bank.createAccount(1, "account1")); // true
        System.out.println(bank.createAccount(2, "account2")); // true

        // Deposit money
        System.out.println("\nDepositing money:");
        System.out.println(bank.deposit(3, "account1", 2000)); // 2000
        System.out.println(bank.deposit(4, "account2", 3000)); // 3000

        // Schedule payments with cashback
        System.out.println("\nScheduling payments:");
        String paymentId1 = bank.pay(5, "account1", 1000); // payment0
        System.out.println(paymentId1); // payment0
        String paymentId2 = bank.pay(6, "account2", 1500); // payment1
        System.out.println(paymentId2); // payment1

        // Check payment status before cashback is processed
        System.out.println("\nChecking payment status before cashback:");
        System.out.println(bank.getPaymentStatus(7, "account1", paymentId1)); // IN_PROGRESS
        System.out.println(bank.getPaymentStatus(8, "account2", paymentId2)); // IN_PROGRESS

        // Check payment status after cashback is processed (24 hours later)
        System.out.println("\nChecking payment status after cashback:");
        System.out.println(bank.getPaymentStatus(5 + 24 * 60 * 60 * 1000, "account1", paymentId1)); // CASHBACK_RECEIVED
        System.out.println(bank.getPaymentStatus(6 + 24 * 60 * 60 * 1000, "account2", paymentId2)); // CASHBACK_RECEIVED

        // Check account balances after cashback
        System.out.println("\nAccount balances after cashback:");
        System.out.println("account1: " + bank.deposit(5 + 24 * 60 * 60 * 1000, "account1", 0)); // 1020 (2000 - 1000 + 20 cashback)
        System.out.println("account2: " + bank.deposit(6 + 24 * 60 * 60 * 1000, "account2", 0)); // 1530 (3000 - 1500 + 30 cashback)
    }
}