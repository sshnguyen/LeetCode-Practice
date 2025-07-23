class Solution {
    public int orangesRotting(int[][] grid) {
        //bfs, using a queue, store location of rotten orange we havn't deal with each day
        //for each rotten, check in 4 direction, if a fresh orange is found, add
        // it to the queue for the next day.
        Queue<int[]> queue = new LinkedList<>();
        int minutes = 0;
        if (grid == null){
            return 0;
        }
        //grid dimensions
        int rowLength = grid.length;
        int colLength = grid[0].length;
        // iterate once through the entire grid to get initial rotten oranges
        for (int i = 0; i < rowLength; i++){
            for (int j = 0; j < colLength; j++){
                if (grid[i][j] == 2){
                    queue.offer(new int[] {i, j});
                }
            }
        }
        int[][] directions = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!queue.isEmpty()){
            int numRotten = queue.size();
            for (int i = 0; i < numRotten; i ++){
                int[] location = queue.poll();
                // check 4 direction for fresh orange
                for (int[] dir : directions){
                    int row = location[0] + dir[0];
                    int col = location[1] +dir[1];
                    if (row >= 0 && col >= 0 && row < rowLength && col < colLength && grid[row][col] == 1){
                        //turn it to rotten, and add to the queue
                        grid[location[0] + dir[0]][location[1] +dir[1]] = 2;
                        queue.offer(new int[]{location[0] + dir[0],location[1] +dir[1]});
                    }
                }
            }
            // don't add the last day
            if(!queue.isEmpty())
                minutes++;
            }
        // check after bfs to see if any orange is still fresh.
        for (int i = 0; i < rowLength; i++){
            for (int j = 0; j < colLength; j++){
                if (grid[i][j] == 1){
                   return -1;
                }
            }
        }
        return minutes;
    }
}