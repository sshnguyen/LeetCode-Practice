class Solution {
    public boolean isAnagram(String s, String t) {
        // for each character, count the number of occurance and add it to map.
        // check if the map of both s and t is the same.
        
        if (s == null || t == null || s.length() != t.length()){
            return false;
        }
        
        HashMap<Character, Integer> sCharsMap = new HashMap<>();
        HashMap<Character, Integer> tCharsMap = new HashMap<>();
        for (int i = 0; i < s.length(); i++){
            sCharsMap.put(s.charAt(i), sCharsMap.getOrDefault(s.charAt(i), 0) + 1);
            tCharsMap.put(t.charAt(i), tCharsMap.getOrDefault(t.charAt(i), 0) + 1);
        }
        
        if (!sCharsMap.equals(tCharsMap)){
            return false;
        }
            
        return true;
    }
}