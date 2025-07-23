from typing import List, Tuple

def find_character_at(matrix: List[str], coordinates: Tuple[int, int]) -> str:
    """
    Given a character matrix and coordinates [x, y] where [0, 0] is bottom-left,
    return the character at that position.
    
    :param matrix: List of strings (each string is a row, from top to bottom)
    :param coordinates: Tuple (x, y)
    :return: Character at (x, y)
    """
    x, y = coordinates
    num_rows = len(matrix)
    target_row = num_rows - 1 - y
    return matrix[target_row][x]

# Example usage
if __name__ == "__main__":
    coords = (2, 4)
    matrix = [
        "AFKPU",
        "BGLQV",
        "CHMRW",
        "DINSX",
        "EJOTY"
    ]
    
    result = find_character_at(matrix, coords)
    print(result)  # Output: K
