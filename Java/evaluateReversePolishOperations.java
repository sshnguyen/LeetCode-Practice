class Solution {
    public int evalRPN(String[] tokens) {
        //stack, if characters not operations, add to stack
        //if poeration then get 2 last number from stak and apply the operation
        if (tokens == null || tokens.length == 0){
            return 0;
        }
        Stack<Integer> stack = new Stack<>();
        Set<String> ops = new HashSet<>(Arrays.asList("+", "-", "*", "/"));
        for (String token : tokens){
            if (!ops.contains(token)){
                //number
                stack.push(Integer.parseInt(token));
            } else{
                int b = stack.pop();
                int a = stack.pop();
                if (token.equals("+")){
                stack.push(a + b);
                } else if (token.equals("-")){
                    stack.push(a - b);
                } else if (token.equals("*")){
                    stack.push(a * b);
                } else { //divide
                    stack.push(a / b);
                }
            }
        }    
        return stack.pop();
    }
}