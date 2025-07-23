class Solution {
    public boolean isValid(String s) {
        // mapping of closing parenthese to opening parenthese
        HashMap<Character, Character> characterMap = new HashMap<>(){{
            put(')', '(');
            put('}', '{');
            put(']', '[');
        }};
        
        // ex ,({})[], we see that open parenthese must have reverse order of closing parenthese
        // for it to be valid. The solution is that we add opening parenthese to a stack,
        // when we get a closing parentheses then we pop from the stack to see if it matches.
        
        Stack<Character> bracketStack = new Stack<>();
        
        for (char bracket : s.toCharArray()){
            // if the character is a closing bracket
            if (characterMap.containsKey(bracket)){
                // pop from the stack and compare
                if (bracketStack.isEmpty() || characterMap.get(bracket) != bracketStack.pop()){
                    return false;
                }
            } else{
                bracketStack.push(bracket);
            }
        }
        
        // if bracketStack is not empty then there's more number of opening bracket than closing
        return bracketStack.isEmpty();
    }
}