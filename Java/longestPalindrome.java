class Solution {
    public int longestPalindrome(String s) {
        //hashmap to store the odd occurance of a character in s
        //if it occur again, then remove it from the hashmap
        
        // after the entire s is traversed, everything that's left in the map is left over
        // odd chaaracters that cannot be used in a palindrome, except 1 that can be used in the middle so the result is calculated by length of s - length of map + 1.
        //if the array is empty then everything in s can be used.
        
        HashSet<Character> oddLetters = new HashSet<>();
        
        for (char letter : s.toCharArray()){
            if (oddLetters.contains(letter)){
                oddLetters.remove(letter);
            } else{
                oddLetters.add(letter);
            }
        }
        if (oddLetters.size() == 0){
            return s.length();
        }
        return s.length() - oddLetters.size() + 1;
    }
}