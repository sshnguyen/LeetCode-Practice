# Part 1: Basic Pattern Finding
# Given a target pattern and a text string, find the first occurrence of the pattern in the text and return its starting index.

#✅✅✅✅
#CLARIFIYING QUESTIONS
  #What if the pattern doesn't exist, what should we return?

def find_pattern(text, pattern):
    #uses sliding window, we check window of size pattern to see if it matches. O(n)
    n, m = len(text), len(pattern)
    for i in range(n - m + 1):
        if text[i:i + m] == pattern:
            return i
    return -1  # Pattern not found

# Example usage:
if __name__ == "__main__":
    text = "CDFGAGB"
    pattern = "AG"
    result = find_pattern(text, pattern)
    print(result)  # Output: 4
