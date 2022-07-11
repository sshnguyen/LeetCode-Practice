class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        //think of the prerequisite like directed a graph, [a,b] as a edge, a -> b
        //create adjacency list,
        ArrayList<Integer>[] adjList = new ArrayList[numCourses];
        
        //fill the adjacency list
        for (int i = 0; i < numCourses; i ++){
            adjList[i] = new ArrayList<>();
        }
        for (int[] prerequisite : prerequisites){
            adjList[prerequisite[0]].add(prerequisite[1]);
        }
        
        //loop through the adj list of each course and check dfs to see if a cycle exist.
        boolean[] visited = new boolean[numCourses];
        for (int i = 0; i < numCourses; i ++){
            if (hasCycle(i, adjList, visited)){
                return false;
            }
        }
        return true;
    }
    
    public boolean hasCycle(int index, ArrayList<Integer>[] adjList, boolean[] visited){
        // no edges means cycle is not possible
        if (adjList[index].size() == 0){
            return false;
        }
        // if node already visited then a cycle
        if (visited[index] == true){
            return true;
        }
        visited[index] = true;
        //dfs search all nodes in adj list
        for (int node : adjList[index]){
            if (hasCycle(node, adjList, visited)){
                return true;
            }
        }
        //no cycle, we can take this course so clear its adj list.
        adjList[index].clear();
        
        return false;
    }
}