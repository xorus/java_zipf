package tp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Created by xorus on 30/05/16.
 */
public class ZipfAnalyzer {
    private Map<String, Integer> wordCount;

    private BufferedReader reader;

    public ZipfAnalyzer(String filename) {
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            System.err.println("Error: could not open input file (" + filename + "), aborting.");
            return;
        }

        wordCount = new TreeMap<>();
        readFile();
        Integer wNumber = wordCount.size();
        System.out.println("Suppression des mots superflus par pseudo stemming");
        WordTool.pseudoStemWordCount(wordCount);
        System.out.println("Ménage effectué ! " + (wNumber - wordCount.size()) + " mots en moins.");
    }

    private void readFile() {
        System.out.println("Lecture du fichier");
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                line = WordTool.cleanLine(line);
                StringTokenizer tokens = new StringTokenizer(line, " -'");

                while (tokens.hasMoreTokens()) {
                    String word = WordTool.cleanWord(tokens.nextToken());

                    // skip empty tokens
                    if (word.trim().length() == 0) {
                        continue;
                    }

                    // count
                    if (wordCount.containsKey(word)) {
                        wordCount.put(word, wordCount.get(word) + 1);
                    } else {
                        wordCount.put(word, 1);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read input file, aborting.");
        }
    }

    public Map<String, Integer> getWords() {
        return wordCount;
    }
}
