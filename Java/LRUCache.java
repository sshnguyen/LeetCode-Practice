//Design a data structure that remove the least used element when capacity is reached.
//implemented by using double linked list, node hold the key and value, remove the tail element when need to remove and
//add a head element when need to add. When get(), move the node to the head
class Node{
    Node prev;
    Node next;
    int key;
    int value;
    public Node(int key,int value){
        this.key=key;
        this.value=value;
    }
}
class LRUCache {
    HashMap<Integer, Node> map = new HashMap<>();
    Node head=new Node(0,0);
    Node tail=new Node(0,0);
    int capacity;
    public LRUCache(int capacity) {
        this.capacity = capacity;
        head.next=tail;
        head.prev= tail;
        tail.prev=head;
        tail.next = head;
    }
    
    public int get(int key) {
        int nodeValue = -1;
        if (map.containsKey(key)){
            Node node=map.get(key);
            delete(node);
            insert(node);
            nodeValue = node.value;
        }
        return nodeValue;
    }
    
    public void put(int key, int value) {
         if(map.containsKey(key)){
            delete(map.get(key));
        }
        if(map.size()>=capacity){
            delete(tail.prev);
        }
        insert(new Node(key,value));
    }
    
    //utility methods
    public void insert(Node node){
        map.put(node.key,node);
        Node headNext=head.next;
        head.next=node;
        node.prev=head;
        headNext.prev=node;
        node.next=headNext;
    }
    public void delete(Node node){
        map.remove(node.key);
        node.prev.next=node.next;
        node.next.prev=node.prev;
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */