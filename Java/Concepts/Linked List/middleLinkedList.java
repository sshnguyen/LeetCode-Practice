/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode middleNode(ListNode head) {
        // using slow and fast pointers, we can check for cycle.
        // if doesn't contain a cycle then slow pointer point to mid.
        if (head == null){
            return head;
        }
        ListNode slowPointer = head;
        ListNode fastPointer = head;
        
        while (fastPointer != null && fastPointer.next != null){
            slowPointer = slowPointer.next;
            fastPointer = fastPointer.next.next;
            //equals means there's a cycle
            if (slowPointer == fastPointer){
                return null;
            }
        }
        return slowPointer;
    }
}