import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Reader {
    /*** Private Section */
    private double number_of_words = 0;
    private Map<String, Integer> wordCount = new HashMap<>();
    private InputStreamReader reader = null;

    public double getNumber_of_words() {
        return number_of_words;
    }

    public Map<String, Integer> getWordCount() {
        return wordCount;
    }

    public void CollectWordsFromTextFile(String inputFile) {
        try {
            reader = new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_16); /**/

            int ch;
            StringBuilder word = new StringBuilder();

            while ((ch = reader.read()) != -1) {
                if (Character.isLetterOrDigit(ch)) //PROBLEM: words with "-"
                    word.append((char) ch);
                else if (!word.isEmpty()) {
                    String string = word.toString().toLowerCase();
                    wordCount.put(string, wordCount.getOrDefault(string, 0) + 1);
                    word.setLength(0);
                    ++number_of_words;
                }
            }
            if (!word.isEmpty()) {
                String string = word.toString().toLowerCase(); //rename it!!!
                wordCount.put(string, wordCount.getOrDefault(string, 0) + 1);
                ++number_of_words;
            }

        } catch (IOException e) {
            System.err.println("ERROR while reading a file:" + e.getLocalizedMessage());
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }
}
