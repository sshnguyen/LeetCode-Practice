class Solution {
    public int[][] merge(int[][] intervals) {
        if (intervals.length <= 1) {
			return intervals;
        }
        // we sort the array by ascnding start time:
        Arrays.sort(intervals, (a, b) -> {
            return a[0] - b[0];
        });
        
        List<int[]> result = new ArrayList<>();
        for (int[] interval : intervals){
            //if result is empty or the start date of this new interval is > last added interval then add
            if (result.isEmpty() || result.get(result.size() - 1)[1] < interval[0]){
                result.add(interval);
            } else{ //update the end date of the last added interval
                result.get(result.size() - 1)[1] = Math.max(result.get(result.size() - 1)[1] ,interval[1]); 
            }
        }
        return result.toArray(new int[result.size()][2]);
    }
}