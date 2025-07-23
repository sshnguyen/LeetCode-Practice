class WorkerRegister:
    def __init__(self):
        self.workers = {}  # workerId -> {current_position, current_compensation, position_history, sessions, current_session, pending_promotion}
    
    def add_worker(self, worker_id, position, compensation):
        if worker_id in self.workers:
            return "invalid_request"
        
        self.workers[worker_id] = {
            'current_position': position,
            'current_compensation': compensation,
            'position_history': [(position, compensation, 0)],  # (position, compensation, start_timestamp)
            'sessions': [],  # [(start_time, end_time, position, compensation), ...]
            'current_session': None,
            'pending_promotion': None  # (new_position, new_compensation, start_timestamp)
        }
        return "success"
    
    def register(self, worker_id, timestamp, action):
        if worker_id not in self.workers:
            return "invalid_request"
        
        worker = self.workers[worker_id]
        
        if action == "enter":
            if worker['current_session'] is not None:
                return "invalid_request"  # Already in office
            
            # Check if there's a pending promotion that should be activated
            if worker['pending_promotion'] and timestamp >= worker['pending_promotion'][2]:
                new_position, new_compensation, _ = worker['pending_promotion']
                worker['current_position'] = new_position
                worker['current_compensation'] = new_compensation
                worker['position_history'].append((new_position, new_compensation, timestamp))
                worker['pending_promotion'] = None
            
            worker['current_session'] = timestamp
            return "success"
            
        elif action == "leave":
            if worker['current_session'] is None:
                return "invalid_request"  # Not in office
            
            start_time = worker['current_session']
            worker['sessions'].append((start_time, timestamp, worker['current_position'], worker['current_compensation']))
            worker['current_session'] = None
            return "success"
        
        return "invalid_request"
    
    def get_time_spent(self, worker_id):
        if worker_id not in self.workers:
            return ""
        
        worker = self.workers[worker_id]
        total_time = 0
        
        # Sum up all completed sessions
        for start_time, end_time, _, _ in worker['sessions']:
            total_time += end_time - start_time
            
        return str(total_time)
    
    def top_n_workers(self, n):
        # Calculate total time for each worker
        worker_times = []
        
        for worker_id, worker_data in self.workers.items():
            total_time = 0
            for start_time, end_time, _, _ in worker_data['sessions']:
                total_time += end_time - start_time
            
            worker_times.append((total_time, worker_id, worker_data['current_position']))
        
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
    
    def promote(self, worker_id, new_position, new_compensation, start_timestamp):
        if worker_id not in self.workers:
            return "invalid_request"
        
        worker = self.workers[worker_id]
        
        # Check if new position is different from current position
        if new_position == worker['current_position']:
            return "invalid_request"
        
        # Check if there's already a pending promotion
        if worker['pending_promotion'] is not None:
            return "invalid_request"
        
        worker['pending_promotion'] = (new_position, new_compensation, start_timestamp)
        return "success"
    
    def calc_salary(self, worker_id, start_timestamp, end_timestamp):
        if worker_id not in self.workers:
            return ""
        
        worker = self.workers[worker_id]
        total_salary = 0
        
        # Calculate salary from completed sessions
        for session_start, session_end, position, compensation in worker['sessions']:
            # Find the overlap between the session and the query period
            overlap_start = max(session_start, start_timestamp)
            overlap_end = min(session_end, end_timestamp)
            
            if overlap_start < overlap_end:
                # There's an overlap, calculate salary for this portion
                overlap_duration = overlap_end - overlap_start
                total_salary += overlap_duration * compensation
        
        return str(total_salary)


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
            
        elif operation == "PROMOTE":
            worker_id, new_position, new_compensation, start_timestamp = query[1], query[2], int(query[3]), int(query[4])
            result = register.promote(worker_id, new_position, new_compensation, start_timestamp)
            results.append(result)
            
        elif operation == "CALC_SALARY":
            worker_id, start_timestamp, end_timestamp = query[1], int(query[2]), int(query[3])
            result = register.calc_salary(worker_id, start_timestamp, end_timestamp)
            results.append(result)
            
    return results


if __name__ == "__main__":
    # Test cases for Level 3
    test_cases = [
        # Test case 1: Basic promotion and salary calculation
        [
            ["ADD_WORKER", "1", "engineer", "100"],
            ["REGISTER", "1", "10", "enter"],
            ["REGISTER", "1", "20", "leave"],  # 10 units at 100 = 1000 salary
            ["PROMOTE", "1", "senior_engineer", "150", "25"],
            ["REGISTER", "1", "30", "enter"],  # Promotion activates
            ["REGISTER", "1", "40", "leave"],  # 10 units at 150 = 1500 salary
            ["CALC_SALARY", "1", "0", "50"]    # Total: 1000 + 1500 = 2500
        ],
        # Test case 2: Promotion with partial overlap
        [
            ["ADD_WORKER", "2", "developer", "120"],
            ["REGISTER", "2", "5", "enter"],
            ["REGISTER", "2", "15", "leave"],   # 10 units at 120 = 1200
            ["PROMOTE", "2", "lead_developer", "200", "10"],
            ["REGISTER", "2", "20", "enter"],   # Promotion already active
            ["REGISTER", "2", "30", "leave"],   # 10 units at 200 = 2000
            ["CALC_SALARY", "2", "12", "25"]    # Partial overlap: (15-12)*120 + 0 = 360
        ],
        # Test case 3: Invalid promotion scenarios
        [
            ["ADD_WORKER", "3", "tester", "90"],
            ["PROMOTE", "3", "tester", "100", "10"],      # Same position
            ["PROMOTE", "3", "senior_tester", "130", "15"],
            ["PROMOTE", "3", "lead_tester", "150", "20"], # Already has pending promotion
            ["PROMOTE", "4", "manager", "200", "25"],     # Non-existent worker
            ["CALC_SALARY", "4", "0", "100"]              # Non-existent worker
        ]
    ]
    
    expected_results = [
        ["success", "success", "success", "success", "success", "success", "2500"],
        ["success", "success", "success", "success", "success", "success", "360"],
        ["success", "invalid_request", "success", "invalid_request", "invalid_request", ""]
    ]
    
    for i, test_case in enumerate(test_cases):
        result = solution(test_case)
        print(f"Test case {i + 1}:")
        print(f"Input: {test_case}")
        print(f"Output: {result}")
        print(f"Expected: {expected_results[i]}")
        print(f"Pass: {result == expected_results[i]}")
        print()