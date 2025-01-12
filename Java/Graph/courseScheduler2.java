class Solution {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List<Integer> order = new ArrayList<>();
        // adj list
        ArrayList<Integer>[] adjList = new ArrayList[numCourses];
        // initialize an empty adjList for every course
        for (int i = 0; i < numCourses; i++){
            adjList[i] = new ArrayList<>();
        }
        // add prerequisites of each course to its adj list
        for (int[] prerequisite : prerequisites){
            adjList[prerequisite[0]].add(prerequisite[1]);
        }
        
        // 3 state, 0 = haven't visit, 1 = visting, 2 = visited.
        // so that we can keep track of courses we already visited and don't add again
        int[] visited = new int[numCourses];
        //traverse DFS checking for cycle
        for (int i = 0; i < numCourses; i ++){
            if (hasCycle(i, adjList, visited, order)){
                return new int[]{};
            }
        }
        int[] result = new int[numCourses];
        for (int i = 0 ; i < order.size(); i ++){
            result[i] = order.get(i);
        }
        return result;
    }
    
    public boolean hasCycle(int index, ArrayList<Integer>[] adjList, int[] visited, List<Integer> order){
        //already visited means it already checked for cycle and already added to order
        if (visited[index] == 2){
            return false;
        }
        if (visited[index] == 1){
            return true;
        }
        visited[index] = 1;
        //check the nodes in adjList
        for (int node : adjList[index]){
            if (hasCycle(node, adjList, visited, order)){
                return true;
            }
        }
        visited[index] = 2;
        order.add(index);
        return false;
        
    }
}