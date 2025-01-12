//store all character of string in hashmap with the character being the key and the value start at 1
//add 1 to the value if the key already exist in map
//loop through each character in the list, and lookup the hashmap for the value of each character
//return the index of the character where the value is 1 in the hashmap.
//O(n) for constructing the map, O(1) space since there is a fix number of character in the alphabet
//overall O(n) runtime
class Solution {
    public static int firstUniqChar(String input){
        Map<Character, Integer> s = new HashMap<>();
        for (char b : input.toCharArray()){
          s.put(b, s.getOrDefault(b, 0) + 1);
        }
        for (int i = 0; i < input.length(); i++){
          if (s.get(input.charAt(i)) == 1){
            return i;
          }
        }
        return -1;
    }
}