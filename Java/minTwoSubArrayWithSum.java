// one pass sliding window with dp
class Solution {
    public int minSumOfLengths(int[] arr, int target) {
        //sliding window to find the sub array that have the sum.
        //dp to store the minSum length of sub array up to an index
        int length = arr.length;
        int i = 0, j = 0; //i left pointer, j right pointer
        int result = Integer.MAX_VALUE;
        int[] dp = new int[length];
        int sum = 0;
        while (j < length){
            dp[j] = Integer.MAX_VALUE; //initialize value for j = 0
            sum += arr[j];
            //shift left pointer if sum is greater than target
            while(i<length && sum > target) {
                sum -= arr[i];
                i++;
            }
            //hit target
            if (sum == target){
                dp[j] = j - i + 1;  // set the dp value to current subarray
                //update result if there is multiple subarray
                if (i > 0 && dp[i - 1] != Integer.MAX_VALUE){ // we check i - 1 so there's no overlap
                    result = Math.min(dp[j] + dp[i-1], result);
                }
            }
            if(j-1>=0){
               dp[j] = Math.min(dp[j], dp[j-1]); // dp[i] represents the shortest length sub so far
            }
            
            j++;
        }
        return result==Integer.MAX_VALUE? -1 : result;
    }
}