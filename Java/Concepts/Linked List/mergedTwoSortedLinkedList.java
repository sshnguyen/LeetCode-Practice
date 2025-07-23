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
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        //create a linked list to store solution
        ListNode head = new ListNode();
        ListNode currentNodeSolution = head;
        ListNode currentNode1 = list1;
        ListNode currentNode2 = list2;
        while(currentNode1 != null && currentNode2 != null){
            if(currentNode1.val <= currentNode2.val){
                currentNodeSolution.next = currentNode1;
                currentNode1 = currentNode1.next;
            } else{
                currentNodeSolution.next = currentNode2;
                currentNode2 = currentNode2.next;
            }
            currentNodeSolution = currentNodeSolution.next;
        }
        while (currentNode2 != null){
                currentNodeSolution.next = currentNode2;
                currentNodeSolution = currentNodeSolution.next;
                currentNode2 = currentNode2.next;
        }
        while (currentNode1 != null){
            currentNodeSolution.next = currentNode1;
            currentNodeSolution = currentNodeSolution.next;
            currentNode1 = currentNode1.next;
        }
        return head.next;
    }
}