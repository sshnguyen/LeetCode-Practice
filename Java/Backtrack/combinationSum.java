class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<Integer> tempList = new ArrayList<>();
        List<List<Integer>> result = new ArrayList<>();
        
        Arrays.sort(candidates);
        backtrack(candidates, result, tempList, target, 0);
        
        return result;
    }
    
    public void backtrack(int[] candidates,List<List<Integer>> result, List<Integer> tempList, int remaining, int start){
        if (remaining == 0){
            result.add(new ArrayList<>(tempList));
            return;
        }
        for (int i = start; i < candidates.length && candidates[i] <= remaining; i++){
             tempList.add(candidates[i]);
             backtrack(candidates, result, tempList, remaining - candidates[i], i);
             // if it reaches here then then it is not the solution so we should remove it
             tempList.remove(tempList.size()-1);
        }          
    }
}