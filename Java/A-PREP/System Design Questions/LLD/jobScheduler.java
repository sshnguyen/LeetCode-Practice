import java.util.PriorityQueue;

// Represents a job with a start and finish time
class Job {
    long startTime;
    long finishTime;

    public Job(long startTime, long finishTime) {
        this.startTime = startTime;
        this.finishTime = finishTime;
    }
}

class JobScheduler {
    // Min-heap (priority queue) to store jobs in order of start time
    private PriorityQueue<Job> jobQueue = new PriorityQueue<>((a, b) -> Long.compare(a.startTime, b.startTime));

    // Method to schedule a new job by adding it to the priority queue
    public void schedule(long startTime, long finishTime) {
        jobQueue.offer(new Job(startTime, finishTime));
    }

    // Runs the scheduled jobs based on their start times
    public void run() {
        while (!jobQueue.isEmpty()) {
            Job job = jobQueue.poll(); // Fetch the next job with the earliest start time
            
            // Calculate the wait time before the job should start
            long waitTime = job.startTime - System.currentTimeMillis();
            if (waitTime > 0) {
                try { 
                    Thread.sleep(waitTime); // Wait until the job's start time 
                } catch (InterruptedException ignored) {}
            }

            // Job execution starts
            System.out.println("Job started at: " + job.startTime);

            // Simulating job duration (running the job)
            try { 
                Thread.sleep(job.finishTime - job.startTime); // Sleep for the job duration
            } catch (InterruptedException ignored) {}

            // Job execution ends
            System.out.println("Job finished at: " + job.finishTime);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        JobScheduler scheduler = new JobScheduler();
        long now = System.currentTimeMillis();

        // Schedule two jobs with different start and finish times
        scheduler.schedule(now + 1000, now + 2000); // Job 1: Start in 1 sec, finish in 2 sec
        scheduler.schedule(now + 3000, now + 4000); // Job 2: Start in 3 sec, finish in 4 sec

        // Start the scheduler to process jobs
        scheduler.run();
    }
}
