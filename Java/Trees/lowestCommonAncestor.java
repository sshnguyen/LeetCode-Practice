/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if( root == p || root == q || root == null)
            return root;
        // LCA has have one child in each of the left, and right subtree. So check both side
        // if they contains it.
        TreeNode left = lowestCommonAncestor( root.left,  p,  q);
        TreeNode right = lowestCommonAncestor( root.right,  p,  q);
        if(left == null)
            return right;
        else if (right == null)
            return left;
        else
            // if they exist in both
            return root;
        
    }
}