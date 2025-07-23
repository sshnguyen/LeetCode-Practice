"""
                                                QUESTION
Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.
The overall run time complexity should be O(log (m+n))
                                                EXAMPLES
Input: nums1 = [1,3], nums2 = [2]
Output: 2.00000

Explanation: merged array = [1,2,3] and median is 2.
Input: nums1 = [1,2], nums2 = [3,4]
Output: 2.50000

Explanation: merged array = [1,2,3,4] and median is (2 + 3) / 2 = 2.5.
Input: nums1 = [0,0], nums2 = [0,0]
Output: 0.

Input: nums1 = [], nums2 = [1]
Output: 1.00000

Input: nums1 = [2], nums2 = []
Output: 2.00000
                                                SOLUTIONS
This solution is not O(log(m+n)) but instead O(m+n). Couldn't think of any partition solutions.
Iterate through the two list and find the median that is the length of the two list / 2.
for odd length get the median at exactly (nums1 + num2 length) / 2 and for even length get the average
of the value at (nums1 + num2 length) / 2 - 1 and (nums1 + num2 length) / 2 index.
"""

class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        double firstMedian = 0;
        double secondMedian = 0;
        int nums1IndexTracker = 0;
        int nums2IndexTracker = 0;
        int mergedLength = (nums1.length + nums2.length);
        for (int i = 0; i <= mergedLength/2; i++){
            if (mergedLength%2 == 0){
                secondMedian = firstMedian;
            }
            if (nums1.length == 0 || nums1.length == nums1IndexTracker){
                firstMedian = nums2[nums2IndexTracker];
                nums2IndexTracker++;
            }
            else if (nums2.length == 0 || nums2.length == nums2IndexTracker){
                firstMedian = nums1[nums1IndexTracker];
                nums1IndexTracker++;
            }
            else {
                if(nums2[nums2IndexTracker] <= nums1[nums1IndexTracker]){
                    firstMedian = nums2[nums2IndexTracker];
                    nums2IndexTracker++;
                }
                else{
                    firstMedian = nums1[nums1IndexTracker];
                    nums1IndexTracker++;
                }
            }
        }
        if (mergedLength%2 == 0){
            return (firstMedian + secondMedian)/ (double)2;
        }
        return firstMedian;
    }
}