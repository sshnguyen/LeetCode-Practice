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
    public boolean isSameTree(TreeNode p, TreeNode q) {
        boolean same = false;
        if ((q == null) && (p == null)){
            return true;
        }if (((q == null) && (p != null)) || ((q != null) && (p == null))){
            return false;
        } else if (q.val == p.val){
            same = true && isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
        }
        return same;
    }
}