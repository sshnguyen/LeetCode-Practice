/* The isBadVersion API is defined in the parent class VersionControl.
      boolean isBadVersion(int version); */

public class Solution extends VersionControl {
    //we can traverse from 0 up to n and check for the first bad version, in O(n)
    // but binary search can be use for O(log n)
    public int firstBadVersion(int n) {
        int start = 1;
        int end = n;
        
        while (start != end){
            int mid = start + (end - start)/2;
            // then the bad version exist in the first half
            if (isBadVersion(mid)){
                end = mid;
            } else{ // first bad version exist in the second half
                start = mid + 1;
            }
        }
        return start;
    }
}