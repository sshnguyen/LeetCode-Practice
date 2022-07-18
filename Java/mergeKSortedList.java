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
    public ListNode mergeKLists(ListNode[] lists) {
        ListNode result = new ListNode(0);
        
        if (lists == null || lists.length == 0){
            return result;
        }
        //priority queue to implement a min heap.
        PriorityQueue<ListNode> queue = new PriorityQueue<>( (a, b) -> {
            return a.val - b.val;
        });
        
        for (ListNode node: lists){
            if (node!=null){
                queue.offer(node);
            }
        }
        ListNode curr = result;
        while (!queue.isEmpty()){
            ListNode next = queue.poll();
            curr.next = next;
            curr = curr.next;
            // we want to add the nd list back to queue if the item we process if not the
            // last item of that Nodelist
            if (curr.next!=null){
                queue.add(curr.next);
            }
        }
        
        return result.next;
    }
}