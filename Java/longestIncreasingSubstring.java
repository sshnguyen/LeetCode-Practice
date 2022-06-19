//dp solution, for each index, we find the longest substring prior to that index and call it dp[].
//starting at first index will have a dp[0] = 1 since the longest substring only contain that index.
// For the subesquence iterations, we can
//find its dp value by comparing the current value to previous elements, if its bigger, then
// we can extend the longest substring by 1 so dp[current] >= dp[prev-element] + 1;
// we can update dp[current] to be largest dp[prev-element] that sastisfy the condition + 1;
// update max as we go through
class Solution {
    public int lengthOfLIS(int[] nums) {
        int length = nums.length;
        int[] dp = new int [length];
        int max = 1;
        if(length == 1){
            return 1;
        }
        dp[0] = 1;
        // loop through all elements, starting at second,
        // check the elements previous to it, updating its dp, and max as needed
        for (int i = 1; i < length; i++){
            dp[i] = 1;
            for (int b = 0; b < i; b++){
                if(nums[i]>nums[b] && dp[i]<=dp[b]){
                    dp[i]=dp[b]+1;
                    max = Math.max(dp[i],max);
                }
            }
        }
        return max;
    }
}