'''                                            QUESTION            
Given n non-negative integers a1, a2, ..., an , where each represents a point at
coordinate (i, ai). n vertical lines are drawn such that the two endpoints of the line i is at (i, ai) and (i, 0).
Find two lines, which, together with the x-axis forms a container, such that the container contains the most water.
                                                EXAMPLES
Input: height = [1,8,6,2,5,4,8,3,7]
Output: 49

Input: height = [4,3,2,1,4]
Output: 16

                                                SOLUTION
Since the minimum of the two end points set the maximum area if we start the two end points (e1, e2)
at the start and end of the array, we only need to update the lower
of the two end points when one of the index in between is higher. This is O(n) complexity.
'''
class Solution(object):
    def maxArea(self, height):
        """
        :type height: List[int]
        :rtype: int
        """
        e1 = 0
        e2 = len(height) - 1
        max_area = min(height[e1], height[e2]) * (e2 - e1)
        while (e1 < e2):
            if (height[e1] < height [e2]):
                e1 += 1
            else:
                e2 -= 1
            
            max_area = max(max_area, min(height[e1], height[e2]) * (e2 - e1))
        return max_area