class Solution {
    
    public int dist(int[] point){
        return point[0]*point[0] + point[1]*point[1];
    }
    public int[][] kClosest(int[][] points, int k) {
//         // Handle null/empty edge cases
//         if (points == null || points.length == 0) return new int[0][0];

//         if (k >= points.length) return points;

//         //sorting ascending distance then choose first k elements
//         Arrays.sort(points, (p1, p2) -> {  // comparator
//             return dist(p1) - dist(p2); //
//         });
//         int[][] result = new int[k][2];
//         for (int i = 0; i < k; i++){
//             result[i] = points[i];
//         }
//         return result;
        // (p1, p2) -> {return p1 - p2} returns min heap
        // (p1, p2) -> {return p2 - p1} returns max heap
        PriorityQueue<int[]> pq = new PriorityQueue<>((p1, p2) ->{
            return dist(p2) - dist(p1);
        });
        
        for (int[] point : points){
            // by creating a max heap using pq, we can poll the biggest item, and
            // keep the heaps at O(k) instead of O(n) if we were to add everything
            pq.offer(point);
            if (pq.size() > k){
                pq.poll();
            }
        }
        int[][] result = new int[k][2];
        
        for (int i = 0; i<k; i++){
            result[i] = pq.poll();
        }
        return result;
    }
}