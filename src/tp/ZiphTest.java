package tp;

import java.io.FileNotFoundException;

public class ZiphTest {
    public static void main(String[] args) {
        ZipfAnalyzer anal = new ZipfAnalyzer("realtext.txt");
        System.out.println("Création du CSV");
        CSVMapWriter.write("zipf.csv", anal.getWords());
    }
}
