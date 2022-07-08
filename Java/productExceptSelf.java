class Solution {
    public int[] productExceptSelf(int[] nums) {
        //store the left product. Ex leftProduct[i] = nums[0] * nums[1] * ... * nums[i - 1]
        int[] leftProduct = new int[nums.length];
        //store the left product. Ex rightProduct[i] = nums[n] * nums[n- 1] * ... * nums[i + 1]
        int[] rightProduct = new int[nums.length];
        int[] result = new int[nums.length];
        
        //base case leftProduct[0] = rightProduct[nums.length] = 1 because
        leftProduct[0] = 1;
        rightProduct[nums.length - 1] = 1;
        for (int i = 1; i < nums.length; i++){
            leftProduct[i] = leftProduct[i - 1] * nums[i - 1];
        }
        for (int i = nums.length - 2; i >= 0; i--){
            rightProduct[i] = rightProduct[i + 1] * nums[i + 1];
            result[i] = rightProduct[i] * leftProduct[i];
        }
        result[nums.length - 1] = leftProduct[nums.length - 1];
            
        return result;
    }
}