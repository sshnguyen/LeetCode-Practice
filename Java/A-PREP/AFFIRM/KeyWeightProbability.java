import java.util.*;

// In this problem, you're asked to implement a data structure that supports the following operations:

// put: Add a key-value pair to the dictionary.
// delete: Remove a key-value pair from the dictionary.
// get: Retrieve the value associated with a given key.
// get_rand_val: Return a random value from the dictionary, with each value having a probability proportional to its frequency of occurrence.
// For example, given a dictionary {a: 5, b: 5, c: 6, d: 5}, the get_rand_val() function should return:

// 5 with a probability of 3/4
// 6 with a probability of 1/4
// Follow up: Modify your previous implementation of the dictionary to ensure that get_rand_val() returns each unique value with equal probability. In the previous example, it should return 5 or 6 with a possibility of 1/2

/*
CLARIFYING QUESTION:
    PART 1 : Time space complexity constraint to these operation that I should strive for?
    WHAT ARE THE EXPECTED BEHAVIOUR IF I DELETE SOME KEY THAT DOESN't EXIST?
    WHAT HAPPENS WHEN I PUT A KEY THAT ALREADY EXIST? DOES THE VALUE CHANGE?
             
    
*/

/*
IDEA:
PART 1.
HASH MAP TO STORE THE DICTIONARY, KEY_VALUE PAIR. (put, get, delete in O(1)).
FOR RANDOM WE USE UTILITY LIBRARY TO CHOOSE A RANDOM NUMBER BETWEEN 0 AND SIZE OF THE COLLECTION,
BUT HASHMAP DOESN't ALLOW FOR RETRIEVAL BY INDICES SO WE CAN USE A LIST TO STORE THE VALUES INSTEAD.

BUT THIS WILL BE O(N) SINCE TO REMOVE ITEMS OR UPDATE ITEMS, WE HAVE TO REMOVE ITEMS FROM THE LIST OF
VALUES, AND THIS WILL TAKE O(N). WE CAN USE SWAP AND POP METHOD TO SWAP THE VALUE REMOVE WITH THE LAST INDEX
AND THEN POP THE LAST INDEX. THIS WILL TAKE O(1) SINCE THE LIST DOESN't HAVE TO REORDER ITSELF. 

PART 2:
SIMPLIFY THE SOLUTION, WE MODIFY SO THAT THE VALUES LIST ONLY STORE THE UNIQUE WORDS. WE CAN UPDATE THIS LIST TO ADD ONLY
ON PUT WHEN THAT VALUE HAS A FREQUENCY OF 1 OR REMOVE IF FREQ 0.

THAT MEANS WE NEED A HASHMAP TO STORE THE VALUE AND ITS FREQUENCY AND WE NEED TO UPDATE THIS FREQUENCY AFTER EVERY PUT, DELETE OPERATION.

TO HAVE DELETE IN O(1), WE USE THE SAME SWAP AND POP STRATEGY, SO HAVE A MAP OF THE VALUE TO THE UNIQUE INDEX



*/

class WeightedRandomDictionary {
    HashMap<String, Integer> dictionary;
    List<Integer> valueList;
    HashMap<Integer, Set<Integer>> valueToListIndices;
    Random random = new Random();
    
    public WeightedRandomDictionary (){
        this.dictionary = new HashMap<>();
        this.valueList = new ArrayList<>();
        this.valueToListIndices = new HashMap<>();
    }
    
    public void put(String key, int value){
        //check if value already exist, delete it first
        if (dictionary.containsKey(key)){
            delete(key);          
        } 
    
        //update dict and value lists
        dictionary.put(key, value);
        valueList.add(value);
        valueToListIndices.putIfAbsent(value, new HashSet<>());
        valueToListIndices.get(value).add(valueList.size() - 1);
        
    }
    
    public int get(String key){
        return dictionary.get(key);
    }
    
