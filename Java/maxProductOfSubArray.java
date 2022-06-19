class Solution {
    //breaking down the examples into cases
    //the max subarray has to be ended by a -int at one end of the subarray because
    // if -int at both end then it can be added to the subarray, same as other 
    // combination of -ve max +ve, +ve max -ve, +ve max +ve, in these cases +ve can be 
    //extended to the subarray. By these cases the subarray starts either on left, or
    //right side and we can calculate by traversing left, and right then compare.
    //outlier is when there's a 0 in the list, then the max sub array can start before or after it. so we need to traverse the entire list and consider it as like a start
    
    public int max(int a, int b) { return (a > b) ? a : b; }
    
    public int maxProduct(int[] nums) {
        int maxLeft = Integer.MIN_VALUE;
        int maxRight = Integer.MIN_VALUE;
        int currProductLeft = 1;
        int currProductRight = 1;
        //find max prod if the subarray starts on the left
        for (int i = 0; i < nums.length; i++){
            currProductLeft = currProductLeft * nums[i];
            maxLeft = max(currProductLeft, maxLeft);
            if(currProductLeft == 0) {  //if at anytime currenProd = 0 reset it to 1
                currProductLeft = 1;
            }
        }
        //find max prod if the subarray start on the right
        for (int i = nums.length - 1; i >= 1; i--){
            currProductRight = currProductRight * nums[i];
            maxRight = max(currProductRight, maxRight);
            if(currProductRight == 0) {  //if at anytime currenProd = 0 reset it to 1
                currProductRight = 1;
            }
        } 
        return max(maxLeft, maxRight);
        
    }
}