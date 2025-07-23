import java.util.*;

public class TransactionProcessor {

    public static Map<Integer, Integer> applyTransactions(List<int[]> transactions, Map<Integer, Integer> balances) {
        // Create a copy to avoid mutating original map
        Map<Integer, Integer> result = new HashMap<>(balances);

        for (int[] tx : transactions) {
            int from = tx[0];
            int to = tx[1];
            int amount = tx[2];

            // Deduct from sender
            result.put(from, result.getOrDefault(from, 0) - amount);

            // Add to receiver
            result.put(to, result.getOrDefault(to, 0) + amount);
        }

        return result;
    }
    
    public static List<int[]> settleBalances(Map<Integer, Integer> balances) {
        List<int[]> result = new ArrayList<>();

        // Max heap for creditors (largest balance first)
        PriorityQueue<int[]> creditors = new PriorityQueue<>(
            (a, b) -> b[1] - a[1]  // Compare by amount (index 1 of the array)
        );

        // Max heap for debtors (largest debt first)
        PriorityQueue<int[]> debtors = new PriorityQueue<>(
            (a, b) -> b[1] - a[1]  // Compare by amount (index 1 of the array)
        );

        // Separate into debtors and creditors
        for (Map.Entry<Integer, Integer> entry : balances.entrySet()) {
            int id = entry.getKey();
            int balance = entry.getValue();
            if (balance > 0) {
                creditors.offer(new int[]{id, balance});
            } else if (balance < 0) {
                debtors.offer(new int[]{id, -balance});  // Store debt as positive
            }
        }

        // Greedily settle debts
        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            int[] creditor = creditors.poll();
            int[] debtor = debtors.poll();

            int amount = Math.min(creditor[1], debtor[1]);
            result.add(new int[]{creditor[0], debtor[0], amount});

            // Update the remaining balances
            if (creditor[1] > amount) {
                creditors.offer(new int[]{creditor[0], creditor[1] - amount});
            }
            if (debtor[1] > amount) {
                debtors.offer(new int[]{debtor[0], debtor[1] - amount});
            }
        }

        return result;
    }


    public static void main(String[] args) {
        // Starting balances
        Map<Integer, Integer> balances = new HashMap<>();
        balances.put(0, 0);
        balances.put(1, 0);
        balances.put(2, 0);
        balances.put(3, 0);

        // Transactions: [from, to, amount]
        List<int[]> transactions = Arrays.asList(
            new int[]{0, 1, 100},
            new int[]{2, 3, 200},
            new int[]{3, 1, 50}
        );

        Map<Integer, Integer> finalBalances = applyTransactions(transactions, balances);

        // Print final balances
        System.out.println("Final Balances:");
        for (Map.Entry<Integer, Integer> entry : finalBalances.entrySet()) {
            System.out.println("ID " + entry.getKey() + ": " + entry.getValue());
        }
        
        
         // Test case for Part 2 - Settling balances
        Map<Integer, Integer> inputBalances = new HashMap<>();
        inputBalances.put(0, -100);
        inputBalances.put(1, 150);
        inputBalances.put(2, -200);
        inputBalances.put(3, 150);

        List<int[]> settlementTransactions = settleBalances(inputBalances);

        // Print settlement transactions
        System.out.println("\nTransactions to Settle Balances:");
        for (int[] tx : settlementTransactions) {
            System.out.println("From ID " + tx[0] + " to ID " + tx[1] + " amount $" + tx[2]);
        }

    }
}
