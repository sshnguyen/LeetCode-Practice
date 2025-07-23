import java.util.*;

//THIS QUESTION IS SIMILAR TO THE Letters appearing most number of words. BUT THE LETTER QUESTION INSTEAD ASK FOR RETURNING ONLY MAX
//CO_OCCURANCE, NOT ALL OF THEM IN SORTED ORDER. SO AFTER WE CREATED THE CO_OCCURANCE_FREQUENCY MAP WE CAN ITERATE THROUGH IT and
//FIND THE FREQUENCY.

// Affirm partners with a lot of merchants today and many users will make purchases at more than one merchant. We'd like to analyze that cross purchasing behavior to make recommendations to new user about where else they might like to shop. Imagine we have a list where each entry is an individual user's history of purchases, i.e., the list of merchants that the user has made a purchase at. We want to take that list and find, for any merchant with at least one purchase, what other merchant(s) are most frequently seen in users' shopping behavior.

// e.g. [['Casper', 'Purple', 'Wayfair'],['Purple', 'Wayfair', 'Tradesy'],['Wayfair', 'Tradesy', 'Peloton']]
// [['Casper', 'Purple', 'Wayfair'],['Purple', 'Wayfair', 'Tradesy'],['Wayfair', 'Tradesy', 'Peloton']] =>
// {
// 'Casper': ['Purple', 'Wayfair'],
// 'Purple': ['Wayfair'],
// 'Wayfair': ['Purple', 'Tradesy'],
// 'Tradesy': ['Wayfair'],
// 'Peloton': ['Wayfair', 'Tradesy']
// }

/*
CLARIFYING QUESTION:
    1.What should the result looks like? For example for a merchant, do we list all the co-merchant that appear?
    2.Do we sort the result list of co-merchant from most frequent to least frequent? Should we return all or
    should we consider a cut off frequency before we recommend
    3. Is there a max number of merchant we need to recommend? (if there is we can use heap to store recommend)
    
*/

/*
APPROACH IDEA:
Build a Co-occurrence Count Map:

Iterate through each user's purchase history.
For each merchant in the history, count how frequently it appears alongside other merchants.
Find the Most Frequently Associated Merchant(s) for Each:
map should look lile {"merchant" : {associateMerchant1 : 2, associateMerchant2 : 1}}

For each merchant, sort co-occurring merchants by frequency.
Select the most frequent one(s).

The worst-case complexity is O(n * m²) for creating the co-occurance frequency map. n is number of user histories
m is max number of merchants in a history. For sorting the merchant by freq, it's O(mlogm)


*/
import java.util.*;

public class MerchantRecommendation {
    public static Map<String, List<String>> findFrequentCoOccurrences(List<List<String>> userHistories) {
        // Step 1: Initialize a frequency map
        Map<String, Map<String, Integer>> frequencyMap = new HashMap<>(); //{"merchant" : {associateMerchant1 : 2, associateMerchant2 : 1}}

        // Step 2: Populate the frequency map
        for (List<String> history : userHistories) {
            for (int i = 0; i < history.size(); i++) {
                String merchant1 = history.get(i);
                frequencyMap.putIfAbsent(merchant1, new HashMap<>());

                for (int j = i + 1; j < history.size(); j++) {
                    String merchant2 = history.get(j);
                    frequencyMap.putIfAbsent(merchant2, new HashMap<>());

                    // Update co-occurrence counts
                    frequencyMap.get(merchant1).put(merchant2, frequencyMap.get(merchant1).getOrDefault(merchant2, 0) + 1);
                    frequencyMap.get(merchant2).put(merchant1, frequencyMap.get(merchant2).getOrDefault(merchant1, 0) + 1);
                }
            }
        }

        // Step 3: Find the most frequent co-occurring merchants for each merchant
        Map<String, List<String>> result = new HashMap<>();
        for (String merchant : frequencyMap.keySet()) {
            // Get the co-occurrence map for the current merchant
            Map<String, Integer> coOccurrenceMap = frequencyMap.get(merchant);

            // Sort the co-occurring merchants by frequency (descending order)
            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(coOccurrenceMap.entrySet());
            sortedEntries.sort((a, b) -> b.getValue().compareTo(a.getValue()));

            // Extract the top co-occurring merchants
            List<String> topMerchants = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                topMerchants.add(entry.getKey());
            }

            // Add to the result
            result.put(merchant, topMerchants);
        }

