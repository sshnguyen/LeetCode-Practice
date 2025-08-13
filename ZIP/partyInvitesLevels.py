from collections import defaultdict

def maxPartySum_backtrack(levels, reporting_chain):
    """
    Backtracking solution that explores all possible invitation combinations.
    
    Time Complexity: O(2^n) - try all subsets of employees
    Space Complexity: O(n) for recursion stack and constraint checking
    """
    # Edge cases
    if not levels:
        return 0
    if len(levels) == 1:
        return levels[0]
    if not reporting_chain:
        return sum(levels)
    
    n = len(levels)
    
    # Build adjacency list for manager -> reports
    children = defaultdict(list)
    parent = {}
    
    for manager, report in reporting_chain:
        children[manager].append(report)
        parent[report] = manager
    
    def is_valid_selection(invited):
        """Check if current selection violates manager-report constraint"""
        for employee in invited:
            # Check if any direct report is also invited
            for report in children[employee]:
                if report in invited:
                    return False
            
            # Check if manager is also invited
            if employee in parent and parent[employee] in invited:
                return False
        
        return True
    
    def backtrack(index, current_invited, current_sum):
        """
        Try all combinations of inviting/not inviting employees
        index: current employee being considered
        current_invited: set of currently invited employees
        current_sum: sum of levels for current selection
        """
        # Base case: processed all employees
        if index == n:
            if is_valid_selection(current_invited):
                return current_sum
            else:
                return 0
        
        # Choice 1: Don't invite current employee
        max_sum = backtrack(index + 1, current_invited, current_sum)
        
        # Choice 2: Invite current employee
        current_invited.add(index)
        max_sum = max(max_sum, backtrack(index + 1, current_invited, current_sum + levels[index]))
        current_invited.remove(index)
        
        return max_sum
    
    return backtrack(0, set(), 0)


def maxPartySum(levels, reporting_chain):
    """
    Party Invites Max Sum Problem
    
    Find maximum sum of employee levels that can be invited to a party.
    Constraint: If an employee is invited, none of their direct reports can be invited.
    
    BRUTE FORCE APPROACH - Backtracking:
    Try all possible combinations of inviting/not inviting each employee.
    For each employee, recursively explore both choices with constraint checking.
    Time Complexity: O(2^n) - exponential due to trying all combinations
    Space Complexity: O(n) for recursion stack depth
    
    OPTIMAL APPROACH - Tree DP:
    Model as tree dynamic programming problem on company hierarchy.
    For each node, calculate max sum when including vs excluding the employee.
    
    Strategy:
    1. Build adjacency list representing manager-report relationships
    2. Find root employee(s) who have no manager
    3. For each subtree rooted at employee, calculate:
       - include[employee] = employee_level + sum(exclude[child] for all children)
       - exclude[employee] = sum(max(include[child], exclude[child]) for all children)
    4. Return max of include/exclude for all roots
    
    Time Complexity: O(n) where n = number of employees
    Space Complexity: O(n) for adjacency list and recursion stack
    
    Args:
        levels: List of employee levels (index = employee_id, value = level)
        reporting_chain: List of [manager, report] pairs
    
    Returns:
        Maximum sum of levels possible
    """
    # Edge cases
    if not levels:
        return 0
    if len(levels) == 1:
        return levels[0]
    if not reporting_chain:
        return sum(levels)
    
    n = len(levels)
    
    # Build adjacency list for manager -> reports
    children = defaultdict(list)
    has_manager = set()
    
    for manager, report in reporting_chain:
        children[manager].append(report)
        has_manager.add(report)
    
    # Find root employees (those with no manager)
    roots = [i for i in range(n) if i not in has_manager]
    
    def dfs(employee):
        """
        Returns (include_sum, exclude_sum) for subtree rooted at employee
        include_sum: max sum when including this employee
        exclude_sum: max sum when excluding this employee
        """
        if employee not in children:
            # Leaf employee
            return levels[employee], 0
        
        include_sum = levels[employee]  # Include current employee
        exclude_sum = 0  # Exclude current employee
        
        # Process all direct reports
        for child in children[employee]:
            child_include, child_exclude = dfs(child)
            
            # If we include current employee, we must exclude all children
            include_sum += child_exclude
            
            # If we exclude current employee, we can choose optimally for each child
            exclude_sum += max(child_include, child_exclude)
        
        return include_sum, exclude_sum
    
    # Calculate max sum across all root employees
    total_max = 0
    for root in roots:
        include_root, exclude_root = dfs(root)
        total_max += max(include_root, exclude_root)
    
    return total_max


# Test cases
if __name__ == "__main__":
    # Test case 1: Given example
    levels1 = [10, 9, 9, 5, 5, 5, 6]
    reporting_chain1 = [[0, 1], [0, 2], [1, 3], [1, 4], [1, 5], [2, 6]]
    # Tree structure:
    #       0(10)
    #      /     \
    #    1(9)    2(9)
    #   / | \     |
    # 3(5)4(5)5(5)6(6)
    # Optimal: Exclude 0, include 1(9)+2(9) + exclude their children = 9+9+(5+5+5)+6 = 39
    
    result1_dp = maxPartySum(levels1, reporting_chain1)
    result1_backtrack = maxPartySum_backtrack(levels1, reporting_chain1)
    
    print(f"Test 1: levels={levels1}")
    print(f"Reporting chain: {reporting_chain1}")
    print(f"Tree DP: {result1_dp}")
    print(f"Backtracking: {result1_backtrack}")
    print()
    
    # Test case 2: Linear chain
    levels2 = [10, 5, 8, 3]
    reporting_chain2 = [[0, 1], [1, 2], [2, 3]]
    # 0(10) -> 1(5) -> 2(8) -> 3(3)
    # Optimal: 0(10) + 2(8) = 18
    
    result2_dp = maxPartySum(levels2, reporting_chain2)
    result2_backtrack = maxPartySum_backtrack(levels2, reporting_chain2)
    
    print(f"Test 2: levels={levels2}")
    print(f"Reporting chain: {reporting_chain2}")
    print(f"Tree DP: {result2_dp}")
    print(f"Backtracking: {result2_backtrack}")
    print()
    
    # Test case 3: Multiple roots
    levels3 = [5, 8, 3, 6]
    reporting_chain3 = [[0, 1], [2, 3]]
    # Two trees: 0(5) -> 1(8) and 2(3) -> 3(6)
    # Optimal: max(5, 8) + max(3, 6) = 8 + 6 = 14
    
    result3_dp = maxPartySum(levels3, reporting_chain3)
    result3_backtrack = maxPartySum_backtrack(levels3, reporting_chain3)
    
    print(f"Test 3: levels={levels3}")
    print(f"Reporting chain: {reporting_chain3}")
    print(f"Tree DP: {result3_dp}")
    print(f"Backtracking: {result3_backtrack}")
    print()
    
    # Test case 4: Single employee
    levels4 = [10]
    reporting_chain4 = []
    
    result4_dp = maxPartySum(levels4, reporting_chain4)
    result4_backtrack = maxPartySum_backtrack(levels4, reporting_chain4)
    
    print(f"Test 4: levels={levels4}")
    print(f"Reporting chain: {reporting_chain4}")
    print(f"Tree DP: {result4_dp}")
    print(f"Backtracking: {result4_backtrack}")