"""                                             QUESTION
Given a string s, find the length of the longest substring without repeating characters.

                                                EXAMPLES
Input: s = "abcabcbb"
Output: 3
Explanation: The answer is "abc", with the length of 3.

Input: s = "bbbbb"
Output: 1
Explanation: The answer is "b", with the length of 1.

                                                SOLUTIONS
Start at the begining of the string and iterate through each character, we create a substring to start building
the solution. If the character is not in the substring then add it to the substring and update the solution if
the substring have the longest length so far. If we have a character that is already in the substring then
create a new substring by splicing the substring to the segment with the indexes after the character and
including the character at the end. Each iteration we have to use in operation (thinks it's O(n) time complexity)
so the total runtime is O(n^2)
"""
class Solution(object):
    def lengthOfLongestSubstring(self, s):
        """
        :type s: str
        :rtype: int
        """
        result = 0
        substring = ""
        for i in xrange(len(s)):
            if s[i] not in substring:
                substring = substring + s[i]
                if (len(substring) > result):
                    result += 1
            else:
                substring= substring[substring.index(s[i]) +1 :] + s[i]
        return result
            