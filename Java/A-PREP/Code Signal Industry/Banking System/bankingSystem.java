

Level1 : implement 3 methods that will add , withdraw and transfer money(edge cases were given in the questions)

Level 2: find top n accounts with most transactions (Pretty straightforward too).

Level 3: method that passes , src account target account , timestamp and amount. you withhold money from source and if successful return a String Transfer<ordinal number of transfer> as result

second method would allow target to accept the money from src only when the money was withdrawn less than 24 hours ago from the src.

Level 1: Basic Banking Operations

Objective: Implement fundamental methods for a bank account.
Requirements:
Deposit Method: Add funds to the account.
Withdraw Method: Remove funds from the account, ensuring sufficient balance.
Transfer Method: Move funds from one account to another, handling edge cases like insufficient funds.
Note: Ensure proper validation and error handling for each operation.

Level 2: Transaction Tracking

Objective: Enhance the system to monitor account activities.
Requirements:
Transaction Count: Maintain a count of all transactions (deposits, withdrawals, transfers) for each account.
Top N Accounts: Implement functionality to retrieve the top N accounts with the highest number of transactions.
Note: Consider creating a Bank class to manage multiple accounts and facilitate this functionality.

Level 3: Timed Transfers

Objective: Introduce time-sensitive transfer operations.
Requirements:
Initiate Transfer: Implement a method to initiate a transfer that withholds the amount from the sender's account and provides a unique transfer ID.
Complete Transfer: Implement a method that allows the recipient to accept the transfer within a 24-hour window. If the transfer is not accepted within this timeframe, the withheld funds should be returned to the sender's account.
Note: Utilize timestamps to track transfer initiation times and ensure accurate time-based validations.

Level 4: Interest Calculation and Account Statements

Objective: Add features for financial growth and reporting.
Requirements:
Interest Application: Implement functionality to calculate and apply interest to account balances periodically (e.g., daily, monthly). Ensure that the interest application considers the account balance and the specified interest rate.
Account Statement Generation: Provide a method to generate account statements detailing all transactions within a specified date range. The statement should include dates, transaction types, amounts, and resulting balances.
Note: Ensure that the interest calculation is precise and that the account statement is comprehensive and formatted clearly.

Proper data design will make the OA question significantly easier.


A map to store userId to a custom data class. It can be something like:


enum TransactionStatus {
  COMPLETED,
  FAILED,
  PENDING
}

enum TransactionType {
  DEPOSIT,
  WITHDRAWAL,
  TRANSFER
}

class Transaction {
  TransactionType type;
  TransactionStatus status;
  int amount;
  string timestamp;
}

class User {
  int id;   // user id
  int completedTransactions;
  vector<Transaction> deposits, withdrawals, transfers;
}
Having the above class, P1 tasks are pretty trivial. Just create an appropriate transaction and update user object.


One optimization we can do: store prefix sums in the deposits, withdrawals, and transfers array. This will help us later in P4.


P2
a priority queue of pair<int, int> containing completed transactions and userId would work.


P3: Begin transfer to a given userId
Create new Transaction object with status = pending; type = transfer.
P3: Accept transfer from a given userId
Update sender's transaction status to completed.
Add new entry to receiver's deposits vector.
Increase completedTransactions for both user accounts.
We are not keeping score of balances. We'll calculate it as and when asked.


P4: Merge two accounts
Didn't understand this, but we can easily add balances and total transactions. Not sure what else this task expects.


P4: Get balance at a timestamp T
This is where we benefit the most from the data design. Since we are storing cumulative deposits, withdrawals, and transfers, we just need to do this:


balance = <completed deposits till time T> - <completed withdrawals till time T>  - <completed transfers till time T>
Since the timestamps in all three arrays are in strictly increasing order, we can do a binary search (lower_bound) to get the index with timestamp <= T. For example, if the index comes out to be 2, balance = deposits[2] - withdrawals[2] - transfers[2]; (since all three are cumulative; it is not clear if a pending transfer should be counted while accounting for balance, can be checked by running against test cases I guess).


Follow up: If a user doesn't accept the transfer from a stranger, revert it after 7 days. We can again binary search on the transfers array to find the index of transfers that are more than 7 days old. Once found, change their status to failed.


This was my thought process on the OA round. Would love to know how you would approach it.


