"""                                             QUESTION
Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.
You may assume that each input would have exactly one solution, and you may not use the same element twice.
You can return the answer in any order.
                                                EXAMPLE
Input: nums = [2,7,11,15], target = 9
Output: [0,1]
Output: Because nums[0] + nums[1] == 9, we return [0, 1]
                                                SOLUTIONS
To solve by brute force we can iterate through the whole nums list for every number in nums. This way we can
check every possible pair and check if their addition is equals to the target O(n^2).Similarly, we can subtract
a number in nums from target and check if the solution exist in the list. This is still O(N^2) as the in operation
of a list have a complexity of O(n). The optimal solution use similar strategy and but to reduce the in operation
complexity we create and use a set since set is implemented as a hash table and lookup/insert/delete operation
in set is O(1). This way the solution complexity is O(n).

"""

class Solution(object):
    def twoSum(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[int]
        """
        num_set = {}
        for i, num in enumerate(nums):
            num_solution = target - num
            if num_solution in num_set:
                return [i, num_set[num_solution]]
            num_set[num] = i
        