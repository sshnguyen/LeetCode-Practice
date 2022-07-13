class MyQueue {

    // 2 stacks, push go into the first stack, when pop(), or peek(), get the first item from the
    // 2nd stack, if empty then pop all from the first stack into the second.
    // pop all from 1st stack into the 2nd, revert first in last out to first in first out.
    
    Stack<Integer> firstStack;
    Stack<Integer> secondStack;
    public MyQueue() {
        firstStack  = new Stack<Integer>();
        secondStack = new Stack<Integer>();
    }
    
    public void push(int x) {
        firstStack.push(x);
    }
    
    public int pop() {
        //throw exception if both stack is empty
        //pop all the items in first stack into the second stack
        if (secondStack.isEmpty()){
            moveStack();
        }
        return secondStack.pop();
    }
    
    public int peek() {
        //throw exception if both stack is empty
        //pop all the items in first stack into the second stack
        if (secondStack.isEmpty()){
            moveStack();
        }
        return secondStack.peek();
    }
    
    public boolean empty() {
        return (firstStack.isEmpty() && secondStack.isEmpty());
    }
    
    public void moveStack(){
        while(!firstStack.isEmpty()){
            secondStack.push(firstStack.pop());
        }
    }
}

/**
 * Your MyQueue object will be instantiated and called as such:
 * MyQueue obj = new MyQueue();
 * obj.push(x);
 * int param_2 = obj.pop();
 * int param_3 = obj.peek();
 * boolean param_4 = obj.empty();
 */