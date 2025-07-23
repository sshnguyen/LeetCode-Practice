import java.util.*;

// Design a transaction logging system with two APIs:

// Record a transaction with its amount and timestamp
// Get the sum of all transactions from the last 60 minutes
// Requirements:

// Timestamps should have second-level precision
// Both APIs will be called frequently and should have O(1) time complexity

/*
CLARIFYING QUESTION:
    CAN THERE BE TWO OF THE SAME TIMESTAMP? What do we do here? 
    merge the amount to keep it less space or keep it in seperate transaction in case we need to extend to some other requirements.
    
    What is the format of the time? int representing seconds?
    
*/

/*
QUEUE WOULD WORK, add is O(1), get hit is O(n) worst case, but O(k) k is the number of hits in the last hour.
SIMPLE IMPLEMENTATION, ADD TO QUEUE. FOR GETHITS, traverse the entire queue, peek() and add all transactions with time >= current time - 3600;
Remove all transaction still in queue otherwise.

DEQUEUE IS BETTER SINCE WE CAN REMOVE AND PEEK BOTH END.
O(1) for adding, Keep a counter of the amount as we add.
FOR GETHITS, START FROM THE START OF DEQUEUE AND REMOVE IT IF TIME < CURRENT TIME - 3600;
UPDATE THE AMOUNT THAT WE REMOVE FROM THE COUNTER. RETURN THE COUNTER
O(n) worst case but if no item to remove then it's O(1) since we return the counter value and doesn't have
to go through the rest of the element like queue implementation.

*/

class Transaction {
    float amount;
    int time;
    public Transaction(float amount, int time){
        this.amount = amount;
        this.time = time;
    }
}

class TransactionLogger {
    Deque<Transaction> transactions;
    float transactionAmount; //total amount in the current transactions queue
    
    public TransactionLogger(){
        this.transactions = new LinkedList<>();
        this.transactionAmount = 0;
    }
    
    public void putTransaction(float amount, int time){
        transactions.offer(new Transaction(amount, time));
        this.transactionAmount += amount;
    }
    
    public float getTotalTransactionInLastHour(int time){
        while (!transactions.isEmpty() && transactions.peekFirst().time < time - 3600) { //search at start of the queue for expire transaction
            transactionAmount -= transactions.pollFirst().amount; // DEQUEUE AND Subtract expired amount
        }
        
        return transactionAmount;
    }
}

public class Solution {

    public static void main(String[] args) {
       TransactionLogger logger = new TransactionLogger();
        
        logger.putTransaction(10, 0);
        logger.putTransaction(20, 50);
        System.out.println(logger.getTotalTransactionInLastHour(60)); // Output: 30

        logger.putTransaction(30, 3650);
        System.out.println(logger.getTotalTransactionInLastHour(3650)); // Output: 30 + 20 = 50
        System.out.println(logger.getTotalTransactionInLastHour(3660)); // Output: 30 
        
    }
}
