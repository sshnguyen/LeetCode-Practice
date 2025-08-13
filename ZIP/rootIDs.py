import json

def findRootIDs_bruteforce(json_blocks):
    """
    Brute force approach to find root IDs by checking each ID against all parent IDs.
    
    Time Complexity: O(n^2) where n = number of blocks
    Space Complexity: O(n) for storing IDs and parent IDs
    """
    # Edge cases
    if not json_blocks:
        return []
    
    # Parse all blocks and collect IDs and parent IDs
    all_ids = set()
    parent_ids = set()
    
    for block_str in json_blocks:
        try:
            block = json.loads(block_str)
            if 'id' in block:
                all_ids.add(block['id'])
            if 'parent_id' in block and block['parent_id'] is not None:
                parent_ids.add(block['parent_id'])
        except json.JSONDecodeError:
            continue
    
    # Root IDs are those that appear in all_ids but not in parent_ids
    root_ids = []
    for block_id in all_ids:
        is_root = True
        # Check if this ID appears as a parent_id in any block
        for other_block_str in json_blocks:
            try:
                other_block = json.loads(other_block_str)
                if ('parent_id' in other_block and 
                    other_block['parent_id'] == block_id):
                    # This ID has children, but we still need to check if it has a parent
                    break
            except json.JSONDecodeError:
                continue
        
        # Check if this ID appears as a child (has a parent)
        for check_block_str in json_blocks:
            try:
                check_block = json.loads(check_block_str)
                if ('id' in check_block and check_block['id'] == block_id and
                    'parent_id' in check_block and check_block['parent_id'] is not None):
                    is_root = False
                    break
            except json.JSONDecodeError:
                continue
        
        if is_root:
            root_ids.append(block_id)
    
    return sorted(root_ids)


def findRootIDs(json_blocks):
    """
    Find all root IDs from JSON blocks with id and parent_id fields.
    
    A root ID is an ID that exists in the blocks but is never referenced as a parent_id,
    OR has parent_id as null/None.
    
    BRUTE FORCE APPROACH:
    For each ID, check if it appears as a parent_id in any other block.
    Iterate through all blocks for each ID to determine if it's a root.
    Time Complexity: O(n^2) where n = number of blocks
    Space Complexity: O(n) for storing IDs
    
    OPTIMAL APPROACH:
    Use sets to track all IDs and all parent IDs in single pass.
    Root IDs = All IDs - Parent IDs (that are not null)
    
    Strategy:
    1. Parse all JSON blocks and collect all IDs
    2. Collect all non-null parent IDs
    3. Root IDs are those in all_ids but not in parent_ids
    4. Also include IDs that explicitly have null parent_id
    
    Time Complexity: O(n) where n = number of blocks
    Space Complexity: O(n) for sets storing IDs
    
    Args:
        json_blocks: List of JSON strings, each containing 'id' and 'parent_id' fields
    
    Returns:
        Sorted list of root IDs
    """
    # Edge cases
    if not json_blocks:
        return []
    
    all_ids = set()
    non_null_parent_ids = set()
    
    # Single pass through all blocks
    for block_str in json_blocks:
        try:
            block = json.loads(block_str)
            
            # Collect all IDs
            if 'id' in block:
                all_ids.add(block['id'])
            
            # Collect non-null parent IDs
            if ('parent_id' in block and 
                block['parent_id'] is not None):
                non_null_parent_ids.add(block['parent_id'])
                
        except json.JSONDecodeError:
            # Skip invalid JSON blocks
            continue
    
    # Root IDs are those that exist but are never referenced as parents
    root_ids = all_ids - non_null_parent_ids
    
    return sorted(list(root_ids))


