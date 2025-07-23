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
    
    def top_n_workers(self, n):
        # Calculate total time for each worker
        worker_times = []
        
        for worker_id, worker_data in self.workers.items():
            total_time = 0
            for start_time, end_time in worker_data['sessions']:
                total_time += end_time - start_time
            
            worker_times.append((total_time, worker_id, worker_data['position']))
        
        # Sort by time (descending), then by worker_id (ascending)
        worker_times.sort(key=lambda x: (-x[0], x[1]))
        
        # Take top n workers
        top_workers = worker_times[:n]
        
        if not top_workers:
            return ""
        
        # Format result: "workerId1(position1), workerId2(position2), ..."
        result_parts = []
        for _, worker_id, position in top_workers:
            result_parts.append(f"{worker_id}({position})")
        
        return ", ".join(result_parts)


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
            
        elif operation == "TOP_N_WORKERS":
            n = int(query[1])
            result = register.top_n_workers(n)
            results.append(result)
            
    return results


if __name__ == "__main__":
    # Test cases for Level 2
    test_cases = [
        # Test case 1: Basic TOP_N_WORKERS functionality
        [
            ["ADD_WORKER", "1", "engineer", "100"],
            ["ADD_WORKER", "2", "manager", "150"],
            ["ADD_WORKER", "3", "developer", "120"],
            ["REGISTER", "1", "10", "enter"],
            ["REGISTER", "1", "25", "leave"],  # 15 time units
            ["REGISTER", "2", "5", "enter"],
            ["REGISTER", "2", "15", "leave"],   # 10 time units
            ["REGISTER", "3", "20", "enter"],
            ["REGISTER", "3", "50", "leave"],   # 30 time units
            ["TOP_N_WORKERS", "2"]
        ],
        # Test case 2: Multiple sessions per worker
        [
            ["ADD_WORKER", "A", "tester", "90"],
            ["ADD_WORKER", "B", "analyst", "110"],
            ["REGISTER", "A", "0", "enter"],
            ["REGISTER", "A", "10", "leave"],   # 10 time units
            ["REGISTER", "A", "20", "enter"],
            ["REGISTER", "A", "35", "leave"],   # 15 more, total 25
            ["REGISTER", "B", "5", "enter"],
            ["REGISTER", "B", "25", "leave"],   # 20 time units
            ["TOP_N_WORKERS", "3"]
        ],
        # Test case 3: Edge cases
        [
            ["ADD_WORKER", "X", "intern", "50"],
            ["TOP_N_WORKERS", "1"],             # No completed sessions
            ["REGISTER", "X", "1", "enter"],
            ["TOP_N_WORKERS", "1"],             # Current session not counted
            ["REGISTER", "X", "6", "leave"],    # 5 time units
            ["TOP_N_WORKERS", "1"]
        ]
    ]
    
    expected_results = [
        ["success", "success", "success", "success", "success", "success", "success", "success", "success", "3(developer), 1(engineer)"],
        ["success", "success", "success", "success", "success", "success", "success", "success", "A(tester), B(analyst)"],
        ["success", "", "success", "", "success", "X(intern)"]
    ]
    
    for i, test_case in enumerate(test_cases):
        result = solution(test_case)
        print(f"Test case {i + 1}:")
        print(f"Input: {test_case}")
        print(f"Output: {result}")
        print(f"Expected: {expected_results[i]}")
        print(f"Pass: {result == expected_results[i]}")
        print()