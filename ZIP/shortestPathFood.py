from collections import deque

def getFood(grid):
    """
    LeetCode 1730: Shortest Path to Get Food
    
    Find shortest path from starting position '*' to any food '#' in a grid.
    Can move up, down, left, right. 'X' represents obstacles, 'O' represents free space.
    
    BRUTE FORCE APPROACH - DFS:
    Use depth-first search with backtracking to explore all possible paths.
    Try every path from start to any food cell and return minimum length.
    Time Complexity: O(4^(m*n)) - exponential due to exploring all paths
    Space Complexity: O(m*n) for recursion stack and visited tracking
    
    OPTIMAL APPROACH - BFS:
    Use breadth-first search to find shortest path in unweighted grid.
    BFS guarantees shortest path in terms of number of steps.
    
    Strategy:
    1. Find starting position '*' in the grid
    2. Use BFS to explore all reachable cells level by level
    3. Return steps when first food '#' is found
    4. Use directions array for 4-directional movement
    
    Time Complexity: O(m*n) where m = rows, n = cols
    Space Complexity: O(m*n) for queue and visited set
    
    Args:
        grid: 2D list representing the grid with '*', '#', 'X', 'O'
    
    Returns:
        Minimum steps to reach food, or -1 if impossible
    """
    # Edge cases
    if not grid or not grid[0]:
        return -1
    
    m, n = len(grid), len(grid[0])
    
    # Find starting position
    start_row = start_col = -1
    for i in range(m):
        for j in range(n):
            if grid[i][j] == '*':
                start_row, start_col = i, j
                break
        if start_row != -1:
            break
    
    if start_row == -1:
        return -1
    
    # BFS setup
    queue = deque([(start_row, start_col, 0)])  # (row, col, steps)
    visited = set()
    visited.add((start_row, start_col))
    
    # 4-directional movement: up, down, left, right
    directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
    
    # BFS traversal
    while queue:
        row, col, steps = queue.popleft()
        
        # Check if we found food
        if grid[row][col] == '#':
            return steps
        
        # Explore all 4 directions
        for dr, dc in directions:
            new_row, new_col = row + dr, col + dc
            
            # Check bounds and validity
            if (0 <= new_row < m and 0 <= new_col < n and 
                (new_row, new_col) not in visited and 
                grid[new_row][new_col] != 'X'):
                
                visited.add((new_row, new_col))
                queue.append((new_row, new_col, steps + 1))
    
    return -1

# Test cases
if __name__ == "__main__":
    test_cases = [
        [["X","X","X","X","X","X"],
         ["X","*","O","O","O","X"],
         ["X","O","O","#","O","X"],
         ["X","X","X","X","X","X"]],  # Expected: 3
        
        [["X","X","X","X","X"],
         ["X","*","X","O","X"],
         ["X","O","X","#","X"],
         ["X","X","X","X","X"]],  # Expected: -1
        
        [["X","X","X","X","X","X","X","X"],
         ["X","*","O","X","O","#","O","X"],
         ["X","O","O","X","O","O","X","X"],
         ["X","O","O","O","O","#","O","X"],
         ["X","X","X","X","X","X","X","X"]],  # Expected: 6
        
        [["O","*"],
         ["#","O"]],  # Expected: 2
    ]
    
    for i, grid in enumerate(test_cases):
        result1 = getFood(grid)
        print(f"Test {i+1}:")
        for row in grid:
            print("  " + "".join(row))
        print(f"  Regular BFS: {result1}")
        print()