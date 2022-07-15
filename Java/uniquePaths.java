class Solution {
    public int uniquePaths(int m, int n) {
        //store unique paths reaching certain grid
        int[][] paths = new int[m][n];
        for (int i = 0; i < m; i++){
            for (int j = 0; j < n; j++){
                //topmost row and leftmost column can only be reach via straight line so
                // only 1 way
                if (i == 0 || j == 0){
                    paths[i][j] = 1;
                }
                else{
                    paths[i][j] = paths[i][j - 1] + paths[i - 1][j];
                }
            }
        }
        
        
        return paths[m - 1][n - 1];
    }
}