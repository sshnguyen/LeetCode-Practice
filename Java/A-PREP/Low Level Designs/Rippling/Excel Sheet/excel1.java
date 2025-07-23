import java.util.HashMap;
import java.util.Map;

/**
 * Minimal spreadsheet implementation that supports:
 * - Setting integer values in cells
 * - Setting arithmetic expressions (e.g., "2+3")
 * - Retrieving computed values
 */
class ExcelPart1 {
    private final Map<String, String> cells; // Stores raw values

    public ExcelPart1() {
        this.cells = new HashMap<>();
    }

    /**
     * Sets a cell's value.
     * The value can be an integer or an arithmetic expression (e.g., "2+3").
     */
    public void setCell(String cell, String value) {
        if (value.isEmpty()) {
            cells.remove(cell); // Reset cell if empty string is provided
        } else {
            cells.put(cell, value);
        }
    }

    /**
     * Retrieves the computed value of a cell.
     * If the value is an arithmetic expression, it is evaluated.
     */
    public int getCell(String cell) {
        String value = cells.get(cell);
        if (value == null) return 0; // Default value for empty cells

        if (value.startsWith("=")) {
            return evaluateExpression(value.substring(1)); // Evaluate arithmetic expression
        } else {
            return Integer.parseInt(value); // Direct integer value
        }
    }

    /**
     * Evaluates a simple arithmetic expression with two operands and a '+' operator.
     */
    private int evaluateExpression(String expression) {
        String[] parts = expression.split("\\+");
        int result = 0;
        for (String part : parts) {
            result += Integer.parseInt(part);
        }
        return result;
    }

    /**
     * Prints the spreadsheet with both raw and computed values.
     */
    public void printSpreadsheet() {
        for (Map.Entry<String, String> entry : cells.entrySet()) {
            String cell = entry.getKey();
            String rawValue = entry.getValue();
            int computedValue = getCell(cell);
            System.out.println(cell + ": Raw=" + rawValue + ", Computed=" + computedValue);
        }
    }
}

public class Solution {
    public static void main(String[] args) {
        ExcelPart1 excel = new ExcelPart1();

        excel.setCell("A1", "13");
        excel.setCell("A2", "14");
        excel.setCell("B1", "=2+3"); // Arithmetic expression without references
        excel.setCell("A2", "");

        System.out.println(excel.getCell("A1")); // 13
        System.out.println(excel.getCell("A2")); // 14
        System.out.println(excel.getCell("B1")); // 5
        System.out.println(excel.getCell("A2")); // 0

        excel.printSpreadsheet();
    }
}

