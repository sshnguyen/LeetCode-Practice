class Solution {
    public int leastInterval(char[] tasks, int n) {
        //use a priorityqueue to implement a max heap order by frequency of same task.
        //since we know that the minimum time required is  # of most frequently occuring task*cooldown
        // we want to choose the tasks with the most frequency everytime the cooldown period is off.
        
        Map<Character, Integer> task_frequency = new HashMap<>();
        for(Character task: tasks) {
            task_frequency.put(task, task_frequency.getOrDefault(task,0) + 1);    
        }
        // max queue using sorting by reverseOrder
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        pq.addAll(task_frequency.values());
        
        int time = 0;
        while(pq.size() > 0) {
            // as we pop the tasks and decrease their count, we need to add it back if the count
            // of task still > 0
            List<Integer> add_back = new ArrayList<>();
            
            // loop for grabbing n tasks from  pq or for idle itme only
            for(int i= 0; i <= n; i++) {

                // for grabbing n tasks from  pq 
                if(pq.size() > 0) {
                    int count = pq.poll(); // retreives max and removes an element from pq.
                    count--;
                    if(count > 0) { // needs add_back
                        add_back.add(count);
                    }  
                }

                time += 1; // accounts for idle time and real task too.
                if(pq.size() == 0 && add_back.size() == 0) {  // this accounts when the last run of tasks is done and no more tasks to ensure no idle runs happen when no tasks to schecule.
                    break;
                }  
            }   
            pq.addAll(add_back);  // re-arrange the max-heap proeprty by adding the add_back counts to pq.
        }
        return time;
    }
}