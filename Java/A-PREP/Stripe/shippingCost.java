import java.io.*;
import java.util.*;
import com.google.gson.Gson;

class ShippingCostCalculator {
    private List<Map<String, String>> parseInput(String input){
        List<Map<String, String>> parsedInput = new ArrayList<>();
        String[] routes = input.split(",");
        for(String route : routes){
            Map<String, String> mapParts = new HashMap<>();
            String[] parts = route.split(":");
            mapParts.put("source", parts[0]);
            mapParts.put("target", parts[1]);
            mapParts.put("method", parts[2]);
            mapParts.put("cost", parts[3]);
            parsedInput.add(mapParts);
        }
        return parsedInput;
    }
    
    int getCost(String input, String source, String target, String method){
        if (source == null || target == null || method == null || source.isEmpty() || target.isEmpty() || method.isEmpty()){
            return -1;
        }
        
        List<Map<String, String>> parsedInput = parseInput(input);
        
        for(Map<String, String> routeMap : parsedInput){
            if (routeMap.get("source").equals(source) && routeMap.get("target").equals(target) && routeMap.get("method").equals(method)){
                return Integer.parseInt(routeMap.get("cost"));
            }
        }
        return -1;
    }
    
    String getCostIntermediate(String input, String source, String target){
        Gson gson = new Gson();
        Map<String, String> result = new HashMap<>();
        
        List<Map<String, String>> routes = parseInput(input);
        
        //loop through the first route
        for(Map<String, String> firstRoute : routes){
            //find first route, which start from source
            if (firstRoute.get("source").equals(source)){
                // check the second route which start from first route target
                String intermediateSource = firstRoute.get("target");
                for(Map<String, String> secondRoute : routes){
                    if (intermediateSource.equals(secondRoute.get("source"))){
                        if (secondRoute.get("target").equals(target)){
                            result.put("route", firstRoute.get("source") +" -> " +secondRoute.get("source"));
                            result.put("method", firstRoute.get("method") +" -> " +secondRoute.get("method"));
                            result.put("cost", String.valueOf(Integer.parseInt(firstRoute.get("cost") + Integer.parseInt(secondRoute.get("cost")))));
                            return gson.toJson(result);
                        }
                    }
                }
            }
        }
        
        return "{}";
        
    }
    
    int getLowestCostWithAtMostOneHop(String inputString, String source, String target){
        int minCost = Integer.MAX_VALUE;
        List<Map<String, String>> routes = parseInput(inputString);
        // check no hop
        for (Map<String, String> firstRoute: routes){
            if (firstRoute.get("source").equals(source)){ // found the source
                if (firstRoute.get("target").equals(target)){//found direct route
                    minCost = Math.min(minCost, Integer.parseInt(firstRoute.get("cost")));
                }
                //try and find the route through intermediate source
                for (Map<String, String> secondRoute: routes){
                    if (secondRoute.get("source").equals(firstRoute.get("target"))){ // intermediate route
                        if (secondRoute.get("target").equals(target)){
                            minCost = Math.min(minCost, Integer.parseInt(firstRoute.get("cost")) + Integer.parseInt(secondRoute.get("cost")));
                        }
                    }
                }
            }
        }
        
        return minCost;
    }
}

public class Solution {

    public static void main(String[] args) {
        //Part 1: Write a method that takes (inputString, sourceCountry, targetCountry, method) as input and outputs the cost. Concatenate sourceCountry, targetCountry, method to use as a key. Handle edge cases: what to return if the country is not found, how to handle empty strings.
        ShippingCostCalculator calculator = new ShippingCostCalculator();
        //test good case
        int cost = calculator.getCost("US:UK:UPS:4,US:UK:DHL:5,UK:CA:FedEx:10,AU:JP:DHL:20", "UK", "CA", "FedEx");
        //not found case
        int cost2 = calculator.getCost("US:UK:UPS:4,US:UK:DHL:5,UK:CA:FedEx:10,AU:JP:DHL:20", "UK", "UKS", "FedEx");
        //input issue case
        int cost3 = calculator.getCost("US:UK:UPS:4,US:UK:DHL:5,UK:CA:FedEx:10,AU:JP:DHL:20", "", "CA", null);
        System.out.println((cost == 10)? "Test 1 Pass" : "Test 1 Failed, expected 10 " + cost);
        System.out.println((cost2 == -1)? "Test 2 Pass" : "Test 2 Failed, expected -1 but got" + cost2);
        System.out.println((cost3 == -1)? "Test 3 Pass" : "Test 3 Failed, expected -1 but got " + cost3);
        // Part 2:
        // If one intermediate country is allowed, output a structure. For example, if the input is US and CA, output:
        // {
        // route: "US -> UK -> CA",
        // method: "UPS -> FedEx",
        // cost: 14
        // }
        String cost4 = calculator.getCostIntermediate("US:UK:UPS:4,US:UK:DHL:5,UK:CA:FedEx:10,AU:JP:DHL:20", "US", "CA");
        System.out.println(cost4); //{"route":"US - UK - CA","cost":14,"method":"UPS - FedEx"}
        
        // Minimum cost with atmost 1 hop
        int cost5 = calculator.getLowestCostWithAtMostOneHop("US:CA:UPS:5,US:UK:UPS:4,US:UK:DHL:5,UK:CA:FedEx:10,AU:JP:DHL:20", "US", "CA");
         System.out.println(cost5); //test direct, expect 5
         int cost6 = calculator.getLowestCostWithAtMostOneHop("US:CA:UPS:5,US:UK:UPS:1,US:UK:DHL:5,UK:CA:FedEx:3,AU:JP:DHL:20", "US", "CA");
         System.out.println(cost6); //test 1 hop cost less than direct, expect 4
        
        //Part 4:Calculate the lowest cost.
        
        //WE CAN SOLVE IT BY CHANGING THE QUESTION INTO A GRAPH QUESTION WHERE COUNTRIES ARE NODES IN THE GRAPH AND the cost are the edges weight.
    //     Dijkstra's Algorithm:

    //     Purpose: Finds the shortest path from a source node to all other nodes in a graph with non-negative edge weights.​

    //     How It Works: It iteratively selects the node with the smallest known distance, updates the distances to its neighboring nodes, and repeats this process until the shortest path to all nodes is determined.​ 

    //     Time Complexity: O((V + E) log V), where V is the number of vertices and E is the number of edges.
    }
}
