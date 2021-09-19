# Simple O(n) greedy solution, choose the best jump and move the index counter
# to the end of that jump
class Solution(object):
    def jump(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if (len(nums) == 1):
            return 0
        jumps = 1
        curr_index = 0
        next_index = nums[0]
        while next_index < len(nums) - 1:
            best_jump = max(nums[i] + i for i in range(curr_index + 1, next_index + 1))
            jumps += 1
            curr_index = next_index
            next_index = best_jump
        return jumps