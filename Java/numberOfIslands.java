// the idea is create a boolean array, and as we traverse to new grid that
// where grid[i][j] == 1 and haven't been visited, then we update the count by 1
//and update visited[i][j] to true, and also update visited[i + 1][j],visited[i - 1][j],visited[i][j + 1],visited[i1][j- 1]
class Solution {
    public void traverse(boolean[][] visited, char[][] grid, int i ,int j){
        if (i < 0 || i >= grid.length || j < 0 || j>=grid[0].length || grid[i][j] == '0' || visited[i][j] == true){
            return;
        }
        visited[i][j] = true;
        //traverse all 4 directions surrounding the current grid
        traverse(visited, grid, i + 1, j);
        traverse(visited, grid, i - 1, j);
        traverse(visited, grid, i, j + 1);
        traverse(visited, grid, i, j - 1);
    }
    public int numIslands(char[][] grid) {
        int gridLength = grid.length;
        int gridWidth = grid[0].length;
        int numIslands = 0;
        boolean[][] visited = new boolean[gridLength][gridWidth];
        for (int i = 0; i < gridLength; i++){
            for (int j = 0; j < gridWidth; j++){
                if (grid[i][j] == '1' && visited[i][j] == false){
                    numIslands += 1;
                    traverse(visited, grid, i, j);
                }
            }
        }
        return numIslands;
    }
}