//find the number of provinces, provinces are cluster of connected cities.

// THe idea is create a set to store visited cities. Traverse through every city and dfs and store
// all connected cities to it and increase the num of provinces by 1.
// if the next city is in the visited cities then skip, if not then it must mean it's in another province.
class Solution {
    public int findCircleNum(int[][] isConnected) {
        int numProvinces = 0;
        HashSet<Integer> visited = new HashSet<>();
        
        if (isConnected == null){
            return numProvinces;
        }
        
        for (int i = 0; i < isConnected[0].length; i++){
            if (!visited.contains(i)){
                numProvinces += 1;
                dfsConnectedCity(isConnected, i, visited);
            }
        }
        
        return numProvinces;
    }
    
    public void dfsConnectedCity(int[][] isConnected, int currentCity, HashSet<Integer> visited){
        visited.add(currentCity);
        for (int i = 0; i < isConnected[0].length; i++){
            if (isConnected[currentCity][i] == 1 && !visited.contains(i)){
                dfsConnectedCity(isConnected, i, visited);
            }
        }
    }
}