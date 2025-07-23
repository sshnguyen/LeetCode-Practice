// Part 2: Design a range-based iterator with a given start, end and step value.
// (1, 10, 2) -> [1,3,5,7,9]

/*CLARIFIFYING QUESTIONS:
1. STEP is input is only postive? We assume negative doesn't make sense since start < end
so negative just mean it doesnt have next. Also 0 step is impossible
*/

import java.util.*;

// Define a custom Iterator interface to avoid conflict with java.util.Iterator
public interface CustomIterator {
    boolean hasNext();
    int next();
}

public class RangeIterator implements CustomIterator {
    private int current;
    private final int end;
    private final int step;

    // Constructor to initialize start, end, and step values
    public RangeIterator(int start, int end, int step) {
        if (step <= 0) {
            throw new IllegalArgumentException("Step value must be greater than zero.");
        }
        
         // CLARIFIYING QUESTION, IF NEGATIVE STEP, WE UPDATE FLIP THE BOUND VALUE AND STEP
         //TO MAKE IT INTO POSITIVE STEP QUESTION
        if (step < 0) {
            int temp = start;
            start = end;
            end = temp;
            step = -step; // Convert step to positive
        }

        this.current = start;
        this.end = end;
        this.step = step;
    }

    @Override
    public boolean hasNext() {
        // Check if the current value is within the range
        return current < end;
    }

    @Override
    public int next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements in range.");
        }
        int value = current;
        current += step;
        return value;
    }
}

public class Main {
    public static void main(String[] args) {
        // Test Case 1: Positive Step
        CustomIterator rangeIterator = new RangeIterator(1, 10, 2);
        while (rangeIterator.hasNext()) {
            System.out.print(rangeIterator.next() + " ");
        }
        // Expected Output: Range Output (1, 10, 2): 1 3 5 7 9
        
        System.out.println();

        // Test Case 2: Single Step
        CustomIterator singleStepIterator = new RangeIterator(1, 5, 1);
        while (singleStepIterator.hasNext()) {
            System.out.print(singleStepIterator.next() + " ");
        }
        // Expected Output: Range Output (1, 5, 1): 1 2 3 4
        
        System.out.println();

        // Test Case 3: Step Larger Than Range
        CustomIterator largeStepIterator = new RangeIterator(1, 10, 20);
        while (largeStepIterator.hasNext()) {
            System.out.print(largeStepIterator.next() + " ");
        }
        // Expected Output: Range Output (1, 10, 20): 1
        
        System.out.println();
    }
}
