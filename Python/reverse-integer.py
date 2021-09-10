"""                                             QUESTION
Given a signed 32-bit integer x, return x with its digits reversed.
If reversing x causes the value to go outside the signed 32-bit integer range [-231, 231 - 1], then return 0.
                                                EXAMPLES
Input: x = 123
Output: 321

Input: x = -123
Output: -321

Input: x = 120
Output: 21
Example 4:

Input: x = 0
Output: 0
                                                SOLUTIONS
To get the current digit of the solution we take the mod 10 of the input. The remainder, which is the last digit of
the input is the first digit of the solution. to update the solution number, we multiply it by 10 and add the next digit.
For the next digit we devide the input by 10, rounded, then do the same. For negative numbers, mod works differently
than what we want it to do so change the input to positive number and just turn the result to negative by the end.
"""

class Solution(object):
    def reverse(self, x):
        """
        :type x: int
        :rtype: int
        """
        result = 0
        negative = 1
        if x < 0:
            x = x * -1
            negative = - 1
        while (x != 0):
            result = result * 10 + x%10
            x = x//10
        if (-2**31) <= result <= (2**31 - 1):
                return result * negative
        return 0
        