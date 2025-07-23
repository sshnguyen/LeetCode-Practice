class Solution {
    public int majorityElement(int[] nums) {
        // hashmap store num and its number of occurances, if ocurrances > n/2 return,
        //O(n) time, O(n) space
        
        //sort the array and return the element at mid. Since there's exist a num that have
        // > n/2 occurance then it must be at mid.
        //O(nlogn) time, O(1) space
        
        //iterate through the array once, keep a counter that will be use to store an
        //ocurrance of num. 
        
        int count = 0;
        int result = 0;
        for (int num : nums){
            if (count == 0){
                result = num;
                count += 1;
            } else if (result == num){
                count += 1;
            } else{
                count -= 1;
            }
        }
        return result;
    }
}