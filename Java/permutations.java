class Solution {
    public List<List<Integer>> permute(int[] nums) {
        List<Integer> tempList = new ArrayList<>();
        List<List<Integer>> result = new ArrayList<>();
        
        backtrack(nums, result, tempList);
        return result;
    }
    
    public void backtrack (int[] nums, List<List<Integer>> result, List<Integer> tempList){
        // only add to result when all of the elements are accounted for
        if (tempList.size() == nums.length){
            result.add(new ArrayList<>(tempList));
        } else{
            for (int i = 0; i < nums.length; i++){
                if (!tempList.contains(nums[i])){
                    tempList.add(nums[i]);
                    backtrack(nums, result, tempList);
                    tempList.remove(tempList.size() - 1);
                }
            }
        }
    }
}