class Solution {
    public int rob(int[] nums) {
        //if we start robbing at house 0 then we cannot rob house n
        // so we can calculate the rob amount starting at house 0 to n - 1
        // and the amount starting at house 1 to n.
        // then choose the max.
        int length = nums.length;
        int[] robAmount1 = new int [length];
        int[] robAmount2 = new int [length];
        if (length == 0){
            return 0;
        } else if (length == 1){
            return nums[0];
        }
        //calculate rob amount starting at house 1 and end at house n- 1
        robAmount1[0] = 0;
        robAmount1[1] = nums[0];
        for (int i = 2; i < length; i++){
            robAmount1[i] = Math.max(robAmount1[i - 1], robAmount1[i-2] + nums[i- 1]);
        }
        //calculate rob amount starting at house 2 and end at house n
        robAmount2[0] = 0;
        robAmount2[1] = nums[1];
        for (int i = 2; i < length; i++){
            robAmount2[i] = Math.max(robAmount2[i - 1], robAmount2[i-2] + nums[i]);
        }
        return Math.max(robAmount1[length - 1], robAmount2[length - 1]);
        
    }
}