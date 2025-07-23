import java.util.*;

public class CurrencyConverter {
    // Graph to store currency conversions: Map<Currency, Map<NeighborCurrency, ConversionRate>>
    private Map<String, Map<String, Double>> graph;

    // Variables to track the maximum rate and corresponding path
    private double maxRate;
    private List<String> maxPath;

    // Constructor to initialize the graph and build it from the given conversions
    public CurrencyConverter(List<String[]> conversions) {
        graph = new HashMap<>();
        buildGraph(conversions);
    }

    // Build the graph from the list of conversions
    private void buildGraph(List<String[]> conversions) {
        for (String[] conv : conversions) {
            String from = conv[0], to = conv[1];
            double rate = Double.parseDouble(conv[2]);

            // Add forward conversion: from -> to with rate
            graph.computeIfAbsent(from, k -> new HashMap<>()).put(to, rate);

            // Add reverse conversion: to -> from with 1/rate
            graph.computeIfAbsent(to, k -> new HashMap<>()).put(from, 1.0 / rate);
        }
    }

    // Get the maximum conversion rate and print the path using DFS
    public double getMaxConversion(String start, String end) {
        // Initialize maxRate and maxPath
        maxRate = -1.0;
        maxPath = new ArrayList<>();

        
        List<String> currentPath = new ArrayList<>();
        currentPath.add(start); // Add the start currency to the path
        // Perform DFS to find the maximum conversion rate
        dfs(start, end, 1.0, currentPath, new HashSet<>());

        // If a valid path is found, print the path and return the rate
        if (maxRate != -1.0) {
            System.out.println("Path: " + String.join(" -> ", maxPath));
            return maxRate;
        } else {
            System.out.println("No conversion path found from " + start + " to " + end);
            return -1.0;
        }
    }

    // DFS helper method
    private void dfs(String current, String end, double currentRate, List<String> currentPath, Set<String> visited) {
        // If the target currency is reached, update the maximum rate and path
        if (current.equals(end)) {
            if (currentRate > maxRate) {
                maxRate = currentRate;
                maxPath = new ArrayList<>(currentPath);
            }
            return;
        }

        // Mark the current currency as visited
        visited.add(current);

        // Explore all neighbors of the current currency
        for (Map.Entry<String, Double> neighbor : graph.getOrDefault(current, Collections.emptyMap()).entrySet()) {
            String nextCurrency = neighbor.getKey();
            double nextRate = currentRate * neighbor.getValue(); // Multiply rates along the path

            // If the neighbor hasn't been visited, continue DFS
            if (!visited.contains(nextCurrency)) {
                currentPath.add(nextCurrency);
                dfs(nextCurrency, end, nextRate, currentPath, visited);
                currentPath.remove(currentPath.size() - 1); // Backtrack
            }
        }

        // Unmark the current currency (backtracking)
        visited.remove(current);
    }
    
    public static void main(String[] args) {
        // Input: List of conversions [currency1, currency2, rate]
        List<String[]> conversions = Arrays.asList(
            new String[]{"USD", "JPY", "2.0"},    // 1 USD = 2 JPY
            new String[]{"USD", "AUD", "0.5"},    // 1 USD = 0.5 AUD
            new String[]{"JPY", "GBP", "4.0"},    // 1 JPY = 4 GBP
            new String[]{"AUD", "GBP", "0.25"}    // 1 AUD = 0.25 GBP
        );

        // Create CurrencyConverter object
        CurrencyConverter converter = new CurrencyConverter(conversions);

        // Test case 1: GBP to AUD (Two Paths)
        System.out.println("Test Case 1: GBP to AUD (Two Paths)");
        System.out.println("Expected: Path: GBP -> AUD, Rate: 4.0");
        System.out.println("Alternate Path: GBP -> JPY -> USD -> AUD, Rate: 1.0 (should not be selected)");
        double rate1 = converter.getMaxConversion("GBP", "AUD");
        System.out.printf("Actual: Rate: %.4f%n%n", rate1);

        // Test case 2: AUD to GBP (Single Path)
        System.out.println("Test Case 2: AUD to GBP (Single Path)");
        System.out.println("Expected: Path: AUD -> GBP, Rate: 4.0 (should not be selected)" );
        System.out.println("Alternate Path: AUD -> USD -> JPY -> GBP, Rate: 16.0 ");
        double rate2 = converter.getMaxConversion("AUD", "GBP");
        System.out.printf("Actual: Rate: %.4f%n%n", rate2);

        // Test case 3: JPY to AUD (Single Path)
        System.out.println("Test Case 3: JPY to AUD (Single Path)");
        System.out.println("Expected: Path: JPY -> GBP -> AUD, Rate: 16.0");
        System.out.println("Alternate Path: JPY -> USD -> AUD, Rate: 0.25 (should not be selected)");
        double rate3 = converter.getMaxConversion("JPY", "AUD");
        System.out.printf("Actual: Rate: %.4f%n%n", rate3);

        // Test case 4: EUR to USD (No Path Exists)
        System.out.println("Test Case 4: EUR to USD (No Path Exists)");
        System.out.println("Expected: No conversion path found, Rate: -1.0");
        double rate4 = converter.getMaxConversion("EUR", "USD");
        System.out.printf("Actual: Rate: %.4f%n%n", rate4);
    }
}