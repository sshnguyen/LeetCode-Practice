class Solution {
    public boolean canConstruct(String ransomNote, String magazine) {
        if(ransomNote.length()>magazine.length()) return false;
	    if(ransomNote.isEmpty() && magazine.isEmpty()) return true;
        // store all the letters in magazine in a hashMap, and their count.
        HashMap<Character, Integer> magazineLetters = new HashMap<>();
        
        // add all the letters of magazine to the set
        for (char letter: magazine.toCharArray()){
            magazineLetters.put(letter, magazineLetters.getOrDefault(letter, 0) + 1);
        }
        //check if any of ltter in ransom letter does not exist in magazine, or already used up 
        for (char letter: ransomNote.toCharArray()){
            // if count is 0 then it doesn't exist, or already used up
            if (magazineLetters.getOrDefault(letter, 0) == 0){
                return false;
            }
            // if exist, decrease the count by 1
            magazineLetters.put(letter, magazineLetters.get(letter) - 1);
            
        }
        
        return true;
    }
}