    public boolean delete(String key){
        if (!dictionary.containsKey(key)){ // doesn't exist'
            return false;
        }
        int value = dictionary.get(key);
        //remove from the dict
        dictionary.remove(key);
        //get the index being remove in the list
        int indexRemove = valueToListIndices.get(value).iterator().next();
        int indexLast = valueList.size() - 1;
        
        //swap value remove with last value in list
        valueList.set(indexRemove, valueList.get(indexLast));

        //update map valueToListIndices
        valueToListIndices.get(valueList.get(indexRemove)).add(indexRemove);
        
        //remove last item of list, and its appearance in valueToListIndices
        valueList.remove(indexLast);
        valueToListIndices.get(value).remove(indexRemove);
        
        return true;
        
    }
    
    public int getRandVal(){
        return valueList.get(random.nextInt(valueList.size()));
    }
    
}

class WeightedRandomDictionary2 {
    private HashMap<String, Integer> dictionary; // Key -> Value
    private HashMap<Integer, Integer> valueFreq; // Value -> Frequency
    private List<Integer> uniqueValues;          // List of unique values
    private HashMap<Integer, Integer> valueUniqueIndex; // Value -> Index in uniqueValues
    private Random random = new Random();

    public WeightedRandomDictionary2() {
        dictionary = new HashMap<>();
        valueFreq = new HashMap<>();
        uniqueValues = new ArrayList<>();
        valueUniqueIndex = new HashMap<>();
    }

    public void put(String key, int value) {
        if (dictionary.containsKey(key)) {
            delete(key); // Remove old value before inserting
        }

        dictionary.put(key, value);
        valueFreq.put(value, valueFreq.getOrDefault(value, 0) + 1);

        // If it's the first occurrence of this value, add it to uniqueValues
        if (valueFreq.get(value) == 1) {
            uniqueValues.add(value);
            valueUniqueIndex.put(value, uniqueValues.size() - 1); // Store its index
        }
    }

    public boolean delete(String key) {
        if (!dictionary.containsKey(key)) return false;

        int value = dictionary.get(key);
        dictionary.remove(key);

        // Update frequency
        valueFreq.put(value, valueFreq.get(value) - 1);

        // If it's the last occurrence, remove it from uniqueValues using swap and pop
        if (valueFreq.get(value) == 0) {
            valueFreq.remove(value);

            int index = valueUniqueIndex.get(value);
            int lastValue = uniqueValues.get(uniqueValues.size() - 1);

            // Swap with the last element
            uniqueValues.set(index, lastValue);
            valueUniqueIndex.put(lastValue, index); // Update index of last value

            // Remove last element
            uniqueValues.remove(uniqueValues.size() - 1);
            valueUniqueIndex.remove(value);
        }
        return true;
    }

    public int get(String key) {
        return dictionary.getOrDefault(key, -1);
    }

    public int getRandVal() {
        if (uniqueValues.isEmpty()) {
            throw new IllegalStateException("No values available.");
        }
        return uniqueValues.get(random.nextInt(uniqueValues.size()));
    }
}



public class Solution {

    public static void main(String[] args) {
        WeightedRandomDictionary wrd = new WeightedRandomDictionary();
        WeightedRandomDictionary2 wrd2 = new WeightedRandomDictionary2();
        wrd.put("a", 5);
        wrd.put("b", 5);
        wrd.put("c", 6);
        wrd.put("d", 5);
        
        wrd2.put("a", 5);
        wrd2.put("b", 5);
        wrd2.put("c", 6);
        wrd2.put("d", 5);

        // Test getRandVal
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        Map<Integer, Integer> frequencyMap2 = new HashMap<>();
        int trials = 1000;
        for (int i = 0; i < trials; i++) {
            Integer val = wrd.getRandVal();
            Integer val2 = wrd2.getRandVal();
            frequencyMap.put(val, frequencyMap.getOrDefault(val, 0) + 1);
            frequencyMap2.put(val2, frequencyMap2.getOrDefault(val2, 0) + 1);
        }

        // Print probabilities
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            System.out.println(" PART 1: Value: " + entry.getKey() + ", Probability: " + (double) entry.getValue() / trials);
        }
        
        // Print probabilities
        for (Map.Entry<Integer, Integer> entry : frequencyMap2.entrySet()) {
            System.out.println("PART 2: Value: " + entry.getKey() + ", Probability: " + (double) entry.getValue() / trials);
        }
    }
}
