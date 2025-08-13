import heapq

def furthest_building(heights, ladders, bricks):
    """
    BRUTE FORCE APPROACH - RECURSION:
    Try all possible combinations of ladder and brick usage at each step.
    For each position, recursively explore using ladders vs bricks.
    Time Complexity: O(2^n) - exponential due to trying all combinations
    Space Complexity: O(n) for recursion stack depth
    
    OPTIMAL APPROACH (Greedy with Max-Heap):
    Use a max-heap to track the largest height differences where we've used bricks.
    Use bricks for small climbs and save ladders for the largest climbs.
    
    Strategy:
    1. For each height increase, use bricks first
    2. Keep track of brick usages in a max-heap
    3. When bricks run out, use a ladder for the largest brick usage
    4. If we can't use a ladder, we've reached our limit
    
    Time Complexity: O(n log n) where n = len(heights)
    Space Complexity: O(n) for the priority queue storing brick usages

    """
    # Edge cases
    if not heights or len(heights) <= 1:
        return 0
    
    # Max-heap to store height differences where we used bricks
    # Python's heapq is min-heap, so we negate values for max-heap behavior
    maxHeap = []
    
    for i in range(len(heights) - 1):
        height_diff = heights[i + 1] - heights[i]
        
        # If next building is lower or same height, move freely
        if height_diff <= 0:
            continue
        
        # Need to climb up - use bricks first
        if bricks >= height_diff:
            bricks -= height_diff
            # Add to max-heap (negate for max behavior)
            heapq.heappush(maxHeap, -height_diff)
        elif ladders > 0:
            # No enough bricks, but we have ladders
            # Use ladder for current climb
            ladders -= 1
            
            # If we have previous brick usages, consider replacing largest one with ladder
            if maxHeap:
                largest_brick_usage = -heapq.heappop(maxHeap)
                if largest_brick_usage > height_diff:
                    # Use ladder for the largest previous usage, bricks for current
                    bricks += largest_brick_usage - height_diff
                    heapq.heappush(maxHeap, -height_diff)
                else:
                    # Current usage is larger, keep using ladder for it
                    heapq.heappush(maxHeap, -largest_brick_usage)
        else:
            # No bricks and no ladders available
            return i
    
    # Reached the end
    return len(heights) - 1


# Test with the provided example
if __name__ == "__main__":
    # Example: [2,3,5,1,7], ladders=1, bricks=2
    heights = [2, 3, 5, 1, 7]
    ladders = 1
    bricks = 2
    
    result = furthest_building(heights, ladders, bricks)
    print(f"Input: heights={heights}, ladders={ladders}, bricks={bricks}")
    print(f"Furthest reachable index: {result}")
    
    # Additional test cases
    test_cases = [
        ([4, 2, 7, 6, 9, 14, 12], 5, 1),
        ([4, 12, 2, 7, 3, 18, 20, 3, 19], 10, 2),
        ([14, 3, 19, 3], 3, 17)
    ]
    
    for heights, ladders, bricks in test_cases:
        result = furthest_building(heights, ladders, bricks)
        print(f"heights={heights}, ladders={ladders}, bricks={bricks} -> {result}")