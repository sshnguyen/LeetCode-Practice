import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class SpreadSheet {
    private List<String[]> rows; // Store rows as arrays of strings

    // Constructor: Read and parse the file
    public SpreadSheet(String filePath) throws IOException {
        rows = new ArrayList<>();
        readFile(filePath);
    }

    // Read the file and parse it into rows
    private void readFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true; // Skip the header row
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip the header
                }
                String[] columns = line.split("\\s+"); // Split by whitespace
                rows.add(columns);
            }
        }
    }

    // Filter rows based on a condition
    public List<String[]> filter(String[] condition) {
        String column = condition[0]; // Column to filter (e.g., "color")
        String operator = condition[1]; // Operator (e.g., "=")
        String value = condition[2]; // Value to compare (e.g., "green")

        List<String[]> result = new ArrayList<>();

        for (String[] row : rows) {
            String rowValue = getColumnValue(row, column);
            if (rowValue != null && matchesCondition(rowValue, operator, value)) {
                result.add(row);
            }
        }

        return result;
    }

    // Get the value of a specific column in a row
    private String getColumnValue(String[] row, String column) {
        switch (column) {
            case "color":
                return row[0];
            case "date":
                return row[1];
            case "number":
                return row[2];
            default:
                return null; // Invalid column
        }
    }

    // Check if a value matches the condition
    private boolean matchesCondition(String rowValue, String operator, String value) {
        switch (operator) {
            case "=":
                return rowValue.equals(value);
            // Add more operators (e.g., ">", "<", "!=") if needed
            default:
                return false; // Unsupported operator
        }
    }

    public static void main(String[] args) {
        try {
            // Create a SpreadSheet object and read the file
            SpreadSheet sheet = new SpreadSheet("a.txt");

            // Filter rows where color = 'green'
            List<String[]> filteredRows = sheet.filter(new String[]{"color", "=", "green"});

            // Print the filtered rows
            for (String[] row : filteredRows) {
                for (String cell : row) {
                    System.out.print(cell + " ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}