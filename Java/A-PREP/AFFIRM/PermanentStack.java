/*
CREATE A PERSISTENT STACK WHERE EACH VERSION AFTER POP AND PUSH IS IMMUTABLE.

CLARIFYING QUESTION:
How do we define persistent in the context of this question?

SOLUTION DISCUSSION:
We can use a stack and deque to do this, and for each version, we would need to copy the entire stack on each push or pop to preserve its previous versions. O(n) push, O(n) pop because we need to deep copy

* We can use linkedlist, which is a better solution because we don't have to copy the entire stack, but create a node and point it to the
previous version's  top node. For pop, we can return the stack with the previous node of the top node.
O(1), O(1).


*/


class PersistentStack<T> {

    // Internal Node class (immutable)
    private static class Node<T> {
        final T value;
        final Node<T> prev;

        Node(T value, Node<T> prev) {
            this.value = value;
            this.prev = prev;
        }
    }

    // The top of this version of the stack
    private final Node<T> top;

    // Constructor for an empty stack
    public PersistentStack() {
        this.top = null;
    }

    // Constructor used internally to create new versions
    private PersistentStack(Node<T> top) {
        this.top = top;
    }

    // Push returns a new stack version with new top
    public PersistentStack<T> push(T value) {
        return new PersistentStack<>(new Node<>(value, this.top));
    }

    // Pop returns a new stack version with top removed
    public PersistentStack<T> pop() {
        if (this.isEmpty()) throw new IllegalStateException("Cannot pop from empty stack");
        return new PersistentStack<>(this.top.prev);
    }

    // Peek returns the value at the top
    public T peek() {
        if (this.isEmpty()) throw new IllegalStateException("Cannot peek from empty stack");
        return this.top.value;
    }

    // Check if the stack is empty
    public boolean isEmpty() {
        return this.top == null;
    }

    // For debugging - print stack from top to bottom
    public void print() {
        Node<T> current = top;
        while (current != null) {
            System.out.print(current.value + " -> ");
            current = current.prev;
        }
        System.out.println("null");
    }
}

public class Main {
    public static void main(String[] args) {
        PersistentStack<Integer> version1 = new PersistentStack<>();
        PersistentStack<Integer> version2 = version1.push(10);
        PersistentStack<Integer> version3 = version2.push(20);
        PersistentStack<Integer> version4 = version3.push(30);

        System.out.println("Version 4:");
        version4.print();  // 30 -> 20 -> 10 -> null

        System.out.println("Version 3:");
        version3.print();  // 20 -> 10 -> null

        System.out.println("Version 2:");
        version2.print();  // 10 -> null
    }
}
