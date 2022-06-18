//to brute force, we can traverse the array and consider each index as a buy price and for each, consider every indices after it as a potential sell price. Keep track of the maximum profit.
//optimally, we can traverse the array once, keeping track of left and right pointer. If the left pointer value is greater than the right then we replace it as the left pointer. otherwise calculate the current profit and update maximum profit if necessary.
class Solution {
    public int maxProfit(int[] prices) {
      int maxProfit = 0;
      int leftPointer = 0;
      int rightPointer = 0;
      //traverse the length of the array
      for (int i = 1; i < prices.length; i++){
        // update left pointer to current right pointer if found a new minimum buy price
        rightPointer = i;
        if (prices[leftPointer] > prices[rightPointer]){
          leftPointer = rightPointer;
        } 
        //find profit if sell at the current right pointer price
        //and update the maximum profit if necessary
        else {
          int currentPrice = prices[rightPointer] - prices[leftPointer];
          maxProfit = (currentPrice > maxProfit) ? currentPrice : maxProfit;
        }
      }
      return maxProfit;

    }
}