import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Reader {
    private double numberOfWords = 0;
    private final Map<String, Integer> wordCount = new HashMap<>();
    private BufferedReader reader = null;

    public double getNumberOfWords() {
        return numberOfWords;
    }
    public Map<String, Integer> getWordCount() {
        return wordCount;
    }

    public void collectWordsFromTextFile(String inputFile) {
        try {
            reader = new BufferedReader(new InputStreamReader
                    (new FileInputStream(inputFile), StandardCharsets.UTF_8));

            int ch;
            StringBuilder wordConstructor = new StringBuilder();

            while ((ch = reader.read()) != -1) {
                if (Character.isLetterOrDigit(ch))
                    wordConstructor.append((char) ch);
                else if (!wordConstructor.isEmpty()) {
                    String completeWord = wordConstructor.toString().toLowerCase();
                    wordCount.put(completeWord, wordCount.getOrDefault(completeWord, 0) + 1);
                    wordConstructor.setLength(0);
                    ++numberOfWords;
                }
            }
            if (!wordConstructor.isEmpty()) {
                String completeWord = wordConstructor.toString().toLowerCase();
                wordCount.put(completeWord, wordCount.getOrDefault(completeWord, 0) + 1);
                ++numberOfWords;
            }

        } catch (IOException e) {
            System.err.println("ERROR while reading a file: " + e.getLocalizedMessage());
            System.exit(1);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }
}
