// Part 1: Design and code an interleaving iterator. Given 2 arrays output the elements in interleaving order.
// array1 = [1,2,3] and array2 = [4,5,6] -> result = [1,4,2,5,3,6]


//PART 1:

import java.util.*;

// Define a custom Iterator interface to avoid conflict with java.util.Iterator
interface CustomIterator {
    boolean hasNext();
    int next();
}

class InterleavingIterator implements CustomIterator {
    // Stores the input lists
    private List<List<Integer>> lists = new ArrayList<>();
    // Pointer to track which list we are currently on
    private int listPointer = 0;
    // Array to track the current element in each list
    private int[] elementPointers;
    // Total number of elements across all lists
    private int totalElements = 0;
    // Number of elements returned so far
    private int returnedElements = 0;

    // Constructor to initialize the iterator with two lists
    public InterleavingIterator(List<Integer> list1, List<Integer> list2) {
        this.lists.add(list1);
        this.lists.add(list2);

        // Initialize the element pointers array
        this.elementPointers = new int[this.lists.size()];

        // Calculate total number of elements across all lists
        for (List<Integer> list : this.lists) {
            this.totalElements += list.size();
        }
    }

    @Override
    public boolean hasNext() {
        // Check if the total elements returned is less than total elements available
        return this.returnedElements < this.totalElements;
    }

    @Override
    public int next() {
        // If no more elements, throw exception as per Iterator contract
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements to iterate.");
        }

        // Loop through the lists in a round-robin fashion
        while (true) {
            List<Integer> currentList = this.lists.get(this.listPointer);

            // Check if the current list has more elements
            if (this.elementPointers[this.listPointer] < currentList.size()) {
                int nextValue = currentList.get(this.elementPointers[this.listPointer]);
                this.returnedElements++;

                // Move to the next element in the current list
                this.elementPointers[this.listPointer]++;

                // Move to the next list for the next call
                this.listPointer = (this.listPointer + 1) % this.lists.size();

                return nextValue;
            } else {
                // Move to the next list if the current list is exhausted
                this.listPointer = (this.listPointer + 1) % this.lists.size();
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        //2 list
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(4, 5, 6, 7);

        InterleavingIterator iterator = new InterleavingIterator(list1, list2);

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        //this approach should also work with k list, just need to add constructor to take
    //     public InterleavingIterator(List<Integer> ... lists) {
    //    for (List<Integer> list : lists) {
    //         this.lists.add(list);
    }
}