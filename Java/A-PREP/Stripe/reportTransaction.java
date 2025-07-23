package org.example;
import java.util.*;

public class ReportGenerator {
    public List<String> generateReport(List<String> requests){
        List<String> result = new ArrayList<>();
        if (requests.size() == 0){
            return result;
        }
        //break each request into String[] parts
        List<String[]> requestsParts = new ArrayList<>();
        for (String request : requests){
            String[] parts = request.split(",");
            requestsParts.add(parts);
        }
        //sort by requests by timestamp
        requestsParts.sort((a, b) -> {
            return Integer.compare(Integer.parseInt(a[0]), Integer.parseInt(b[0]));
        });

        for (String[] parts : requestsParts){
            String requestReport = String.format("%s %s %s APPROVE", parts[0], parts[1], parts[2]);
            result.add(requestReport);
        }

        return result;
    }

    public List<String> generateReportWithFraudRules(List<String> requests, List<String> fraudRules){

        List<String> result = new ArrayList<>();
        if (requests.size() == 0){
            return result;
        }
        //break each request into String[] parts
        List<String[]> requestsParts = new ArrayList<>();
        for (String request : requests){
            String[] parts = request.split(",");
            requestsParts.add(parts);
        }
        //sort by requests by timestamp
        requestsParts.sort((a, b) -> {
            return Integer.compare(Integer.parseInt(a[0]), Integer.parseInt(b[0]));
        });

        //create 2 hashMap that store timestamp -> fraud rules
        HashMap<String, Set<String>> merchantFrauds = new HashMap<>();
        HashMap<String, Set<String>> cardFrauds = new HashMap<>();

        for (String rule : fraudRules){
            String[] ruleParts = rule.split(",");
            if (ruleParts[1].equals("merchant")){
                merchantFrauds.putIfAbsent(ruleParts[0], new HashSet<>());
                merchantFrauds.get(ruleParts[0]).add(ruleParts[2]);
            } else if (ruleParts[1].equals("card_number")){
                cardFrauds.putIfAbsent(ruleParts[0], new HashSet<>());
                cardFrauds.get(ruleParts[0]).add(ruleParts[2]);
            }
        }

        for (String[] parts : requestsParts){
            String timestamp = parts[0];
            String id = parts[1];
            String amount = parts[2];
            String cardNumber = parts[3];
            String merchant = parts[4];
            String approveStatus = "APPROVE";

            for (Map.Entry<String, Set<String>> merchantFraud : merchantFrauds.entrySet()){
                if ((Integer.parseInt(timestamp) >= Integer.parseInt(merchantFraud.getKey())) && (merchantFraud.getValue().contains(merchant))){
                    approveStatus = "REJECT";
                    break;
                }
            }
            for (Map.Entry<String, Set<String>> cardFraud : cardFrauds.entrySet()){
                if ((Integer.parseInt(timestamp) >= Integer.parseInt(cardFraud.getKey())) && (cardFraud.getValue().contains(cardNumber))){
                    approveStatus = "REJECT";
                    break;
                }
            }

            // loop through fraud map to see if any fraud condition is sastisfied

            String requestReport = String.format("%s %s %s %s", timestamp, id, amount, approveStatus);
            result.add(requestReport);
        }

        return result;
    }

    public double calculateSumLost(List<String> requests, List<String> fraudRules){
        double result = 0;
        if (requests.size() == 0){
            return result;
        }
        //break each request into String[] parts
        List<String[]> requestsParts = new ArrayList<>();
        for (String request : requests){
            String[] parts = request.split(",");
            requestsParts.add(parts);
        }
        //sort by requests by timestamp
        requestsParts.sort((a, b) -> {
            return Integer.compare(Integer.parseInt(a[0]), Integer.parseInt(b[0]));
        });

        //create 2 hashMap that store timestamp -> fraud rules
        HashMap<String, Set<String>> merchantFrauds = new HashMap<>();
        HashMap<String, Set<String>> cardFrauds = new HashMap<>();

        for (String rule : fraudRules){
            String[] ruleParts = rule.split(",");
            if (ruleParts[1].equals("merchant")){
                merchantFrauds.putIfAbsent(ruleParts[0], new HashSet<>());
                merchantFrauds.get(ruleParts[0]).add(ruleParts[2]);
            } else if (ruleParts[1].equals("card_number")){
                cardFrauds.putIfAbsent(ruleParts[0], new HashSet<>());
                cardFrauds.get(ruleParts[0]).add(ruleParts[2]);
            }
        }

        for (String[] parts : requestsParts){
            String timestamp = parts[0];
            String id = parts[1];
            double amount = Double.parseDouble(parts[2]);
            String cardNumber = parts[3];
            String merchant = parts[4];
            String approveStatus = "APPROVE";

            for (Map.Entry<String, Set<String>> merchantFraud : merchantFrauds.entrySet()){
                if ((Integer.parseInt(timestamp) >= Integer.parseInt(merchantFraud.getKey())) && (merchantFraud.getValue().contains(merchant))){
                    approveStatus = "REJECT";
                    break;
                }
            }
            for (Map.Entry<String, Set<String>> cardFraud : cardFrauds.entrySet()){
                if ((Integer.parseInt(timestamp) >= Integer.parseInt(cardFraud.getKey())) && (cardFraud.getValue().contains(cardNumber))){
                    approveStatus = "REJECT";
                    break;
                }
            }

            // check approved transaction once again on all fraud rules to see if it's identified afterwards
            boolean isFraud = false;
            if (approveStatus.equals("APPROVE")){
                for (Map.Entry<String, Set<String>> merchantFraud : merchantFrauds.entrySet()){
                    if (merchantFraud.getValue().contains(merchant)){
                        System.out.println(merchantFraud.getValue());
                        System.out.println(merchant);
                        isFraud = true;
                        break;
                    }
                }
                for (Map.Entry<String, Set<String>> cardFraud : cardFrauds.entrySet()){
                    if (cardFraud.getValue().contains(cardNumber)){
                        System.out.println(cardFraud.getValue());
                        System.out.println(cardNumber);
                        isFraud = true;
                        break;
                    }
                }
            }
            System.out.println(isFraud);
            if (isFraud){
                result += amount;
            }
        }
        return result;
    }
}
