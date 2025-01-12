class Solution {
    public int[][] floodFill(int[][] image, int sr, int sc, int color) {
        if (image == null){
            return new int[][] {};
        }
        // no need for visited array since the changing of the colors will make sure
        // we don't traverse through the nodes we have visited
        if (color != image[sr][sc]){
            dfsFill( image, sr,  sc, color, image[sr][sc]);
        }
        return image;
    }
    
    public void dfsFill (int[][] image, int sr, int sc, int color, int originalColor){
        if(sr < 0 || sr >= image.length || sc < 0 || sc >= image[sr].length || image[sr][sc] != originalColor){
            return;
        }
        image[sr][sc] =  color;
        
        //up
        dfsFill( image, sr - 1,  sc, color, originalColor);
        //down
        dfsFill( image, sr + 1,  sc, color, originalColor);
        //left
        dfsFill( image, sr,  sc - 1, color, originalColor);
        //up
        dfsFill( image, sr,  sc + 1, color, originalColor);
    }
}