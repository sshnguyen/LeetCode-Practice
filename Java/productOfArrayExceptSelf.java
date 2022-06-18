class Solution {
    //brute force is for each index, go through the entire array to find the product without that index. obviously o(n^2)
  //solution, find the left product for each of the element as part 1 of the solution. Then find the right product. Then multiply left and right to get the final solution
  public int[] productExceptSelf(int[] nums) {
      int[] result = new int[nums.length];
      result[0] = 1;
      //find the left product for each element
      for (int i = 1; i < nums.length; i ++){
        result[i] = result[i - 1] * nums[i - 1];
      }
      int rightProduct = 1;
      //find the right product for each element
      // and multiply with left product for final result
      for (int i = nums.length - 1; i >= 0; i--){
         result[i] = result[i] * rightProduct;
         rightProduct = rightProduct * nums[i];
      }
      return result;
  }
}