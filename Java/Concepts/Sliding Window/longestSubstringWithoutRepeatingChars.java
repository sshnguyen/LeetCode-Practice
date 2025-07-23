class Solution {
    public int lengthOfLongestSubstring(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Input string is null");
        }

        int len = s.length();
        if (len <= 1) {
            return len;
        }
        //map a character to where they are last found
        HashMap<Character, Integer> charPosition = new HashMap<>();
        int maxLength = 0;
        //holding the start position of our sliding window
        int start = 0;
        
        //end position of our sliding indow
        for (int end = 0; end < len; end++){
            //get current character
            char currentChar = s.charAt(end);
            // if the check if the current char already appear,
            // if it is, update start of the sliding window if the current character appear
            //after the start position .
            if (charPosition.containsKey(currentChar)){
                start = Math.max(start, charPosition.get(currentChar) + 1);
            }
            maxLength = Math.max(maxLength, end - start + 1);
            charPosition.put(currentChar, end);
        }
        return maxLength; 
    }
}