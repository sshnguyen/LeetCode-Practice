class Solution {
    //iterate through the entire matrix, store all the column and rows that will be be turn to 0s
    //then iterate through the entire matrix again, for each element check if its column or
    //row index is in the set of the stored 0s indexes. if it is set to 0.
    public void setZeroes(int[][] matrix) {
        HashSet<Integer> zeroesRows = new HashSet<>();
        HashSet<Integer> zeroesColumns = new HashSet<>();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0){
            return;
        }
        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[0].length; j++ ){
                if (matrix[i][j] == 0){
                    zeroesRows.add(i);
                    zeroesColumns.add(j);
                }
            }
        }
        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[0].length; j++ ){
                if (zeroesRows.contains(i) || zeroesColumns.contains(j)){
                    matrix[i][j] = 0;
                }
            }
        }
            
    
    }
}