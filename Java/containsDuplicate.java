class Solution {
    public boolean containsDuplicate(int[] nums) {
      //hash set to store unique elements
      HashSet<Integer> elementSet = new HashSet<>();
      for (int num : nums){
        //check if the current element is unique
        if (elementSet.contains(num)){
          return true;
        } else {
          elementSet.add(num);
        }
      }
      return false;
  }
}