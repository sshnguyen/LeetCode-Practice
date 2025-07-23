import java.util.*;

public class WorkerHoursRegister {
    // Map to store worker information (workerId -> {position, compensation})
    private Map<String, WorkerInfo> workers;
    // Map to store worker's office entry and exit times (workerId -> list of timestamps)
    private Map<String, List<Integer>> workerTimestamps;
    // Map to track whether a worker is currently in the office (workerId -> boolean)
    private Map<String, Boolean> inOffice;

    // Inner class to store worker information
    private static class WorkerInfo {
        String position;
        int compensation;

        WorkerInfo(String position, int compensation) {
            this.position = position;
            this.compensation = compensation;
        }
    }

    // Constructor to initialize the maps
    public WorkerHoursRegister() {
        workers = new HashMap<>();
        workerTimestamps = new HashMap<>();
        inOffice = new HashMap<>();
    }

    // Method to add a worker to the system
    public String addWorker(String workerId, String position, int compensation) {
        if (workers.containsKey(workerId)) {
            return "false"; // Worker already exists
        }
        workers.put(workerId, new WorkerInfo(position, compensation));
        workerTimestamps.put(workerId, new ArrayList<>());
        inOffice.put(workerId, false);
        return "true"; // Worker added successfully
    }

    // Method to register the time when a worker enters or leaves the office
    public String register(String workerId, int timestamp) {
        if (!workers.containsKey(workerId)) {
            return "invalid_request"; // Worker doesn't exist
        }
        List<Integer> timestamps = workerTimestamps.get(workerId);
        boolean isInOffice = inOffice.get(workerId);
        if (isInOffice) {
            // Worker is leaving the office
            timestamps.add(timestamp);
            inOffice.put(workerId, false);
        } else {
            // Worker is entering the office
            timestamps.add(timestamp);
            inOffice.put(workerId, true);
        }
        return "registered";
    }

    // Method to get the total time a worker has spent in the office
    public String get(String workerId) {
        if (!workers.containsKey(workerId)) {
            return ""; // Worker doesn't exist
        }
        List<Integer> timestamps = workerTimestamps.get(workerId);
        int totalTime = 0;
        for (int i = 0; i < timestamps.size(); i += 2) {
            if (i + 1 < timestamps.size()) {
                totalTime += timestamps.get(i + 1) - timestamps.get(i);
            }
        }
        return String.valueOf(totalTime);
    }

    // Main method to test the functionality
    public static void main(String[] args) {
        WorkerHoursRegister register = new WorkerHoursRegister();

        // Test case 1: ADD_WORKER operation
        System.out.println(register.addWorker("Ashley", "Middle Developer", 150)); // "true"

        // Test case 2: ADD_WORKER operation (same workerId)
        System.out.println(register.addWorker("Ashley", "Junior Developer", 100)); // "false"

        // Test case 3: REGISTER operation (enter office)
        System.out.println(register.register("Ashley", 10)); // "registered"

        // Test case 4: REGISTER operation (leave office)
        System.out.println(register.register("Ashley", 25)); // "registered"

        // Test case 5: GET operation
        System.out.println(register.get("Ashley")); // "15"

        // Test case 6: REGISTER operation (enter office)
        System.out.println(register.register("Ashley", 40)); // "registered"

        // Test case 7: REGISTER operation (leave office)
        System.out.println(register.register("Ashley", 67)); // "registered"

        // Test case 8: GET operation
        System.out.println(register.get("Ashley")); // "42"

        // Test case 9: GET operation (non-existent worker)
        System.out.println(register.get("walter")); // ""

        // Test case 10: REGISTER operation (non-existent worker)
        System.out.println(register.register("walter", 120)); // "invalid_request"
    }
}