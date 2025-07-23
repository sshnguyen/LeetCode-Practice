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
    boolean flag = true;
    public boolean isBalanced(TreeNode root) {
        if (root == null){
            return flag;
        }
        
        checkBalance(root);
        
        return flag;
    }
    
    public int checkBalance(TreeNode root){
        //dfs, return the height of the TreeNode, while checking for balance conditions
        if(root == null){
            //height is 0
            return 0;
        }
        int leftHeight = checkBalance(root.left);
        int rightHeight = checkBalance(root.right);
        // max height of left or right + 1 is the height of current root.
        int rootHeight = Math.max(leftHeight, rightHeight)+1;
        
        if(Math.abs(leftHeight-rightHeight) >1){
            flag= false;
        }
        return rootHeight;
        
    }
}