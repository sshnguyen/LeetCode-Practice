# You are building a program to play a card game. Each card has three properties:
# Prefix: one of +, -, or =
# Letter: one of A, B, or C
# Number of letters: either 1, 2, or 3 (e.g., A, AA, or AAA)

# The goal is to identify all valid hands of exactly three cards such that, for each property, 
# the values across the three cards are either all the same or all different.

#EXAMPLE INPUT = [-A, -B, -BB, +C, -C, -CC, =CCC]
# RETURN [(+C, -CC, =CCC), (-A, -B, -C)]

# +C, -CC, =CCC # Prefix: +, -, = → all different # Letter: C, C, C → all same# Length: 1, 2, 3 → all different

# -A, -B, -C # Prefix: -, -, - → all same # Letter: A, B, C → all different # Length: 1, 1, 1 → all same


#✅✅✅✅
#CLARIFIYING QUESTIONS
#       1. For the card inputs, can we assume that the card will always be valid? Prefix always one of three property
#          a card will only has one letter, and the order of the property are correct, prefix first then the value.

#       2. Are all the card distinct? If there can be duplicate then there are cases where there are
#         there duplicate hands in our solution so we want to remove duplicate

from typing import *

class Solution:

    def parse_card(self, card: str) -> Tuple[str, str, int]:
        """
        Parse a single card string into its components:
        - prefix: one character (+, -, =)
        - letter: single character (A, B, C)
        - length: length of the card string (e.g., "-BB" -> length=3)
        
        Example: "-BB" -> ('-', 'B', 3)
        """
        prefix = card[0]
        letter = card[1]
        length = len(card)  # total length of the card string
        return (prefix, letter, length)

    def is_valid_hand(self, parsed_cards: List[Tuple[str, str, int]]) -> bool:
        """
        Check if a list of 3 parsed cards is a valid hand.
        For each property (prefix, letter, length), the values across
        the 3 cards must be all the same or all different.
        """
        prefixes = [card[0] for card in parsed_cards]
        letters = [card[1] for card in parsed_cards]
        lengths = [card[2] for card in parsed_cards]

        def all_same_or_all_different(values):
            unique_values = set(values)
            return len(unique_values) == 1 or len(unique_values) == len(values)

        return (all_same_or_all_different(prefixes) and
                all_same_or_all_different(letters) and
                all_same_or_all_different(lengths))

    def find_valid_hands(self, cards: List[str]) -> List[Tuple[str, str, str]]:
        """
        Given a list of card strings, find all valid 3-card hands.
        Return a list of tuples, each tuple contains three card strings.
        """
        valid_hands = []
        if len(cards) < 3:
            return valid_hands

        # Parse all cards once
        parsed_cards = [self.parse_card(card) for card in cards]

        n = len(cards)
        for i in range(n):
            for j in range(i + 1, n):
                for k in range(j + 1, n):
                    hand = [parsed_cards[i], parsed_cards[j], parsed_cards[k]]
                    if self.is_valid_hand(hand):
                        # Use cards (original strings) at the same indices
                        valid_hands.append((cards[i], cards[j], cards[k]))

        return valid_hands
    
if __name__ == "__main__":
  # Example input cards
  cards = ["-A", "-B", "-BB", "+C", "-C", "-CC", "=CCC"]

  # Create an instance of Solution
  solution = Solution()

  # Find all valid 3-card hands
  valid_hands = solution.find_valid_hands(cards)

  # Optional: assert expected output for testing
  expected = [("+C", "-CC", "=CCC"), ("-A", "-B", "-C")]
  assert set(valid_hands) == set(expected), "Test failed!"
  print("Test passed!")