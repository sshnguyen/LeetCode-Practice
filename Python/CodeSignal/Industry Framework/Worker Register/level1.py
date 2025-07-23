class WorkerRegister:
    def __init__(self):
        self.workers = {}  # workerId -> {position, compensation, sessions, current_session}
    
    def add_worker(self, worker_id, position, compensation):
        if worker_id in self.workers:
            return "invalid_request"
        
        self.workers[worker_id] = {
            'position': position,
            'compensation': compensation,
            'sessions': [],  # [(start_time, end_time), ...]
            'current_session': None
        }
        return "success"
    
    def register(self, worker_id, timestamp, action):
        if worker_id not in self.workers:
            return "invalid_request"
        
        worker = self.workers[worker_id]
        
        if action == "enter":
            if worker['current_session'] is not None:
                return "invalid_request"  # Already in office
            worker['current_session'] = timestamp
            return "success"
            
        elif action == "leave":
            if worker['current_session'] is None:
                return "invalid_request"  # Not in office
            
            start_time = worker['current_session']
            worker['sessions'].append((start_time, timestamp))
            worker['current_session'] = None
            return "success"
        
        return "invalid_request"
    
    def get_time_spent(self, worker_id):
        if worker_id not in self.workers:
            return ""
        
        worker = self.workers[worker_id]
        total_time = 0
        
        # Sum up all completed sessions
        for start_time, end_time in worker['sessions']:
            total_time += end_time - start_time
            
        return str(total_time)


def solution(queries):
    register = WorkerRegister()
    results = []
    
    for query in queries:
        operation = query[0]
        
        if operation == "ADD_WORKER":
            worker_id, position, compensation = query[1], query[2], int(query[3])
            result = register.add_worker(worker_id, position, compensation)
            results.append(result)
            
        elif operation == "REGISTER":
            worker_id, timestamp, action = query[1], int(query[2]), query[3]
            result = register.register(worker_id, timestamp, action)
            results.append(result)
            
        elif operation == "GET":
            worker_id = query[1]
            result = register.get_time_spent(worker_id)
            results.append(result)
            
    return results


if __name__ == "__main__":
    # Test cases for Level 1
    test_cases = [
        # Test case 1: Basic functionality
        [
            ["ADD_WORKER", "1", "engineer", "100"],
            ["REGISTER", "1", "10", "enter"],
            ["REGISTER", "1", "20", "leave"],
            ["GET", "1"]
        ],
        # Test case 2: Multiple sessions
        [
            ["ADD_WORKER", "2", "manager", "150"],
            ["REGISTER", "2", "5", "enter"],
            ["REGISTER", "2", "15", "leave"],
            ["REGISTER", "2", "25", "enter"],
            ["REGISTER", "2", "35", "leave"],
            ["GET", "2"]
        ],
        # Test case 3: Invalid operations
        [
            ["ADD_WORKER", "3", "developer", "120"],
            ["ADD_WORKER", "3", "tester", "110"],  # Duplicate worker
            ["REGISTER", "4", "10", "enter"],      # Non-existent worker
            ["REGISTER", "3", "10", "enter"],
            ["REGISTER", "3", "15", "enter"],      # Already in office
            ["REGISTER", "3", "20", "leave"],
            ["REGISTER", "3", "25", "leave"],      # Not in office
            ["GET", "3"],
            ["GET", "4"]                           # Non-existent worker
        ]
    ]
    
    expected_results = [
        ["success", "success", "success", "10"],
        ["success", "success", "success", "success", "success", "20"],
        ["success", "invalid_request", "invalid_request", "success", "invalid_request", "success", "invalid_request", "5", ""]
    ]
    
    for i, test_case in enumerate(test_cases):
        result = solution(test_case)
        print(f"Test case {i + 1}:")
        print(f"Input: {test_case}")
        print(f"Output: {result}")
        print(f"Expected: {expected_results[i]}")
        print(f"Pass: {result == expected_results[i]}")
        print()