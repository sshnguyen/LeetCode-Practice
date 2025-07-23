// Uses an array mapArray of size 1000001 → Each index represents a key. -> Get the contraint by interviewer, contrain the number of key value pair
// Default value is -1 → Meaning a key is not present.
// to handle alphabetical keys we would need to hash it into a number
// put(key, value) → Stores value at mapArray[key].
// get(key) → Returns the value stored at mapArray[key] (or -1 if not present).
// remove(key) → Sets mapArray[key] to -1, effectively deleting it.

import java.io.*;
import java.util.*;

class MyHashMap {
	int[] mapArray;
	public MyHashMap()
	{
		mapArray = new int[1000001];
		Arrays.fill(mapArray, -1);
	}

	public void put(int key, int value)
	{
		mapArray[key] = value;
	}

	public int get(int key) { return mapArray[key]; }

	public void remove(int key) { mapArray[key] = -1; }

	// Drivers code
	public static void main(String args[])
	{
		MyHashMap hashMap = new MyHashMap();
		hashMap.put(1, 1);
		hashMap.put(2, 2);
		System.out.println(hashMap.get(1));
		System.out.println(hashMap.get(3));
		hashMap.put(2, 1);
		System.out.println(hashMap.get(2));
		hashMap.remove(2);
		System.out.println(hashMap.get(2));
	}
}