def findRootIDs_detailed(json_blocks):
    """
    Alternative approach that explicitly handles null parent_id cases.
    
    Time Complexity: O(n)
    Space Complexity: O(n)
    """
    # Edge cases
    if not json_blocks:
        return []
    
    all_ids = set()
    has_parent = set()  # IDs that have a non-null parent
    
    for block_str in json_blocks:
        try:
            block = json.loads(block_str)
            
            if 'id' in block:
                block_id = block['id']
                all_ids.add(block_id)
                
                # Check if this block has a parent
                if ('parent_id' in block and 
                    block['parent_id'] is not None):
                    has_parent.add(block_id)
                    
        except json.JSONDecodeError:
            continue
    
    # Root IDs are those that don't have parents
    root_ids = all_ids - has_parent
    
    return sorted(list(root_ids))


# Test cases
if __name__ == "__main__":
    # Test case 1: Basic tree structure
    test_blocks1 = [
        '{"id": "A", "parent_id": null}',
        '{"id": "B", "parent_id": "A"}',
        '{"id": "C", "parent_id": "A"}',
        '{"id": "D", "parent_id": "B"}',
        '{"id": "E", "parent_id": null}',
        '{"id": "F", "parent_id": "E"}'
    ]
    # Expected: ["A", "E"]
    
    result1_optimal = findRootIDs(test_blocks1)
    result1_bruteforce = findRootIDs_bruteforce(test_blocks1)
    result1_detailed = findRootIDs_detailed(test_blocks1)
    
    print("Test 1: Basic tree structure")
    print(f"Optimal: {result1_optimal}")
    print(f"Brute force: {result1_bruteforce}")
    print(f"Detailed: {result1_detailed}")
    print()
    
    # Test case 2: Single root with deep hierarchy
    test_blocks2 = [
        '{"id": 1, "parent_id": null}',
        '{"id": 2, "parent_id": 1}',
        '{"id": 3, "parent_id": 2}',
        '{"id": 4, "parent_id": 3}'
    ]
    # Expected: [1]
    
    result2_optimal = findRootIDs(test_blocks2)
    result2_bruteforce = findRootIDs_bruteforce(test_blocks2)
    
    print("Test 2: Single root with deep hierarchy")
    print(f"Optimal: {result2_optimal}")
    print(f"Brute force: {result2_bruteforce}")
    print()
    
    # Test case 3: Multiple disconnected trees
    test_blocks3 = [
        '{"id": "root1", "parent_id": null}',
        '{"id": "child1", "parent_id": "root1"}',
        '{"id": "root2", "parent_id": null}',
        '{"id": "child2", "parent_id": "root2"}',
        '{"id": "root3", "parent_id": null}'
    ]
    # Expected: ["root1", "root2", "root3"]
    
    result3_optimal = findRootIDs(test_blocks3)
    result3_bruteforce = findRootIDs_bruteforce(test_blocks3)
    
    print("Test 3: Multiple disconnected trees")
    print(f"Optimal: {result3_optimal}")
    print(f"Brute force: {result3_bruteforce}")
    print()
    
    # Test case 4: Invalid JSON and edge cases
    test_blocks4 = [
        '{"id": "valid", "parent_id": null}',
        '{"invalid": "json"',  # Missing closing brace
        '{"id": "child", "parent_id": "valid"}',
        '{"id": "orphan"}'  # No parent_id field - should be treated as root
    ]
    # Expected: ["orphan", "valid"]
    
    result4_optimal = findRootIDs(test_blocks4)
    result4_bruteforce = findRootIDs_bruteforce(test_blocks4)
    
    print("Test 4: Invalid JSON and edge cases")
    print(f"Optimal: {result4_optimal}")
    print(f"Brute force: {result4_bruteforce}")
    print()
    
    # Test case 5: Empty input
    test_blocks5 = []
    
    result5_optimal = findRootIDs(test_blocks5)
    result5_bruteforce = findRootIDs_bruteforce(test_blocks5)
    
    print("Test 5: Empty input")
    print(f"Optimal: {result5_optimal}")
    print(f"Brute force: {result5_bruteforce}")