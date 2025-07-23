import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Enhanced spreadsheet that supports:
 * - Numeric values
 * - Arithmetic expressions
 * - References to other cells (e.g., "=A1+B2")
 * - Cycle detection to prevent infinite loops
 */
class ExcelPart2 {
    private final Map<String, String> cells; // Stores raw cell values

    public ExcelPart2() {
        this.cells = new HashMap<>();
    }

    /**
     * Sets a cell's value.
     * Supports integers, arithmetic expressions, and references to other cells.
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
     * Resolves expressions and references recursively.
     */
    public int getCell(String cell) {
        return evaluateExpression(cell, new HashSet<>());
    }

    /**
     * Evaluates an expression recursively.
     * Uses a `visited` set to detect cycles.
     */
    private int evaluateExpression(String cell, Set<String> visited) {
        if (visited.contains(cell)) {
            return -1; // Cycle detected, return -1
        }

        visited.add(cell);
        String value = cells.get(cell);

        if (value == null) return 0; // Default value for empty cells

        if (value.charAt(0) == '=') {
            int result = evaluateFormula(value.substring(1), visited);
            visited.remove(cell); // Remove after evaluation to allow reuse
            return result;
        } else {
            visited.remove(cell); // Remove since it’s a direct integer
            return Integer.parseInt(value);
        }
    }

    /**
     * Evaluates an arithmetic formula containing cell references and integers.
     */
    private int evaluateFormula(String expression, Set<String> visited) {
        String[] parts = expression.split("\\+");
        int result = 0;

        for (String part : parts) {
            int value;
            if (Character.isLetter(part.charAt(0))) {
                value = evaluateExpression(part, visited); // Resolve cell reference
            } else {
                value = Integer.parseInt(part); // cell is an integer
            }
            if (value == -1) {
                return -1; // Propagate cycle detection up
            }
            result += value;
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

/**
 * Main class to test spreadsheet with references.
 */
public class Solution {
    public static void main(String[] args) {
        ExcelPart2 excel = new ExcelPart2();

        // Setting values
        excel.setCell("A1", "13");
        excel.setCell("A2", "14");
        excel.setCell("A3", "=A1+A2"); // Reference to A1 and A2
        excel.setCell("B1", "=2+3");
        excel.setCell("B2", "=A3+B1");

        // Getting values
        System.out.println(excel.getCell("A1")); // 13
        System.out.println(excel.getCell("A2")); // 14
        System.out.println(excel.getCell("A3")); // 27 (A1 + A2)
        System.out.println(excel.getCell("B1")); // 5
        System.out.println(excel.getCell("B2")); // 32 (A3 + B1)

        // Cycle test
        excel.setCell("C1", "=B2+C1"); // This creates a cycle
        System.out.println(excel.getCell("C1")); // -1 (cycle detected)

        // Print entire spreadsheet
        excel.printSpreadsheet();
    }
}
