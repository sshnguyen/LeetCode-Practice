class Solution {
    public int[][] insert(int[][] intervals, int[] newInterval) {
         List<int[]> result = new ArrayList<>();
        
        // Handling edge case when intervals is empty
        if(intervals.length == 0){
            if(newInterval.length != 0)
                result.add(newInterval);
            return result.toArray(new int[result.size()][]);
        }
        
        int i=0;
        
        // 1. Copy until overlap is not found
        while(i < intervals.length && newInterval[0] > intervals[i][1])
            result.add(intervals[i++]);
        
        // 2. Update newInterval by comparing it with overlapped intervals
        while(i < intervals.length && newInterval[1] >= intervals[i][0]){
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        
        // 3. Add the updated newInterval
        result.add(newInterval);
        
        // 4. Copy the remaining in the intervals list to the final result
        while(i < intervals.length)
            result.add(intervals[i++]);
        
        return result.toArray(new int[result.size()][]);
    }
}