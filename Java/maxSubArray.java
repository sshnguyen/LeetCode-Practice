  //naive, brute force is for each element, consider that and loop through the rest of the indexes to find the element that give the maximum sub array, and then compare it to the overall maximum.
  //divide and conquer O(nlogn), divide the array into left and right half, find the maxSubArray of left half, right half, and subarray that crosses mid point. then choose the maximum of the three
  //O(n) solution, traverse through the array once, storing maximum sub-array as we go. If the
  //sum of current subarray + nums[index] < nums[index] then the parts upto this point is obviously not in the
  //final solution so change the start of the subarray to this num,
class Solution {
    // A utility function to find maximum of two integers
    public int max(int a, int b) { return (a > b) ? a : b; }

    // A utility function to find maximum of three integers
    public int max(int a, int b, int c) { return max(max(a, b), c); }
    
    public int findCrossSum(int[] nums, int start, int mid, int end){
        //start at mid, find max leftsum
        int maxLeftSum = -999999;
        int currentLeftSum = 0;
        for (int i = mid; i >= start; i --){
            currentLeftSum =currentLeftSum +  nums[i];
            maxLeftSum = max(maxLeftSum, currentLeftSum);
        }
        int maxRightSum = -999999;
        int currentRightSum = 0;
        // start after mid, find max rightsum
        for (int i = mid + 1; i <= end; i ++){
            currentRightSum = currentRightSum + nums[i];
            maxRightSum = max(maxRightSum, currentRightSum);
        }
        return(max(maxLeftSum, maxRightSum, (maxLeftSum + maxRightSum)));
    }
    
    public int findMaxSum(int[] nums, int start, int end){
        if(start==end){ 
            return nums[start];
        }
        int mid = (end+start)/2;
        return(max(findMaxSum(nums,start, mid), findMaxSum(nums,mid+1, end), findCrossSum(nums,start, mid ,end)));
    }
    
    public int maxSubArray(int[] nums) {
        return findMaxSum(nums, 0, nums.length - 1);
    }
}

class Solution {
    public int maxSubArray(int[] nums) {
        int currentSum = nums[0];
        int maxSum = currentSum;
        for (int i = 1; i < nums.length; i ++){
            if (nums[i] > (currentSum + nums[i])){
                currentSum = nums[i];
            } else{
                currentSum += nums[i];
            }
            if (currentSum > maxSum){
                maxSum = currentSum;
            }
        }
        return maxSum;
    }
}