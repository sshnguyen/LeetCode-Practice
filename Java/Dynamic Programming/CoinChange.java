//dp solution, we know dp[0] take 0 coins to reach, use this fact and iterate through
//all the coins value, for each coin value go through all the dp[any amount] that is able to use
//the coin. If dp[any amount - coinValue] is not MaxValue then we can try to update dp[any amount]
//with min(dp[anyamount - coinValue] + 1, dp[anyamount]). This guaranteed dp[anyamount]
//will not be integer.Max_Value
class Solution {
    public static int coinChange(int[] coins, int amount) {
    //dp[amount] will store min no. of coins required to pay the amount,so taking array of size amount + 1
    int[] dp = new int[amount + 1];
    //fill dp with Integer.MAX_VALUE initially, until we confirm that amount can be reach
    Arrays.fill(dp, Integer.MAX_VALUE);
    // if amount if 0 then take 0 coins to reach
    dp[0] = 0;
    //iterate through the different coins values and update
    //the amount that can be reach with dp
    for (int coinValue : coins){
      // for each amount that can use this coin value, try to update 
      for (int i = coinValue; i <= amount; i++){
          if(dp[i - coinValue] != Integer.MAX_VALUE){
              dp[i] = Math.min(dp[i], dp[i - coinValue] + 1);
          }
      }
    }
    return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
  }
}