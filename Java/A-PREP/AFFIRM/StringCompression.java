/*
QUESTION:

Write two functions to handle strings: one to compress and one to decompress.
Input might look like aaabbbbbbcccccddd, and you have to come up with a way to compress/decompress the string. You can decide on how to compress the string yourself.

CLARIFYING:
  1.From the example, it looks like the input string have consecutive characters. So to clarify, the goal is to reduce this repeatble character when compressing correct?
  2.Will the input only contain lowercase alphabetic characters, or can it include digits or special characters too?
  3.I'll assume that empty string input or 1 letter string are already optimal and doesn't need compression'
  
SOLUTION:
Compression: 
Loop through the string, count consecutive repeating characters.
Append the character + count to the result.
O(n), O(n) since we have to visit every letter in the string,

Decompression:
terate through the string, read each character followed by one or more digits.
Parse the count and append the character that many times
O(n), O(n) where n is length of compressed string, 

FOLLOW UPS:
This work assuming that the input is alphabetical, but if there's number, we can use clear seperator between the string char and the 
count follow by a clear delimiter for example "a:3|b:6|1:2"
*/
public class StringCompressor {

    /**
     * Compresses the input string using run-length encoding.
     * For example, "aaabbbcc" becomes "a3b3c2".
     */
    public static String compress(String input) {
        // Handle null or empty input
        if (input == null || input.isEmpty()) return "";

        StringBuilder compressed = new StringBuilder();
        int count = 1;

        // Loop through characters starting from the second one
        for (int i = 1; i < input.length(); i++) {
            // If the current character is the same as the previous, increment count
            if (input.charAt(i) == input.charAt(i - 1)) {
                count++;
            } else {
                // Append previous character and its count
                compressed.append(input.charAt(i - 1)).append(count);
                count = 1; // Reset count for the new character
            }
        }

        // Append the last character group
        compressed.append(input.charAt(input.length() - 1)).append(count);

        return compressed.toString();
    }

    /**
     * Decompresses a string encoded with run-length encoding.
     * For example, "a3b3c2" becomes "aaabbbcc".
     */
    public static String decompress(String input) {
        // Handle null or empty input
        if (input == null || input.isEmpty()) return "";

        StringBuilder decompressed = new StringBuilder();
        int i = 0;

        // Iterate through the compressed string
        while (i < input.length()) {
            char letter = input.charAt(i++); // Read the character

            // Read all consecutive digits (in case the count is > 9)
            StringBuilder countBuilder = new StringBuilder();
            while (i < input.length() && Character.isDigit(input.charAt(i))) {
                countBuilder.append(input.charAt(i++));
            }

            // Parse the count and append the character that many times
            int count = Integer.parseInt(countBuilder.toString());
            for (int j = 0; j < count; j++) {
                decompressed.append(letter);
            }
        }

        return decompressed.toString();
    }

    /**
     * Test the compress and decompress functions with example input.
     */
    public static void main(String[] args) {
        String original = "aaabbbbbbcccccddd";

        // Compress the original string
        String compressed = compress(original);

        // Decompress it back to the original
        String decompressed = decompress(compressed);

        // Print all results
        System.out.println("Original: " + original);
        System.out.println("Compressed: " + compressed);
        System.out.println("Decompressed: " + decompressed);
    }
}
