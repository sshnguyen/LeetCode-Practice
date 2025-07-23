/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public List<Integer> rightSideView(TreeNode root) {
        //each level can only have 1 node in the solution.
        // we will check reverse inorder traversal, with a queue, and only look for a solution
        // if that level hasn't given a solution yet.
        List<Integer> result = new ArrayList<>();
        if (root == null){
            return result;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int level = 0;
        while (!queue.isEmpty()){
            int levelNodes = queue.size();
            for (int i = 0; i < levelNodes; i++){
                TreeNode currentNode = queue.poll();
                // hasn't found a solution yet since number of solution = level + 1
                if (result.size() == level){
                    result.add(currentNode.val);
                    
                }
                // since we add the right first in the queue, in the next level, it will be 
                //given priority due to First in first out nature of queue
                if (currentNode.right != null){
                    queue.offer(currentNode.right);
                }
                if (currentNode.left != null){
                    queue.offer(currentNode.left);
                }
            }  
            level += 1;
        }
        return result;
    }
}