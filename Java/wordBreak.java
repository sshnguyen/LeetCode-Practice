class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        //hashset to decrease O(n) lookup for list to O(1)
        Set wordSet= new HashSet<>();
        for (String word : wordDict){
            wordSet.add(word);
        }
        //not necessarily only one or two words, can be any number of words.
        //create an array to store whether or not the words of s, if end at this index can be created
        //using wordDict
        int length = s.length();
        boolean[] possible = new boolean[length + 1];
        possible[0] = true;
        
        //we want to iterate from the start to current ending position and if we find
        //known segment, then we just have to check the segment after that.
        for (int i = 1; i <= length; i++){
            for (int j = 0; j < i; j++){
                //check possible segments after known true segment
                if (possible[j] && wordSet.contains(s.substring(j, i))){
                    possible[i] = true;
                    break;
                }
            }
        }
        return possible[length];
    }
}