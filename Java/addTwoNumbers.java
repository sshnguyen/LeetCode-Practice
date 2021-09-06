"""
                                                QUESTION
You are given two non-empty linked lists representing two non-negative integers.
The digits are stored in reverse order, and each of their nodes contains a single digit.
Add the two numbers and return the sum as a linked list.
You may assume the two numbers do not contain any leading zero, except the number 0 itself.
                                                EXAMPLE
Input: l1 = [2,4,3], l2 = [5,6,4]
Output: [7,0,8]
Explanation: 342 + 465 = 807.
                                                SOLUTIONS
Start from the head of each of the link list and creating the new result linked list based on the addition of
the two items at those indices. If the result of the two digits are > 10 then the result = %10 and keep track
of carry variable to be added to the next index. Notice the cases where the two linked list have different length,
and at some index there would be no two digits addition. The resulting index would have the same value as the
longer linked list (need to add carry variable). This solution run in O(n).

"""
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
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head = new ListNode() {};
        int carry = 0;
        ListNode left = l1;
        ListNode right = l2;
        ListNode current = head;
        int num;
        int leftValue;
        int rightValue;
        while(left!= null || right != null || carry == 1){
            leftValue = 0;
            rightValue = 0;
            if (left != null){
                leftValue = left.val;
                left = left.next;
            }
            if (right != null){
                rightValue = right.val;
                right = right.next;
            }
            num = leftValue + rightValue + carry;
            carry = num/10;
            current.next =  new ListNode(num%10);
            current = current.next;
        }
        return head.next;
    }
}