class MinStack {
    //stack with a constant getMin operation.
    Stack<Integer> stack;
    // store the order of minimum in the stack as they appear. add new minimum to the top
    // if they are < previous minimum.
    Stack<Integer> minimumStack;

    public MinStack() {
        stack = new Stack<>();
        minimumStack = new Stack<>();
    }

    public void push(int val) {
        if (minimumStack.isEmpty() || val <= minimumStack.peek()){
            minimumStack.push(val);
        }
        stack.push(val);
    }

    public void pop() {
        //pop the stack and also pop the minimum stack if it's the same item
        if (stack.peek().equals(minimumStack.peek())){
            minimumStack.pop();
        }
        stack.pop();
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return minimumStack.peek();
    }
}

/**
 * Your MinStack object will be instantiated and called as such:
 * MinStack obj = new MinStack();
 * obj.push(val);
 * obj.pop();
 * int param_3 = obj.top();
 * int param_4 = obj.getMin();
 */