// Solution in O(n^2), iterate through each character and check the length of
//the palindrome as if that character is the center. Return the maximum length
//we have found
class Solution {
    int start = 0;
    int end = 0;
    int tmpStart;
    int tmpEnd;
    public String longestPalindrome(String s) {
        for (int i = 0; i < s.length(); i++){
            tmpStart = i;
            tmpEnd = i;
            char centerChar = s.charAt(i);
            //for cases where the centre of the palindrome is duplicates
            //start and end at a new character to the left and right of center
            while(tmpStart >= 0 && s.charAt(tmpStart) == centerChar){
                tmpStart --;
            }
            while(tmpEnd < s.length() && s.charAt(tmpEnd) == centerChar){
                tmpEnd ++;
            }
            while (tmpStart >= 0 && tmpEnd < s.length()){
                if (s.charAt(tmpStart) == s.charAt(tmpEnd)){
                    tmpStart--;
                    tmpEnd++;
                } else {
                    break;
                }
                
            }
            // start and end is not part of the palindrome,
            // Start can be as low as -1, and end can be as high as s.length()
            // End can be use as subString parameter but Start needs to be modified.
            tmpStart ++;
            if (tmpEnd - tmpStart > end - start){
                start = tmpStart;
                end = tmpEnd;
            }
        }
        return s.substring(start, end);
    }
}