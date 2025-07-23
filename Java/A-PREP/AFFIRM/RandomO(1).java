import java.util.*;

// RandomizedSet() Initializes the RandomizedSet object. bool insert(int val) Inserts an item val into the set if not present. 
//Returns true if the item was not present, false otherwise. bool remove(int val) Removes an item val from the set if present. 
//Returns true if the item was present, false otherwise. int getRandom() Returns a random element from the current set of elements
// (it's guaranteed that at least one element exists when this method is called). Each element must have the same probability of being returned. 
//You must implement the functions of the class such that each function works in average O(1) time complexity. (Leetcode 380)

//PART 2: ALLOW DUPLICATES, probablity of duplicates should increase proportionally

/*
CLARIFYING QUESTION:
    PART 1 : SHOULD WE CONSIDER DUPLICATES?
             Can we use built in functions like Random?
             
    
*/

/*
IDEA:
SET WOULD WORK FOR O(1) INSERT AND REMOVE,
FOR RANDOM, THE APPROACH IS GET THE LENGTH OF THE SET, AND USE A RANDOM UTILITY FUNCTION TO GET A RANDOM INDEX,
HOWEVER, SET DOESN't SUPPORT GETTING BY INDEX SO WE CAN ALSO USE A LIST WITH THE SET. LIST get at index is O(1).
THE ISSUE IS THAT when we remove an item, we can remove it from set in O(1) but the list removal would take O(n)

THE APPROACH THAT WORK IS THEN CHANGE SET TO USING A HASHMAP THAT MAP THE VALUE TO THE INDEX OF THE LIST.
REMOVING AN ITEM FROM MIDDLE OF THE LIST WOULD STILL TAKE O(n) since it has to shift the indexes right of it to the left
EXCEPT AT THE END. SO WE CAN MAKE IT IN O(1) BY SWAPPING THE VALUE AT THE END OF THE LIST WITH THIS REMOVE INDEX,
THEN REMOVE THE INDEX AT THE END. THEN UPDATE THE MAP SO THAT THE INDEX IS UPDATE FOR THIS SWAPPED VALUE.

PART 2: SAME IDEA IN PART 1, SO THE LIST CAN CONTAIN MULTIPLE OF SAME VALUES SO WE NEED TO CHANGE OUR MAP TO
<Integer, Set<Integer>> instead to hold all the indexes

*/

class RandomizedSet {
    Map<Integer, Integer> mapValueToListIndex; // map value to ValueList index
    List<Integer> valueList;
    Random random = new Random();
    
    public RandomizedSet() {
        this.mapValueToListIndex = new HashMap<>();
        this.valueList = new ArrayList<>();
    }
    
    public boolean insert(int val) {
        if (mapValueToListIndex.containsKey(val)){//value already exist
            return false;
        }
        valueList.add(val);
       valueList.set(indexRemove, valueList.get(valueList.size() - 1));
        return true;
    }
    
    public boolean remove(int val) {
        if (!mapValueToListIndex.containsKey(val)){ // value doesn't exist in storage
            return false;
        }
        //get the index of the value being remove in list
        int indexRemove = mapValueToListIndex.get(val);
        
        //swap the index of the value remove with last index
        valueList.set(indexRemove, valueList.size() - 1);
        //update the map
        mapValueToListIndex.put(valueList.get(indexRemove), indexRemove);
        
        //remove the val
        valueList.remove(valueList.size() - 1);
        mapValueToListIndex.remove(val);
        return true;
    }
    
    public int getRandom() {
        return valueList.get(random.nextInt(valueList.size()));
    }
}

class RandomizedSet2 {
    Map<Integer, Set<Integer>> mapValueToListIndex; // map value to ValueList index
    List<Integer> valueList;
    Random random = new Random();
    
    public RandomizedSet2() {
        this.mapValueToListIndex = new HashMap<>();
        this.valueList = new ArrayList<>();
    }
    
    public boolean insert(int val) {
        boolean isNew = mapValueToListIndex.containsKey(val);
        valueList.add(val);
        mapValueToListIndex.putIfAbsent(val, new HashSet<>());
        mapValueToListIndex.get(val).add(valueList.size() - 1);
        return isNew;
    }
    
    public boolean remove(int val) {
        if (!mapValueToListIndex.containsKey(val)){ // value doesn't exist in storage
            return false;
        }
        //get the index of the value being remove in list,
        int indexRemove = mapValueToListIndex.get(val).iterator().next();
        int lastIndex = valueList.size() - 1;
        
        //swap the index of the value remove with last index
        valueList.set(indexRemove, valueList.get(lastIndex));
        //update the map

        mapValueToListIndex.get(valueList.get(indexRemove)).add(indexRemove);
        
        //remove the val
        valueList.remove(valueList.size() - 1);
        mapValueToListIndex.get(val).remove(indexRemove);
        return true;
    }
    
    public int getRandom() {
        return valueList.get(random.nextInt(valueList.size()));
    }
}

public class Solution {

    public static void main(String[] args) {
        RandomizedSet randomSet = new RandomizedSet();
        System.out.println(randomSet.insert(1));//return true
        System.out.println(randomSet.insert(1));//return false
        System.out.println(randomSet.insert(2));//return true
        System.out.println(randomSet.insert(3));//return true
        
        System.out.println(randomSet.remove(3));//return true
        System.out.println(randomSet.remove(3));//return false
        
        System.out.println(randomSet.getRandom());//50% chance of 1 or 2
        
        System.out.println(randomSet.insert(1));//return true
        System.out.println(randomSet.insert(1));//return false because duplicate
        System.out.println(randomSet.insert(2));//return true
        System.out.println(randomSet.insert(3));//return true
        
        System.out.println(randomSet.remove(3));//return true
        System.out.println(randomSet.remove(3));//return false
        
        System.out.println(randomSet.getRandom());//66% chance of 1, 33% chance of  2
        
    }
}
