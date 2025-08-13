from collections import deque

def openLock(deadends, target):
    """
    Find minimum number of turns to open a 4-digit combination lock.
    Starting from "0000", each turn can rotate one wheel up or down.
    Cannot pass through deadend combinations.
    
    BRUTE FORCE APPROACH - DFS:
    Try all possible paths using depth-first search with backtracking.
    Explore every combination until finding target or exhausting possibilities.
    Time Complexity: O(10^4 * 2^(10^4)) - exponential due to exploring all paths
    Space Complexity: O(10^4) for recursion stack and visited set
    
    OPTIMAL APPROACH - BFS:
    Use breadth-first search to find shortest path from "0000" to target.
    BFS guarantees minimum number of steps in unweighted graphs.
    
    Strategy:
    1. Start from "0000" and explore all possible next states
    2. For each state, generate 8 neighbors (4 wheels * 2 directions)
    3. Skip deadends and already visited states
    4. Return steps when target is reached
    
    Time Complexity: O(10^4) - at most 10000 possible combinations
    Space Complexity: O(10^4) for queue and visited set
    
    Args:
        deadends: List of forbidden combinations
        target: Target combination to reach
    
    Returns:
        Minimum number of turns, or -1 if impossible
    """
    # Edge cases
    if not target or len(target) != 4:
        return -1
    if target == "0000":
        return 0
    
    deadend_set = set(deadends)
    if "0000" in deadend_set:
        return -1
    if target in deadend_set:
        return -1
    
    # BFS setup
    queue = deque([("0000", 0)])  # (current_state, steps)
    visited = {"0000"}
    
    # BFS traversal
    while queue:
        current_state, steps = queue.popleft()
        
        # Check if we reached the target
        if current_state == target:
            return steps
        
        # Generate all possible next states by turning one wheel
        for i in range(4):
            digit = int(current_state[i])
            # Turn wheel up
            new_digit = (digit + 1) % 10
            neighbor = current_state[:i] + str(new_digit) + current_state[i+1:]
            if neighbor not in visited and neighbor not in deadend_set:
                visited.add(neighbor)
                queue.append((neighbor, steps + 1))
            
            # Turn wheel down
            new_digit = (digit - 1) % 10
            neighbor = current_state[:i] + str(new_digit) + current_state[i+1:]
            if neighbor not in visited and neighbor not in deadend_set:
                visited.add(neighbor)
                queue.append((neighbor, steps + 1))
    
    return -1


def openLock_bidirectional(deadends, target):
    """
    Optimized version using bidirectional BFS for better performance.
    BFS on both start and target and check when they meet in the middle.
    
    Time Complexity: O(10^2) - significantly faster than regular BFS
    Space Complexity: O(10^4) for sets and queues
    """
    # Edge cases
    if not target or len(target) != 4:
        return -1
    if target == "0000":
        return 0
    
    deadend_set = set(deadends)
    if "0000" in deadend_set or target in deadend_set:
        return -1
    
    # Bidirectional BFS
    start_queue = {"0000"}
    end_queue = {target}
    visited = set()
    steps = 0
    
    def get_neighbors(state):
        neighbors = []
        for i in range(4):
            digit = int(state[i])
            new_digit = (digit + 1) % 10
            neighbors.append(state[:i] + str(new_digit) + state[i+1:])
            new_digit = (digit - 1) % 10
            neighbors.append(state[:i] + str(new_digit) + state[i+1:])
        return neighbors
    
    while start_queue and end_queue:
        # Always expand the smaller queue
        if len(start_queue) > len(end_queue):
            start_queue, end_queue = end_queue, start_queue
        
        steps += 1
        next_queue = set()
        
        for state in start_queue:
            for neighbor in get_neighbors(state):
                if neighbor in end_queue:
                    return steps
                
                if neighbor not in visited and neighbor not in deadend_set:
                    visited.add(neighbor)
                    next_queue.add(neighbor)
        
        start_queue = next_queue
    
    return -1


# Test cases
if __name__ == "__main__":
    test_cases = [
        (["0201","0101","0102","1212","2002"], "0202"),  # Expected: 6
        (["8888"], "0009"),  # Expected: 1
        (["8887","8889","8878","8898","8788","8988","7888","9888"], "8888"),  # Expected: -1
        (["0000"], "8888"),  # Expected: -1
    ]
    
    for i, (deadends, target) in enumerate(test_cases):
        result1 = openLock(deadends, target)
        result2 = openLock_bidirectional(deadends, target)
        print(f"Test {i+1}: deadends={deadends}, target='{target}'")
        print(f"  Regular BFS: {result1}")
        print(f"  Bidirectional BFS: {result2}")
        print()