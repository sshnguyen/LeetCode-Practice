/**
 * Definition for singly-linked list.
 * class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
public class Solution {
    public boolean hasCycle(ListNode head) {
        //Set to store the unique ListNodes that we have traverse past.
        // if we visit a node already in the set then it has a cycle
        HashSet<ListNode> visitedNodes = new HashSet<>();
        
        while (head != null){
            if (visitedNodes.contains(head)){
                return true;
            }
            visitedNodes.add(head);
            head = head.next;
        }
        return false;
        
    }
    // slow and fast pointer, if they ever meet before fast pointer reach the end then it has
    // a cycle.
    public boolean hasCycle(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while(fast != null && fast.next != null){            
            fast = fast.next.next; //2 times
            slow = slow.next;
            if(fast == slow) return true;
        }
        return false;
    }
}