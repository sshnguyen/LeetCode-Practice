//dp solution, first index can be reach, store it in dp[0]
// traverse through nums, if the index can be reach
//then all the index up to index + num[index] can be reached. Update dp[index] for index that can be reach
class Solution {
   public boolean canJump(int[] nums) {
        // dp[index] to store whether index can be reached.
        boolean[] dp = new boolean [nums.length];
        Arrays.fill(dp, false);
        dp[0] = true;
        //for each element, check if that element can be reach
        // with dp[element], if it is then update elements
        // that we can jump to
        for (int i = 0; i < nums.length; i++){
            int jumps = nums[i];
            for (int b = 1; b <= jumps; b++){
                //make sure that we don't go over the final index
                if (((i+b) < (nums.length)) && (dp[i] == true)){
                    dp[i+b] = true;
                }
            }
          }
        return dp[nums.length - 1];
    }
}