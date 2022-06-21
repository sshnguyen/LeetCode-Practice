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
 // using queue to add all items of this level, while creating the next level
class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> solution = new ArrayList<>();
        if (root == null){
            return solution;
        }
        //create a queue for each level of tree
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);
        while (!queue.isEmpty()){
            // make a list to store nodes at this level
            List<Integer> levelList = new ArrayList<>();
            int size = queue.size();
            for (int i = 0; i < size; i++){
                if(queue.peek().left != null) queue.offer(queue.peek().left);
                if(queue.peek().right != null) queue.offer(queue.peek().right);
                levelList.add(queue.poll().val);
            }
            solution.add(levelList);
        }
        return solution;
    }
}