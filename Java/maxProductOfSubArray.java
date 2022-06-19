class Solution {
    // for each element, we can look into the rest of the array for two other elements that all add to 0
    // This will be turn into 2 sum question.To do this, we sort the list at the begining,
    // then for each element, look for the two elements after that index as the
    // other two numbers has to be bigger since we sorted the list.
    // If the element is positive then the solution does not exist in a sorted array.
    // * we need to skips duplicate elements after it's been added as a solution once
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> solution =new ArrayList<>();
        if (nums.length < 3){
            return solution;
        }
        //sort the list to be use later
        Arrays.sort(nums);
        //for each element, turn the question into 2 sum question
        for (int i = 0; i < nums.length - 1; i ++){
            if (nums[i] > 0){
                    return solution;
            }
            //skip elements that we already worked with
            if(i==0||nums[i]!=nums[i-1]){
                int target = nums[i];
                int leftPointer = i + 1;
                int rightPointer = nums.length - 1;
                while(leftPointer < rightPointer){
                    if (nums[leftPointer] + nums[rightPointer] + target > 0){
                        rightPointer --;
                    } else if (nums[leftPointer] + nums[rightPointer] + target < 0){
                        leftPointer ++;
                    } else{
                        solution.add(Arrays.asList(nums[i],nums[leftPointer],nums[rightPointer]));
                        //we skip duplicate elements
                        while(leftPointer < rightPointer&&nums[leftPointer]==nums[leftPointer+1])leftPointer++;
                        while(leftPointer < rightPointer&&nums[rightPointer]==nums[rightPointer-1])rightPointer--;
                        rightPointer --;
                        leftPointer ++;
                    }
                }
            }
        }
        
        return solution;
    }
}