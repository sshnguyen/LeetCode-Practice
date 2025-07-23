class Solution {
    //to brute force, for each element, check the entire array for duplicate 
  //We can add all element into a hashmap. if element already exist then it contains duplicate 
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