        return result;
    }

    public static void main(String[] args) {
        // Example input
       List<List<String>> userHistories = Arrays.asList(
            Arrays.asList("Casper", "Purple", "Wayfair"),
            Arrays.asList("Purple", "Wayfair", "Tradesy"),
            Arrays.asList("Wayfair", "Tradesy", "Peloton")
        );

        // Find frequent co-occurrences
        Map<String, List<String>> result = findFrequentCoOccurrences(userHistories);

        // Print the result
        for (Map.Entry<String, List<String>> entry : result.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}


/*
Given an input list of strings, for each letter appearing anywhere 
in the list, find the other letter(s) that appear in the most 
number of words with that letter.

Example: 
['abc', 'bcd', 'cde'] =>
  {
	a: [b, c],	# b appears in 1 word with a, c appears in 1 word with a
	b: [c], 	# c appears in 2 words with b, a and d each appear in only 1 word with b
	c: [b, d], 	# b appears in 2 words with c, d appears in 2 words with c. But a and e each 
					  appear in only 1 word with c.
	d: [c],		# c appears in 2 words with d. But b and e each appear in only 1 word with d
	e: [c, d], 	# c appears in 1 word with e, d appears in 1 word with e
		
  }
*/

/*
APPROACH: 

Brute Force Steps
1.Iterate through each letter (a-z):

2.Check in how many words it appears.
For each letter, compare it with every other letter:

3.Iterate through every other letter (b, c, d, ...).
Count how many words contain both the current letter and the other letter.
Find the most frequent co-occurring letter(s) for each letter:

Keep track of the maximum count.
Store the letters that appear together the most.



OPTIMAL
Step 1: Mapping Letters to Words
First, we create a mapping of each letter to the words in which it appears.

Letter	Appears in Words
a	{"abc"}
b	{"abc", "bcd"}
c	{"abc", "bcd", "cde"}
d	{"bcd", "cde"}
e	{"cde"}
Now, we know which words contain each letter.

Step 2: Counting Co-Occurrences
For each letter, we count how many times other letters appear with it.
Map<Character, Map<Character, Integer>> coOccurrenceCount

Example: Letter 'b'
'b' appears in "abc" and "bcd".
Other letters in these words:
"abc" → a, b, c
"bcd" → b, c, d
Co-occurrence counts:
'a' appears 1 time (in "abc")
'c' appears 2 times (in "abc", "bcd") ✅ (Most frequent)
'd' appears 1 time (in "bcd")
Thus, for 'b', the most frequently co-occurring letter is 'c'.

Step 3: Find the Most Frequent Co-Occurrences
We do this process for all letters:

Letter	Co-occurring Letters Count	Most Frequent Letter(s)
a	{'b': 1, 'c': 1}	['b', 'c']
b	{'a': 1, 'c': 2, 'd': 1}	['c']
c	{'a': 1, 'b': 2, 'd': 2, 'e': 1}	['b', 'd']
d	{'b': 1, 'c': 2, 'e': 1}	['c']
e	{'c': 1, 'd': 1}	['c', 'd']

Step 1: O(N * M), n is number of words, n the max number of letters 
Step 2: O(N * M²)
Step 3: O(1)


*/
import java.util.*;

public class LetterCoOccurrence {
    public static Map<Character, List<Character>> findMostFrequentCoOccurrences(String[] words) {
        // Step 1: Create a map to track which letters appear in which words
        Map<Character, Set<Integer>> letterToWords = new HashMap<>();
        
        // Populate letterToWords map
        for (int i = 0; i < words.length; i++) {
            for (char c : words[i].toCharArray()) {
                letterToWords.putIfAbsent(c, new HashSet<>());
                letterToWords.get(c).add(i);  // Track word indices where the letter appears
            }
        }

        // Step 2: Count co-occurrences for each letter
        Map<Character, Map<Character, Integer>> coOccurrenceCount = new HashMap<>();
        
        for (char letter : letterToWords.keySet()) {
            coOccurrenceCount.putIfAbsent(letter, new HashMap<>());

            // Get all words this letter appears in
            Set<Integer> wordIndices = letterToWords.get(letter);

            // Count occurrences of other letters in the same words
            for (int index : wordIndices) {
                for (char otherLetter : words[index].toCharArray()) {
                    if (otherLetter != letter) {  // Avoid self-counting
                        coOccurrenceCount.get(letter).put(
                            otherLetter, coOccurrenceCount.get(letter).getOrDefault(otherLetter, 0) + 1
                        );
                    }
                }
            }
        }

        // Step 3: Find the most frequently co-occurring letter(s) for each letter
        Map<Character, List<Character>> result = new HashMap<>();

        for (char letter : coOccurrenceCount.keySet()) {
            Map<Character, Integer> counts = coOccurrenceCount.get(letter);

            // Find max frequency among co-occurring letters
            int maxCount = counts.values().stream().max(Integer::compare).orElse(0);

            // Collect letters that have this max frequency
            List<Character> mostFrequentLetters = new ArrayList<>();
            for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
                if (entry.getValue() == maxCount) {
                    mostFrequentLetters.add(entry.getKey());
                }
            }

            result.put(letter, mostFrequentLetters);
        }

        return result;
    }

    public static void main(String[] args) {
        String[] words = {"abc", "bcd", "cde"};
        Map<Character, List<Character>> result = findMostFrequentCoOccurrences(words);
        
        // Print the result
        for (Map.Entry<Character, List<Character>> entry : result.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
