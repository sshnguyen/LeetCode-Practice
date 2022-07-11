class Solution {
    public int rob(int[] nums) {
        int length = nums.length;
        //store the maximum amount money robbed, up to and including to the current house.
        int[] robAmount = new int[length];
        // To calculate the robbed amount of house i, we want to compare and choose the higest
        // between robAmount[i - 2] + nums[i] and robAmount[i- 1];
        
        if (nums == null){
            return 0;
        }
        robAmount[0] = nums[0];
        for (int i = 1; i < length; i++){
            // second element, choose max between the first element and this
            if (i == 1){
                robAmount[i] = Math.max(nums[i - 1], nums[i]);
            }
            else{
                robAmount[i] = Math.max(robAmount[i - 1], robAmount[i - 2] + nums[i]);
            }
        }
        
        return robAmount[length - 1];
        
    }
}