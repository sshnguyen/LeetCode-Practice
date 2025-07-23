class Solution {
    public int[] twoSum(int[] nums, int target) {
        //map to store the the num and its corresponding index in the array
        HashMap<Integer, Integer> indexMap = new HashMap<>();
        int[] result = new int[2];
        
        // iterate through the array, for each num, calculate the difference
        // to target and check previous nums to see if any matches the difference
        
        for (int i = 0; i< nums.length; i++){
            int difference = target - nums[i];
            if (indexMap.get(difference) != null){
                result[0] = i;
                result[1] = indexMap.get(difference);
            }
            indexMap.put(nums[i], i);
        }
        return result;
    }
}