import java.util.*;

// Given an input list of names, for each name, find the shortest substring that only appears in that name.

// Example:

// Input: ["cheapair", "cheapoair", "peloton", "pelican"]
// Output:
// "cheapair": "pa"  // every other 1-2 length substring overlaps with cheapoair
// "cheapoair": "po" // "oa" would also be acceptable
// "pelican": "ca"   // "li", "ic", or "an" would also be acceptable
// "peloton": "t"    // this single letter doesn't occur in any other string

/*
CLARIFYING QUESTION:
    WHAT IS THE RETURN IF IT DOESN"t HAVE A UNIQUE SUBSTRING?
    WHAT SHOULD IT RETURN IF IT"S A TIE BETWEEN 2 SUBSTRING OF SAME LENGTH? TIE BREAK BY ORDER OF LEXI?
    
*/

/*
IDEA:
BRUTEFORCE:
1.For each name in the list, generate all possible substrings.
2.For each substring, check if it appears in any other name by iterating through the rest of the names and check .contains() O(m)
3.Track the shortest unique substring for each name.

O(n^2 * m^3) time.  O(n* m^2) to generate all possible substring for all name, then checking for uniqueness is O(n*m)
O(1) space


HASHMAP:
1.GENERATE A MAP TO STORE ALL POSSIBLE SUBSTRINGS AND IT's FREQUENCY.
2.WHEN GENERATING FOR EACH NAME, MAINTAIN A SET SO WE DON't INCREASE A SUBSTRING TWICE FOR THE SAME NAME.
3. LOOP THROUGH ALL NAME TO GENERATE POSSIBLE SUBSTRING AGAIN, CHECK IF FREQUENCY IS 1 THEN IT"S UNIQUE.
4. UPDATE THE THE RESULT MAINTAINING SHORTEST UNIQUE SUBSTRING FOR THAT NAME.
O(n* m^2), m longest name length, n is number of names
O(n* m^2) space to maintain the frequency hashmap

OPTIMAL. TRIES:
Insert all the substring of all words into Trie, with that we will maintain unique indexes of strings at each Trie node. This will help finding that if any other string has substring reaching to this node or not, by calculating the size of set, if cnt.size() > 1 then there are substrings from multiple strings exists at this node, so it can not be unique to any one string.

Then we will run queries again for each string, get all the substring(i to n) and check if there does not exist more then a substrings at some node by checking cnt.size() == 1 at each node. if we find substring which is unique, we would compare with other uniquesubstrings from the same main string, and will find the string with minLength and lexicographically smaller string.


*/
class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    int count = 0; // Number of times this substring appears
}

class Solution {
    public static String[] shortestUncommonSubstring(String[] names){
        String[] result = new String[names.length];
        Map<String, Integer> subStringFreq = new HashMap<>(); // map substring -> freq in number of strings
        
        //STEP 1: GENERATE THE SUBSTRING FREQ MAP
        for (String name: names){//iterate through strings
            Set<String> substringsSet = new HashSet<>(); // to store current word substrings so we don't increase freq of a substring more than once a word
            for (int i = 0; i < name.length(); i++){ // generate all possible substrings
                for (int j = i + 1; j <= name.length(); j++){
                    String subString = name.substring(i, j);
                    if (!substringsSet.contains(subString)){ // if a new substring is found
                        subStringFreq.put(subString, subStringFreq.getOrDefault(subString, 0) + 1);//update freq
                        substringsSet.add(subString);
                    }
                }
            }
        }
        //STEP 2: GENERATE ALL POSSIBLE SUBSTRING AND CHECK AGAINST FREQ MAP FOR UNIQUE SUBSRING
        //UPDATE THE ERSULT ARRAY AS WE GO.
        
        for (int k = 0; k < names.length; k++){//iterate through strings
            String shortestSubString = "";
            for (int i = 0; i < names[k].length(); i++){ // generate all possible substrings
                for (int j = i +1; j <= names[k].length(); j++){
                    String subString = names[k].substring(i, j);
                    if (subStringFreq.get(subString) == 1){ //unique, only found in this word
                        // check if it's the shortest, if tie check order of lexicography
                        if (shortestSubString.isEmpty() || (subString.length() < shortestSubString.length()) || subString.length() == shortestSubString.length() && subString.compareTo(shortestSubString) < 0){
                            shortestSubString = subString;
                        }
                    }
                }
                result[k] = shortestSubString;
            }
        }
        
        return result;
    }
    
    // public static Map<String, String> findShortestUniqueSubstrings(String[] names) {
    //     TrieNode root = new TrieNode();
    //     Map<String, String> result = new HashMap<>();

    //     // Insert all substrings of all names into the trie
    //     for (String name : names) {
    //         int n = name.length();
    //         for (int i = 0; i < n; i++) {
    //             TrieNode node = root;
    //             for (int j = i; j < n; j++) {
    //                 char c = name.charAt(j);
    //                 node.children.putIfAbsent(c, new TrieNode());
    //                 node = node.children.get(c);
    //                 node.count++; // Increment the count for this substring
    //             }
    //         }
    //     }

    //     // Find the shortest unique substring for each name
    //     for (String name : names) {
    //         int n = name.length();
    //         String shortestUniqueSubstring = "";

    //         // Try substrings of increasing length
    //         outer:
    //         for (int len = 1; len <= n; len++) {
    //             for (int i = 0; i <= n - len; i++) {
    //                 String substring = name.substring(i, i + len);

    //                 // Check if this substring is unique
    //                 if (isUnique(root, substring)) {
    //                     shortestUniqueSubstring = substring;
    //                     break outer;
    //                 }
    //             }
    //         }

    //         result.put(name, shortestUniqueSubstring);
    //     }

    //     return result;
    // }

    // private static boolean isUnique(TrieNode root, String substring) {
    //     for (char c : substring.toCharArray()) {
    //         root = root.children.get(c);
    //         if (root == null) {
    //             return false; // Substring not found in the trie
    //         }
    //     }
    //     return root.count == 1; // Substring is unique if it appears only once
    // }

    public static void main(String[] args) {
        String[] input = new String[]{"cheapair", "cheapoair", "peloton", "pelican", "air"};
        String[] result = shortestUncommonSubstring(input);
        
        for (int i = 0; i < input.length; i++){
            System.out.println(input[i] + " shortest unique substring is : " + result[i]);
        }
        //should return array with
        // "cheapair": "pa"  // every other 1-2 length substring overlaps with cheapoair
        // "cheapoair": "po" // "oa" would also be acceptable
        // "pelican": "ca"   // "li", "ic", or "an" would also be acceptable
        // "peloton": "t"    // this single letter doesn't occur in any other string
        // "air": "" //this string has no unique substring
        
        // String[] names = {"cheapair", "cheapoair", "peloton", "pelican", "air"};
        // Map<String, String> result2 = findShortestUniqueSubstrings(names);

        // for (Map.Entry<String, String> entry : result2.entrySet()) {
        //     System.out.println(entry.getKey() + ": " + entry.getValue());
        // }
    }
}
