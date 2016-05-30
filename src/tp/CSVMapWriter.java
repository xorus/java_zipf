package tp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class CSVMapWriter {
    final static String CSV_DELIMITER = ";";

    public static void write(String filename, Map<String, Integer> count) {
        StringBuffer csvOut = new StringBuffer();

        // sort the map by desc value (we do it by filling in a new sorted map)
        Map<Integer, String> sortedByValues = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            // build a csv line: word;count
            String line = entry.getKey() + CSV_DELIMITER + entry.getValue() + "\n";
            if (sortedByValues.containsKey(entry.getValue())) {
                sortedByValues.put(entry.getValue(),
                        sortedByValues.get(entry.getValue()) + line);
            } else {
                sortedByValues.put(entry.getValue(), line);
            }
        }

        File output = new File(filename);
        PrintWriter writer;
        try {
            writer = new PrintWriter(output);
            writer.print(csvOut);
            // print our sorted map in the csv file
            for (Map.Entry<Integer, String> entry : sortedByValues.entrySet()) {
                writer.print(entry.getValue());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.print("Could not open output file.");
        }
    }
}
